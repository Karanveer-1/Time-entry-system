package com.corejsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


import com.corejsf.timesheet.data.EmployeeData;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named
@RequestScoped
public class EmployeeDetails implements EmployeeList {
    @Inject EmployeeData employeeData;
    @Inject Login currentUser;

    @Override
    public List<Employee> getEmployees() {
        return employeeData.getEmployeeList();
    }

    @Override
    public Employee getEmployee(String name) {
        for (Employee emp: getEmployees()) {
            if (emp.getName().equalsIgnoreCase(name)) {
                return emp;
            }
        }
        System.out.println("User not found!");
        return null;
    }
    
    public Employee getEmployeeWithUserName(String userName) {
        for (Employee emp: getEmployees()) {
            if (emp.getUserName().equalsIgnoreCase(userName)) {
                return emp;
            }
        }
        System.out.println("User not found!");
        return null;
    }

    @Override
    public Map<String, String> getLoginCombos() {
        return employeeData.getCredentialsMap();
    }

    @Override
    public Employee getCurrentEmployee() {
        return getEmployeeWithUserName(currentUser.getCredentials().getUserName());
    }

    @Override
    public boolean verifyUser(Credentials credential) {
        Map<String, String> loginCredentials = getLoginCombos();
        if (loginCredentials.containsKey(credential.getUserName())) {
            if (loginCredentials.get(credential.getUserName()).equals(credential.getPassword())) {
                return true;
            }
        }
        System.out.println("Wrong credentials");
        return false;
    }
    
    @Override
    public Employee getAdministrator() {
        return null;
    }

    @Override
    public void deleteEmployee(Employee userToDelete) {
        employeeData.getEmployeeList().remove(userToDelete);
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        employeeData.getEmployeeList().add(newEmployee);
    }
    
    @Override
    public String logout(Employee employee) {
        return null;
    }
}
