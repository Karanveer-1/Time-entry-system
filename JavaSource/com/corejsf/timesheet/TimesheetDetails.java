package com.corejsf.timesheet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
    /** Manager from Timesheet objects.*/
    @Inject private TimesheetManager timesheetManager;
    /** Manager from TimesheetRow objects.*/
    @Inject private TimesheetRowManager rowManager;
    
    /** Map for storing all projectID(key) 
     *  and WP(value) pairs in a given timesheet. 
     */
    private Map<Integer, ArrayList<String>> projectWP =
            new HashMap<Integer, ArrayList<String>>();
    
    /** timesheets getter. */
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheetManager.getAll();
    }

    /** Get all timesheets for an employee. */
    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        if (currentUser.getTimesheetList() == null) {
            ArrayList<Timesheet> allTimesheetsForEmployee =
                    new ArrayList<Timesheet>();
            for (Timesheet ts: getTimesheets()) {
                if (ts.getEmployee().getEmpNumber() == e.getEmpNumber()) {
                    allTimesheetsForEmployee.add(ts);
                }
            }
            currentUser.setTimesheetList(allTimesheetsForEmployee);
        }
        return currentUser.getTimesheetList();
    }
    
    /**
     * Calculate the current end week's date as a string.
     * @return the endWeek as string
     */
    public String getWeekEnding() {
        Calendar c = getCalender();
        return (c.get(Calendar.MONTH) + 1) 
                + "/" + c.get(Calendar.DAY_OF_MONTH)
                + "/" + c.get(Calendar.YEAR);
    }
    
    /**
     * Returns the current week number.
     * @return the calculated week number
     */
    public int getWeekNumber() {
        Calendar c = getCalender();
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * Creates a new timesheet and adds that to database.
     */
    @Override
    public String addTimesheet() {
        Timesheet temp = new Timesheet();
        ArrayList<TimesheetRow> tempRows =
                (ArrayList<TimesheetRow>) temp.getDetails();
        
        for (int i = 0; i < ADDITIONAL_ROWS; i++) {
            TimesheetRow tr = new TimesheetRow();
            tempRows.add(tr);
        }
        temp.setEmployee(emp.getCurrentEmployee());
        if (timesheetManager.persist(temp)) {
            currentUser.getTimesheetList().add(temp);
            for (int i = 0; i < ADDITIONAL_ROWS; i++) {
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
            temp.getDetails().get(
                    temp.getDetails().size() - 1).
                    setId(rowManager.getLastId());
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
    
    /**
     * Sets the calender object.
     * @return calender object
     */
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
    public void validateData(FacesContext context,
            UIComponent component, Object value) {
        UIInput projectInput = (UIInput) component.findComponent("projectID");
        UIInput wpInput = (UIInput) component.findComponent("WP");
        UIInput inputSat = (UIInput) component.findComponent("InputHoursSat");
        UIInput inputSun = (UIInput) component.findComponent("InputHoursSun");
        UIInput inputMon = (UIInput) component.findComponent("InputHoursMon");
        UIInput inputTue = (UIInput) component.findComponent("InputHoursTue");
        UIInput inputWed = (UIInput) component.findComponent("InputHoursWed");
        UIInput inputThu = (UIInput) component.findComponent("InputHoursThu");
        UIInput inputFri = (UIInput) component.findComponent("InputHoursFri");
        
        Integer projectID = null;
        String wp = null;
                
        if (projectInput.isValid() && wpInput.isValid() && inputSat.isValid()
                && inputSun.isValid() && inputMon.isValid() 
                && inputTue.isValid() && inputWed.isValid()
                && inputThu.isValid() && inputFri.isValid()) {
            projectID = (Integer) projectInput.getLocalValue();
            wp = wpInput.getLocalValue().toString();
            
            if (projectID == null) {
                projectID = 0;
            }
            
            if (inputSat.getLocalValue() != null  
                    || inputSun.getLocalValue() != null 
                    || inputMon.getLocalValue() != null
                    || inputTue.getLocalValue() != null
                    || inputWed.getLocalValue() != null 
                    || inputThu.getLocalValue() != null 
                    || inputFri.getLocalValue() != null) {
                
                if (projectID == 0 || wp.equals("")) {
                    throw new ValidatorException(
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                                 getBundle().getString("WPandIDreqForHours")));
                }
            }
  
            
            if (projectID < 0) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                         getBundle().getString("projectIdMustBeNonNegative")));
            }
            
            if (!wp.isEmpty() && projectID.equals(0)) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "", 
                                getBundle().getString("projectIdReq") + wp));
            } else if (projectID != 0 && wp.isEmpty()) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "", 
                                getBundle().getString("WPReq") + projectID));
            } else if (projectWP.containsKey(projectID)) {
                ArrayList<String> wpList = projectWP.get(projectID);
                if (wpList.contains(wp)) {
                    throw new ValidatorException(
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                                getBundle().getString("uniqueWPAndProjectID")));
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

    /**
     * Returns the resource bundle based on Locale. 
     * @return resource bundle
     */
    private ResourceBundle getBundle() {
      FacesContext facescontext = FacesContext.getCurrentInstance();
      Locale locale = facescontext.getViewRoot().getLocale();
      ResourceBundle bundle = ResourceBundle.
              getBundle("com.corejsf.timesheet.messages", locale);
      return bundle;
    }

    /**
     * Validates whether hours has more than one fraction digits.
     * @param context 
     * @param component 
     * @param value 
     */
    public void validateFraction(FacesContext context,
                UIComponent component, Object value) {
        if (value == null) {
            return;
        } else {
            String hours = value.toString();
            int integerPlaces = hours.indexOf('.');
            int decimalPlaces = 0;
            if (!(integerPlaces == -1)) {
                decimalPlaces = hours.length() - integerPlaces - 1;
            }
            if (decimalPlaces > 1) {
                FacesContext facescontext = FacesContext.getCurrentInstance();
                Locale locale = facescontext.getViewRoot().getLocale();
                ResourceBundle bundle = ResourceBundle.
                        getBundle("com.corejsf.timesheet.messages", locale);
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "", bundle.getString("hoursError")));
            }
        }
    }
   
    /**
     * Saves the timesheet row into the database.
     * @return Navigation string
     */
    public String save() {
        Employee loggedInUser = emp.getCurrentEmployee();
        Timesheet ts = getCurrentTimesheet(loggedInUser);
        
        for (TimesheetRow tr: ts.getDetails()) {
            rowManager.merge(tr);
        }
        return "currentTimeSheet?faces-redirect=true";
    }
}
