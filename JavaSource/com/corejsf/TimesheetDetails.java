package com.corejsf;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.timesheet.data.TimesheetData;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

@Named
@RequestScoped
public class TimesheetDetails implements TimesheetCollection {
    @Inject TimesheetData timesheetData;
    
    @Override
    public List<Timesheet> getTimesheets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String addTimesheet() {
        // TODO Auto-generated method stub
        return null;
    }

}
