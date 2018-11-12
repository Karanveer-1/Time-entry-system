package com.corejsf.timsheet.access;

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


@ConversationScoped
public class EmployeeManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Resource(mappedName = "java:jboss/datasources/tes")
    private DataSource ds;

    public Employee find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM CREDENTIALS WHERE EMPLOYEE_ID = '" + id + "'");
                    if (result.next()) {
                        return new Employee(result.getString("EMPLOYEE_NAME"), result.getInt("EMPLOYEE_ID"), result.getString("EMPLOYEE_USERNAME"));
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

    public void persist(Employee emp) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO EMPLOYEE VALUES (?, ?, ?)");
                    stmt.setInt(1, emp.getEmpNumber());
                    stmt.setString(2, emp.getName());
                    stmt.setString(3, emp.getUserName());
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
            System.out.println("Error in persist " + emp);
            ex.printStackTrace();
        }

    }

    public void merge(Employee emp) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE EMPLOYEE SET EMPLOYEE_NAME = ?  WHERE EMPLOYEE_ID =  ?");
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

    public void remove(Employee emp) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID =  ?");
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
        }
    }

    public List<Employee> getAll() {
        Connection connection = null;
        Statement stmt = null;
        ArrayList<Employee> list = new ArrayList<Employee>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT * FROM EMPLOYEE ORDER BY EMPLOYEE_ID");
                    while (result.next()) {
                        list.add(new Employee(result.getString("EMPLOYEE_NAME"), result.getInt("EMPLOYEE_ID"), result.getString("EMPLOYEE_USERNAME")));
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
