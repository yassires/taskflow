package com.youcode.taskflow.dto.request;

import lombok.Data;

@Data
public class ApproveDemandRequestDto {

    private Long DemandId;
    private Long userId;
    private Long newUserId;
}
