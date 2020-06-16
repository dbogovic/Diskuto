/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Post;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "home")
@RequestScoped
public class Home {

    private List<org.diskuto.models.Post> items = new ArrayList();
    private HashSet<String> messageUsers = new HashSet<>();
    private String unread;

    /**
     * Creates a new instance of Index
     */
    public Home() throws Exception {
        ResourceIterator iterator = AppHelper.getResourceSet("/posts/post/id").getIterator();
        while (iterator.hasMoreResources()) {
            for (String id : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                Retriever retriever = new Retriever(id);
                Post post = retriever.post();
                items.add(post);
            }
        }
        this.unread = new XmlHelper(AppHelper.getResource("count(/messages/message[recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" and seen=\"0\"])")).rawValue();
        ResourceIterator iterator2 = AppHelper.getResourceSet("/messages/message[recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" and seen=\"0\"]/sender").getIterator();
        while (iterator2.hasMoreResources()) {
            Resource resource = iterator2.nextResource();
            XmlHelper helper = new XmlHelper(resource);
            List<String> results = helper.makeListValue("/sender");

            results.forEach((user) -> {
                messageUsers.add(user);
            });
        }
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public HashSet<String> getMessageUsers() {
        return messageUsers;
    }

    public void setMessageUsers(HashSet<String> messageUsers) {
        this.messageUsers = messageUsers;
    }
}
