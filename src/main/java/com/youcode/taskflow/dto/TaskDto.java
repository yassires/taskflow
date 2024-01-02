package com.youcode.taskflow.dto;


import com.youcode.taskflow.entities.Tag;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.entities.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto implements Serializable {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;

    @FutureOrPresent(message = "Start date must be in the present or future")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Created by user ID is required")
    private User createdBy;

    @NotNull(message = "Assigned to user ID is required")
    private User assignTo;
    @NotEmpty(message = "At least one tag is required")
    private List<Integer> tag;
}
