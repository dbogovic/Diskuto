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
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "registration")
@RequestScoped
public class Registration {

    private String email;
    private String username;
    private String password;
    private String rpassword;
    private String errorText;

    /**
     * Creates a new instance of Registration
     */
    public Registration() {
    }

    public void doRegistration() throws Exception {
        this.errorText = "";
        if (check()) {
            User user = new User();
            user.register(email, username, password);
            FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration");
        }
    }

    private boolean check() throws Exception {

        if (email.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            this.errorText = AppHelper.getOutput("error.somethingMissing");
            return false;
        } else {
            if (!AppHelper.regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", email)) {
                this.errorText = AppHelper.getOutput("error.email1");
                return false;
            } else if (AppHelper.userExists(email)) {
                this.errorText = AppHelper.getOutput("error.email2");
                return false;
            } else if (username.length() < 3) {
                this.errorText = AppHelper.getOutput("error.username1");
                return false;
            } else if (AppHelper.usernameExists(username)) {
                this.errorText = AppHelper.getOutput("error.username2");
                return false;
            } else if (password.length() < 8) {
                this.errorText = AppHelper.getOutput("error.password1");
                return false;
            } else if (!password.equals(rpassword)) {
                this.errorText = AppHelper.getOutput("error.password2");
                return false;
            }
        }
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRpassword() {
        return rpassword;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
