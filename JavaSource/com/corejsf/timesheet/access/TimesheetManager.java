package com.corejsf.timesheet.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

/**
 * Handle CRUD actions for Timesheet class.
 * 
 * @author Karanveer Khanna
 * @version 1.0
 * 
 */
@ConversationScoped
public class TimesheetManager implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;
    /** Holds the reference for Timesheet Row Manager class. */
    @Inject private TimesheetRowManager rowManager;
    /** Holds the reference for Employee Manager class. */
    @Inject private EmployeeManager employeeManager;

    /**
     * Persist Timesheet into database. id must be unique.
     * 
     * @param newSheet the record to be persisted.
     * @return boolean, true if successful else false
     */
    public boolean persist(Timesheet newSheet) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO TIMESHEET VALUES (?, ?)");
                    stmt.setInt(1, newSheet.getEmployee().getEmpNumber());
                    stmt.setDate(2, new Date(newSheet.getEndWeek().getTime()));
                    stmt.executeUpdate();
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
            System.out.println("Error in persist " + newSheet);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Return Timesheet table as a list of Timesheet.
     * 
     * @return List of all records in Timesheet table and TimesheetRow Table
     */
    public List<Timesheet> getAll() {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<Timesheet> list = new ArrayList<Timesheet>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM TIMESHEET");
                    while (result.next()) {
                        Employee emp = employeeManager.
                                find(result.getInt("EMPLOYEE_ID"));
                        List<TimesheetRow> rows = rowManager.
                                find(result.getInt("EMPLOYEE_ID"),
                                        result.getDate("END_WEEK"));
                        list.add(new Timesheet(emp, result.getDate("END_WEEK"),
                                rows));
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
