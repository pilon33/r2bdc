package com.example.r2dgbc.demo.repository;

import com.example.r2dgbc.demo.repository.entity.Department;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface DepartmentRepository {
    Flux<Department> findAll();

    Mono<Department> findById(long id);

    Mono<Department> findByName(String name);

    Mono<Department> save(Department department);

    Mono<Void> delete(Department department);
}
