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
import javax.faces.context.FacesContext;
import org.diskuto.helpers.AppHelper;
import org.diskuto.models.Forum;
import org.diskuto.models.Post;
import org.diskuto.models.User;

/**
 *
 * @author dario
 */
@Named(value = "search")
@RequestScoped
public class Search {
    
    private String term;
    private List<User> userResults;
    private List<org.diskuto.models.Forum> diskutoResults;
    private List<org.diskuto.models.Post> postResults;

    /**
     * Creates a new instance of Search
     */
    public Search() {
        this.term = AppHelper.param("term");

        if (this.term == null || !"".equals(this.term)) {
            userResults = new ArrayList<>();
            diskutoResults = new ArrayList<>();
            postResults = new ArrayList<>();
            
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
    
}
