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
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Comment;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "profile")
@ViewScoped
public class Profile implements Serializable {

    private User chosen;
    private boolean me;
    private String p_created;
    private boolean ignored;
    private long upvotes;
    private long downvotes;
    private List<org.diskuto.models.Post> posts;
    private int postsIteratorId = 0;
    private int totalPosts;
    private List<Comment> comments;
    private int commentsIteratorId = 0;
    private int totalComments;

    /**
     * Creates a new instance of Profile
     */
    public Profile() throws Exception {
        String _user = AppHelper.param("name");
        if (_user.equals(AppHelper.getActiveUser().getUsername())) {
            me = true;
            this.chosen = AppHelper.getActiveUser();
        } else {
            me = false;
            this.chosen = new User();
            this.chosen.setUsername(_user);
            if (!this.chosen.retrieveData()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
            }
        }

        Database db = new Database();
        this.upvotes = db.xquery("//upvote[user=\"" + this.chosen.getUsername() + "\"]").getSize();
        this.downvotes = db.xquery("//downvote[user=\"" + this.chosen.getUsername() + "\"]").getSize();
        this.totalPosts = Integer.parseInt(db.xquery("count(//post[owner=\"" + this.chosen.getUsername() + "\"]/id)").getIterator().nextResource().getContent().toString());
        this.totalComments = Integer.parseInt(db.xquery("count(//post/comments/comment[owner=\"" + this.chosen.getUsername() + "\"])").getIterator().nextResource().getContent().toString());
        db.close();

        posts = new ArrayList<>();
        loadPosts();
        comments = new ArrayList<>();
        loadComments();

    }

    public long getUpvotes() throws Exception {
        return upvotes;
    }

    public long getDownvotes() throws Exception {
        return downvotes;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public int getPostsIteratorId() {
        return postsIteratorId;
    }

    public int getCommentsIteratorId() {
        return commentsIteratorId;
    }

    public List<org.diskuto.models.Post> getPosts() {
        return posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public User getChosen() {
        return chosen;
    }

    public boolean isMe() {
        return me;
    }

    public String getP_created() {
        return AppHelper.date(chosen.getCreated());
    }

    public boolean isIgnored() throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("/users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                + "\"]/ignore/user[\"" + this.chosen.getUsername() + "\"]");
        db.close();

        this.ignored = result.getSize() > 0;
        return this.ignored;
    }

    public void ignore() throws Exception {

        Database db = new Database();

        if (!this.ignored) {
            db.xquery("for $x in /users/user where $x/name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\" return update insert <user>" + this.chosen.getUsername() + "</user> into $x/ignore");
            this.ignored = true;
        } else {
            db.xquery("for $x in /users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\"]/ignore[user=\"" + this.chosen.getUsername() + "\"] return update delete $x/user");
            this.ignored = false;
        }

        db.close();
    }

    public void loadPosts() throws Exception {

        Database db = new Database();
        ResourceSet query = db.xquery("//post[ owner=\"" + chosen.getUsername() + "\" and " + postsIteratorId + " < position() and position() <= 10 ]/id");
        ResourceIterator iterator = query.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            List<String> results = helper.makeRawValue("/id");
            for (String s : results) {
                org.diskuto.models.Post post = new org.diskuto.models.Post();
                post.setId(Integer.parseInt(s));
                post.retrieveData();
                posts.add(post);
            }
        }
        postsIteratorId += 10;
        db.close();
    }

    public void loadComments() throws Exception {

        Database db = new Database();
        ResourceSet query = db.xquery("//post/comments/comment[owner=\"" + this.chosen.getUsername()
                + "\" and " + commentsIteratorId + " < position() and position() <= 10 ]");
        ResourceIterator iterator = query.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);

            Comment comment = new Comment();
            comment.commentFromXML(helper);
            org.diskuto.models.Post p = new org.diskuto.models.Post();
            p.setId(Integer.parseInt(
                    db.xquery("for $x in /posts/post where $x/comments/comment/id=\""
                            + comment.getId() + "\" return data($x/id)").getIterator()
                            .nextResource().getContent().toString()));
            p.retrieveData();
            comment.setPost(p.getId());
            this.comments.add(comment);
        }
        commentsIteratorId += 10;
        db.close();
    }

}
