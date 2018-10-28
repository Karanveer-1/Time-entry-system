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

/**
 * Responsible for creating and validating users. 
 * @author Karanveer
 * @version 1.1
 */
@Named
@RequestScoped
public class User extends Employee {
    /** Manager for Employees. */
    @Inject private EmployeeDetails emp;
    /** Password of the newly created user. */
    private String password;
    
    
    /**
     * Redirects the user to createNewUser page.
     * @return Navigation string
     */
    public String create() {
        return "createNewUser?faces-redirect=true";
    }
    
    /**
     * Redirects the user to editUsers page.
     * @return Navigation string
     */
    public String edit() {
        return "editUsers?faces-redirect=true";
    }
    
    /**
     * Adds the newly created user to the sample employee list.
     * @return Navigation string
     */
    public String add() {
        Map<String, String> credentialsMap = emp.getLoginCombos();
        credentialsMap.put(this.getUserName(), password);
        emp.addEmployee(this);
        return "editUsers?faces-redirect=true";
    }
    
    /**
     * Getter for password string.
     * @return a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password the password to set to
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Validates whether the user name is unique or already taken.
     * @param context 
     * @param component 
     * @param value 
     */
    public void validate(FacesContext context,
                        UIComponent component, Object value) {
        String un = value.toString();
        Map<String, String> credentialsMap = emp.getLoginCombos();
        
        boolean valid = credentialsMap.containsKey(un);

        if (valid) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "This user name is already taken. Please try another one.",
                    "This user name is already taken. Please try another one.")
                    );
        }
    }
    
}
