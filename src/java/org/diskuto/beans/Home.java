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
    private int totalItems;
    private int itemsIteratorId = 0;
    private String query;

    /**
     * Creates a new instance of Index
     */
    public Home() throws Exception {

        if (AppHelper.getActiveUser() == null) {
            this.totalItems = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(/posts/post/id)")).rawValue());
            loadItems();
        } else {

            if (!AppHelper.getActiveUser().getSubscriptions().isEmpty()) {
                this.query = "for $x in /posts/post where $x/diskuto=(";
                AppHelper.getActiveUser().getSubscriptions().forEach(diskuto -> {
                    this.query += "\"" + diskuto + "\",";
                });
                this.query = this.query.substring(0, this.query.length() - 1)
                        + ") order by $x/created return $x/id";
                this.totalItems = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(" + query + ")")).rawValue());
                loadItems();
            }

            this.unread = new XmlHelper(AppHelper.getResource("count(/messages/message[recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" and seen=\"0\"])")).rawValue();
            ResourceIterator resourceIterator = AppHelper.getResourceSet("/messages/message[recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" and seen=\"0\"]/sender").getIterator();
            while (resourceIterator.hasMoreResources()) {
                Resource resource = resourceIterator.nextResource();
                XmlHelper helper = new XmlHelper(resource);
                List<String> results = helper.makeListValue("/sender");

                results.forEach((user) -> {
                    messageUsers.add(user);
                });
            }
        }
    }

    public void loadItems() throws Exception {
        ResourceIterator iterator;

        if (AppHelper.getActiveUser() == null) {
            iterator = AppHelper.getResourceSet("reverse(/posts/post[position() <= last()-"
                    + itemsIteratorId + " and position() > last()-10 ]/id)").getIterator();
        } else {
            iterator = AppHelper.getResourceSet("reverse((" + query + ")[position() <= last()-"
                    + itemsIteratorId + " and position() > last()-" + (10 + itemsIteratorId) + "])").getIterator();
        }
        
        while (iterator.hasMoreResources()) {
            for (String id : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                Retriever retrievePost = new Retriever(id);
                Post post = retrievePost.post();
                if (AppHelper.getActiveUser() == null || !AppHelper.getActiveUser().getIgnored().contains(post.getOwner())) {
                    items.add(post);
                }
            }
        }
        itemsIteratorId += 10;
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

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getItemsIteratorId() {
        return itemsIteratorId;
    }

    public void setItemsIteratorId(int itemsIteratorId) {
        this.itemsIteratorId = itemsIteratorId;
    }
}
