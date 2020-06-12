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
import org.diskuto.helpers.Retriever;
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
@Named(value = "post")
@ViewScoped
public class Post implements Serializable {

    private org.diskuto.models.Post chosen;
    private String p_created;
    private String myComment;
    private User user;
    private boolean enabledButton;
    private List<Comment> comments;

    /**
     * Creates a new instance of Post
     *
     * @throws java.lang.Exception
     */
    public Post() throws Exception {
        this.user = AppHelper.getActiveUser();
        Retriever retriever = new Retriever(AppHelper.param("id"));
        this.chosen = retriever.post();
        
        if (this.chosen == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }

        retrieveComments();
    }

    public org.diskuto.models.Post getChosen() {
        return chosen;
    }

    public String getP_created() {
        return AppHelper.fullDate(this.chosen.getCreated());
    }

    public String getMyComment() {
        return myComment;
    }

    public void setMyComment(String myComment) {
        this.myComment = myComment;
    }

    public boolean isEnabledButton() {
        return enabledButton;
    }

    public void setEnabledButton(boolean enabledButton) {
        this.enabledButton = enabledButton;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void upvotePost() throws Exception {
        if (!this.chosen.getUpvote().contains(this.user.getUsername())) {
            this.chosen.addVote("upvote", user.getUsername());

            if (this.chosen.getDownvote().contains(this.user.getUsername())) {
                this.chosen.dropVote("downvote", user.getUsername());
            }
        } else {
            this.chosen.dropVote("upvote", user.getUsername());
        }
    }

    public void downvotePost() throws Exception {
        if (!this.chosen.getDownvote().contains(this.user.getUsername())) {
            this.chosen.addVote("downvote", user.getUsername());

            if (this.chosen.getUpvote().contains(this.user.getUsername())) {
                this.chosen.dropVote("upvote", user.getUsername());
            }
        } else {
            this.chosen.dropVote("downvote", user.getUsername());
        }
    }

    public void upvoteComment(Comment comment) throws Exception {
        if (!comment.getUpvote().contains(this.user.getUsername())) {
            comment.addVote("upvote", user.getUsername());

            if (comment.getDownvote().contains(this.user.getUsername())) {
                comment.dropVote("downvote", user.getUsername());
            }
        } else {
            comment.dropVote("upvote", user.getUsername());
        }
    }

    public void downvoteComment(Comment comment) throws Exception {
        if (!comment.getDownvote().contains(this.user.getUsername())) {
            comment.addVote("downvote", user.getUsername());

            if (comment.getUpvote().contains(this.user.getUsername())) {
                comment.dropVote("upvote", user.getUsername());
            }
        } else {
            comment.dropVote("downvote", user.getUsername());
        }
    }

    public String sendComment() throws Exception {
        if (!this.myComment.equals("")) {
            Comment comment = new Comment();
            comment.save(this.chosen.getId(), this.myComment, this.user.getUsername());
            this.comments.add(comment);
            this.myComment = "";
        }
        return "";
    }

    public void enableButton() {
        this.enabledButton = !"".equals(this.myComment);
    }

    private void retrieveComments() throws Exception {
        this.comments = new ArrayList<>();

        Database db = new Database();
        ResourceSet result = db.xquery("/posts/post[id=\"" + this.chosen.getId() + "\"]/comments/comment");
        ResourceIterator iterator = result.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            XmlHelper helper = new XmlHelper(r);

            Comment comment = new Comment();
            comment.retrieve(helper);
            comment.setPost(this.chosen.getId());
            this.comments.add(comment);
        }
    }

    public String formatted(long time) {
        return AppHelper.fullDate(time);
    }
}
