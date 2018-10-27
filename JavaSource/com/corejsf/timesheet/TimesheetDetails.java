package com.corejsf.timesheet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.timesheet.data.TimesheetData;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named
@RequestScoped
public class TimesheetDetails implements TimesheetCollection {
    @Inject TimesheetData timesheetData;
    @Inject EmployeeDetails emp;
    
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheetData.getTimesheetList();
    }

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

    @Override
    public String addTimesheet() {
        Timesheet temp = new Timesheet();
        ArrayList<TimesheetRow> tempRows = (ArrayList<TimesheetRow>) temp.getDetails();
        
        for(int i = 0; i < 5;i++) {
            tempRows.add(new TimesheetRow());
        }
        
        temp.setEmployee(emp.getCurrentEmployee());
        timesheetData.getTimesheetList().add(temp);
        
        System.out.println("success");
        
        return "editTimesheet?faces-redirect=true";
    }

    public String addRows() {
        Timesheet temp = getCurrentTimesheet(emp.getCurrentEmployee());
        for(int i = 0; i < 5;i++) {
            temp.getDetails().add(new TimesheetRow());
        }
        return "editTimesheet?faces-redirect=true";
    }
    
    
    
    
    
    
    
    
    
}






//if (ts.getEndWeek().compareTo(endWeek) < 0) {
//System.out.println("lesser"); // this got clicked
//} else if (ts.getEndWeek().compareTo(endWeek) > 0) {
//System.out.println("greater");
//} else {
//System.out.println("equal");
//}

//System.out.println("1:" + ts.getWeekNumber());
//System.out.println("2:" + c.get(Calendar.WEEK_OF_YEAR));



// ts.getEndWeek().before(endWeek)




