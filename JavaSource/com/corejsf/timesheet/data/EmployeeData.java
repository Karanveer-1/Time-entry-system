package com.corejsf.timesheet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import ca.bcit.infosys.employee.Employee;

@Named
@ApplicationScoped
public class EmployeeData {
    ArrayList<Employee> employeeList = new ArrayList<Employee>(Arrays.asList(
            new Employee("admin", 0, "admin"),
            new Employee("a", 1, "a"),
            new Employee("b", 2, "b"),
            new Employee("c", 3, "c")));
    
    HashMap<String, String> credentialsMap = new HashMap<String, String>();

    @PostConstruct
    public void init() {
        credentialsMap.put("admin", "admin");
        credentialsMap.put("a", "a");
        credentialsMap.put("b", "b");
        credentialsMap.put("c", "c");
    }
    
    public HashMap<String, String> getCredentialsMap() {
        return credentialsMap;
    }


    public void setCredentialsMap(HashMap<String, String> credentialsMap) {
        this.credentialsMap = credentialsMap;
    }
    

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }


    public void setEmployeeList(ArrayList<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
