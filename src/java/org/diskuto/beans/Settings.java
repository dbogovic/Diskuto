/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.listeners.Listener;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "settings")
@ViewScoped
public class Settings implements Serializable {

    private User me;
    private String username;
    private String email;
    private String password;
    private boolean wantEmail, wantPassword, wantDisable, errorEmail, errorPassword, errorDisable;
    private List<org.diskuto.models.Forum> listDiskuto = new ArrayList();
    private List<User> ignoredUsers = new ArrayList();

    /**
     * Creates a new instance of Settings
     */
    public Settings() throws Exception {
        ResourceSet resource;
        ResourceIterator iterator;

        me = AppHelper.getActiveUser();
        this.username = me.getUsername();
        this.email = me.getEmail();
        this.wantEmail = false;
        this.wantPassword = false;

        Database db = new Database();
        resource = db.xquery("/users/user[name=\"" + me.getUsername() + "\"]/subscriptions/forum");
        iterator = resource.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            XmlHelper helper = new XmlHelper(r);
            Object objekt = helper.makeObject("");
            org.diskuto.models.Forum diskuto = new org.diskuto.models.Forum();
            diskuto.setName(helper.makeValue("forum", objekt));
            listDiskuto.add(diskuto);
        }
        resource = db.xquery("/users/user[name=\"" + me.getUsername() + "\"]/ignore/user");
        iterator = resource.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            XmlHelper helper = new XmlHelper(r);
            Object objekt = helper.makeObject("");

            Retriever retriever = new Retriever(helper.makeValue("user", objekt));
            User user = retriever.user();
            ignoredUsers.add(user);
        }
        db.close();

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

    public List<org.diskuto.models.Forum> getListDiskuto() {
        return listDiskuto;
    }

    public void setListDiskuto(List<org.diskuto.models.Forum> listDiskuto) {
        this.listDiskuto = listDiskuto;
    }

    public List<User> getIgnoredUsers() {
        return ignoredUsers;
    }

    public void setIgnoredUsers(List<User> ignoredUsers) {
        this.ignoredUsers = ignoredUsers;
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

    public boolean isWantDisable() {
        return wantDisable;
    }

    public void setWantDisable(boolean wantDisable) {
        this.wantDisable = wantDisable;
    }

    public boolean isErrorDisable() {
        return errorDisable;
    }

    public void setErrorDisable(boolean errorDisable) {
        this.errorDisable = errorDisable;
    }

    public String getUsername() {
        return username;
    }

    public void changeEmail() throws Exception {
        errorEmail = false;

        if (!wantEmail) {
            wantEmail = true;
        } else {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);

            if (!matcher.find()) {
                errorEmail = true;
            } else {
                Database db = new Database();
                ResourceSet result = db.xquery("for $x in /users/user where $x/email=\"" + email + "\" return $x");

                if (result.getSize() > 0) {
                    errorEmail = true;
                } else {
                    errorEmail = false;
                    wantEmail = false;

                    db.xquery("update value /users/user[name=\"" + me.getUsername() + "\"]/email with '" + email + "'");
                    me.setEmail(email);
                }

                db.close();
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

                Database db = new Database();
                db.xquery("update value /users/user[name=\"" + me.getUsername() + "\"]/password with '" + this.password + "'");
                db.close();
                me.setPassword(password);
            }
        }
    }

    public void unsubscribe(org.diskuto.models.Forum d) throws Exception {
        Database db = new Database();
        db.xquery("for $x in /users/user[name=\"" + me.getUsername()
                + "\"]/subscriptions[forum=\"" + d.getName() + "\"] return update delete $x/forum");
        db.close();
        this.listDiskuto.remove(d);
    }

    public void unignore(User u) throws Exception {
        Database db = new Database();
        db.xquery("for $x in /users/user[name=\"" + me.getUsername()
                + "\"]/ignore[user=\"" + u.getUsername() + "\"] return update delete $x/user");
        db.close();
        this.ignoredUsers.remove(u);
    }

    public void disableAccount() throws Exception {
        errorDisable = false;

        if (!wantDisable) {
            wantDisable = true;
            this.password = "";
        } else {
            if (!password.equals(me.getPassword())) {
                errorDisable = true;
                this.password = "";
            } else {
                Database db = new Database();
                db.xquery("update value /users/user[name=\"" + me.getUsername() + "\"]/disabled with '1'");
                db.close();
                Listener.deleteFromSession("user");
                FacesContext.getCurrentInstance().getExternalContext().redirect("disabled");
            }
        }
    }

}
