package com.corejsf.timesheet;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * Responsible for creating and validating users.
 * 
 * @author Karanveer
 * @version 1.1
 */
@Named
@RequestScoped
public class User extends Employee {
    /** Manager for Employees. */
    @Inject
    private EmployeeDetails emp;
    /** Password of the newly created user. */
    private String password;

    /**
     * Adds the newly created user to the sample employee list.
     * 
     * @return Navigation string
     */
    public String add() {

        if (emp.addCredentials(new Credentials(this.getUserName(), password))) {
            emp.addEmployee(this);
        }
        return "editUsers?faces-redirect=true";
    }

    /**
     * Validates whether the user name is unique.
     * 
     * @param context 
     * @param component 
     * @param value 
     */
    public void validate(FacesContext context,
            UIComponent component, Object value) {
        String un = value.toString().trim();
        
        Map<String, String> credentialsMap = emp.getLoginCombos();
        
        FacesContext facescontext = FacesContext.getCurrentInstance();
        Locale locale = facescontext.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(
                        "com.corejsf.timesheet.messages", locale);
        
        if (un.isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "",
                    bundle.getString("emptyUsername")));
        }

        boolean contains = credentialsMap.containsKey(un);

        if (contains) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "", bundle.getString("usernameerror")));
        }

    }
    
    /**
     * Validates whether the Employee name is empty.
     * 
     * @param context 
     * @param component 
     * @param value 
     */
    public void validateName(FacesContext context,
                        UIComponent component, Object value) {
        String name = value.toString().trim();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        Locale locale = facescontext.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(
                        "com.corejsf.timesheet.messages", locale);
        
        if (name.isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "", 
                    bundle.getString("emptyName")));
        }
        
    }
    

    /**
     * Redirects the user to createNewUser page.
     * 
     * @return Navigation string
     */
    public String create() {
        return "createNewUser?faces-redirect=true";
    }

    /**
     * Redirects the user to editUsers page.
     * 
     * @return Navigation string
     */
    public String edit() {
        return "editUsers?faces-redirect=true";
    }

    /**
     * Getter for password string.
     * 
     * @return a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password the password to set to
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
