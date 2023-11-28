package com.example.r2dgbc.demo.service.impl;



import com.example.r2dgbc.demo.controller.dto.request.CreateEmployeeRequest;
import com.example.r2dgbc.demo.exceptions.EmployeeNotFoundException;
import com.example.r2dgbc.demo.repository.EmployeeRepository;
import com.example.r2dgbc.demo.repository.entity.Employee;
import com.example.r2dgbc.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    /**
     * Returns all Employees, optionally filtered by position or full time status.
     *
     * @param position   Employee Position
     * @param isFullTime Is Employee Full Time
     * @return Flux of {@link Employee}
     */
    @Override
    public Flux <Employee> getEmployees(String position, Boolean isFullTime) {
        if (position != null) {
            if (isFullTime != null) {
                return this.repository.findAllByPositionAndFullTime(position, isFullTime);
            } else {
                return this.repository.findAllByPosition(position);
            }
        } else {
            if (isFullTime != null) {
                return this.repository.findAllByFullTime(isFullTime);
            } else {
                return this.repository.findAll();
            }
        }
    }

    /**
     * Returns an Employee by ID.
     *
     * @param id Employee ID
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <Employee> getEmployee(Long id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
    }

    /**
     * Creates and returns a new Employee.
     *
     * @param request {@link CreateEmployeeRequest}
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <Employee> createEmployee(CreateEmployeeRequest request) {
        return this.repository.save(
                Employee.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .position(request.position())
                        .fullTime(request.isFullTime())
                        .build());
    }

    /**
     * Updates and returns an Employee.
     *
     * @param id       Employee ID
     * @param employee {@link Employee}
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <Employee> updateEmployee(Long id, Employee employee) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                .flatMap(existingEmployee -> {
                    existingEmployee.setFirstName(employee.getFirstName());
                    existingEmployee.setLastName(employee.getLastName());
                    existingEmployee.setPosition(employee.getPosition());
                    existingEmployee.setFullTime(employee.isFullTime());
                    return this.repository.save(existingEmployee);
                });
    }
    /**
     * Deletes an Employee by ID.
     *
     * @param id Employee ID
     * @return Mono of {@link Void}
     */
    @Override
    public Mono <Void> deleteEmployee(Long id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                .flatMap(this.repository::delete)
                .then();
    }
}
