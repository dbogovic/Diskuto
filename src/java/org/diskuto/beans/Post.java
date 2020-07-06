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
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "post")
@ViewScoped
public class Post implements Serializable {

    private org.diskuto.models.Post thing;
    private List<Comment> comments = new ArrayList<>();
    private String myComment;

    /**
     * Creates a new instance of Post
     *
     * @throws java.lang.Exception
     */
    public Post() throws Exception {
        Retriever retriever = new Retriever(AppHelper.param("id"));
        this.thing = retriever.post();

        if (this.thing == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        } else {
            ResourceIterator iterator = AppHelper.getResourceSet("/posts/post[id=\"" + this.thing.getId() + "\"]/comments/comment").getIterator();
            while (iterator.hasMoreResources()) {
                Comment comment = new Comment();
                comment.retrieve(new XmlHelper(iterator.nextResource()));
                comment.setPost(this.thing);
                if (AppHelper.getActiveUser() == null || !AppHelper.getActiveUser().getIgnored().contains(comment.getOwner())) {
                    this.comments.add(comment);
                }
            }
        }
    }

    public void sendComment() throws Exception {
        AppHelper.checkLogged();
        if (!this.myComment.equals("")) {
            Comment comment = new Comment();
            comment.save(this.thing, this.myComment, AppHelper.getActiveUser().getUsername());
            this.comments.add(comment);
            this.myComment = "";
        }
    }

    public void upvotePost() throws Exception {
        AppHelper.checkLogged();
        if (!this.thing.getUpvote().contains(AppHelper.getActiveUser().getUsername())) {
            this.thing.addVote("upvote", AppHelper.getActiveUser().getUsername());

            if (this.thing.getDownvote().contains(AppHelper.getActiveUser().getUsername())) {
                this.thing.dropVote("downvote", AppHelper.getActiveUser().getUsername());
            }
        } else {
            this.thing.dropVote("upvote", AppHelper.getActiveUser().getUsername());
        }
    }

    public void downvotePost() throws Exception {
        AppHelper.checkLogged();
        if (!this.thing.getDownvote().contains(AppHelper.getActiveUser().getUsername())) {
            this.thing.addVote("downvote", AppHelper.getActiveUser().getUsername());

            if (this.thing.getUpvote().contains(AppHelper.getActiveUser().getUsername())) {
                this.thing.dropVote("upvote", AppHelper.getActiveUser().getUsername());
            }
        } else {
            this.thing.dropVote("downvote", AppHelper.getActiveUser().getUsername());
        }
    }

    public void upvoteComment(Comment comment) throws Exception {
        AppHelper.checkLogged();
        if (!comment.getUpvote().contains(AppHelper.getActiveUser().getUsername())) {
            comment.addVote("upvote", AppHelper.getActiveUser().getUsername());

            if (comment.getDownvote().contains(AppHelper.getActiveUser().getUsername())) {
                comment.dropVote("downvote", AppHelper.getActiveUser().getUsername());
            }
        } else {
            comment.dropVote("upvote", AppHelper.getActiveUser().getUsername());
        }
    }

    public void downvoteComment(Comment comment) throws Exception {
        AppHelper.checkLogged();
        if (!comment.getDownvote().contains(AppHelper.getActiveUser().getUsername())) {
            comment.addVote("downvote", AppHelper.getActiveUser().getUsername());

            if (comment.getUpvote().contains(AppHelper.getActiveUser().getUsername())) {
                comment.dropVote("upvote", AppHelper.getActiveUser().getUsername());
            }
        } else {
            comment.dropVote("downvote", AppHelper.getActiveUser().getUsername());
        }
    }

    public org.diskuto.models.Post getThing() {
        return thing;
    }

    public void setThing(org.diskuto.models.Post thing) {
        this.thing = thing;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getMyComment() {
        return myComment;
    }

    public void setMyComment(String myComment) {
        this.myComment = myComment;
    }

}
