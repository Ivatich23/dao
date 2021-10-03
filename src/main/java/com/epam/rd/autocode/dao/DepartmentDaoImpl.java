package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DepartmentDaoImpl implements DepartmentDao {
    public ResultSet executeQuery(String inquiry) {
        ResultSet resultSet = null;
        try (final Connection conn = ConnectionSource.instance().createConnection();
             Statement statement = conn.createStatement()) {
            resultSet = statement.executeQuery(inquiry);
        } catch (SQLException e) {
            e.getSQLState();
        }
        return resultSet;
    }

    public List<Employee> getByDepartment(Department department) {
        List<Employee> employeeList = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * " +
                "FROM EMPLOYEE e  JOIN department d" +
                " ON e.department=d.id");
        FullName empName;
        Employee employee;
        try {
            while (resultSet.next()) {
                BigInteger empId = new BigInteger(resultSet.getString("ID"));
                String empFirstName = resultSet.getString("FIRSTNAME");
                String empLastName = resultSet.getString("LASTNAME");
                String empMiddleName = resultSet.getString("MIDDLENAME");
                empName = new FullName(empFirstName, empLastName, empMiddleName);
                Position empPosition = Position.valueOf(resultSet.getString("POSITION"));
                Date date = resultSet.getDate("HIREDATE");
                LocalDate empHireDate = date.toLocalDate();
                BigDecimal empSalary = resultSet.getBigDecimal("SALARY");
                BigInteger managerId = new BigInteger(resultSet.getString("MANAGER"));
                BigInteger departmentId = new BigInteger(resultSet.getString("DEPARTMENT"));
                employee = new Employee(empId, empName, empPosition, empHireDate, empSalary, managerId, departmentId);
                employeeList.add(employee);
                if(employee.getId().equals(department.getId())){
                    employeeList.add(employee);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeeList;
    }


    @Override
    public Optional<Department> getById(BigInteger id) {
        Department dep = null;
        ResultSet resultSet = executeQuery(("SELECT * " +
                "FROM department " +
                "WHERE id = " + id));
        try {
            BigInteger depId = BigInteger.valueOf(resultSet.getInt("ID"));
            String depName = resultSet.getString("NAME");
            String depLocation = resultSet.getString("LOCATION");
            dep = new Department(depId, depName, depLocation);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(dep);
    }

    @Override
    public List<Department> getAll() {
        List<Department> departmentList = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * " +
                "From department");
        BigInteger depId;
        String depName;
        String depLocation;
        try {
            while (resultSet.next()) {
                depId = new BigInteger(resultSet.getString("ID"));
                depName = resultSet.getString("NAME");
                depLocation = resultSet.getString("LOCATION");
                Department department = new Department(depId, depName, depLocation);
                departmentList.add(department);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return departmentList;
    }

    @Override
    public Department save(Department department) {
        BigInteger depId = department.getId();
        String depName = department.getName();
        String depLocation = department.getLocation();
        executeQuery("INSERT INTO department " +
                "(ID, NAME, LOCATION,)" +
                "VALUES(" + depId + "," + depName + "," + depLocation);

        return getById(department.getId()).get();

    }

    @Override
    public void delete(Department department) {
        executeQuery("DELETE " +
                "FROM department " +
                "WHERE id = " + department.getId());
    }
}
