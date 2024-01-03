package com.youcode.taskflow.dto;

import com.youcode.taskflow.entities.Task;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.entities.enums.Action;
import com.youcode.taskflow.entities.enums.DemandStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DemandDto implements Serializable {

    private Long id;
    private Task task;
    private LocalDate createdAt;
    private User oldUser;
    private User newUser;
    private Action action;
    private DemandStatus demandStatus;

    private String status;
    private String message;

    public DemandDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
