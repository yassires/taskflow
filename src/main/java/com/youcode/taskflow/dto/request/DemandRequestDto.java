package com.youcode.taskflow.dto.request;

import com.youcode.taskflow.entities.enums.Action;
import lombok.Data;

@Data
public class DemandRequestDto {

    private Long taskId;
    private Long userId;
    private Action action;
}
