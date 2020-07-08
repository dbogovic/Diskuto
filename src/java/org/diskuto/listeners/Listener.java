/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.listeners;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 * @author dario
 */
@WebListener
public class Listener implements ServletContextListener {

    public static ServletContext sc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sc = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    public static void addToSession(String attribute, Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(attribute, object);
    }

    public static void deleteFromSession(String attribute) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(attribute);
    }

    public static Object getFromSession(String attribute) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(attribute);
    }

}
