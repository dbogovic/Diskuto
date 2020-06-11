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
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "myDiskuto")
@ViewScoped
public class MyDiskuto implements Serializable {

    private List<org.diskuto.models.Forum> owner = new ArrayList();
    private List<org.diskuto.models.Forum> moderator = new ArrayList();
    
    /**
     * Creates a new instance of myDiskuto
     */
    public MyDiskuto() throws Exception {
        Database db = new Database();
        ResourceSet ownership = db.xquery("/forums/forum[owner=\""+AppHelper.getActiveUser().getUsername()+"\"]/name");
        ResourceSet moderatoring = db.xquery("/forums/forum[moderators/moderator=\""+AppHelper.getActiveUser().getUsername()+"\"]/name");
        db.close();
        
        fillIn(ownership, owner);
        fillIn(moderatoring, moderator);
    }

    public List<org.diskuto.models.Forum> getOwner() {
        return owner;
    }

    public List<org.diskuto.models.Forum> getModerator() {
        return moderator;
    }
    
    public void fillIn(ResourceSet resourceSet, List<org.diskuto.models.Forum> list) throws Exception {
        ResourceIterator iterator = resourceSet.getIterator();
        while(iterator.hasMoreResources()){
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("");
            org.diskuto.models.Forum forum = new org.diskuto.models.Forum();
            forum.setName(helper.makeValue("name", objekt));
            list.add(forum);
        }
    }
}
