/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Post;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "home")
@RequestScoped
public class Home {

    private List<org.diskuto.models.Post> items = new ArrayList();

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
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

}
