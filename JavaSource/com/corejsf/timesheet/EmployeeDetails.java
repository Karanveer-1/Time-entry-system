package com.corejsf.timesheet;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;

import javax.inject.Inject;
import javax.inject.Named;


import com.corejsf.timesheet.data.Data;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named
@ConversationScoped
public class EmployeeDetails implements EmployeeList {
    private Employee tempEmployee;
    private String tempPassword;

    @Inject Data employeeData;
    @Inject Login currentUser;
    @Inject Conversation convo;

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
            if (emp.getUserName().equals(userName)) {
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
    
    
    public String changePassword() {
        Map<String, String> loginCredentials = getLoginCombos();
        loginCredentials.replace(currentUser.getCredentials().getUserName(), currentUser.getCredentials().getPassword());
        return "currentTimeSheet?faces-redirect=true";
    }
    
    
    public boolean isAdmin() {
        if (currentUser.getCredentials().getUserName().equals(getAdministrator().getUserName())) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Employee getAdministrator() {
        return getEmployeeWithUserName(employeeData.getAdminUserName());
    }

    @Override
    public void deleteEmployee(Employee userToDelete) {
        Map<String, String> loginCredentials = getLoginCombos();
        loginCredentials.remove(userToDelete.getUserName());
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
    
    public String save() {
        for (Employee e : getEmployees()) {
            if (e.isEditable()) {
                e.setEditable(false);
            }
        }
        return null;
    }
    
    public String resetPassword(Employee e) {
        convo.begin();
        setTempEmployee(e);
        return "resetPassword?faces-redirect=true";
    }
    
    public String savePassword() {
        Map<String, String> loginCredentials = getLoginCombos();
        loginCredentials.replace(getTempEmployee().getUserName(), tempPassword);
        convo.end();
        return "editUsers?faces-redirect=true";
    }
    
    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public Employee getTempEmployee() {
        return tempEmployee;
    }

    public void setTempEmployee(Employee temp) {
        this.tempEmployee = temp;
    }
}
