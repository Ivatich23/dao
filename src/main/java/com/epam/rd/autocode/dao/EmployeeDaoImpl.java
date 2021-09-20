package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeDaoImpl implements EmployeeDao {
    public ResultSet executeQuery(String inquiry) {
        ResultSet resultSet = null;
        try( final Connection conn = ConnectionSource.instance().createConnection();
             Statement statement = conn.createStatement()) {
            resultSet = statement.executeQuery(inquiry);
        } catch (SQLException e) {
            e.getSQLState();
        }
        return resultSet;
    }

    @Override
    public Optional<Employee> getById(BigInteger id) {
        ResultSet resultSet = executeQuery("SELECT * " +
                "FROM employee " +
                "WHERE id = " + id);
        Employee employee = null;
        FullName empName = null;
        try {
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
        } catch (SQLException e) {
            e.getSQLState();
        }
        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT *" +
                " FROM employee");
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
            }
        } catch (SQLException e) {
            e.getSQLState();
        }
        return employeeList;
    }

    @Override
    public Employee save(Employee employee) {
        ResultSet resultSet = executeQuery("SELECT * " +
                "FROM employee" );
        try {
            BigInteger empId = employee.getId();
            String empFirstName = resultSet.getString("FIRSTNAME");
            String empLastName = resultSet.getString("LASTNAME");
            String empMiddleName = resultSet.getString("MIDDLENAME");
            Position empPosition = Position.valueOf(resultSet.getString("POSITION"));
            Date date = resultSet.getDate("HIREDATE");
            LocalDate empHireDate = date.toLocalDate();
            BigDecimal empSalary = resultSet.getBigDecimal("SALARY");
            BigInteger managerId = new BigInteger(resultSet.getString("MANAGER"));
            BigInteger departmentId = new BigInteger(resultSet.getString("DEPARTMENT"));
            executeQuery("INSERT INTO employee " +
                            "(ID, FIRSTNAME, LASTNAME, MIDDLENAME," +
                    " POSITION, MANAGER, HIREDATE, SALARY, DEPARTMENT)"+
                    "VALUES("+empId+","+empFirstName+","+empLastName+","+empMiddleName+","+empPosition+
                    ","+managerId+","+empHireDate+","+empSalary+","+departmentId);


        } catch (SQLException e) {
            e.getSQLState();
        }
        return getById(employee.getId()).get();
    }

    @Override
    public void delete(Employee employee) {
        executeQuery("DELETE FROM employee WHERE id = " + employee.getId());
    }

    @Override
    public List<Employee> getByDepartment(Department department) {
        List<Employee> employeeList = getAll();
        return employeeList.stream()
                .filter(empDep -> empDep.getId().equals(department.getId()))
                .collect(Collectors.toList());


    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        return null;
    }
}
