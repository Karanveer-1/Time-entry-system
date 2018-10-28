package com.corejsf.timesheet.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Simulates sample database for application.
 * @author Karanveer
 * @version 1.1
 */
@Named
@ApplicationScoped
public class Data {
    /** Values required for sample data.*/
    private static final int ONE = 1;
    /** Values required for sample data.*/
    private static final int TWO = 2;
    /** Values required for sample data.*/
    private static final int THREE = 3;
    /** Values required for sample data.*/
    private static final int FOUR = 4;
    /** Values required for sample data.*/
    private static final int MONTH = 9;
    /** Values required for sample data.*/
    private static final int YEAR = 2018;
    /** Values required for sample data.*/
    private static final int DATE_1 = 12;
    /** Values required for sample data.*/
    private static final int DATE_2 = 19;
    /** Values required for sample data.*/
    private static final int DATE_3 = 26;
    
    /** Admin's username. */
    private final String adminUserName = "admin";
    /** Sample employee. */
    private Employee admin = new Employee("admin", ONE, "admin");
    /** Sample employee. */
    private Employee e1 = new Employee("karan", TWO, "karan");
    /** Sample employee. */
    private Employee e2 = new Employee("ryan", THREE, "ryan");
    /** Sample employee. */
    private Employee e3 = new Employee("test", FOUR, "test");
    
    
    /** sample hour. */
    private BigDecimal bigDec1 = new BigDecimal(ONE);
    /** sample hour. */
    private BigDecimal bigDec2 = new BigDecimal(TWO);
    /** sample hour. */
    private BigDecimal bigDec3 = new BigDecimal(THREE);
    /** sample hour. */
    private BigDecimal bigDec4 = new BigDecimal(FOUR);
    
    
    /** sample hour's array. */
    private BigDecimal[] bigDecimals1 = new BigDecimal[] {bigDec1,
                        bigDec2, bigDec1, bigDec2, bigDec1, bigDec2, bigDec1 };
    /** sample hour's array. */
    private BigDecimal[] bigDecimals2 = new BigDecimal[] {bigDec3,
                        bigDec4, bigDec3, bigDec4, bigDec3, bigDec4, bigDec3 };
    /** sample hour's array. */
    private BigDecimal[] bigDecimals3 = new BigDecimal[] {bigDec1,
                        bigDec4, bigDec3, bigDec1, bigDec3, bigDec4, bigDec3 };
    
    
    /** sample timesheet row. */
    private TimesheetRow timesheetRow1 =
                new TimesheetRow(ONE, "P1", bigDecimals1, "Comments1");
    /** sample timesheet row. */
    private TimesheetRow timesheetRow2 =
                new TimesheetRow(TWO, "P2", bigDecimals2, "Comments2");
    /** sample timesheet row. */
    private TimesheetRow timesheetRow3 =
                new TimesheetRow(THREE, "P3", bigDecimals3, "Comments3");
    
    
    /** sample timesheet row list. */
    private ArrayList<TimesheetRow> rowList1 = new ArrayList<TimesheetRow>();
    /** sample timesheet row list. */
    private ArrayList<TimesheetRow> rowList2 = new ArrayList<TimesheetRow>();
    /** sample timesheet row list. */
    private ArrayList<TimesheetRow> rowList3 = new ArrayList<TimesheetRow>();
    
    
    /** sample calender. */
    private Calendar calendar1 = new GregorianCalendar(YEAR, MONTH, DATE_1);
    /** sample calender. */
    private Calendar calendar2 = new GregorianCalendar(YEAR, MONTH, DATE_2);
    /** sample calender. */
    private Calendar calendar3 = new GregorianCalendar(YEAR, MONTH, DATE_3);
    
    
    /** sample date. */
    private Date date1 = calendar1.getTime();
    /** sample date. */
    private Date date2 = calendar2.getTime();
    /** sample date. */
    private Date date3 = calendar3.getTime();
    
    
    /** sample timesheet. */
    private Timesheet timesheet1;
    /** sample timesheet. */
    private Timesheet timesheet2;
    /** sample timesheet. */
    private Timesheet timesheet3;
    
    
    /** Hashmap for storing the credentials. */
    private HashMap<String, String> credentialsMap =
                                    new HashMap<String, String>();
    /** List for storing the employee data. */
    private ArrayList<Employee> employeeList = new ArrayList<Employee>();
    /** List for storing the timesheet data. */
    private ArrayList<Timesheet> timesheetList = new ArrayList<Timesheet>();
    

    /** Initializes the employeeList, timesheetList and credentialsMap. */
    @PostConstruct
    public void init() {
        employeeList.add(admin);
        employeeList.add(e1);
        employeeList.add(e2);
        employeeList.add(e3);
        
        rowList1.add(timesheetRow1);
        rowList1.add(timesheetRow2);
        rowList2.add(timesheetRow3);
        rowList2.add(timesheetRow2);
        rowList3.add(timesheetRow1);
        rowList3.add(timesheetRow3);
        
        timesheet1 = new Timesheet(e1, date1, rowList1);
        timesheet2 = new Timesheet(e2, date2, rowList2);
        timesheet3 = new Timesheet(e3, date3, rowList3);
        
        timesheetList.add(timesheet1);
        timesheetList.add(timesheet2);
        timesheetList.add(timesheet3);
        
        credentialsMap.put("admin", "admin");
        credentialsMap.put("karan", "karan");
        credentialsMap.put("ryan", "ryan");
        credentialsMap.put("test", "test");
    }
    
    
    /**
     * Getter for timesheet ArrayList.
     * @return an ArrayList
     */
    public ArrayList<Timesheet> getTimesheetList() {
        return timesheetList;
    }

    /**
     * Setter for timesheet ArrayList.
     * @param timesheetList the list to set to
     */
    public void setTimesheetList(ArrayList<Timesheet> timesheetList) {
        this.timesheetList = timesheetList;
    }
    
    /**
     * Getter for credential's HashMap.
     * @return a HasMap
     */
    public HashMap<String, String> getCredentialsMap() {
        return credentialsMap;
    }

    /**
     * Setter for credential's HashMap.
     * @param credentialsMap 
     */
    public void setCredentialsMap(HashMap<String, String> credentialsMap) {
        this.credentialsMap = credentialsMap;
    }
    
    /**
     * Getter for employee list.
     * @return an ArrayList of employees
     */
    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Setter for employee list.
     * @param employeeList the list to set to
     */
    public void setEmployeeList(ArrayList<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    /**
     * Getter for admin user name.
     * @return a string
     */
    public String getAdminUserName() {
        return adminUserName;
    }
}
