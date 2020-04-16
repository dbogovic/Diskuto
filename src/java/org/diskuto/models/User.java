/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.Random;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.MailHelper;
import org.diskuto.helpers.XmlHelper;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
public class User {

    private String email;
    private String username;
    private String password;
    private int confirmCode;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmCode = new Random().nextInt(899999) + 100000;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public int getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(int confirmCode) {
        this.confirmCode = confirmCode;
    }

    public void register() throws Exception {
        sendConfirmMail();
        Database db = new Database();
        db.xquery("update insert <user><email>" + email + "</email><name>" + username
                + "</name><password>" + password + "</password><code>" + confirmCode
                + "</code></user> into /users");
        db.close();
    }

    public void sendConfirmMail() throws Exception {
        MailHelper mh = new MailHelper(email, "Potvrdite registraciju",
                "Kod za registraciju je: " + confirmCode);
        mh.sendMail();
    }

    public void confirmUser() throws Exception {
        setConfirmCode(-1);
        Database db = new Database();
        db.xquery("for $x in /users/user where $x/email=\"" + this.email + "\" return update value $x/code with \"-1\"");
        db.close();
    }

    public boolean login() throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /users/user where $x/name=\"" + username + "\" and $x/password=\"" + password + "\" return $x");
        db.close();

        if (result.getSize() != 0) {
            ResourceIterator iterator = result.getIterator();
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object object = helper.makeObject("user");

            setEmail(helper.makeValue("email", object));
            setConfirmCode(Integer.parseInt(helper.makeValue("code", object)));

            return true;
        } else {
            return false;
        }
    }
}
