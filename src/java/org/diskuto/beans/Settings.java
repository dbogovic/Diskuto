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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
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
    private boolean wantEmail, wantPassword, errorEmail, errorPassword;
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
        this.password = me.getPassword();
        this.wantEmail = false;
        this.wantPassword = false;

        Database db = new Database();
        resource = db.xquery("/users/user[name=\"" + me.getUsername() + "\"]/subscriptions/forum");
        iterator = resource.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("");
            listDiskuto.add(new org.diskuto.models.Forum().getForum(helper.makeValue("forum", objekt)));
        }
        resource = db.xquery("/users/user[name=\"" + me.getUsername() + "\"]/ignore/user");
        iterator = resource.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("");
            User user = new User(helper.makeValue("user", objekt));
            user.retrieveData();
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

}
