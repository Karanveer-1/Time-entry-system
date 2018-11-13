package com.corejsf.timsheet.access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;

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
                        rowList.add(new TimesheetRow(result.getInt("PROJECT_ID"), result.getString("WORK_PACKAGE"),
                                        new BigDecimal[] {result.getBigDecimal("SAT"), result.getBigDecimal("SUN"),
                                                result.getBigDecimal("MON"), result.getBigDecimal("TUE"), result.getBigDecimal("WED"),
                                                result.getBigDecimal("THU"), result.getBigDecimal("FRI") },
                                                result.getString("NOTES")));
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
}
