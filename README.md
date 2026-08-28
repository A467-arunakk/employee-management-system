CREATE DATABASE IF NOT EXISTS employee_management;
USE employee_management;
CREATE TABLE IF NOT EXISTS employees (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
department VARCHAR(50),
salary DOUBLE
);
bash
git clone https://github.com/A467-arunakk/employee-management-system
