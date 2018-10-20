package com.corejsf.timesheet.data;

import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.timesheet.Timesheet;

@Named
@ApplicationScoped
public class TimesheetData {
    
    ArrayList<Timesheet> timesheetList = new ArrayList<Timesheet>();
    
    public ArrayList<Timesheet> getTimesheetList() {
        return timesheetList;
    }

    public void setTimesheetList(ArrayList<Timesheet> timesheetList) {
        this.timesheetList = timesheetList;
    }    
}
