package com.corejsf.timesheet;

import java.math.BigDecimal;
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
    private Map<Integer, ArrayList<String>> projectWP = new HashMap<Integer, ArrayList<String>>();

       
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
    public void validateData(FacesContext context, UIComponent component, Object value) {
        UIInput projectInput = (UIInput) component.findComponent("projectID");
        UIInput wpInput = (UIInput) component.findComponent("WP");
        UIInput inputHoursSat = (UIInput) component.findComponent("InputHoursSat");
        UIInput inputHoursSun = (UIInput) component.findComponent("InputHoursSun");
        UIInput inputHoursMon = (UIInput) component.findComponent("InputHoursMon");
        UIInput inputHoursTue = (UIInput) component.findComponent("InputHoursTue");
        UIInput inputHoursWed = (UIInput) component.findComponent("InputHoursWed");
        UIInput inputHoursThu = (UIInput) component.findComponent("InputHoursThu");
        UIInput inputHoursFri = (UIInput) component.findComponent("InputHoursFri");
        
        Integer projectID = null;
        String wp = null;
        boolean hoursAdded = false;
                
        if (projectInput.isValid() && wpInput.isValid() && inputHoursSat.isValid()
                && inputHoursSun.isValid() && inputHoursMon.isValid() && inputHoursTue.isValid()
                && inputHoursWed.isValid() && inputHoursThu.isValid() && inputHoursFri.isValid()) {
            projectID = (Integer) projectInput.getLocalValue();
            wp = wpInput.getLocalValue().toString();
            
            if (projectID == null) {
                projectID = 0;
            }
            
            if (inputHoursSat.getLocalValue() != null || inputHoursSun.getLocalValue() != null || inputHoursMon.getLocalValue() != null
                    || inputHoursTue.getLocalValue() != null
                    || inputHoursWed.getLocalValue() != null || inputHoursThu.getLocalValue() != null || inputHoursFri.getLocalValue() != null) {
                hoursAdded = true;
            }
  
            if (hoursAdded) {
                if (projectID == 0 || wp.equals("")) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "ProjectID and WP must be filled in, if you want to fill hours."));
                }
            }
            
            if (projectID < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Project ID must be non-negative"));
            }
            
            if (!wp.isEmpty() && projectID.equals(0)) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Project ID must be filled for WP: " + wp));
            }else if (projectID != 0 && wp.isEmpty()) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "WorkPackage must be filled for project ID: " + projectID));
            }else if (projectWP.containsKey(projectID)) {
                ArrayList<String> wpList = projectWP.get(projectID);
                if (wpList.contains(wp)) {
                    throw new ValidatorException( new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "WorkPackage must be a unique for a given project ID"));
                } else {
                    wpList.add(wp);
                }
            } else if (!projectID.equals(0) && !wp.isEmpty()) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(wp);
                projectWP.put(projectID, data);
            }
        } else {
            return;
        }
    }
    
    public void validateFraction(FacesContext context, UIComponent component, Object value) {
        if(value == null) {
            return;
        } else {
            String hours = value.toString();
            int integerPlaces = hours.indexOf('.');
            int decimalPlaces = 0;
            if (!(integerPlaces == -1)) {
                decimalPlaces = hours.length() - integerPlaces - 1;
            }
            if (decimalPlaces > 1) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Hours must have only one fraction part."));
            }
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
