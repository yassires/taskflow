package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.entities.enums.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    List<TaskDto> getAllTasks();

    TaskDto createTask(TaskDto taskDTO);

    TaskDto updateTask(Integer taskId, TaskDto taskDto);

    TaskDto updateTaskStatus(Integer taskId, TaskStatus newStatus);

    void updateExpiredTasksStatus();


}
