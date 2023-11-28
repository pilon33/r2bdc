package com.example.r2dgbc.demo.controller.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateEmployeeRequest(@NotNull @NotEmpty String firstName, @NotNull @NotEmpty String lastName,
                                    @NotNull @NotEmpty String position, @NotNull boolean isFullTime) {
}
