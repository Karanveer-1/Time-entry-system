package com.corejsf.timesheet.access;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Credentials;

/**
 * Handle CRUD actions for Credentials class.
 * 
 * @author Karanveer Khanna
 * @version 1.0
 * 
 */
@ConversationScoped
public class CredentialsManager implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;
    
    /**
     * Find Credentials record from database.
     * 
     * @param id primary key for record.
     * @return the Credentials record with key = id, null if not found.
     */
    public Credentials find(String id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM"
                     + " CREDENTIALS WHERE EMPLOYEE_USERNAME = '" + id + "'");
                    if (result.next()) {
                        return new Credentials(
                                result.getString("EMPLOYEE_USERNAME"),
                                result.getString("EMPLOYEE_PASSWORD"));
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
     * Persist Credentials into database. id must be unique.
     * 
     * @param cred the record to be persisted.
     * @return boolean, true if successful else false
     */
    public boolean persist(Credentials cred) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO CREDENTIALS VALUES (?, ?)");
                    stmt.setString(1, cred.getUserName());
                    stmt.setString(2, cred.getPassword());
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
            System.out.println("Error in persist " + cred);
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    /**
     * merge Credential record fields into existing database record.
     * 
     * @param cred the record to be merged.
     */
    public void merge(Credentials cred) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE CREDENTIALS SET EMPLOYEE_PASSWORD = ?"
                            + " WHERE EMPLOYEE_USERNAME =  ?");
                    stmt.setString(1, cred.getPassword());
                    stmt.setString(2, cred.getUserName());
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
            System.out.println("Error in merge " + cred);
            ex.printStackTrace();
        }
    }

    /**
     * Remove Credentails from database.
     * 
     * @param userName primary key to be removed from database
     * @return boolean, true if successful else false
     */
    public boolean remove(String userName) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                       "DELETE FROM CREDENTIALS WHERE EMPLOYEE_USERNAME =  ?");
                    stmt.setString(1, userName);
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
            System.out.println("Error in remove " + userName);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Return Credentials table as a map of username(key) and password(value).
     * 
     * @return Map of all records in Credentials table
     */
    public Map<String, String> getAll() {
        Connection connection = null;
        Statement stmt = null;
        Map<String, String> credentialsMap = new HashMap<String, String>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM CREDENTIALS");
                    while (result.next()) {
                        credentialsMap.put(
                                result.getString("EMPLOYEE_USERNAME"),
                                result.getString("EMPLOYEE_PASSWORD"));
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

        return credentialsMap;
    }

}
