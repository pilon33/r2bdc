package com.example.r2dgbc.demo.repository;

import com.example.r2dgbc.demo.repository.entity.Car;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CarRepository extends R2dbcRepository<Car, Integer> {
}
