package com.example.r2dgbc.demo.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;



public record CreateDepartmentRequest(
        @NotNull(message = "Name can not be null") @NotEmpty(message = "Name can not be empty") String name) {
}
