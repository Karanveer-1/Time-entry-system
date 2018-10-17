package com.corejsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
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

    @Override
    public List<Employee> getEmployees() {
        return employeeData.getEmployeeList();
    }

    @Override
    public Employee getEmployee(String name) {
        for (Employee emp: employeeData.getEmployeeList()) {
            if (emp.getName().equalsIgnoreCase(name)) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getAdministrator() {
        // TODO Auto-generated method stub
        return null;
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
    public String logout(Employee employee) {
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

}
