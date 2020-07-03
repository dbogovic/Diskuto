/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;

/**
 *
 * @author dario
 */
@Named(value = "settings")
@ViewScoped
public class Settings implements Serializable {

    private String username = AppHelper.getActiveUser().getUsername();
    private String email = AppHelper.getActiveUser().getEmail();
    private String password;
    private boolean wantEmail = false;
    private boolean wantPassword = false;
    private boolean wantDisable = false;
    private boolean errorEmail, errorPassword, errorDisable;

    /**
     * Creates a new instance of Settings
     */
    public Settings() throws Exception {
        AppHelper.checkLogged();
    }

    public void changeEmail() throws Exception {
        errorEmail = false;

        if (!wantEmail) {
            wantEmail = true;
        } else {
            if (!AppHelper.regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", email)) {
                errorEmail = true;
            } else {
                if (AppHelper.userExists(email)) {
                    errorEmail = true;
                } else {
                    errorEmail = false;
                    wantEmail = false;
                    AppHelper.getActiveUser().setEmail(email);

                    Database db = new Database();
                    db.xquery("update value /users/user[name=\"" + AppHelper.getActiveUser().getUsername() + "\"]/email with '" + email + "'");
                    db.close();
                }
            }
        }
    }

    public void changePassword() throws Exception {
        errorPassword = false;

        if (!wantPassword) {
            wantPassword = true;
            this.password = "";
        } else {
            if (password.length() < 8) {
                errorPassword = true;
            } else {
                errorPassword = false;
                wantPassword = false;
                AppHelper.getActiveUser().setPassword(password);

                Database db = new Database();
                db.xquery("update value /users/user[name=\"" + AppHelper.getActiveUser().getUsername() + "\"]/password with '" + this.password + "'");
                db.close();
            }
        }
    }

    public void disableAccount() throws Exception {
        errorDisable = false;

        if (!wantDisable) {
            wantDisable = true;
            this.password = "";
        } else {
            if (!password.equals(AppHelper.getActiveUser().getPassword())) {
                errorDisable = true;
                this.password = "";
            } else {
                AppHelper.getActiveUser().disable();
                FacesContext.getCurrentInstance().getExternalContext().redirect("disabled");
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isWantEmail() {
        return wantEmail;
    }

    public void setWantEmail(boolean wantEmail) {
        this.wantEmail = wantEmail;
    }

    public boolean isWantPassword() {
        return wantPassword;
    }

    public void setWantPassword(boolean wantPassword) {
        this.wantPassword = wantPassword;
    }

    public boolean isWantDisable() {
        return wantDisable;
    }

    public void setWantDisable(boolean wantDisable) {
        this.wantDisable = wantDisable;
    }

    public boolean isErrorEmail() {
        return errorEmail;
    }

    public void setErrorEmail(boolean errorEmail) {
        this.errorEmail = errorEmail;
    }

    public boolean isErrorPassword() {
        return errorPassword;
    }

    public void setErrorPassword(boolean errorPassword) {
        this.errorPassword = errorPassword;
    }

    public boolean isErrorDisable() {
        return errorDisable;
    }

    public void setErrorDisable(boolean errorDisable) {
        this.errorDisable = errorDisable;
    }

}
