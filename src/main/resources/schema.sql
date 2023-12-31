CREATE TABLE IF NOT EXISTS departments(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS employees(
    id           BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL UNIQUE,
    last_name    VARCHAR(255) NOT NULL UNIQUE,
    position     VARCHAR(255) NOT NULL,
    is_full_time BOOLEAN      NOT NULL
);

create table  IF NOT EXISTS car (
                     id BIGSERIAL PRIMARY KEY,
                     brand varchar(255),
                     km integer
);

CREATE TABLE IF NOT EXISTS department_employees(
    department_id BIGSERIAL REFERENCES departments (id),
    employee_id   BIGSERIAL UNIQUE REFERENCES employees (id),
    PRIMARY KEY(department_id, employee_id)
);

CREATE TABLE IF NOT EXISTS department_managers(
    department_id BIGSERIAL UNIQUE REFERENCES departments (id),
    employee_id   BIGSERIAL UNIQUE REFERENCES employees (id),
    car_id  integer REFERENCES car (id),
    PRIMARY KEY(department_id, employee_id)
);

