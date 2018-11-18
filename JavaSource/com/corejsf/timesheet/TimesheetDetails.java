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

import com.corejsf.timesheet.access.TimesheetManager;
import com.corejsf.timesheet.access.TimesheetRowManager;

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
    /** Manager for Employees. */
    @Inject private EmployeeDetails emp;
    /** Hold reference of current user. */
    @Inject private Login currentUser;
    
    /** Map for storing all projectID(key) 
     *  and WP(value) pairs in a given timesheet. 
     */
    private Map<String, ArrayList<String>> projectWP = new HashMap<String, ArrayList<String>>();

       
    @Inject TimesheetManager timesheetManager;
    @Inject TimesheetRowManager rowManager;
    
    /** timesheets getter. */
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheetManager.getAll();
    }

    /** Get all timesheets for an employee. */
    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        if (currentUser.getTimesheetList() == null) {
            ArrayList<Timesheet> allTimesheetsForEmployee = new ArrayList<Timesheet>();
            for (Timesheet ts: getTimesheets()) {
                if (ts.getEmployee().getEmpNumber() == e.getEmpNumber()) {
                    allTimesheetsForEmployee.add(ts);
                }
            }
            currentUser.setTimesheetList(allTimesheetsForEmployee);
        }
        return currentUser.getTimesheetList();
    }
    
    
    public String getWeekEnding() {
        Calendar c = getCalender();
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
    }
    
    public int getWeekNumber() {
        Calendar c = getCalender();
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    
    /** Creates a Timesheet object and adds it to the collection. */
    @Override
    public String addTimesheet() {
        Timesheet temp = new Timesheet();
        ArrayList<TimesheetRow> tempRows = (ArrayList<TimesheetRow>) temp.getDetails();
        
        for (int i = 0; i < ADDITIONAL_ROWS; i++) {
            TimesheetRow tr = new TimesheetRow();
            tempRows.add(tr);
        }
        temp.setEmployee(emp.getCurrentEmployee());
        if (timesheetManager.persist(temp)) {
            currentUser.getTimesheetList().add(temp);
            for(int i = 0; i < ADDITIONAL_ROWS; i++) {
                rowManager.persist(temp.getEmployee(), temp.getEndWeek());
                temp.getDetails().get(i).setId(rowManager.getLastId());
            }
        }
        return null;
    }
    
    /**
     * Adds more rows in the timesheet.
     * @return Navigation string
     */
    public String addRows() {
        Timesheet temp = getCurrentTimesheet(emp.getCurrentEmployee());
        if (rowManager.persist(temp.getEmployee(), temp.getEndWeek())) {
            temp.getDetails().add(new TimesheetRow());
            temp.getDetails().get(temp.getDetails().size()-1).setId(rowManager.getLastId());
        }
        return null;
    }
      
    /**
     * Checks if user has a timesheet for the current week or not.
     * @param e Employee for which it checks
     * @return a boolean
     */
    public boolean hasCurrentTimesheet(Employee e) {
        for (Timesheet ts: getTimesheets(e)) {
            if (ts.getWeekEnding().equals(getWeekEnding())) {
                return true;
            }
        }
        return false;
    }

    /** Get current timesheet for an employee. */
    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        for (Timesheet ts: getTimesheets(e)) {
            if (ts.getWeekEnding().equals(getWeekEnding())) {
                return ts;
            }
        }
        return null;
    }
    
    private Calendar getCalender() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date endWeek = c.getTime();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c;
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
    
    public String save() {
        Employee currentUser = emp.getCurrentEmployee();
        Timesheet ts = getCurrentTimesheet(currentUser);
        
        for (TimesheetRow tr: ts.getDetails()) {
            rowManager.merge(tr);
        }
        
        return "currentTimeSheet?faces-redirect=true";
    }
    
}
