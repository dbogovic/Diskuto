/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
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

    public String date(long unixTime) {
        return AppHelper.date(unixTime);
    }

    public String fullDate(long unixTime) {
        return AppHelper.fullDate(unixTime);
    }

    public org.diskuto.models.Forum retrieveDiskuto(String name) throws Exception {
        Retriever retriever = new Retriever(name);
        return retriever.forum();
    }
    
    public void subscribe(org.diskuto.models.Forum forum) throws Exception {
        AppHelper.checkLogged();
        if (!AppHelper.getActiveUser().getSubscriptions().contains(forum.getName())) {
            forum.setSubscribers(forum.getSubscribers() + 1);
            AppHelper.getActiveUser().subscribe(forum.getName());
        } else {
            forum.setSubscribers(forum.getSubscribers() - 1);
            AppHelper.getActiveUser().unsubscribe(forum.getName());
        }
    }
}
