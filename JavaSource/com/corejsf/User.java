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
    private Credentials cred = new Credentials();
    private @Inject EmployeeDetails emp;
//    private @Inject Conversation convo;
//    private boolean isConvoStart = false;
    
    public String create() {
//        if (!isConvoStart) {
//            convo.begin();
//            isConvoStart = true;
//        }
        return "createNewUser?faces-redirect=true";
    }
    
    public String edit() {
        return "editUsers?faces-redirect=true";
    }
    
    public String add() {
        emp.addEmployee(this);
        Map<String, String> credentialsMap = emp.getLoginCombos();
        credentialsMap.put(this.getUserName(), cred.getPassword());
//        if (isConvoStart) {
//            convo.end();
//            isConvoStart = false;
//        }
        return "index?faces-redirect=true";
    }
    
    public Credentials getCred() {
        return cred;
    }

    public void setCred(Credentials cred) {
        this.cred = cred;
    }
    
}
