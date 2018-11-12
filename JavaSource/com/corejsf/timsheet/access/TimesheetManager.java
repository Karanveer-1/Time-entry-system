package com.corejsf.timsheet.access;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;

@ConversationScoped
public class TimesheetManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Resource(mappedName = "java:jboss/datasources/inventory")
    private DataSource ds;

    public Employee find(int id) {
        //read
        return null;
    }

    public void persist(Employee emp) {
        //insert
    }

    public void merge(Employee emp) {
        //update
    }

    public void remove(Employee emp) {
        //delete
    }

    public Employee[] getAll() {
        return null;
    }

}
