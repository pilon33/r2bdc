package com.example.r2dgbc.demo.controller.dto;

import com.example.r2dgbc.demo.repository.entity.Car;
import com.example.r2dgbc.demo.repository.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Optional;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("employees")
public class DepartmentDto {
    @Id
    private Long id;
    private String name;
    private Optional<Employee> manager;
    private List<Employee> employees;
    private Optional<Car> car;


 //   public record DepartmentDto(Long id , String name, Optional<Employee> manager, List<Employee> employees, Optional <Car> car)
    {

    }


}