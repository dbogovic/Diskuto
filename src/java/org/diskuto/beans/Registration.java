/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.diskuto.helpers.Database;
import org.diskuto.listeners.Listener;
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
    private List<String> errorText = new ArrayList();

    /**
     * Creates a new instance of Registration
     */
    public Registration() {
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

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }

    public void doRegistration() throws Exception {
        errorText.clear();
        if (check()) {
            User user = new User(email, username, password);
            user.register();
            Listener.addToSession("user", user);
            FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration.xhtml");
        }
    }

    private boolean check() throws Exception {

        Matcher matcher;
        ResourceSet resultEmail, resultUsername;
        boolean error = true;

        if (email.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            error = printError("Niste unijeli sve podatke");
        } else {
            matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);

            Database db = new Database();
            resultEmail = db.xquery("for $x in /users/user where $x/email=\"" + email + "\" return $x");
            resultUsername = db.xquery("for $x in /users/user where $x/name=\"" + username + "\" return $x");
            db.close();

            if (!matcher.find()) {
                error = printError("E-mail nije ispravan");
            } else if (resultEmail.getSize() > 0) {
                error = printError("E-mail već postoji u bazi");
            } else if (username.length() < 3) {
                error = printError("Korisničko ime je prekratko");
            } else if (resultUsername.getSize() > 0) {
                error = printError("Korisničko ime već postoji u bazi");
            } else if (password.length() < 8) {
                error = printError("Lozinka je preslaba");
            } else if (!password.equals(rpassword)) {
                error = printError("Lozinke ne odgovaraju");
            }
        }
        return error;
    }

    private boolean printError(String errorText) {
        this.errorText.add(errorText);
        return false;
    }
}
