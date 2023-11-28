package com.example.r2dgbc.demo.service.impl;


import com.example.r2dgbc.demo.controller.dto.DepartmentDto;
import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
import com.example.r2dgbc.demo.controller.dto.request.CreateDepartmentRequest;
import com.example.r2dgbc.demo.exceptions.DepartmentAlreadyExistsException;
import com.example.r2dgbc.demo.exceptions.DepartmentNotFoundException;

import com.example.r2dgbc.demo.repository.DepartmentRepository;

import com.example.r2dgbc.demo.repository.entity.Department;
import com.example.r2dgbc.demo.repository.entity.Employee;

import com.example.r2dgbc.demo.service.DepartmantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class DepartamentServiceImpl implements DepartmantService {

    private final DepartmentRepository repository;

    /**
     * Returns all Departments.
     *
     * @return Flux of {@link Department}
     */
    @Override
    public Flux <DepartmentDto> getDepartments() {

        return repository.findAll()
                .flatMap(department -> Mono.just(new DepartmentDto(department.getId(), department.getName(),department.getManager(),department.getEmployees(),department.getCar())))
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(null)));

    }

    /**
     * Returns a Department by ID.
     *
     * @param id Department ID
     * @return Mono of {@link Department}
     */
    @Override
    public Mono <DepartmentDto> getDepartment(Long id) {
        return this.repository.findById(id)
                .flatMap(department -> Mono.just(new DepartmentDto(department.getId(), department.getName(),department.getManager(),department.getEmployees(),department.getCar())))
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)));
    }

    /**
     * Returns the Employees of a Department by ID.
     *
     * @param id         Department ID
     * @param isFullTime Filter employees on full time status
     * @return Flux of {@link Employee}
     */
//    @Override
//    public Flux <EmployeeDto> getDepartmentEmployees(Long id, Boolean isFullTime) {
//        if (isFullTime != null) {
//            return this.repository.findById(id)
//                    .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
//                    .flatMapMany(department ->
//                            Flux.fromStream(department.getEmployees()
//                                    .stream()
//                                    .filter(employee -> employee.isFullTime() == isFullTime)));
//        } else {
//            return this.repository.findById(id)
//                    .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
//                    .flatMapMany(department -> Flux.fromIterable(department.getEmployees()));
//        }
//    }

    @Override
    public Flux<EmployeeDto> getDepartmentEmployees(Long id, Boolean isFullTime) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                .flatMapMany(department -> {
                    Stream <Employee> employeesStream = department.getEmployees().stream();
                    if (isFullTime != null) {
                        employeesStream = employeesStream.filter(employee -> employee.isFullTime() == isFullTime);
                    }
                    return Flux.fromStream(employeesStream)
                            .map(emp -> new EmployeeDto(emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getPosition(), emp.isFullTime()));
                });
    }



    /**
     * Creates and returns a new Department.
     *
     * @param request {@link CreateDepartmentRequest}
     * @return Mono of {@link Department}
     */
    @Override
    public Mono<DepartmentDto> createDepartment(CreateDepartmentRequest request) {
        return this.repository.findByName(request.name())
                .flatMap(department -> Mono.error(new DepartmentAlreadyExistsException(department.getName())))
                .defaultIfEmpty(Department.builder().name(request.name()).build()).cast(Department.class)
                .flatMap(this.repository::save)
                .map(department -> new DepartmentDto(department.getId(), department.getName(),department.getManager(),department.getEmployees(),department.getCar()));
    }

    /**
     * Updates and returns a Department.
     *
     * @param id         Department ID
     * @param departmentDto {@link Department}
     * @return Mono of {@link Department}
     */
//    @Override
//    public Mono <Department> updateDepartment(Long id, Department department) {
//        return this.repository.findById(id)
//                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
//                .doOnNext(currentDepartment -> {
//                    currentDepartment.setName(department.getName());
//
//                    if(department.getManager().isPresent()){
//                        currentDepartment.setManager(department.getManager().get());
//                    }
//
//                    currentDepartment.setEmployees(department.getEmployees());
//                })
//                .flatMap(this.repository::save);
//    }

    @Override
    public Mono<DepartmentDto> updateDepartment(Long id, DepartmentDto departmentDto) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                .flatMap(currentDepartment -> {
                    currentDepartment.setName(departmentDto.getName());

                    departmentDto.getManager().ifPresent(currentDepartment::setManager);

                    currentDepartment.setEmployees(departmentDto.getEmployees());

                    return this.repository.save(currentDepartment);
                })
                .map(updatedDepartment -> new DepartmentDto(
                updatedDepartment.getId(),
                updatedDepartment.getName(),
                updatedDepartment.getManager(),
                updatedDepartment.getEmployees(),
                updatedDepartment.getCar()));
    }
    /**
     * Deletes a Department by ID.
     *
     * @param id Department ID
     * @return Mono of {@link Void}
     */
    @Override
    public Mono<Void> deleteDepartment(Long id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                .flatMap(this.repository::delete)
                .then();
    }
}
