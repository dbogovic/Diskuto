/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
@Named(value = "forum")
@ViewScoped
public class Forum implements Serializable {

    private org.diskuto.models.Forum diskuto;
    private String category;
    private List<org.diskuto.models.Post> items = new ArrayList();
    private int totalItems;
    private int itemsIteratorId = 0;

    /**
     * Creates a new instance of Forum
     */
    public Forum() throws Exception {
        this.category = AppHelper.param("cat");
        Retriever retrieveForum = new Retriever(AppHelper.param("name"));
        this.diskuto = retrieveForum.forum();
        if (this.diskuto == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }

        String query = "count(/posts/post[diskuto=\"" + this.diskuto.getName() + "\"";
        if (this.category != null) {
            query += " and category=\"" + this.category + "\"";
        }
        query += "])";

        this.totalItems = Integer.parseInt(new XmlHelper(AppHelper.getResource(query)).rawValue());
        loadItems();
    }

    public void loadItems() throws Exception {
        String query = "reverse(/posts/post[diskuto=\"" + this.diskuto.getName() + "\"";
        if (this.category != null) {
            query += " and category=\"" + this.category + "\"";
        }
        query += "][position() <= last()-" + itemsIteratorId + " and position() > last()-10 ]/id)";
        ResourceIterator iterator = AppHelper.getResourceSet(query).getIterator();
        while (iterator.hasMoreResources()) {
            for (String id : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                Retriever retrievePost = new Retriever(id);
                Post post = retrievePost.post();
                items.add(post);
            }
        }
        itemsIteratorId += 10;
    }

    public List<Post> freshPost(String category) throws Exception {
        List<Post> freshPost = new ArrayList();

        Post post = new Post();
        Resource resource = AppHelper.getResource("(for $x in /posts/post where $x/category=\"" + category
                + "\" and $x/diskuto=\"" + this.diskuto.getName() + "\" order by $x/created descending return $x)[position() le 1]");
        if (resource != null) {
            post.retrieve(new XmlHelper(resource));
            freshPost.add(post);
        }

        return freshPost;
    }

    public void subscribe(org.diskuto.models.Forum forum) throws Exception {
        if (!AppHelper.getActiveUser().getSubscriptions().contains(forum.getName())) {
            forum.setSubscribers(forum.getSubscribers() + 1);
            AppHelper.getActiveUser().subscribe(forum.getName());
        } else {
            forum.setSubscribers(forum.getSubscribers() - 1);
            AppHelper.getActiveUser().unsubscribe(forum.getName());
        }
    }

    public org.diskuto.models.Forum getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(org.diskuto.models.Forum diskuto) {
        this.diskuto = diskuto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
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
