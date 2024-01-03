package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.DemandDto;
import com.youcode.taskflow.dto.request.ApproveDemandRequestDto;
import com.youcode.taskflow.dto.request.DemandRequestDto;
import com.youcode.taskflow.dto.request.RejectDemandRequestDto;

import java.util.List;

public interface DemandService {
    List<DemandDto> getAllDemands();

    DemandDto createDemand(DemandRequestDto requestDTO);
    DemandDto approveDemand(ApproveDemandRequestDto requestDTO);
    DemandDto rejectDemand(RejectDemandRequestDto requestDTO);
}
