package com.corejsf.timesheet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.timesheet.data.Data;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Responsible for managing current user's timesheet details (CRUD).
 * @author Karanveer
 * @version 1.1
 */
@Named
@RequestScoped
public class TimesheetDetails implements TimesheetCollection {
    /** Upper limit for additional rows. */
    private static final int ADDITIONAL_ROWS = 5;
    /** Access the data from sample data class. */
    @Inject private Data timesheetData;
    /** Manager for Employees. */
    @Inject private EmployeeDetails emp;
    
    /** Map for storing all projectID(key) 
     *  and WP(value) pairs in a given timesheet. 
     */
    private Map<String, ArrayList<String>> projectWP =
                        new HashMap<String, ArrayList<String>>();
    
    /** timesheets getter. */
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheetData.getTimesheetList();
    }

    /** Get all timesheets for an employee. */
    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        ArrayList<Timesheet> allTimesheets = new ArrayList<Timesheet>();
        for (Timesheet ts: getTimesheets()) {
            if (ts.getEmployee().getEmpNumber() == e.getEmpNumber()) {
                allTimesheets.add(ts);
            }
        }
        return allTimesheets;
    }
    
    /**
     * Checks if user has a timesheet for the current week or not.
     * @param e Employee for which it checks
     * @return a boolean
     */
    public boolean hasCurrentTimesheet(Employee e) {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        
        for (Timesheet ts: getTimesheets(e)) {
            if (ts.getWeekNumber() == c.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /** Get current timesheet for an employee. */
    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);

        for (Timesheet ts: getTimesheets(e)) {
            if (ts.getWeekNumber() == c.get(Calendar.WEEK_OF_YEAR)) {
                return ts;
            }
        }
        return null;
    }

    /** Creates a Timesheet object and adds it to the collection. */
    @Override
    public String addTimesheet() {
        Timesheet temp = new Timesheet();
        ArrayList<TimesheetRow> tempRows = 
                        (ArrayList<TimesheetRow>) temp.getDetails();
        for (int i = 0; i < ADDITIONAL_ROWS; i++) {
            tempRows.add(new TimesheetRow());
        }
        temp.setEmployee(emp.getCurrentEmployee());
        timesheetData.getTimesheetList().add(temp);
        return "editTimesheet?faces-redirect=true";
    }

    /**
     * Adds more rows in the timesheet.
     * @return Navigation string
     */
    public String addRows() {
        Timesheet temp = getCurrentTimesheet(emp.getCurrentEmployee());
        for (int i = 0; i < ADDITIONAL_ROWS; i++) {
            temp.getDetails().add(new TimesheetRow());
        }
        return "editTimesheet?faces-redirect=true";
    }
    
    /**
     * Validate whether WP and ProjectIDs are unique in a timesheet.
     * @param context 
     * @param component 
     * @param value 
     */
    public void validate(FacesContext context,
                        UIComponent component, Object value) {
        UIInput projectInput = (UIInput) component.findComponent("projectID");
        String projectID = projectInput.getLocalValue().toString();
        
        UIInput wpInput = (UIInput) component.findComponent("WP");
        String wp = wpInput.getLocalValue().toString();
        
        if (projectWP.containsKey(projectID)) {
            ArrayList<String> wpList =
                        projectWP.get(projectID);
            if (wpList.contains(wp)) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                       "WorkPackage must be a unique for a given project ID"));
            } else {
                wpList.add(wp);
            }
        } else if (!projectID.equals("0") && !wp.isEmpty()) {
            ArrayList<String> data = new ArrayList<String>();
            data.add(wp);
            projectWP.put(projectID, data);
        }
    }
    
}
