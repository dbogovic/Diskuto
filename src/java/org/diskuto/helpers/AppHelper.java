/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import java.util.Calendar;
import java.util.Map;
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
                + "." + calendar.get(Calendar.YEAR) + ". " + (calendar.get(Calendar.HOUR_OF_DAY) + 2)
                + ":" + calendar.get(Calendar.MINUTE);
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

    /*    public static ResourceSet getResourceSet(String query) throws Exception {
        Database db = new Database();
        ResourceSet resourceSet = db.xquery(query);
        db.close();
        
        return resourceSet;
    }*/
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
        ResourceSet result = db.xquery("for $x in /users/user where $x/name=\"" + username + "\" and $x/password=\"" + password + "\" return $x");
        db.close();

        return result.getSize() != 0;
    }
}
