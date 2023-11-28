package com.example.r2dgbc.demo.service;

import com.example.r2dgbc.demo.controller.dto.request.CreateEmployeeRequest;
import com.example.r2dgbc.demo.repository.entity.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Flux<Employee> getEmployees(String position, Boolean isFullTime);
    Mono<Employee> getEmployee(Long id);

    Mono<Employee> createEmployee(CreateEmployeeRequest request);

    Mono<Employee> updateEmployee(Long id, Employee employee);

    Mono<Void> deleteEmployee(Long id);


}
