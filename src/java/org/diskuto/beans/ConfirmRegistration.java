/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.diskuto.helpers.AppHelper;

/**
 *
 * @author dario
 */
@Named(value = "confirmRegistration")
@RequestScoped
public class ConfirmRegistration {

    private String insertedCode;
    private String errorText;

    /**
     * Creates a new instance of ConfirmRegistration
     */
    public ConfirmRegistration() {
    }

    public void validate() {
        try {
            int code = Integer.parseInt(insertedCode);
            if (code != AppHelper.getActiveUser().getConfirmCode()) {
                this.errorText = ("Unijeli ste pogrešan kod");
            } else {
                this.errorText = ("Potvrdili ste registraciju");
                AppHelper.getActiveUser().confirmUser();
                FacesContext.getCurrentInstance().getExternalContext().redirect("discover");
            }
        } catch (Exception ex) {
            this.errorText = ("Unijeli ste pogrešan kod");
        }
    }

    public String sendNewMail() throws Exception {
        AppHelper.getActiveUser().sendConfirmMail();
        return "";
    }

    public String getInsertedCode() {
        return insertedCode;
    }

    public void setInsertedCode(String insertedCode) {
        this.insertedCode = insertedCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
