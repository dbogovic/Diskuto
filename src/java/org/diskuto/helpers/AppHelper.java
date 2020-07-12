/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import org.diskuto.listeners.Listener;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
public class AppHelper {

    public static User getActiveUser() {
        return (User) Listener.getFromSession("user");
    }

    public static String param(String key) {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        return params.get(key);
    }

    public static String getOutput(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "output");
        return bundle.getString(key);
    }

    public static void checkLogged() throws Exception {
        if (getActiveUser() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login");
        }
    }

    public static String getAttachmentsPath() {
        return Listener.sc.getRealPath("/") + "resources" + File.separator + "attachments";
    }

    public static int generateId(String query) throws Exception {
        Database db = new Database();
        ResourceSet rs = db.xquery(query);
        db.close();

        if (rs.getSize() > 0) {
            return Integer.parseInt(new XmlHelper(rs.getResource(0)).rawValue()) + 1;
        } else {
            return 1;
        }
    }

    public static String date(long unixTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime * 1000);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR) + ".";
    }

    public static String fullDate(long unixTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime * 1000);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR) + ". "
                + String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static ResourceSet getResourceSet(String query) throws Exception {
        Database db = new Database();
        ResourceSet resourceSet = db.xquery(query);
        db.close();

        return resourceSet;
    }

    public static Resource getResource(String query) throws Exception {
        Database db = new Database();
        ResourceSet resourceSet = db.xquery(query);
        db.close();

        if (resourceSet.getSize() == 0) {
            return null;
        } else {
            return resourceSet.getResource(0);
        }
    }

    public static boolean login(String username, String password) throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /users/user where $x/disabled=0 and $x/name=\"" + username + "\" and $x/password=\"" + password + "\" return $x");
        db.close();

        return result.getSize() != 0;
    }

    public static boolean userExists(String email) throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /users/user where $x/email=\"" + email + "\" return $x");
        db.close();

        return result.getSize() != 0;
    }

    public static boolean usernameExists(String name) throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /users/user where $x/name=\"" + name + "\" return $x");
        db.close();

        return result.getSize() != 0;
    }

    public static boolean forumExists(String name) throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return $x");
        db.close();

        return result.getSize() != 0;
    }

    public static boolean regex(String pattern, String matcher) {
        Matcher match = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(matcher);
        return match.find();
    }
}
