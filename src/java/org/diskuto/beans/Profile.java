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
import org.diskuto.models.Comment;
import org.diskuto.models.Post;
import org.diskuto.models.User;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "profile")
@ViewScoped
public class Profile implements Serializable {

    private User user;
    private boolean me;
    private int upvotes;
    private int downvotes;
    private int totalPosts;
    private int totalComments;
    private List<org.diskuto.models.Post> posts = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private int postsIteratorId = 0, commentsIteratorId = 0;

    /**
     * Creates a new instance of Profile
     */
    public Profile() throws Exception {
        AppHelper.checkLogged();

        if (AppHelper.getActiveUser() != null) {

            String user = AppHelper.param("name");
            if (user.equals(AppHelper.getActiveUser().getUsername())) {
                me = true;
                this.user = AppHelper.getActiveUser();
            } else {
                me = false;
                Retriever retriever = new Retriever(user);
                this.user = retriever.user();
                if (this.user == null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
                }
                if (this.user.isDisabled()) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("disabledProfile");                    
                }
            }

            this.upvotes = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(//post[owner=\""
                    + this.user.getUsername() + "\"]/upvote/user)+count(//comment[owner=\"" + this.user.getUsername()
                    + "\"]/upvote/user)")).rawValue());
            this.downvotes = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(//post[owner=\""
                    + this.user.getUsername() + "\"]/downvote/user)+count(//comment[owner=\"" + this.user.getUsername()
                    + "\"]/downvote/user)")).rawValue());
            this.totalPosts = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(//post[owner=\""
                    + this.user.getUsername() + "\"]/id)")).rawValue());
            this.totalComments = Integer.parseInt(new XmlHelper(AppHelper.getResource("count(//post/comments/comment[owner=\""
                    + this.user.getUsername() + "\"])")).rawValue());

            loadPosts();
            loadComments();
        }
    }

    public void loadPosts() throws Exception {
        ResourceIterator iterator = AppHelper.getResourceSet("reverse(//post[ owner=\"" + user.getUsername() + "\"][ position() <= last()-" + postsIteratorId + " and position() > last()-10 ]/id)").getIterator();
        while (iterator.hasMoreResources()) {
            for (String id : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                Retriever retriever = new Retriever(id);
                org.diskuto.models.Post post = retriever.post();
                if (!post.isDeleted()) {
                    posts.add(post);
                }
            }
        }
        postsIteratorId += 10;
    }

    public void loadComments() throws Exception {
        ResourceIterator iterator = AppHelper.getResourceSet("reverse(//post/comments/comment[owner=\"" + this.user.getUsername()
                + "\"][position() <= last()-" + commentsIteratorId + " and position() > last()-10 ])").getIterator();
        while (iterator.hasMoreResources()) {
            Comment comment = new Comment();
            comment.retrieve(new XmlHelper(iterator.nextResource()));
            comment.post();
            if (!comment.isDeleted()) {
                this.comments.add(comment);
            }
        }
        commentsIteratorId += 10;
    }

    public void ignore() throws Exception {
        if (!AppHelper.getActiveUser().getIgnored().contains(this.user.getUsername())) {
            AppHelper.getActiveUser().ignore(this.user.getUsername());
        } else {
            AppHelper.getActiveUser().unignore(this.user.getUsername());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getPostsIteratorId() {
        return postsIteratorId;
    }

    public void setPostsIteratorId(int postsIteratorId) {
        this.postsIteratorId = postsIteratorId;
    }

    public int getCommentsIteratorId() {
        return commentsIteratorId;
    }

    public void setCommentsIteratorId(int commentsIteratorId) {
        this.commentsIteratorId = commentsIteratorId;
    }

}
