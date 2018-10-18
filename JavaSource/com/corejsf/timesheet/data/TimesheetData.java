package com.corejsf.timesheet.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named
@ApplicationScoped
public class TimesheetData {
    ArrayList<Timesheet> timesheetList = new ArrayList<Timesheet>();
    
    ArrayList<TimesheetRow> timesheetrowList = new ArrayList<TimesheetRow>(Arrays.asList());
    
}
