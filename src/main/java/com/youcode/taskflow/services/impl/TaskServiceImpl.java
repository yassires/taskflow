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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
}
