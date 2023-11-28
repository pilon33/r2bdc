package com.example.r2dgbc.demo.exceptions;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String name) {
        super(String.format("Department with name \"%s\" already exists.", name));
    }
}
