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


public class EmployeeDaoImpl implements EmployeeDao {
    public ResultSet executeQuery(String inquiry) {
        ResultSet resultSet = null;
        try (final Connection conn = ConnectionSource.instance().createConnection();
             Statement statement = conn.createStatement()) {
            resultSet = statement.executeQuery(inquiry);
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public Optional<Employee> getById(BigInteger id) {
        String sql = "SELECT * " +
                "FROM employee " +
                "WHERE id = ? ";
        Employee employee = null;
        FullName empName;
        try (final Connection conn = ConnectionSource.instance().createConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, new BigDecimal(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                BigInteger empId = new BigInteger(resultSet.getString("ID"));
                String empFirstName = resultSet.getString("FIRSTNAME");
                String empLastName = resultSet.getString("LASTNAME");
                String empMiddleName = resultSet.getString("MIDDLENAME");
                empName = new FullName(empFirstName, empLastName, empMiddleName);
                Position empPosition = Position.valueOf(resultSet.getString("POSITION"));
                Date date = resultSet.getDate("HIREDATE");
                LocalDate empHireDate = date.toLocalDate();
                BigDecimal empSalary = resultSet.getBigDecimal("SALARY");
                String department = resultSet.getString("DEPARTMENT");
                BigInteger departmentId = department != null ? new BigInteger(department) : new BigInteger(String.valueOf(0));
                String manager = resultSet.getString("MANAGER");
                BigInteger managerId = manager != null ? new BigInteger(manager) : new BigInteger(String.valueOf(0));
                employee = new Employee(empId, empName, empPosition, empHireDate, empSalary, managerId, departmentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                String department = resultSet.getString("DEPARTMENT");
                BigInteger departmentId = department != null ? new BigInteger(department) : new BigInteger(String.valueOf(0));
                String manager = resultSet.getString("MANAGER");
                BigInteger managerId = manager != null ? new BigInteger(manager) : new BigInteger(String.valueOf(0));
                employee = new Employee(empId, empName, empPosition, empHireDate, empSalary, managerId, departmentId);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    @Override
    public Employee save(Employee employee) {
        try (final Connection conn = ConnectionSource.instance().createConnection()) {
            String sqlQuery = "INSERT INTO employee  VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setInt(1, employee.getId().intValue());
            prepareStatement.setString(2, employee.getFullName().getFirstName());
            prepareStatement.setString(3, employee.getFullName().getLastName());
            prepareStatement.setString(4, employee.getFullName().getMiddleName());
            prepareStatement.setString(5, String.valueOf(employee.getPosition()));
            prepareStatement.setInt(6, employee.getManagerId().intValue());
            prepareStatement.setDate(7, java.sql.Date.valueOf(employee.getHired()));
            prepareStatement.setDouble(8, employee.getSalary().doubleValue());
            prepareStatement.setInt(9, employee.getDepartmentId().intValue());
            int i = prepareStatement.executeUpdate();
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return getById(employee.getId()).get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getById(employee.getId()).get();
    }

    @Override
    public void delete(Employee employee) {
        try (final Connection conn = ConnectionSource.instance().createConnection()) {
            String sqlQuery = "DELETE FROM employee WHERE id = ?";
            PreparedStatement prepareStatement = conn.prepareStatement(sqlQuery);
            prepareStatement.setInt(1, employee.getId().intValue());
            prepareStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Employee> getByDepartment(Department department) {
        List<Employee> employeeList = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * " +
                "FROM EMPLOYEE e  " +
                "WHERE department =" + department.getId());
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
                employeeList.add(employee);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeeList;


    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        List<Employee> employeeList = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * " +
                "FROM EMPLOYEE " +
                "WHERE manager = " + employee.getId());
        try {
            while (resultSet.next()) {
                Employee manegerOfEmployee;
                FullName empName;
                BigInteger empId = new BigInteger(resultSet.getString("ID"));
                String empFirstName = resultSet.getString("FIRSTNAME");
                String empLastName = resultSet.getString("LASTNAME");
                String empMiddleName = resultSet.getString("MIDDLENAME");
                empName = new FullName(empFirstName, empLastName, empMiddleName);
                Position empPosition = Position.valueOf(resultSet.getString("POSITION"));
                Date date = resultSet.getDate("HIREDATE");
                LocalDate empHireDate = date.toLocalDate();
                BigDecimal empSalary = resultSet.getBigDecimal("SALARY");
                String department = resultSet.getString("DEPARTMENT");
                BigInteger departmentId = department != null ? new BigInteger(department) : new BigInteger(String.valueOf(0));
                String manager = resultSet.getString("MANAGER");
                BigInteger managerId = manager != null ? new BigInteger(manager) : new BigInteger(String.valueOf(0));
                manegerOfEmployee = new Employee(empId, empName, empPosition, empHireDate, empSalary, managerId, departmentId);
                employeeList.add(manegerOfEmployee);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeeList;
    }
}
