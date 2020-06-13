/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.diskuto.helpers.Database;
import org.diskuto.models.User;
import org.xmldb.api.base.ResourceSet;

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
            this.errorText = ("Niste unijeli sve podatke");
            return false;
        } else {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);

            Database db = new Database();
            ResourceSet resultEmail = db.xquery("for $x in /users/user where $x/email=\"" + email + "\" return $x");
            ResourceSet resultUsername = db.xquery("for $x in /users/user where $x/name=\"" + username + "\" return $x");
            db.close();

            if (!matcher.find()) {
                this.errorText = ("E-mail nije ispravan");
                return false;
            } else if (resultEmail.getSize() > 0) {
                this.errorText = ("E-mail već postoji u bazi");
                return false;
            } else if (username.length() < 3) {
                this.errorText = ("Korisničko ime je prekratko");
                return false;
            } else if (resultUsername.getSize() > 0) {
                this.errorText = ("Korisničko ime već postoji u bazi");
                return false;
            } else if (password.length() < 8) {
                this.errorText = ("Lozinka je preslaba");
                return false;
            } else if (!password.equals(rpassword)) {
                this.errorText = ("Lozinke ne odgovaraju");
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
