package com.example.r2dgbc.demo.controller;


import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateEmployeeRequest;
import com.example.r2dgbc.demo.repository.entity.Employee;
import com.example.r2dgbc.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping
    public Flux<EmployeeDto> getEmployees(@RequestParam(required = false) String position, @RequestParam(name = "fullTime", required = false) Boolean isFullTime) {
        return this.service.getEmployees(position, isFullTime);
    }

    @GetMapping("/{id}")
    public Mono<EmployeeDto> getEmployee(@PathVariable Long id) {
        return this.service.getEmployee(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return this.service.createEmployee(request);
    }

    @PutMapping("/{id}")
    public Mono<EmployeeDto> updateEmployee(@PathVariable Long id,  @RequestBody  EmployeeDto employee) {
        return this.service.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEmployee(@PathVariable Long id) {
        return this.service.deleteEmployee(id);
    }
}
