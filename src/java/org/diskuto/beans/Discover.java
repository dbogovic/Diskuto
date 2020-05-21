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
import org.diskuto.helpers.Database;
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
    
    public void subscribe(org.diskuto.models.Forum forum) throws Exception {
        Database db = new Database();
        
        if (!this.subscribed(forum)) {
            forum.setSubscribers(forum.getSubscribers() + 1);
            db.xquery("for $x in /users/user where $x/name=\"" + me.getUsername()
                    + "\" return update insert <forum>" + forum.getName() + "</forum> into $x/subscriptions");
            this.me.getSubscriptions().add(forum.getName());
        } else {
            forum.setSubscribers(forum.getSubscribers() - 1);
            db.xquery("for $x in /users/user[name=\"" + me.getUsername()
                    + "\"]/subscriptions[forum=\"" + forum.getName() + "\"] return update delete $x/forum");
            this.me.getSubscriptions().remove(forum.getName());
        }

        db.xquery("for $x in /forums/forum where $x/name=\"" + forum.getName()
                + "\" return update value $x/subscribers with \"" + forum.getSubscribers() + "\"");

        db.close();
    }
    
    public boolean subscribed(org.diskuto.models.Forum forum) {
        return this.me.getSubscriptions().contains(forum.getName());
    }
    
}
