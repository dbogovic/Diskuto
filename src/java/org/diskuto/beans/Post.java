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
@Named(value = "post")
@ViewScoped
public class Post implements Serializable {

    private final org.diskuto.models.Post chosen;
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
        this.chosen = new org.diskuto.models.Post();
        this.chosen.setId(Integer.parseInt(AppHelper.param("id")));
        
        if (!this.chosen.retrieveData()) {
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
            this.chosen.addUpvote(user.getUsername());

            if (this.chosen.getDownvote().contains(this.user.getUsername())) {
                this.chosen.dropDownvote(user.getUsername());
            }
        } else {
            this.chosen.dropUpvote(user.getUsername());
        }
    }

    public void downvotePost() throws Exception {
        if (!this.chosen.getDownvote().contains(this.user.getUsername())) {
            this.chosen.addDownvote(user.getUsername());

            if (this.chosen.getUpvote().contains(this.user.getUsername())) {
                this.chosen.dropUpvote(user.getUsername());
            }
        } else {
            this.chosen.dropDownvote(user.getUsername());
        }
    }

    public void upvoteComment(Comment comment) throws Exception {
        if (!comment.getUpvote().contains(this.user.getUsername())) {
            comment.addUpvote(user.getUsername());

            if (comment.getDownvote().contains(this.user.getUsername())) {
                comment.dropDownvote(user.getUsername());
            }
        } else {
            comment.dropUpvote(user.getUsername());
        }
    }

    public void downvoteComment(Comment comment) throws Exception {
        if (!comment.getDownvote().contains(this.user.getUsername())) {
            comment.addDownvote(user.getUsername());

            if (comment.getUpvote().contains(this.user.getUsername())) {
                comment.dropUpvote(user.getUsername());
            }
        } else {
            comment.dropDownvote(user.getUsername());
        }
    }

    public String sendComment() throws Exception {
        if (!this.myComment.equals("")) {
            Comment comment = new Comment();
            comment.setPost(this.chosen.getId());
            comment.setText(this.myComment);
            comment.setOwner(this.user.getUsername());
            comment.save();
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
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);

            Comment comment = new Comment();
            comment.commentFromXML(helper);
            comment.setPost(this.chosen.getId());
            this.comments.add(comment);
        }
    }

    public String formatted(long time) {
        return AppHelper.fullDate(time);
    }
}
