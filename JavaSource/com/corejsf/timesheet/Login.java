package com.corejsf.timesheet;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;

import java.io.Serializable;

/**
 * Manages current user credentials and logged in status.
 * @author Karanveer
 * @version 1.1
 */
@Named("login")
@SessionScoped
public class Login implements Serializable {
    /** Serial version number. */
    private static final long serialVersionUID = 1L;
    
    /** Credentials of logged in user. */
    private Credentials credentials;
    /** Determines if someone is logged in or not. */
    private boolean loggedIn;
    
    /** Constructor for Login. */
    public Login() {
        credentials = new Credentials();
        loggedIn = false;
    }

    /**
     * Set the loggedIn to true and redirect the user to Current timesheet page.
     * @return Navigation string
     */
    public String loginUser() {
        loggedIn = true;
        return "currentTimeSheet?faces-redirect=true";
    }
    
    /**
     * Set the loggedIn to false and redirect the user to logout page.
     * @return Navigation string
     */
    public String logoutUser() {
        loggedIn = false;
        return "login?faces-redirect=true";
    }

    /**
     * Returns true if user is logged in else false.
     * @return a boolean value
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    /**
     * Getter for logged in user's credentials.
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }
    
    /**
     * Sets the credentials of logged in user.
     * @param credentials the credentials to set
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
