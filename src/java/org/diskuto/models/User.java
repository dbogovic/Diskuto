/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.MailHelper;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.listeners.Listener;

/**
 *
 * @author dario
 */
public class User {

    private String email;
    private String username;
    private String password;
    private int confirmCode;
    private long created;
    private boolean disabled;
    private List<String> ignored;
    private List<String> subscriptions;
    private String language;

    public User() {
    }

    public void retrieve(XmlHelper helper) throws Exception {
        Object object = helper.makeObject("user");
        this.email = helper.makeValue("email", object);
        this.username = helper.makeValue("name", object);
        this.password = helper.makeValue("password", object);
        this.confirmCode = Integer.parseInt(helper.makeValue("code", object));
        this.created = Long.parseLong(helper.makeValue("created", object));
        this.disabled = Integer.parseInt(helper.makeValue("disabled", object)) == 1;
        this.language = helper.makeValue("language", object);
        this.subscriptions = helper.makeListValue("user/subscriptions/forum");
        this.ignored = helper.makeListValue("user/ignore/user");
    }

    public void register(String email, String username, String password) throws Exception {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmCode = new Random().nextInt(899999) + 100000;
        this.created = System.currentTimeMillis() / 1000L;
        this.disabled = false;
        this.ignored = new ArrayList();
        this.subscriptions = new ArrayList();
        this.language = "en";

        sendConfirmMail();
        Database db = new Database();
        db.xquery("update insert <user><email>" + this.email + "</email><name>" + this.username
                + "</name><password>" + this.password + "</password><code>" + this.confirmCode
                + "</code><created>" + this.created + "</created><subscriptions/><ignore/>"
                + "<disabled>0</disabled><language>" + this.language + "</language></user> into /users");
        db.close();

        Listener.addToSession("user", this);
    }

    public void login() {
        Listener.addToSession("user", this);
    }

    public void logout() {
        Listener.deleteFromSession("user");
    }

    public void disable() throws Exception {
        this.disabled = true;
        Database db = new Database();
        db.xquery("update value /users/user[name=\"" + AppHelper.getActiveUser().getUsername() + "\"]/disabled with '1'");
        db.close();
        this.logout();
    }

    public void sendConfirmMail() throws Exception {
        MailHelper mh = new MailHelper(email, AppHelper.getOutput("mail.h2"),
                AppHelper.getOutput("mail.t2") + confirmCode);
        mh.sendMail();
    }

    public void confirmUser() throws Exception {
        setConfirmCode(-1);
        Database db = new Database();
        db.xquery("for $x in /users/user where $x/email=\"" + this.email + "\" return update value $x/code with \"-1\"");
        db.close();
    }

    public void changeLanguage(String lang) throws Exception {
        this.language = lang;

        Database db = new Database();
        db.xquery("update value /users/user[name=\"" + this.username + "\"]/language with \"" + this.language + "\"");
        db.close();
    }

    public void subscribe(String diskuto) throws Exception {
        this.subscriptions.add(diskuto);

        Database db = new Database();
        db.xquery("for $x in /users/user where $x/name=\"" + AppHelper.getActiveUser().getUsername()
                + "\" return update insert <forum>" + diskuto + "</forum> into $x/subscriptions");
        db.xquery("let $number := data(/forums/forum[name=\"" + diskuto +"\"]/subscribers)\n"
                + "return update value /forums/forum[name=\"" + diskuto + "\"]/subscribers with $number+1");
        db.close();
    }

    public void unsubscribe(String diskuto) throws Exception {
        this.subscriptions.remove(diskuto);

        Database db = new Database();
        db.xquery("for $x in /users/user[name=\"" + this.username
                + "\"]/subscriptions[forum=\"" + diskuto + "\"] return update delete $x/forum");
        db.xquery("let $number := data(/forums/forum[name=\"" + diskuto +"\"]/subscribers)\n"
                + "return update value /forums/forum[name=\"" + diskuto + "\"]/subscribers with ($number - 1)");
        db.close();
    }

    public void ignore(String user) throws Exception {
        this.ignored.add(user);

        Database db = new Database();
        db.xquery("for $x in /users/user where $x/name=\"" + this.username
                + "\" return update insert <user>" + user + "</user> into $x/ignore");
        db.close();
    }

    public void unignore(String user) throws Exception {
        this.ignored.remove(user);

        Database db = new Database();
        db.xquery("for $x in /users/user[name=\"" + this.username
                + "\"]/ignore[user=\"" + user + "\"] return update delete $x/user");
        db.close();
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<String> getIgnored() {
        return ignored;
    }

    public void setIgnored(List<String> ignored) {
        this.ignored = ignored;
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
