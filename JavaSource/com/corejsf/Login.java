package com.corejsf;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;

import java.io.Serializable;
import java.util.Map;

@Named("login")
@SessionScoped
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    private @Inject Credentials credentials;
    private @Inject EmployeeDetails emp;
    
    public String changePassword() {
        Map<String, String> loginCredentials = emp.getLoginCombos();
        loginCredentials.replace(credentials.getUserName(), credentials.getPassword());
        return "index?faces-redirect=true";
    }
    
    public void validateUsernamePassword(FacesContext context, UIComponent component, Object value) {
            UIInput userNameInput = (UIInput) component.findComponent("loginUserName");
            String un = ((String) userNameInput.getLocalValue());
            String pw = ((String) value.toString());
            Credentials temp = new Credentials();
            temp.setUserName(un);
            temp.setPassword(pw);
            
            boolean valid = emp.verifyUser(temp);

            if (!valid) {
                throw new ValidatorException(
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, "Incorrect Username or Password", "Incorrect Username or Password" ) );
            }
    }


    public String loginUser() {
        return "index?faces-redirect=true";
    }
    
    public String logoutUser() {
        return "login?faces-redirect=true";
    }
    
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    
}