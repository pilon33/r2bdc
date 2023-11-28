package com.example.r2dgbc.demo.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super(String.format("Employee not found. Id: %d", id));
    }
}
