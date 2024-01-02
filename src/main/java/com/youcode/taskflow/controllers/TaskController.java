package com.youcode.taskflow.controllers;

import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.entities.enums.TaskStatus;
import com.youcode.taskflow.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;


    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> taskList = taskService.getAllTasks();
        return ResponseEntity.ok(taskList);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDTO) {
        TaskDto createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Integer taskId, @RequestBody @Valid TaskDto taskDto) {
        taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok("updatedTask");
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Integer taskId, @RequestBody @Valid TaskStatus status) {
        taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok("updatedTask");
    }
}
