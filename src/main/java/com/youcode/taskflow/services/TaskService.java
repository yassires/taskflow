package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    List<TaskDto> getAllTasks();

    TaskDto createTask(TaskDto taskDTO);

}
