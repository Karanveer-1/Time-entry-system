package com.corejsf.timesheet.access;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;


@RequestScoped
public class EmployeeManager {
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;

    public Employee find(int id) {
        return null;
    }

    public void persist(Employee emp) {
    }

    public void merge(Employee emp) {
    }

    public void remove(Employee emp) {
    }

    public Employee[] getAll() {
        return null;
    }

}
