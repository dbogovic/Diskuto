/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.models.Forum;
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "discover")
@ViewScoped
public class Discover implements Serializable {

    private final List<org.diskuto.models.Forum> popular;
    private final List<org.diskuto.models.Forum> newest;
    private final User me;
    
    /**
     * Creates a new instance of Discover
     */
    public Discover() {
        me = AppHelper.getActiveUser();
        popular = new ArrayList<>();
        newest = new ArrayList<>();
    }

    public List<Forum> getPopular() {
        return popular;
    }

    public List<Forum> getNewest() {
        return newest;
    }
    
    public void subscribe(org.diskuto.models.Forum forum) {
        
    }
    
    public boolean subscribed(org.diskuto.models.Forum forum) {
        return this.me.getSubscriptions().contains(forum);
    }
    
}
