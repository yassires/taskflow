package com.youcode.taskflow.services.impl;


import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.dto.UserDto;
import com.youcode.taskflow.entities.Tag;
import com.youcode.taskflow.entities.Task;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.entities.enums.Role;
import com.youcode.taskflow.entities.enums.TaskStatus;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<TaskDto> getAllTasks() {
        List<TaskDto> list = new ArrayList<>();

        userRepository.findAll().forEach(task -> {
            list.add(modelMapper.map(task, TaskDto.class));
        });

        return list;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Integer createdById = taskDto.getCreatedBy().getId();
        if (createdById != null){
            User createdBy = userRepository.findById(createdById).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createdById));

            if (!createdBy.getRole().equals(Role.ADMIN)){
                throw new IllegalArgumentException("User with id " + createdById + " is not an ADMIN");
            }
            Task task = modelMapper.map(taskDto,Task.class);
            task.setCreatedAt(LocalDate.now());
            task.setStatus(TaskStatus.TODO);

            LocalDate startDate = task.getStartDate();
            if (startDate != null && startDate.isBefore(LocalDate.now().plusDays(3))) {
                throw new IllegalArgumentException("Start date must be at least 3 days from now");
            }

            LocalDate endDate = task.getEndDate();
            if (endDate != null && startDate != null) {
                if (endDate.isBefore(startDate.plusDays(3)) || endDate.isAfter(startDate.plusDays(14))) {
                    throw new IllegalArgumentException("Deadline must be between 3 and 14 days from the start date");
                }
            }

            List<Integer> tagId = taskDto.getTag();
            if (tagId != null && !tagId.isEmpty()){
                List<Tag> tags = tagRepository.findAllById(tagId);
                task.setTag(tags);
            }

            task = taskRepository.save(task);
            TaskDto taskDto1 = modelMapper.map(task,TaskDto.class);
            return taskDto1;
        } else {
        throw new IllegalArgumentException("CreatedById cannot be null");
        }
    }

    @Override
    public TaskDto updateTask(Integer taskId, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            validateTaskDTO(taskDto);
            Task task = optionalTask.get();
            modelMapper.map(taskDto, task);
            task.setAssignTo(getUserById(taskDto.getAssignTo().getId()));
            task.setTag(getTagsByIds(taskDto.getTag()));
            Task updatedTask = taskRepository.save(task);
            return modelMapper.map(updatedTask, TaskDto.class);
        } else {
            throw new ValidationException("Task not found with ID: " + taskId);
        }
    }



    private void validateTaskDTO(TaskDto taskDto) {
        LocalDate currentDate = LocalDate.now();

        if (taskDto.getStartDate().isBefore(currentDate)) {
            throw new ValidationException("Task start date cannot be in the past.");
        }

        if (taskDto.getEndDate().isBefore(taskDto.getStartDate())) {
            throw new ValidationException("Task end date cannot be before the start date.");
        }

        if (taskDto.getStartDate().isAfter(currentDate.plusDays(3))) {
            throw new ValidationException("Task must be scheduled at least 3 days in advance.");
        }


        getUserById(taskDto.getCreatedBy().getId());
        getUserById(taskDto.getAssignTo().getId());

        if (taskDto.getCreatedBy().getRole() == Role.USER) {
            if (!taskDto.getCreatedBy().getId().equals(taskDto.getAssignTo().getId())) {
                throw new ValidationException("User with role USER can only assign tasks to themselves.");
            }
        }
    }


    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found with ID: " + userId));
    }

    private List<Tag> getTagsByIds(List<Integer> tagIds) {
        List<Tag> existingTags = tagRepository.findAllById(tagIds);
        if (existingTags.size() != tagIds.size()) {
            throw new ValidationException("One or more tags do not exist.");
        }
        return existingTags;
    }

}
