/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import org.diskuto.helpers.AppHelper;

/**
 *
 * @author dario
 */
@Named(value = "language")
@SessionScoped
public class Language implements Serializable {

    public static String lang;

    /**
     * Creates a new instance of Language
     */
    public Language() {
        lang = "en";
    }

    public String getLang() {
        if (AppHelper.getActiveUser() == null) {
            return "en";
        } else {
            return AppHelper.getActiveUser().getLanguage();
        }
    }

    public void setLang(String lang) throws Exception {
        AppHelper.getActiveUser().changeLanguage(lang);
        this.lang = lang;
    }

    public Object chooseLanguage() {
        return "";
    }

}
