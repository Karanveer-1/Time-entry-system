package com.corejsf.timsheet.access;

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

@ConversationScoped
public class TimesheetRowManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;
    
    public List<TimesheetRow> find(int id, Date endweek) {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<TimesheetRow> rowList = new ArrayList<TimesheetRow>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM TIMESHEETROW WHERE EMPLOYEE_ID = '" + id + "' AND END_WEEK = '" + endweek + "'");
                    while (result.next()) {
                        TimesheetRow tr = new TimesheetRow(result.getInt("PROJECT_ID"), result.getString("WORK_PACKAGE"),
                                        new BigDecimal[] {result.getBigDecimal("SAT"), result.getBigDecimal("SUN"),
                                        result.getBigDecimal("MON"), result.getBigDecimal("TUE"), result.getBigDecimal("WED"),
                                        result.getBigDecimal("THU"), result.getBigDecimal("FRI") },
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
    
    public boolean persist(Employee e, Date d) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO TIMESHEETROW(EMPLOYEE_ID, END_WEEK) VALUES (?, ?)");
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
    
    public void merge(TimesheetRow row) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE TIMESHEETROW SET PROJECT_ID = ?, WORK_PACKAGE = ?, NOTES = ?,"
                            + " SAT=?, SUN=?, MON=?, TUE=?, WED=?, THU=?, FRI=? WHERE ID = ?");
                    stmt.setInt(1, row.getProjectID());
                    stmt.setString(2, row.getWorkPackage());
                    stmt.setString(3, row.getNotes());
                    stmt.setBigDecimal(4, row.getHour(0));
                    stmt.setBigDecimal(5, row.getHour(1));
                    stmt.setBigDecimal(6, row.getHour(2));
                    stmt.setBigDecimal(7, row.getHour(3));
                    stmt.setBigDecimal(8, row.getHour(4));
                    stmt.setBigDecimal(9, row.getHour(5));
                    stmt.setBigDecimal(10, row.getHour(6));
                    stmt.setInt(11, row.getId());
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
    
    public int getLastId() {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                    if(result.next()) {
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










