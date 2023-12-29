package com.youcode.taskflow.dto;


import com.youcode.taskflow.entities.enums.TaskStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto implements Serializable {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserDto createdBy;
    private UserDto assignTo;
    private List<TagDto> tag;
    private TaskStatus status;
}
