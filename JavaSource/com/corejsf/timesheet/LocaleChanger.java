package com.corejsf.timesheet;

import java.io.Serializable;
import java.util.Locale;

import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@Named
@SessionScoped
public class LocaleChanger implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Locale locale;
    
    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
    
    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

   public String frenchAction() {
       FacesContext context = FacesContext.getCurrentInstance();
       context.getViewRoot().setLocale(Locale.FRENCH);
       locale = new Locale("fr");
       return null;
   }

   public String englishAction() {
       FacesContext context = FacesContext.getCurrentInstance();
       context.getViewRoot().setLocale(Locale.FRENCH);
       locale = new Locale("en");
       return null;
   }
   
}
