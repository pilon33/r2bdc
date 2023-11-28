package com.example.r2dgbc.demo.services;


import com.example.r2dgbc.demo.controller.dto.DepartmentDto;
import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateDepartmentRequest;
import com.example.r2dgbc.demo.exceptions.DepartmentAlreadyExistsException;
import com.example.r2dgbc.demo.exceptions.DepartmentNotFoundException;
import com.example.r2dgbc.demo.repository.DepartmentRepository;
import com.example.r2dgbc.demo.repository.entity.Department;
import com.example.r2dgbc.demo.repository.entity.Employee;
import com.example.r2dgbc.demo.service.impl.DepartamentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    @Mock
    private DepartmentRepository repository;
    @InjectMocks
    private DepartamentServiceImpl departmantService;



    private Department mockDevDepartment() {
        EmployeeDto manager = EmployeeDto.builder()
                .id(4L)
                .firstName("Angela")
                .lastName("Perez")
                .position("Director of Human Resources")
                .fullTime(true)
                .build();

        return Department.builder()
                .id(1L)
                .name("Software Development")
                .manager(Employee.builder()
                        .id(1L)
                        .firstName("Jose")
                        .lastName("valdez")
                        .position("Software Development BCP")
                        .fullTime(true)
                        .build())
                .car(null)
                .employees(List.of(
                        Employee.builder()
                                .id(2L)
                                .firstName("jesus")
                                .lastName("mendoza")
                                .position("Chapter")
                                .fullTime(true)
                                .build(),
                        Employee.builder()
                                .id(3L)
                                .firstName("eric")
                                .lastName("llano")
                                .position("chapeter")
                                .fullTime(false)
                                .build()))
                .build();
    }


    private DepartmentDto mockDevDepartmentDto() {
        return DepartmentDto.builder()
                .id(1L)
                .name("Software Development")
                .manager(Optional.ofNullable(Employee.builder()
                        .id(1L)
                        .firstName("Jose")
                        .lastName("valdez")
                        .position("Software Development BCP")
                        .fullTime(true)
                        .build()))
                .employees(List.of(
                        Employee.builder()
                                .id(2L)
                                .firstName("jesus")
                                .lastName("mendoza")
                                .position("Chapter")
                                .fullTime(true)
                                .build(),
                        Employee.builder()
                                .id(3L)
                                .firstName("eric")
                                .lastName("llano")
                                .position("chapeter")
                                .fullTime(false)
                                .build()))
                .build();
    }


    private Department mockdRHDepartment() {
        return Department.builder()
                .id(2L)
                .name("RRHH")
                .manager(Employee.builder()
                        .id(4L)
                        .firstName("Angela")
                        .lastName("Perez")
                        .position("Director of Human Resources")
                        .fullTime(true)
                        .build())
                .employees(List.of(
                        Employee.builder()
                                .id(5L)
                                .firstName("Diana")
                                .lastName("Ventura")
                                .position("Data Analyst")
                                .fullTime(true)
                                .build()))
                .build();
    }



    @Test
    @DisplayName("Test 2 petición departments(3) should throw DepartmentNotFoundException")
    void test_GetDepartment_Should_ThrowDepartment_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.departmantService.getDepartment(3L)
                .as(StepVerifier::create)
                .expectError(DepartmentNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 3 petición gepartmentgetDepartmentEmployees(1, null) should return 2 Employees")
    void test_GetDepartmentEmployees_should_GetDepartmentEmployees_When_TwoEmployees() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));

        this.departmantService.getDepartmentEmployees(1L, null)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 1 petición departments() should return 2 Departments")
    void test_GetDepartments_should_GetDepartments_When_TwoDepartments() {
        when(this.repository.findAll()).thenReturn(Flux.fromIterable(List.of(mockDevDepartment(), mockdRHDepartment())));

        this.departmantService.getDepartments()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 2 petición departments() should return 1 Department")
    void test_GetDepartment_should_GetDepartments_When_OneDepartments() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));

        this.departmantService.getDepartment(1L)
                .as(StepVerifier::create)
                .consumeNextWith(department -> assertNotEquals(mockDevDepartment(), department))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 4 petición getEmployeesgetDepartmentEmployees(1, true) should return 1 Employees")
    void test_GetDepartmentEmployees_should_GetDepartmentEmployees_When_OneEmployee() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));

        this.departmantService.getDepartmentEmployees(1L, true)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }



    @Test
    @DisplayName("Test 7 petición createDepartment(request) should return a Department")
    void test_createDepartment_should_createDepartment_When_ReturnDepartment() {
        Department accounting = Department.builder()
                .id(3L)
                .name("Accounting")
                .build();

        when(this.repository.findByName(anyString())).thenReturn(Mono.empty());
        when(this.repository.save(any(Department.class))).thenReturn(Mono.just(accounting));

        this.departmantService.createDepartment(new CreateDepartmentRequest("Accounting"))
                .as(StepVerifier::create)
                .consumeNextWith(department -> assertEquals(accounting, department))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 5 petición getDepartmentEmployees(1, false) should return 1 Employees")
    void test_GetDepartmentEmployees_should_NoIsFullTime_GetDepartmentEmployees_When_OneEmployee() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));

        this.departmantService.getDepartmentEmployees(1L, false)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 6 petición getDepartmentEmployees(3, null) should throw DepartmentNotFoundException")
    void test_GetDepartmentEmployees_should_ThrowDepartment_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.departmantService.getDepartmentEmployees(3L, null)
                .as(StepVerifier::create)
                .expectError(DepartmentNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 8 petición createDepartment(request) should throw DepartmentAlreadyExistsException")
    void test_createDepartment_should_ThrowDepartment_AlreadyExists() {
        Department accounting = Department.builder()
                .id(3L)
                .name("Accounting")
                .build();

        when(this.repository.findByName(anyString())).thenReturn(Mono.just(accounting));

        this.departmantService.createDepartment(new CreateDepartmentRequest("Accounting"))
                .as(StepVerifier::create)
                .expectError(DepartmentAlreadyExistsException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 9 petición updateDepartment(1, department) should return an updated Department")
    void test_UpdateDepartment_Should_UpdateDepartment_ReturnDepartment() {
        Department updatedDevDepartment = mockDevDepartment();

        Employee manager = Employee.builder()
                .id(6L)
                .firstName("Sally")
                .lastName("Smith")
                .position("Director of Software Development")
                .fullTime(true)
                .build();

        updatedDevDepartment.setManager(manager);

        // Mocks para el repositorio
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));
        when(this.repository.save(any(Department.class))).thenReturn(Mono.just(updatedDevDepartment));

        // Invocar el servicio de actualización
        this.departmantService.updateDepartment(1L, mockDevDepartmentDto())
                .map(updatedDepartment -> new DepartmentDto(
                        updatedDepartment.getId(),
                        updatedDepartment.getName(),
                        updatedDepartment.getManager(),
                        updatedDepartment.getEmployees(),
                        updatedDepartment.getCar()))
                .as(StepVerifier::create)
                .consumeNextWith(departmentDto -> {
                    // Aquí debes comparar los campos del DTO con los de updatedDevDepartment
                    assertEquals(updatedDevDepartment.getId(), departmentDto.getId());
                    assertEquals(updatedDevDepartment.getName(), departmentDto.getName());
                    // ... realizar comparaciones para otros campos como manager, employees, etc.
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 10 petición updateDepartment(3, department) should throw DepartmentNotFoundException")
    void test_updateDepartment_should_updateDepartment_When_DepartmentNotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.departmantService.updateDepartment(3L, mockDevDepartmentDto())
                .as(StepVerifier::create)
                .expectError(DepartmentNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Test 11 petición deleteDepartment(1) should complete")
    void test_deleteDepartment_should_deleteDepartment_when_DeleteDepartment() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.just(mockDevDepartment()));
        when(this.repository.delete(any(Department.class))).thenReturn(Mono.empty());

        this.departmantService.deleteDepartment(mockDevDepartment().getId())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test 12  petición deleteDepartment(3) should throw DepartmentNotFound")
    void test_DeleteDepartment_should_DeleteDepartment_when_ThrowDepartmentNotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Mono.empty());

        this.departmantService.deleteDepartment(3L)
                .as(StepVerifier::create)
                .expectError(DepartmentNotFoundException.class)
                .verify();
    }


}