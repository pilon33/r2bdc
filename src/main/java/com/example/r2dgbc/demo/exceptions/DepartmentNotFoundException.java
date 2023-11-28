package com.example.r2dgbc.demo.exceptions;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(Long id) {
        super(String.format("Department not found. Id: %d", id));
    }
}
