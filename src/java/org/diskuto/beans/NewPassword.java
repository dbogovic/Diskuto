/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.MailHelper;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "newPassword")
@ViewScoped
public class NewPassword implements Serializable {

    private boolean abolishedPassword;
    private String email;
    private List<String> errorText = new ArrayList();
    private String apCode;
    private String password;
    private String rpassword;

    /**
     * Creates a new instance of NewPassword
     */
    public NewPassword() {
        String code = AppHelper.param("code");
        if (code != null) {
            apCode = code;
            abolishedPassword = true;
        } else {
            abolishedPassword = false;
        }
    }

    public boolean isAbolishedPassword() {
        return abolishedPassword;
    }

    public void setAbolishedPassword(boolean abolishedPassword) {
        this.abolishedPassword = abolishedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
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

    public void abolishPassword() throws Exception {

        Database db = new Database();
        ResourceSet resultEmail = db.xquery("for $x in /users/user where $x/email=\"" + email + "\" return $x");
        db.close();

        if (resultEmail.getSize() > 0) {
            String abolishPasswordCode = UUID.randomUUID().toString();

            Database db2 = new Database();
            db2.xquery("for $x in /users/user where $x/email=\"" + email
                    + "\" return update insert <abolishPasswordCode>"
                    + abolishPasswordCode + "</abolishPasswordCode> into $x");
            db2.xquery("for $x in /users/user where $x/email=\"" + email
                    + "\" return update delete $x/password");
            db2.close();

            this.errorText.add("Upute za kreiranje nove lozinke su poslane na vaš e-mail");
            MailHelper mh = new MailHelper(email, "Nova lozinka",
                    "Slijedite ovu poveznicu kako bi ste unesli novu lozinku: "
                    + "http://127.0.0.1:3000/Diskuto/faces/newPassword?code="
                    + abolishPasswordCode);
            mh.sendMail();
        } else {
            this.errorText.add("Unijeli ste nepostojeći e-mail");
        }
    }

    public void addNewPassword() throws Exception {
        this.errorText.clear();
        if (check()) {
            this.errorText.add("Lozinka je promijenjena");
            Database db = new Database();
            db.xquery("for $x in /users/user where $x/abolishPasswordCode=\"" + apCode
                    + "\" return update insert <password>"
                    + password + "</password> into $x");
            db.xquery("for $x in /users/user where $x/abolishPasswordCode=\"" + apCode
                    + "\" return update delete $x/abolishPasswordCode");
            db.close();
            FacesContext.getCurrentInstance().getExternalContext().redirect("home");
        }
    }

    private boolean check() throws Exception {

        boolean error = true;

        if (password.length() == 0 || rpassword.length() == 0) {
            error = printError("Niste unijeli sve podatke");
        } else {
            if (password.length() < 8) {
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
