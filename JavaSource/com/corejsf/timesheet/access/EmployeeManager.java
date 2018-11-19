package com.corejsf.timesheet.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;

/**
 * Handle CRUD actions for Employee class.
 * 
 * @author Karanveer Khanna
 * @version 1.0
 * 
 */
@ConversationScoped
public class EmployeeManager implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;

    /**
     * Find Employee record from database.
     * 
     * @param id primary key for record.
     * @return the Employee record with key = id, null if not found.
     */
    public Employee find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM"
                            + " EMPLOYEE WHERE EMPLOYEE_ID = '" + id + "'");
                    if (result.next()) {
                        return new Employee(result.getString("EMPLOYEE_NAME"), 
                                result.getInt("EMPLOYEE_ID"),
                                result.getString("EMPLOYEE_USERNAME"));
                    } else {
                        return null;
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
            System.out.println("Error in find " + id);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Persist Employee record into database. id must be unique.
     * 
     * @param emp the record to be persisted.
     * @return boolean, true if successful else false
     */
    public boolean persist(Employee emp) {
        Connection connection = null;
        PreparedStatement stmt = null;
        Statement stmt1 = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO EMPLOYEE(EMPLOYEE_NAME,"
                            + " EMPLOYEE_USERNAME) VALUES (?, ?)");
                    stmt.setString(1, emp.getName());
                    stmt.setString(2, emp.getUserName());
                    stmt.executeUpdate();
                    stmt1 = connection.createStatement();
                    ResultSet result = stmt1.executeQuery(
                            "SELECT LAST_INSERT_ID()");
                    if (result.next()) {
                        emp.setEmpNumber(result.getInt(1));
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
            System.out.println("Error in persist " + emp);
            ex.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * merge Employee record fields into existing database record.
     * 
     * @param emp the record to be merged.
     */
    public void merge(Employee emp) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE EMPLOYEE SET EMPLOYEE_NAME = ?"
                            + "  WHERE EMPLOYEE_ID =  ?");
                    stmt.setString(1, emp.getName());
                    stmt.setInt(2, emp.getEmpNumber());
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
            System.out.println("Error in merge " + emp);
            ex.printStackTrace();
        }
    }

    /**
     * Remove employee from database.
     * 
     * @param emp Employee to be removed from database
     * @return boolean, true if successful else false
     */
    public boolean remove(Employee emp) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID =  ?");
                    stmt.setInt(1, emp.getEmpNumber());
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
            System.out.println("Error in remove " + emp);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Return Employee table as a list of Employees.
     * 
     * @return List of all records in Employee table
     */
    public List<Employee> getAll() {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<Employee> list = new ArrayList<Employee>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM EMPLOYEE ORDER BY EMPLOYEE_ID");
                    while (result.next()) {
                        list.add(new Employee(
                                result.getString("EMPLOYEE_NAME"),
                                result.getInt("EMPLOYEE_ID"),
                                result.getString("EMPLOYEE_USERNAME")));
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
