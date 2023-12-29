package com.youcode.taskflow.dto;


import com.youcode.taskflow.entities.Tag;
import com.youcode.taskflow.entities.User;
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
    private User createdBy;
    private User assignTo;
    private List<Integer> tag;
}
