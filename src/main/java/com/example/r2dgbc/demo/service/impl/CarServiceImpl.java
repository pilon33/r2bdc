package com.example.r2dgbc.demo.service.impl;

import com.example.r2dgbc.demo.controller.dto.CarDto;
import com.example.r2dgbc.demo.repository.CarRepository;
import com.example.r2dgbc.demo.repository.entity.Car;
import com.example.r2dgbc.demo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public Mono<CarDto> getCar(Integer carId) {
        return carRepository.findById(carId)
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getKm()));
    }

    @Override
    public Mono<CarDto> createCar(CarDto carDto) {
        return carRepository.save(Car.builder()
                .brand(carDto.brand())
                .km(carDto.km())
                .build())
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getKm()));
    }

    @Override
    public Mono<CarDto> updateCar(Integer carId, CarDto carDto) {
        return carRepository.save(Car.builder()
                .id(carId)
                .brand(carDto.brand())
                .km(carDto.km())
                .build())
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getKm()));
    }

    @Override
    public Mono<Void> deleteCar(Integer carId) {
        return carRepository.deleteById(carId);
    }

    @Override
    public Flux<CarDto> getAllCars() {
        return carRepository.findAll()
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getKm()));
    }
}
