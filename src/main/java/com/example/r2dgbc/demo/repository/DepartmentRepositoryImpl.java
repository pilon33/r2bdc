package com.example.r2dgbc.demo.repository;


import com.example.r2dgbc.demo.repository.entity.Department;
import com.example.r2dgbc.demo.repository.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {
    private final EmployeeRepository employeeRepository;
    private final DatabaseClient client;
    private static final String SELECT_QUERY = """
            SELECT c.id c_id,c.brand,c.km, d.id d_id, d.name d_name, m.id m_id, m.first_name m_firstName, m.last_name m_lastName,
                m.position m_position, m.is_full_time m_isFullTime, e.id e_id, e.first_name e_firstName,
                e.last_name e_lastName, e.position e_position, e.is_full_time e_isFullTime
            FROM departments d
            LEFT JOIN department_managers dm ON dm.department_id = d.id
            LEFT JOIN employees m ON m.id = dm.employee_id
            LEFT JOIN car c on c.id = dm.employee_id
            LEFT JOIN department_employees de ON de.department_id = d.id
            LEFT JOIN employees e ON e.id = de.employee_id
            """;
    private final CarRepository carRepository;

    /**
     * Returns all Departments.
     *
     * @return Flux of {@link Department}
     */
    @Override
    public Flux<Department> findAll() {
        String query = String.format("%s ORDER BY d.id", SELECT_QUERY);

        return client.sql(query)
                .fetch()
                .all()// Obtenga todos los datos que le solicitamos
                .bufferUntilChanged(result -> result.get("d_id"))//Agrupe las filas en una lista basada en departamental.id
                .flatMap(Department::fromRows); //Convierta cada conjunto de resultados en unDepartment
    }

    /**
     * Returns a Department by ID.
     *
     * @param id Department ID
     * @return Mono of {@link Department}
     */
    @Override
    public Mono<Department> findById(long id) {
        String query = String.format("%s WHERE d.id = :id", SELECT_QUERY);

        return client.sql(query)
                .bind("id", id)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("d_id"))
                .flatMap(Department::fromRows)
                .singleOrEmpty(); //Un nuevo método al final garantiza que solo devolvamos un único resultado o Mono.empty()
    }

    /**
     * Returns a Department by name.
     *
     * @param name Department Name
     * @return Mono of {@link Department}
     */
    @Override
    public Mono<Department> findByName(String name) {
        String query = String.format("%s WHERE d.name = :name", SELECT_QUERY);

        return client.sql(query)
                .bind("name", name)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("d_id"))
                .flatMap(Department::fromRows)
                .singleOrEmpty();
    }

    /**
     * Saves and returns a Department.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    @Override
    @Transactional
    public Mono <Department> save(Department department) {
//        Guarde o actualice el car
//        Guardar o actualizar el administradorEmployee
//        Guardar o actualizar cada empleado
//        Guarde o actualice el Department
//        Actualizar la relación entre DepartmentyManager
//        Actualizar la relación entre Departmenty Employees
        return this.saveDepartment(department)
                .flatMap(this::saveCar)
                .flatMap(this::saveManager)
                .flatMap(this::saveEmployees)
                .flatMap(this::deleteDepartmentManager)
                .flatMap(this::saveDepartmentManager)
                .flatMap(this::deleteDepartmentEmployees)
                .flatMap(this::saveDepartmentEmployees);
    }

    /**
     * Deletes a Department.
     *
     * @param department {@link Department}
     * @return Mono of {@link Void}
     */
    @Override
    @Transactional
    public Mono<Void> delete(Department department) {
        return this.deleteDepartmentManager(department)
                .flatMap(this::deleteDepartmentEmployees)
                .flatMap(this::deleteDepartment)
                .then();
    }

    /**
     * Saves a Department.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveDepartment(Department department) {
        if (department.getId() == null) {
            return client.sql("INSERT INTO departments(name) VALUES(:name)")
                    .bind("name", department.getName())
                    .filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
                    .fetch().first()
                    .doOnNext(result -> department.setId(Long.parseLong(result.get("id").toString())))
                    .thenReturn(department);
        } else {
            return this.client.sql("UPDATE departments SET name = :name WHERE id = :id")
                    .bind("name", department.getName())
                    .bind("id", department.getId())
                    .fetch().first()
                    .thenReturn(department);
        }
    }

    /**
     * Saves a Department Manager.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveManager(Department department) {
        return Mono.justOrEmpty(department.getManager())
                .flatMap(employeeRepository::save)
                .doOnNext(department::setManager)
                .thenReturn(department);
    }

    /**
     * Saves Department Employees.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveEmployees(Department department) {
        return Flux.fromIterable(department.getEmployees())
                .flatMap(this.employeeRepository::save)
                .collectList()
                .doOnNext(department::setEmployees)
                .thenReturn(department);
    }

    /**
     * Saves Department car.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveCar(Department department) {
        return Mono.justOrEmpty(department.getCar())
                .flatMap(carRepository::save)
                .doOnNext(department::setCar)
                .thenReturn(department);
    }

    /**
     * Saves the relationship between Department and Manager.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveDepartmentManager(Department department) {

        String query = "INSERT INTO department_managers(department_id, employee_id,car_id) VALUES (:id, :empId ,:carId)";
        //Una característica útil de Mono, es que devuelve 0 a 1 objetos,
        return Mono.justOrEmpty(department.getManager())        // por lo que si el 'administrador' está vacío, flatMap

                .flatMap(manager -> client.sql(query)
                        .bind("id", department.getId())
                        .bind("empId", manager.getId())
                        .bind("carId", 1)
                        .fetch().rowsUpdated()) // configurar la consulta, se ejecuta con fetch(),
                  // y luego se llama a rowsUpdated() para obtener un Mono<Integer>
                .thenReturn(department);        // no se llama y pasamos a devolverlo Departmental final del método.

    }

    /**
     * Saves the relationship between Department and Employees.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> saveDepartmentEmployees(Department department) {
        String query = "INSERT INTO department_employees(department_id, employee_id) VALUES (:id, :empId)";

        return Flux.fromIterable(department.getEmployees())
                .flatMap(employee -> client.sql(query)
                        .bind("id", department.getId())
                        .bind("empId", employee.getId())
                        .fetch().rowsUpdated())
                .collectList()
                .thenReturn(department);
    }

    /**
     * Deletes a Department.
     *
     * @param department {@link Department}
     * @return Mono of {@link Void}
     */
    private Mono<Void> deleteDepartment(Department department) {
        return client.sql("DELETE FROM departments WHERE id = :id")
                .bind("id", department.getId())
                .fetch().first()
                .then();
    }

    /**
     * Deletes the relationship between Department and Manager.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> deleteDepartmentManager(Department department) {
        String query = "DELETE FROM department_managers WHERE department_id = :departmentId OR employee_id = :managerId";

        return Mono.just(department)
                .flatMap(dep -> client.sql(query)
                        .bind("departmentId", dep.getId())
                        // Se usa bindNull para managerId en un tipo Long y luego se sobrescribe con el valor real de managerId. Si el departamento
                        // no tiene un manager (dep.getManager()), se usa un valor predeterminado de 0L como un caso especial.
                        .bindNull("managerId", Long.class)
                        .bind("managerId", dep.getManager().orElseGet(() -> Employee.builder().id(0L).build()).getId())
                        .fetch().rowsUpdated())
                .thenReturn(department);
    }

    /**
     * Deletes the relationship between Department and Employees.
     *
     * @param department {@link Department}
     * @return Mono of {@link Department}
     */
    private Mono<Department> deleteDepartmentEmployees(Department department) {
        String query = "DELETE FROM department_employees WHERE department_id = :id OR employee_id in (:ids)";

        //se extraen los IDs de los empleados de un objeto department y se almacenan en una lista.
        List<Long> employeeIds = department.getEmployees().stream().map(Employee::getId).toList();

        return Mono.just(department)
                .flatMap(dep -> client.sql(query)
                        .bind("id", department.getId())
                        .bind("ids", employeeIds.isEmpty() ? List.of(0) : employeeIds)
                        //Se vinculan los parámetros :id y :ids a los valores correspondientes. Si la lista employeeIds está vacía,
                        // se pasa una lista con un solo elemento, 0, para evitar errores SQL.
                        .fetch().rowsUpdated())
                .thenReturn(department);
    }
}
