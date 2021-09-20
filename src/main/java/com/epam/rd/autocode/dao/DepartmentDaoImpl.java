package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        ResultSet result = executeQuery("SELECT * " +
                "FROM DEPARTMENT");

        try {
            while (result.next()) {

            }


        } catch (SQLException throwables) {
            throwables.getSQLState();
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
            BigInteger depId = BigInteger.valueOf(resultSet.getInt(1));
            String depName = resultSet.getString(2);
            String depLocation = resultSet.getString(3);
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

        return getById(department.getId()).get();
        //
    }

    @Override
    public void delete(Department department) {
        executeQuery("DELETE " +
                "FROM department " +
                "WHERE id = " + department.getId());
    }
}
