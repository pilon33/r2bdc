package com.example.r2dgbc.demo.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("departments")
public class Department {
    @Id
    private Long id;
    private String name;
    private Employee manager;
    private Car car;

    @Builder.Default
    private List<Employee> employees = new ArrayList<>();


    public Optional<Employee> getManager(){
        return Optional.ofNullable(this.manager);
    }

    public Optional<Car> getCar(){
        return Optional.ofNullable(this.car);
    }


    public static Mono<Department> fromRows(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) return Mono.empty();
        return Mono.just(Department.builder()
                .id((Long.parseLong(rows.get(0).get("d_id").toString())))
                .name((String) rows.get(0).get("d_name"))
                .manager(Employee.managerFromRow(rows.get(0)))
                .car(Car.carFromRow(rows.get(0)))
                .employees(rows.stream()
                        .map(Employee::fromRow)
                        .filter(Objects::nonNull)
                        .toList())
                .build());
    }
}
