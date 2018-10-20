package com.corejsf;

import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

@Named
@RequestScoped
public class User extends Employee {
    private String password;
    private @Inject EmployeeDetails emp;
    
    public String create() {
        return "createNewUser?faces-redirect=true";
    }
    
    public String edit() {
        return "editUsers?faces-redirect=true";
    }
    
    public String add() {
        Map<String, String> credentialsMap = emp.getLoginCombos();
        credentialsMap.put(this.getUserName(), password);
        emp.addEmployee(this);
        return "index?faces-redirect=true";
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
