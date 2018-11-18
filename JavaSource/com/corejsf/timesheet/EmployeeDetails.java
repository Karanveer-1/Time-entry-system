package com.corejsf.timesheet;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.timesheet.access.CredentialsManager;
import com.corejsf.timesheet.access.EmployeeManager;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Responsible for managing user details between UI and Database layers (CRUD).
 * @author Karanveer
 * @version 1.1
 */
@Named
@ConversationScoped
public class EmployeeDetails implements EmployeeList {
    private static final String adminUserName = "admin";
    /** Temporary employee used for resetting password. */
    private Employee tempEmployee;
    /** Temporary String used for resetting password. */
    private String tempPassword;
    /** Hold reference of current user. */
    @Inject private Login currentUser;
    /** mechanism for starting conversation. */
    @Inject private Conversation convo;
    @Inject private CredentialsManager credentialManager;
    @Inject private EmployeeManager employeeManager;

    /** Returns a list of all the employees. */
    @Override
    public List<Employee> getEmployees() {
        if (currentUser.getList() ==  null) {
            currentUser.setList(employeeManager.getAll());
        }
        return currentUser.getList();
    }
    
    /** Returns employee with a given name. */
    @Override
    public Employee getEmployee(String name) {
        for (Employee emp: getEmployees()) {
            if (emp.getName().equalsIgnoreCase(name)) {
                return emp;
            }
        }
        return null;
    }
    
    /**
     * Returns employee with a given Username.
     * @param userName the username field
     * @return the employees
     */
    public Employee getEmployeeWithUserName(String userName) {
        for (Employee emp: getEmployees()) {
            if (emp.getUserName().equals(userName)) {
                return emp;
            }
        }
        return null;
    }
    
    /** Returns the logged in employee. */
    @Override
    public Employee getCurrentEmployee() {
        return getEmployeeWithUserName(
                currentUser.getCredentials().getUserName());
    }
    
    /** Return map of valid passwords for userNames. */
    @Override
    public Map<String, String> getLoginCombos() {
        return credentialManager.getAll();
    }
    
    /** Verifies that the loginID and password is a valid combination. */
    @Override
    public boolean verifyUser(Credentials credential) {
        Credentials c = credentialManager.find(credential.getUserName());
        if (c != null) {
            if (c.getPassword().equals(credential.getPassword())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates the loginID and password when user tries to login.
     * @param context 
     * @param component 
     * @param value 
     */
    public void validateUsernamePassword(FacesContext context,
                                    UIComponent component, Object value) {
        UIInput userNameInput = 
                (UIInput) component.findComponent("loginUserName");
        String un = userNameInput.getLocalValue().toString();
        String pw = value.toString();
        Credentials temp = new Credentials();
        temp.setUserName(un);
        temp.setPassword(pw);
        
        boolean valid = verifyUser(temp);

        if (!valid) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                            "Incorrect Username or Password"));
        }
    }

    /**
     * Change the password for the current user.
     * @return Navigation string 
     */
    public String changePassword() {
        credentialManager.merge(currentUser.getCredentials());
        return "currentTimeSheet?faces-redirect=true";
    }
    
    /** Adds a new Employee to the collection of Employees. */
    @Override
    public void addEmployee(Employee newEmployee) {
        if (employeeManager.persist(newEmployee)) {
            currentUser.getList().add(newEmployee);
        }
    }

    public boolean addCredentials(Credentials credentials) {
        return credentialManager.persist(credentials);
    }

    /** Deletes the specified user from the collection of Users. */
    @Override
    public void deleteEmployee(Employee userToDelete) {
        if (employeeManager.remove(userToDelete)) {
            currentUser.getList().remove(userToDelete);
        }
        credentialManager.remove(userToDelete.getUserName());
    }

    /**
     * Sets the editable property of employee objects in employee list to false.
     * @return Navigation string
     */
    public String save() {
        for (Employee e : getEmployees()) {
            if (e.isEditable()) {
                employeeManager.merge(e);
                e.setEditable(false);
            }
        }
        return null;
    }
    
    /**
     * Begins the conversation sets the tempEmployee value and redirect
     * user to resetPasswor page.
     * @param e change password for this employee
     * @return Navigation string
     */
    public String resetPassword(Employee e) {
        convo.begin();
        setTempEmployee(e);
        return "resetPassword?faces-redirect=true";
    }
    
    /**
     * Replaces the password in the loginCredentials maps and ends 
     * the conversation.
     * @return Navigation string
     */
    public String savePassword() {
        credentialManager.merge(new Credentials(getTempEmployee().getUserName(), getTempPassword()));
        convo.end();
        return "editUsers?faces-redirect=true";
    }
    
    /**
     * Ends the conversation and redirects the user to editUser page.
     * @return Navigation string
     */
    public String cancelReset() {
        convo.end();
        return "editUsers?faces-redirect=true";
    }
    
    /**
     * Return true if user is admin else false.
     * @return a boolean
     */
    public boolean isAdmin() {
        return currentUser.getCredentials().
                        getUserName().equals(getAdministrator().getUserName());
    }
    
    /** returns the employee object with username of password. */
    @Override
    public Employee getAdministrator() {
        return getEmployeeWithUserName(adminUserName);
    }

    /**
     * Return the temporary password.
     * @return a string
     */
    public String getTempPassword() {
        return tempPassword;
    }

    /**
     * Sets the temporary password.
     * @param tempPassword the password to set to
     */
    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    /**
     * Return the temporary employee.
     * @return and employee object
     */
    public Employee getTempEmployee() {
        return tempEmployee;
    }

    /**
     * Sets the temporary employee.
     * @param temp the employee to set to
     */
    public void setTempEmployee(Employee temp) {
        this.tempEmployee = temp;
    } 
    
    @Override
    public String logout(Employee employee) {
        return null;
    }

}
