package com.example.r2dgbc.demo.services;


import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateEmployeeRequest;
import com.example.r2dgbc.demo.exceptions.EmployeeNotFoundException;
import com.example.r2dgbc.demo.repository.EmployeeRepository;
import com.example.r2dgbc.demo.repository.entity.Employee;
import com.example.r2dgbc.demo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository repository;
    @InjectMocks
    private EmployeeServiceImpl service;



    private Employee mockEmployee() {
        return Employee.builder()
                .id(1L)
                .firstName("jose")
                .lastName("valdez")
                .position("Software Developer")
                .fullTime(true)
                .build();
    }

    private EmployeeDto mockEmployeeDto() {
        return EmployeeDto.builder()
                .id(1L)
                .firstName("jose")
                .lastName("valdez")
                .position("Software Developer")
                .fullTime(true)
                .build();
    }



    @Test
    @DisplayName("Test 3 petición getEmployees(null, true) Should return 1 Employee")
    void test_GetEmployeesByFullTime_Should_GetEmployeesByFullTime_ReturnEmployees() {
        when(this.repository.findAllByFullTime(anyBoolean())).thenReturn(Flux.just(mockEmployee()));

        this.service.getEmployees(null, true)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 4 petición getEmployees(\"Software Developer\", true) Should return 1 Employee")
    void test_GetEmployeesByPositionAndFullTime_Should_GetEmployeesByPositionAndFullTime_ReturnEmployees() {
        when(this.repository.findAllByPositionAndFullTime(anyString(), anyBoolean())).thenReturn(Flux.just(mockEmployee()));

        this.service.getEmployees("Software Developer", true)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 1 petición getEmployees(null, null) Should return 1 Employee")
    void test_GetEmployees_Should_GetEmployees_ReturnEmployees() {
        when(this.repository.findAll()).thenReturn(Flux.just(mockEmployee()));

        this.service.getEmployees(null, null)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 2  petición getEmployees(\"Software Developer\", null) Should return 1 Employee")
    void test_GetEmployeesByPosition_Should_GetEmployeesByPosition_ReturnEmployees() {
        when(this.repository.findAllByPosition(anyString())).thenReturn(Flux.just(mockEmployee()));

        this.service.getEmployees("Software Developer", null)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    @DisplayName("Test 7 petición createEmployee(request) Should return an Employee")
    void test_CreateEmployee_Should_CreateEmployee_ReturnEmployee() {
        Employee newEmployee = Employee.builder()
                .firstName("Bob")
                .lastName("Walker")
                .position("Dog Walker")
                .fullTime(false)
                .build();

        when(this.repository.save(any(Employee.class))).thenReturn(Mono.just(newEmployee));

        this.service.createEmployee(new CreateEmployeeRequest("Bob", "Walker", "Dog Walker", false))
                .as(StepVerifier::create)
                .consumeNextWith(employee -> assertEquals(newEmployee, employee))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 8 petición updateEmployee(1, employee) Should return an updated Employee")
    void test_UpdateEmployee_Should_UpdateEmployee_ReturnEmployee() {
        EmployeeDto updatedEmployee = mockEmployeeDto();

        updatedEmployee.setFirstName("George");

        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockEmployee()));
        when(this.repository.save(any(Employee.class)).map(employee -> new EmployeeDto(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getPosition(), employee.isFullTime())) ).thenReturn(Mono.just(updatedEmployee));

        this.service.updateEmployee(1L, updatedEmployee).map(employee -> new EmployeeDto(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getPosition(), employee.isFullTime()))
                .as(StepVerifier::create)
                .consumeNextWith(employee -> assertEquals(updatedEmployee, employee))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 9 petición updateEmployee(2, employee) Should throw EmployeeNotFoundException")
    void test_UpdateEmployee_Should_UpdateEmployee_ThrowEmployeeNotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.service.updateEmployee(2L, mockEmployeeDto())
                .as(StepVerifier::create)
                .expectError(EmployeeNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 5 petición getEmployee(1) Should return an Employee")
    void test_GetEmployee_Should_Return_GetEmployee_Employee() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockEmployee()));

        this.service.getEmployee(1L)
                .as(StepVerifier::create)
                .consumeNextWith(employee -> assertEquals(mockEmployee(), employee))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 6 petición getEmployee(2) Should throw EmployeeNotFoundException")
    void test_GetEmployee_Should_GetEmployee_ThrowEmployeeNotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.service.getEmployee(2L)
                .as(StepVerifier::create)
                .expectError(EmployeeNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 10 petición deleteEmployee(1) Should complete")
    void test_DeleteEmployee_Should_DeleteEmployee_DeleteEmployee() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockEmployee()));
        when(this.repository.delete(any(Employee.class))).thenReturn(Mono.empty());

        this.service.deleteEmployee(1L)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 11 petición deleteEmployee(2) Should throw EmployeeNotFoundException")
    void test_DeleteEmployee_Should_DeleteEmployee_ReturnEmployeeNotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.service.deleteEmployee(2L)
                .as(StepVerifier::create)
                .expectError(EmployeeNotFoundException.class)
                .verify();
    }


}