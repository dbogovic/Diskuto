/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.diskuto.listeners.Listener;
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "confirmRegistration")
@RequestScoped
public class ConfirmRegistration {

    private String insertedCode;
    private List<String> errorText = new ArrayList();
    
    /**
     * Creates a new instance of ConfirmRegistration
     */
    public ConfirmRegistration() {
    }

    public String getInsertedCode() {
        return insertedCode;
    }

    public void setInsertedCode(String insertedCode) {
        this.insertedCode = insertedCode;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }

    public User getRegisteredUser(){
        return (User) Listener.getFromSession("user");
    }
    
    public void validate() {
        try {
            int code = Integer.parseInt(insertedCode);
            if(code != getRegisteredUser().getConfirmCode()) {
                this.errorText.add("Unijeli ste pogrešan kod");                
            }
            else {
                this.errorText.add("Potvrdili ste registraciju");   
                getRegisteredUser().confirmUser();
                //discover page umjesto toga
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            }
        }
        catch(Exception ex){
            this.errorText.add("Unijeli ste pogrešan kod");
        }
    }
    
    public String sendNewMail(){
        getRegisteredUser().sendConfirmMail();
        return "";
    }
}
