/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

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
    
}
