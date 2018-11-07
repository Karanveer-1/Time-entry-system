package com.corejsf.timesheet.access;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Credentials;

@RequestScoped
public class TimesheetManager {
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;

    public Credentials find(int id) {
        return null;
    }

    public void persist(Credentials cred) {
    }

    public void merge(Credentials cred) {
    }

    public void remove(Credentials cred) {
    }

    public Credentials[] getAll() {
        return null;
    }
}
