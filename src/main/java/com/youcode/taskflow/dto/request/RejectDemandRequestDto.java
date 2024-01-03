package com.youcode.taskflow.dto.request;

import lombok.Data;

@Data
public class RejectDemandRequestDto {
    private Long DemandId;
    private Long userId;
}
