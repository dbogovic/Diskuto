/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.UUID;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.MailHelper;

/**
 *
 * @author dario
 */
@Named(value = "newPassword")
@ViewScoped
public class NewPassword implements Serializable {

    private int state;
    private String email;
    private String errorText;
    private String code;
    private String password;
    private String rpassword;

    /**
     * Creates a new instance of NewPassword
     */
    public NewPassword() {
        String code = AppHelper.param("code");
        if (code != null) {
            this.code = code;
            state = 2;
        } else {
            state = 1;
        }
    }

    public void abolishPassword() throws Exception {
        this.errorText = "";

        if (AppHelper.userExists(email)) {
            String abolishPasswordCode = UUID.randomUUID().toString();

            Database db = new Database();
            db.xquery("for $x in /users/user where $x/email=\"" + email
                    + "\" return update insert <abolishPasswordCode>"
                    + abolishPasswordCode + "</abolishPasswordCode> into $x");
            db.xquery("for $x in /users/user where $x/email=\"" + email
                    + "\" return update delete $x/password");
            db.close();

            this.errorText = AppHelper.getOutput("success.msg2");
            MailHelper mh = new MailHelper(email, AppHelper.getOutput("mail.h1"),
                    AppHelper.getOutput("mail.t1")
                    + "http://127.0.0.1:3000/Diskuto/faces/newPassword?code="
                    + abolishPasswordCode);
            mh.sendMail();
            this.state = 3;
        } else {
            this.errorText = AppHelper.getOutput("error.email3");
        }
    }

    public void addNewPassword() throws Exception {
        this.errorText = "";
        if (check()) {
            this.errorText = "Lozinka je promijenjena";
            Database db = new Database();
            db.xquery("for $x in /users/user where $x/abolishPasswordCode=\"" + code
                    + "\" return update insert <password>"
                    + password + "</password> into $x");
            db.xquery("for $x in /users/user where $x/abolishPasswordCode=\"" + code
                    + "\" return update delete $x/abolishPasswordCode");
            db.close();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login");
        }
    }

    private boolean check() throws Exception {

        if (password.length() == 0 || rpassword.length() == 0) {
            this.errorText = AppHelper.getOutput("error.somethingMissing");
            return false;
        } else {
            if (password.length() < 8) {
                this.errorText = AppHelper.getOutput("error.password1");
                return false;
            } else if (!password.equals(rpassword)) {
                this.errorText = AppHelper.getOutput("error.password2");
                return false;
            }
        }
        return true;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
