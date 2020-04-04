/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.listeners;

import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author dario
 */
public class Listener implements HttpSessionListener {

    private static HttpSession session;
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        session = se.getSession();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Enumeration attributes = session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            deleteFromSession(attributes.nextElement().toString());
        }
    }
    
    public static void addToSession(String attribute, Object object) {
        session.setAttribute(attribute, object);
    }
    
    public static void deleteFromSession(String attribute) {
        session.removeAttribute(attribute);
    }
    
    public static Object getFromSession(String attribute) {
        return session.getAttribute(attribute);
    }
    
}
