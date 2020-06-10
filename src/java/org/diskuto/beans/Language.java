/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

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
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Object chooseLanguage() {
        return "";
    }
    
}
