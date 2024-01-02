package com.youcode.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDto {

    @NotBlank(message = "Name is required")
    private String name;
}
