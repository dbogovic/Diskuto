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
@Named(value = "index")
@RequestScoped
public class Index {

    private String username;
    private String password;
    private List<String> errorText = new ArrayList();
    
    /**
     * Creates a new instance of Index
     */
    public Index() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }
    
    public void doLogin() throws Exception {
        User user = new User(username, password);
        if(user.login()) {
            Listener.addToSession("user", user);
            if(user.getConfirmCode() != -1) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration.xhtml");
            }
            else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            }
        }
        else {
            this.errorText.add("Neuspjela prijava");
        }
    }
    
    public void newPassword(){
        
    }
    
    public String register(){
        return "registration.xhtml";
    }
}
