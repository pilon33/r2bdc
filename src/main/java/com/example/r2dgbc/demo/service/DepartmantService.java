package com.example.r2dgbc.demo.service;

import com.example.r2dgbc.demo.controller.dto.DepartmentDto;
import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateDepartmentRequest;
import com.example.r2dgbc.demo.repository.entity.Department;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepartmantService {
    Flux<DepartmentDto> getDepartments();
    Mono<DepartmentDto> getDepartment(Long id);
    Flux<EmployeeDto> getDepartmentEmployees(Long id, Boolean isFullTime);
    Mono<DepartmentDto> createDepartment(CreateDepartmentRequest request);
    Mono<DepartmentDto> updateDepartment(Long id, DepartmentDto department);
    Mono<Void> deleteDepartment(Long id);
}
