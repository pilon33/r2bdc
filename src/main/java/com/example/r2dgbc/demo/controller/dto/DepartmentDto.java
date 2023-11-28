package com.example.r2dgbc.demo.controller.dto;

import com.example.r2dgbc.demo.repository.entity.Car;
import com.example.r2dgbc.demo.repository.entity.Employee;

import java.util.List;
import java.util.Optional;

public record DepartmentDto(Long id , String name, Optional<Employee> manager, List<Employee> employees, Optional <Car> car)
{

}


