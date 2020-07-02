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
    private final String user;
    private List<Comment> comments = new ArrayList<>();
    private String myComment;

    /**
     * Creates a new instance of Post
     *
     * @throws java.lang.Exception
     */
    public Post() throws Exception {
        user = AppHelper.getActiveUser().getUsername();
        Retriever retriever = new Retriever(AppHelper.param("id"));
        this.thing = retriever.post();

        if (this.thing == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }

        ResourceIterator iterator = AppHelper.getResourceSet("/posts/post[id=\"" + this.thing.getId() + "\"]/comments/comment").getIterator();
        while (iterator.hasMoreResources()) {
            Comment comment = new Comment();
            comment.retrieve(new XmlHelper(iterator.nextResource()));
            comment.setPost(this.thing);
            if (!AppHelper.getActiveUser().getIgnored().contains(comment.getOwner())) {
                this.comments.add(comment);
            }
        }
    }

    public void sendComment() throws Exception {
        if (!this.myComment.equals("")) {
            Comment comment = new Comment();
            comment.save(this.thing, this.myComment, this.user);
            this.comments.add(comment);
            this.myComment = "";
        }
    }

    public void upvotePost() throws Exception {
        if (!this.thing.getUpvote().contains(this.user)) {
            this.thing.addVote("upvote", this.user);

            if (this.thing.getDownvote().contains(this.user)) {
                this.thing.dropVote("downvote", this.user);
            }
        } else {
            this.thing.dropVote("upvote", this.user);
        }
    }

    public void downvotePost() throws Exception {
        if (!this.thing.getDownvote().contains(this.user)) {
            this.thing.addVote("downvote", this.user);

            if (this.thing.getUpvote().contains(this.user)) {
                this.thing.dropVote("upvote", this.user);
            }
        } else {
            this.thing.dropVote("downvote", this.user);
        }
    }

    public void upvoteComment(Comment comment) throws Exception {
        if (!comment.getUpvote().contains(this.user)) {
            comment.addVote("upvote", this.user);

            if (comment.getDownvote().contains(this.user)) {
                comment.dropVote("downvote", this.user);
            }
        } else {
            comment.dropVote("upvote", this.user);
        }
    }

    public void downvoteComment(Comment comment) throws Exception {
        if (!comment.getDownvote().contains(this.user)) {
            comment.addVote("downvote", this.user);

            if (comment.getUpvote().contains(this.user)) {
                comment.dropVote("upvote", this.user);
            }
        } else {
            comment.dropVote("downvote", this.user);
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
