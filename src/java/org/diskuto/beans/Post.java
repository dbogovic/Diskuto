/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.models.User;

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

    /**
     * Creates a new instance of Post
     *
     * @throws java.lang.Exception
     */
    public Post() throws Exception {
        this.user = AppHelper.getActiveUser();
        this.chosen = new org.diskuto.models.Post(Integer.parseInt(AppHelper.param("id")));

        if (!this.chosen.retrieveData()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }
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

    public void upvotePost() throws Exception {
        if (!this.chosen.getUpvote().contains(this.user.getUsername())) {
            this.chosen.addUpvote(user.getUsername());
            
            if(this.chosen.getDownvote().contains(this.user.getUsername())) {
                this.chosen.dropDownvote(user.getUsername());
            }
        } else {
            this.chosen.dropUpvote(user.getUsername());
        }
    }

    public void downvotePost() throws Exception {
        if (!this.chosen.getDownvote().contains(this.user.getUsername())) {
            this.chosen.addDownvote(user.getUsername());
            
            if(this.chosen.getUpvote().contains(this.user.getUsername())) {
                this.chosen.dropUpvote(user.getUsername());
            }
        } else {
            this.chosen.dropDownvote(user.getUsername());
        }
    }

    public void sendComment() {

    }

}
