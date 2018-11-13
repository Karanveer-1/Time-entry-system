package com.corejsf.timsheet.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

@ConversationScoped
public class TimesheetManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject TimesheetRowManager rowManager;
    @Inject EmployeeManager employeeManager;
    
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;

    public Timesheet find(int id) {
        //read
        return null;
    }

    public void persist(Timesheet emp) {
        //insert
    }

    public void merge(Timesheet emp) {
        //update
    }

    public void remove(Timesheet emp) {
        //delete
    }

    public List<Timesheet> getAll() {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<Timesheet> list = new ArrayList<Timesheet>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM TIMESHEET");
                    while (result.next()) {
                        Employee emp = employeeManager.find(result.getInt("EMPLOYEE_ID"));
                        List<TimesheetRow> rows = rowManager.find(result.getInt("EMPLOYEE_ID"), result.getDate("END_WEEK"));
                        list.add(new Timesheet(emp, result.getDate("END_WEEK"), rows));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }

        return list;
    }

}








