package com.example.r2dgbc.demo.service;

import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateEmployeeRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Flux  <EmployeeDto> getEmployees(String position, Boolean isFullTime);
    Mono<EmployeeDto> getEmployee(Long id);

    Mono<EmployeeDto> createEmployee(CreateEmployeeRequest request);

    Mono<EmployeeDto> updateEmployee(Long id, EmployeeDto employee);

    Mono<Void> deleteEmployee(Long id);


}
