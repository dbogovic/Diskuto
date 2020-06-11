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
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Forum;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "discover")
@ViewScoped
public class Discover implements Serializable {

    private final List<org.diskuto.models.Forum> diskutos;
    private final User me;
    
    /**
     * Creates a new instance of Discover
     */
    public Discover() throws Exception {
        me = AppHelper.getActiveUser();
        diskutos = new ArrayList<>();
        
        Database db = new Database();
        ResourceSet info = db.xquery("/forums/forum");
        db.close();

        ResourceIterator iterator = info.getIterator();

        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("forum");

            org.diskuto.models.Forum diskuto = new org.diskuto.models.Forum();
            diskuto.setName(helper.makeValue("name", objekt));
            diskuto.setDescription(helper.makeValue("description", objekt));
            diskuto.setSubscribers(Integer.parseInt(helper.makeValue("subscribers", objekt)));
            
            diskutos.add(diskuto);
        }
    }

    public List<Forum> getDiskutos() {
        return diskutos;
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
