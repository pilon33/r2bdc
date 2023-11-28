package com.example.r2dgbc.demo.service;

import com.example.r2dgbc.demo.controller.dto.DepartmentDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateDepartmentRequest;
import com.example.r2dgbc.demo.repository.entity.Department;
import com.example.r2dgbc.demo.repository.entity.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepartmantService {
    Flux<DepartmentDto> getDepartments();
    Mono<DepartmentDto> getDepartment(Long id);
    Flux<Employee> getDepartmentEmployees(Long id, Boolean isFullTime);
    Mono<Department> createDepartment(CreateDepartmentRequest request);
    Mono<Department> updateDepartment(Long id, Department department);
    Mono<Void> deleteDepartment(Long id);
}
