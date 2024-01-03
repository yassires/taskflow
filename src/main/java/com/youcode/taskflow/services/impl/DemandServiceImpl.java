package com.youcode.taskflow.services.impl;

import com.youcode.taskflow.dto.DemandDto;
import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.request.ApproveDemandRequestDto;
import com.youcode.taskflow.dto.request.DemandRequestDto;
import com.youcode.taskflow.dto.request.RejectDemandRequestDto;
import com.youcode.taskflow.entities.Task;
import com.youcode.taskflow.entities.Demand;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.entities.enums.Action;
import com.youcode.taskflow.entities.enums.DemandStatus;
import com.youcode.taskflow.entities.enums.Role;
import com.youcode.taskflow.entities.enums.TaskStatus;
import com.youcode.taskflow.repository.DemandRepository;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.services.DemandService;
import com.youcode.taskflow.services.TaskService;
import com.youcode.taskflow.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandServiceImpl implements DemandService {

    private final DemandRepository demandRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<DemandDto> getAllDemands() {
        List<DemandDto> list = new ArrayList<>();

        demandRepository.findAll().forEach(demand -> {
            list.add(modelMapper.map(demand, DemandDto.class));
        });

        return list;
    }

    @Override
    public DemandDto createDemand(DemandRequestDto requestDto) {
        try {
            Long taskId = requestDto.getTaskId();
            Long userId = requestDto.getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            if (user.getToken() <= 0) {
                throw new IllegalArgumentException("User does not have enough tokens");
            }

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

            Long assignedToId = task.getAssignTo() != null ? task.getAssignTo().getId() : null;
            if (!Objects.equals(userId, assignedToId)) {
                throw new IllegalArgumentException("Invalid userId provided for Task with id: " + taskId);
            }

            boolean hasApprovedTaskReplacement = demandRepository.existsByTaskAndStatus(task, DemandStatus.ACCEPTED);

            if (hasApprovedTaskReplacement) {
                return new DemandDto("error", "This task has already been edited or deleted once");
            }


            if (requestDto.getAction() == Action.EDIT) {
                LocalDate startDate = task.getStartDate();
                LocalDate currentDate = LocalDate.now();
                if (startDate != null && currentDate.plusDays(1).isAfter(startDate)) {
                    throw new IllegalArgumentException("Cannot create Demand less than 24 hours before the task's start date");
                }
            }


            Demand demand = new Demand();
            demand.setTask(task);
            demand.setCreatedAt(LocalDateTime.now());
            demand.setOldUser(task.getAssignTo());
            demand.setNewUser(null);
            demand.setAction(requestDto.getAction());
            demand.setStatus(requestDto.getAction() == Action.DELETE ? DemandStatus.ACCEPTED : DemandStatus.PENDING);

            demand = demandRepository.save(demand);


            if (requestDto.getAction() == Action.DELETE) {
                task.setAssignTo(null);
                taskRepository.save(task);


                user.setToken(user.getToken() - 1);
                userRepository.save(user);
            }

            DemandDto responseDTO = modelMapper.map(demand, DemandDto.class);
            responseDTO.setStatus("success");
            responseDTO.setMessage("Demand created successfully");
            return responseDTO;
        } catch (EntityNotFoundException e) {
            return new DemandDto("error", "Task not found with id: " + requestDto.getTaskId());
        } catch (IllegalArgumentException e) {
            return new DemandDto("error", e.getMessage());
        } catch (Exception e) {
            return new DemandDto("error", "Error creating Demand: " + e.getMessage());
        }
    }

    @Override
    public DemandDto approveDemand(ApproveDemandRequestDto requestDTO) {
        try {
            Long demandId = requestDTO.getDemandId();
            Long userId = requestDTO.getUserId();
            Long newUserId = requestDTO.getNewUserId();

            Demand demand = demandRepository.findById(demandId)
                    .orElseThrow(() -> new EntityNotFoundException("Demand not found with id: " + demandId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));


            if (!user.getRole().equals(Role.ADMIN)) {
                throw new RuntimeException("Unauthorized access: User does not have admin role");
            }

            if (demand.getAction() != Action.EDIT || demand.getStatus() != DemandStatus.PENDING) {
                throw new IllegalArgumentException("Invalid Demand state for approval");
            }

            demand.setStatus(DemandStatus.ACCEPTED);


            if (newUserId != null) {
                User newUser = userRepository.findById(newUserId)
                        .orElseThrow(() -> new EntityNotFoundException("New user not found with id: " + newUserId));
                demand.setNewUser(newUser);
                demand.getTask().setAssignTo(newUser);
            }


            demand = demandRepository.save(demand);


            User oldUser = demand.getOldUser();
            if (oldUser != null && oldUser.getToken() > 0) {
                oldUser.setToken(oldUser.getToken() - 1);
                userRepository.save(oldUser);
            }

            DemandDto responseDTO = modelMapper.map(demand, DemandDto.class);
            responseDTO.setStatus("success");
            responseDTO.setMessage("Demand approved successfully");
            return responseDTO;
        } catch (EntityNotFoundException e) {
            return new DemandDto("error", "Demand not found with id: " + requestDTO.getDemandId());
        } catch (RuntimeException e) {
            return new DemandDto("error", e.getMessage());
        } catch (Exception e) {
            return new DemandDto("error", "Error approving Demand: " + e.getMessage());
        }
    }

    @Override
    public DemandDto rejectDemand(RejectDemandRequestDto requestDTO) {
        try {
            Long demandId = requestDTO.getDemandId();
            Long userId = requestDTO.getUserId();

            Demand demand = demandRepository.findById(demandId)
                    .orElseThrow(() -> new EntityNotFoundException("TaskReplacement not found with id: " + demandId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));


            if (!user.getRole().equals(Role.ADMIN)) {
                throw new RuntimeException("Unauthorized access: User does not have admin role");
            }


            demand.setStatus(DemandStatus.REJECTED);
            demand = demandRepository.save(demand);


            DemandDto responseDTO = modelMapper.map(demand, DemandDto.class);
            responseDTO.setStatus("success");
            responseDTO.setMessage("TaskReplacement rejected successfully");
            return responseDTO;
        } catch (EntityNotFoundException e) {
            return new DemandDto("error", "TaskReplacement not found with id: " + requestDTO.getDemandId());
        } catch (RuntimeException e) {
            return new DemandDto("error", e.getMessage());
        } catch (Exception e) {
            return new DemandDto("error", "Error rejecting task replacement: " + e.getMessage());
        }
    }


}
