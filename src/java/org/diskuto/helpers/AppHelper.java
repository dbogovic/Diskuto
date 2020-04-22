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

/**
 *
 * @author dario
 */
public class AppHelper {
    
    public static User getActiveUser(){
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
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) +
                "."+ calendar.get(Calendar.YEAR) + ".";
    }
    
    public static String fullDate(long unixTime) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime * 1000);
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) +
                "."+ calendar.get(Calendar.YEAR) + ". " + (calendar.get(Calendar.HOUR_OF_DAY)+2) 
                + ":" + calendar.get(Calendar.MINUTE);
    }
    
}
