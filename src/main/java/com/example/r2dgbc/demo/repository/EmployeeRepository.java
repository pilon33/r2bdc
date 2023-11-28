package com.example.r2dgbc.demo.repository;

import com.example.r2dgbc.demo.repository.entity.Employee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends R2dbcRepository<Employee, Long> {
    Flux<Employee> findAllByPosition(String position);
    Flux<Employee> findAllByFullTime(boolean isFullTime);
    Flux<Employee> findAllByPositionAndFullTime(String position, boolean isFullTime);
    Mono<Employee> findByFirstName(String firstName);
}
