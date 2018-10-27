package com.corejsf.timesheet;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

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
        return "editUsers?faces-redirect=true";
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void validate(FacesContext context, UIComponent component, Object value) {
        String un = value.toString();
        Map<String, String> credentialsMap = emp.getLoginCombos();
        
        boolean valid = credentialsMap.containsKey(un);

        if (valid) {
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "This user name is already taken. Please try another one.",
                                                    "This user name is already taken. Please try another one." ) );
        }
    }
    
}
