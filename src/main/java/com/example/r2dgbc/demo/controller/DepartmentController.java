package com.example.r2dgbc.demo.controller;


import com.example.r2dgbc.demo.controller.dto.DepartmentDto;
import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateDepartmentRequest;
import com.example.r2dgbc.demo.repository.entity.Department;
import com.example.r2dgbc.demo.repository.entity.Employee;
import com.example.r2dgbc.demo.service.DepartmantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmantService service;

    @GetMapping
    public Flux<DepartmentDto> getDepartments() {
        return this.service.getDepartments();
    }

    @GetMapping("/{id}")
    public Mono<DepartmentDto> getDepartment(@PathVariable Long id) {
        return this.service.getDepartment(id);
    }

    @GetMapping("/{id}/employees")
    public Flux<EmployeeDto> getDepartmentEmployees(@PathVariable Long id, @RequestParam(name = "fullTime", required = false) Boolean isFullTime) {
        return this.service.getDepartmentEmployees(id, isFullTime);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DepartmentDto> createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return this.service.createDepartment(request);
    }

    @PutMapping("/{id}")
    public Mono<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto department) {
        return this.service.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteDepartment(@PathVariable Long id) {
        return this.service.deleteDepartment(id);
    }
}
