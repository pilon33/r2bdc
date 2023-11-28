package com.example.r2dgbc.demo.service.impl;



import com.example.r2dgbc.demo.controller.dto.EmployeeDto;
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
    public Flux <EmployeeDto> getEmployees(String position, Boolean isFullTime) {
        Flux <EmployeeDto> empleadoDto = this.repository.findAll()
                .map(emp -> new EmployeeDto(emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getPosition(), emp.isFullTime()));

        if (position != null) {
            empleadoDto = empleadoDto.filter(e -> e.getPosition().equals(position));
        }
        if (isFullTime != null) {
            empleadoDto = empleadoDto.filter(e -> e.isFullTime() == isFullTime);
        }

        return empleadoDto;
    }


    /**
     * Returns an Employee by ID.
     *
     * @param id Employee ID
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <EmployeeDto> getEmployee(Long id) {
        return this.repository.findById(id)
                .map(emp -> new EmployeeDto(emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getPosition(), emp.isFullTime()))
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
    }

    /**
     * Creates and returns a new Employee.
     *
     * @param request {@link CreateEmployeeRequest}
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <EmployeeDto> createEmployee(CreateEmployeeRequest request) {
        return this.repository.save(
                Employee.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .position(request.position())
                        .fullTime(request.isFullTime())
                        .build()).map(emp -> new EmployeeDto(emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getPosition(), emp.isFullTime())) ;

    }

    /**
     * Updates and returns an Employee.
     *
     * @param id       Employee ID
     * @param employee {@link Employee}
     * @return Mono of {@link Employee}
     */
    @Override
    public Mono <EmployeeDto> updateEmployee(Long id, EmployeeDto employee) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id))) // Manejar empleado no encontrado
                .flatMap(emp -> {
                    // Actualizar propiedades del empleado existente
                    emp.setFirstName(employee.getFirstName());
                    emp.setLastName(employee.getLastName());
                    emp.setPosition(employee.getPosition());
                    emp.setFullTime(employee.isFullTime());
                    return this.repository.save(emp);
                })
                .map(updatedEmp -> new EmployeeDto(updatedEmp.getId(), updatedEmp.getFirstName(), updatedEmp.getLastName(), updatedEmp.getPosition(), updatedEmp.isFullTime()));
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
