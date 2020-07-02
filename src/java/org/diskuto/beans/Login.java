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
import org.diskuto.helpers.Retriever;
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "login")
@RequestScoped
public class Login {

    private User user;
    private String username;
    private String password;
    private String errorText;

    /**
     * Creates a new instance of Login
     */
    public Login() {
    }

    public void doLogin() throws Exception {
        errorText = "";
        if (AppHelper.login(username, password)) {
            Retriever retriever = new Retriever(username);
            this.user = retriever.user();
            this.user.login();
            if (user.getConfirmCode() != -1) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("home");
            }
        } else {
            this.errorText = AppHelper.getOutput("error.login");
        }
    }

    public void logout() throws Exception {
        this.user.logout();
        FacesContext.getCurrentInstance().getExternalContext().redirect("home");
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

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

}
