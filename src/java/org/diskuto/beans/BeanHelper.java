/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "beanHelper")
@RequestScoped
public class BeanHelper {

    /**
     * Creates a new instance of BeanHelper
     */
    public BeanHelper() {
    }
    
    public User getActiveUser() {
        return AppHelper.getActiveUser();
    }
    
    public String date(long unixTime){
        return AppHelper.date(unixTime);
    }
    
    public String fullDate(long unixTime){
        return AppHelper.fullDate(unixTime);
    }
}