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
import org.diskuto.helpers.Database;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Post;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

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
        Database db = new Database();
        ResourceSet query = db.xquery("/posts/post/id");
        ResourceIterator iterator = query.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            XmlHelper helper = new XmlHelper(r);
            List<String> results = helper.makeListValue("/id");
            for (String s : results) {
                Retriever retriever = new Retriever(s);
                Post post = retriever.post();
                items.add(post);
            }
        }
        db.close();
    }
    
    public List<Post> getItems() {
        return items;
    }

}
