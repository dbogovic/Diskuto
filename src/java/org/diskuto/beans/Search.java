/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Forum;
import org.diskuto.models.Post;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "search")
@ViewScoped
public class Search implements Serializable {

    private String term;
    private List<User> userResults;
    private List<org.diskuto.models.Forum> diskutoResults;
    private List<org.diskuto.models.Post> postResults;

    /**
     * Creates a new instance of Search
     */
    public Search() throws Exception {
        this.term = AppHelper.param("term");

        if (this.term != null && !"".equals(this.term)) {
            userResults = new ArrayList<>();
            diskutoResults = new ArrayList<>();
            postResults = new ArrayList<>();

            Database db = new Database();
            ResourceSet query;
            ResourceIterator iterator;

            query = db.xquery("for $x in /forums/forum where contains(lower-case($x/name), \""
                    + this.term.toLowerCase() + "\") return $x/name");
            iterator = query.getIterator();
            while (iterator.hasMoreResources()) {
                Resource r = iterator.nextResource();
                String value = (String) r.getContent();
                XmlHelper helper = new XmlHelper(value);
                List<String> results = helper.makeRawValue("/name");
                for (String s : results) {
                    Forum diskuto = new Forum();
                    diskuto.setName(s);
                    diskutoResults.add(diskuto);
                }
            }

            query = db.xquery("for $x in /users/user where contains(lower-case($x/name), \""
                    + this.term.toLowerCase() + "\") return $x/name");
            iterator = query.getIterator();
            while (iterator.hasMoreResources()) {
                Resource r = iterator.nextResource();
                String value = (String) r.getContent();
                XmlHelper helper = new XmlHelper(value);
                List<String> results = helper.makeRawValue("/name");
                for (String s : results) {
                    User user = new User();
                    user.setUsername(s);
                    user.retrieveData();
                    userResults.add(user);
                }
            }

            query = db.xquery("for $x in /posts/post where contains(lower-case($x/headline), \""
                    + this.term.toLowerCase() + "\") return $x/id");
            iterator = query.getIterator();
            while (iterator.hasMoreResources()) {
                Resource r = iterator.nextResource();
                String value = (String) r.getContent();
                XmlHelper helper = new XmlHelper(value);
                List<String> results = helper.makeRawValue("/id");
                for (String s : results) {
                    Post post = new Post();
                    post.setId(Integer.parseInt(s));
                    post.retrieveData();
                    postResults.add(post);
                }
            }

            db.close();
        }
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<User> getUserResults() {
        return userResults;
    }

    public void setUserResults(List<User> userResults) {
        this.userResults = userResults;
    }

    public List<Forum> getDiskutoResults() {
        return diskutoResults;
    }

    public void setDiskutoResults(List<Forum> diskutoResults) {
        this.diskutoResults = diskutoResults;
    }

    public List<Post> getPostResults() {
        return postResults;
    }

    public void setPostResults(List<Post> postResults) {
        this.postResults = postResults;
    }

    public void subscribe(org.diskuto.models.Forum forum) throws Exception {
        Database db = new Database();

        if (!this.subscribed(forum)) {
            forum.setSubscribers(forum.getSubscribers() + 1);
            db.xquery("for $x in /users/user where $x/name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\" return update insert <forum>" + forum.getName() + "</forum> into $x/subscriptions");
            AppHelper.getActiveUser().getSubscriptions().add(forum.getName());
        } else {
            forum.setSubscribers(forum.getSubscribers() - 1);
            db.xquery("for $x in /users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\"]/subscriptions[forum=\"" + forum.getName() + "\"] return update delete $x/forum");
            AppHelper.getActiveUser().getSubscriptions().remove(forum.getName());
        }

        db.xquery("for $x in /forums/forum where $x/name=\"" + forum.getName()
                + "\" return update value $x/subscribers with \"" + forum.getSubscribers() + "\"");

        db.close();
    }

    public boolean subscribed(org.diskuto.models.Forum forum) {
        return AppHelper.getActiveUser().getSubscriptions().contains(forum.getName());
    }

    public void showResults() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("search?term=" + term);
    }
}
