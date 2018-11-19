package com.corejsf.timesheet;

import java.io.Serializable;
import java.util.Locale;

import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Responsible for changing the locale of the user programatically.
 * @author Karanveer Khanna
 * @version 1.1
 */
@Named
@SessionScoped
public class LocaleChanger implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Represents current Locale. */
    private Locale locale;
    
    /** Sets the locale to current zone locale on initial request. */
    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().
                getExternalContext().getRequestLocale();
    }
    
    /**
     * Locale getter.
     * @return locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Language getter.
     * @return english or french
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * Change the locale to French.
     * @return null
     */
    public String frenchAction() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.FRENCH);
        locale = new Locale("fr");
        return null;
    }
    
    /**
     * Change the locale to English.
     * @return null
     */
    public String englishAction() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.FRENCH);
        locale = new Locale("en");
        return null;
    }
}
