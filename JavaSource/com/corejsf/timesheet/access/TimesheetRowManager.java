package com.corejsf.timesheet.access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Handle CRUD actions for TimesheetRow class.
 * 
 * @author Karanveer Khanna
 * @version 1.0
 * 
 */
@ConversationScoped
public class TimesheetRowManager implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Column index of project id. */
    private static final int PROJECT_ID = 1;
    /** Column index of WP. */
    private static final int WP = 2;
    /** Column index of notes. */
    private static final int NOTES = 3;
    /** Column index of sat. */
    private static final int SAT = 4;
    /** Column index of sun. */
    private static final int SUN = 5;
    /** Column index of mon. */
    private static final int MON = 6;
    /** Column index of tue. */
    private static final int TUE = 7;
    /** Column index of wed. */
    private static final int WED = 8;
    /** Column index of thu. */
    private static final int THU = 9;
    /** Column index of fri. */
    private static final int FRI = 10;
    /** Column index of unique ID. */
    private static final int UNIQUE_ID = 11;
    
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;
    
    /**
     * Find all the timesheet row record from database.
     * 
     * @param id primary key for record.
     * @param endweek primary key for record.
     * @return the List of timesheetrows with key = id.
     */
    public List<TimesheetRow> find(int id, Date endweek) {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<TimesheetRow> rowList = new ArrayList<TimesheetRow>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM TIMESHEETROW WHERE EMPLOYEE_ID"
                            + " = '" + id + "' AND END_WEEK"
                                    + " = '" + endweek + "'");
                    while (result.next()) {
                        TimesheetRow tr = new TimesheetRow(
                                result.getInt("PROJECT_ID"),
                                result.getString("WORK_PACKAGE"),
                                new BigDecimal[] {result.getBigDecimal("SAT"),
                                        result.getBigDecimal("SUN"),
                                        result.getBigDecimal("MON"),
                                        result.getBigDecimal("TUE"),
                                        result.getBigDecimal("WED"),
                                        result.getBigDecimal("THU"),
                                        result.getBigDecimal("FRI") },
                                        result.getString("NOTES"));
                        tr.setId(result.getInt("ID"));
                        rowList.add(tr);
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
        return rowList;
    }
    
    /**
     * Persist TimesheetRow record into database. id must be unique.
     * 
     * @param e the record to be persisted.
     * @param d the record to be persisted.
     * @return boolean, true if successful else false
     */
    public boolean persist(Employee e, Date d) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO TIMESHEETROW"
                            + "(EMPLOYEE_ID, END_WEEK) VALUES (?, ?)");
                    stmt.setInt(1, e.getEmpNumber());
                    stmt.setDate(2, new java.sql.Date(d.getTime()));
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
            System.out.println("Error in persist " + e + " " + d);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * merge TimesheetRow record fields into existing database record.
     * 
     * @param row the record to be merged.
     */
    public void merge(TimesheetRow row) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE TIMESHEETROW SET PROJECT_ID = ?,"
                            + " WORK_PACKAGE = ?, NOTES = ?,"
                            + " SAT=?, SUN=?, MON=?, TUE=?,"
                            + " WED=?, THU=?, FRI=? WHERE ID = ?");
                    stmt.setInt(PROJECT_ID, row.getProjectID());
                    stmt.setString(WP, row.getWorkPackage());
                    stmt.setString(NOTES, row.getNotes());
                    stmt.setBigDecimal(SAT, row.getHour(TimesheetRow.SAT));
                    stmt.setBigDecimal(SUN, row.getHour(TimesheetRow.SUN));
                    stmt.setBigDecimal(MON, row.getHour(TimesheetRow.MON));
                    stmt.setBigDecimal(TUE, row.getHour(TimesheetRow.TUE));
                    stmt.setBigDecimal(WED, row.getHour(TimesheetRow.WED));
                    stmt.setBigDecimal(THU, row.getHour(TimesheetRow.THU));
                    stmt.setBigDecimal(FRI, row.getHour(TimesheetRow.FRI));
                    stmt.setInt(UNIQUE_ID, row.getId());
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
            System.out.println("Error in merge " + row);
            ex.printStackTrace();
        }
    }
    
    /**
     * Return the last inserted id in Timesheetrow table.
     * 
     * @return int
     */
    public int getLastId() {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT LAST_INSERT_ID()");
                    if (result.next()) {
                        return result.getInt(1);
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
            System.out.println("Error in getLastId");
            ex.printStackTrace();
        }
        return 0;
    }
    
    
}










