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
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Forum;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "myDiskuto")
@ViewScoped
public class MyDiskuto implements Serializable {

    private List<org.diskuto.models.Forum> owner;
    private List<org.diskuto.models.Forum> moderator;

    /**
     * Creates a new instance of myDiskuto
     */
    public MyDiskuto() throws Exception {
        if (AppHelper.getActiveUser() != null) {
            this.owner = fillIn("/forums/forum[owner=\"" + AppHelper.getActiveUser().getUsername() + "\"]/name");
            this.moderator = fillIn("/forums/forum[moderators/moderator=\"" + AppHelper.getActiveUser().getUsername() + "\"]/name");
        }
    }

    private List<org.diskuto.models.Forum> fillIn(String query) throws Exception {
        List<org.diskuto.models.Forum> list = new ArrayList<>();

        ResourceIterator iterator = AppHelper.getResourceSet(query).getIterator();
        while (iterator.hasMoreResources()) {
            XmlHelper helper = new XmlHelper(iterator.nextResource());
            Object objekt = helper.makeObject("");
            Retriever retriever = new Retriever(helper.makeValue("name", objekt));
            list.add(retriever.forum());
        }

        return list;
    }

    public List<Forum> getOwner() {
        return owner;
    }

    public void setOwner(List<Forum> owner) {
        this.owner = owner;
    }

    public List<Forum> getModerator() {
        return moderator;
    }

    public void setModerator(List<Forum> moderator) {
        this.moderator = moderator;
    }

}
