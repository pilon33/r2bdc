DELETE FROM department_managers;
DELETE FROM department_employees;
DELETE FROM departments;
DELETE FROM employees;

ALTER SEQUENCE departments_id_seq RESTART WITH 1;
ALTER SEQUENCE employees_id_seq RESTART WITH 1;

INSERT INTO departments(name)
VALUES ('Tribu BCP'),
       ('RRHH');

INSERT INTO employees(first_name, last_name, position, is_full_time)
VALUES ('jesus', 'Quispe', 'Chapter', true),
       ('jose', 'Valdez', 'Software Developer', true),
       ('eric', 'Llanos', 'Software Tester', false),
       ('rodolfo', 'ariel', 'Director of Human Resources', true),
       ('Erica', 'Gimenez', 'Data Analyst', true);

INSERT INTO car (brand,km) VALUES
    ('Ford',4500),
    ('Toyota',1000),
    ('Nissan',2000),
    ('Chevrolet',3000),
    ('Hyundai',4000)    ;

INSERT INTO department_managers(department_id, employee_id ,car_id)
VALUES (1, 1 ,1 ),
       (2, 2,1);

INSERT INTO department_employees(department_id, employee_id)
VALUES (1, 2),
       (1, 3),
       (2, 5);


