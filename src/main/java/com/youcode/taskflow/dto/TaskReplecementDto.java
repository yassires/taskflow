package com.youcode.taskflow.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskReplecementDto implements Serializable {
    private Long taskId;
    private Long userId;
}
