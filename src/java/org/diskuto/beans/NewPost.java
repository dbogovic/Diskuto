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
import org.diskuto.models.Forum;
import org.diskuto.models.Post;

/**
 *
 * @author dario
 */
@Named(value = "newPost")
@ViewScoped
public class NewPost implements Serializable {

    private org.diskuto.models.Forum chosen;
    private List<String> errorText = new ArrayList();
    private String headline;
    private String description;
    private String selectedCategory;

    /**
     * Creates a new instance of newPost
     */
    public NewPost() throws Exception {
        Retriever retriever = new Retriever(AppHelper.param("on"));
        this.chosen = retriever.forum();

        if (this.chosen == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }
    }

    public Forum getChosen() {
        return chosen;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void post() throws Exception {
        errorText.clear();

        if (headline == null || headline.length() == 0) {
            errorText.add("Naslov je obavezan");
        } else if (description == null || description.length() == 0) {
            errorText.add("Opis je obavezan");
        } else {
            Post post = new Post();
            post.save(headline, description, AppHelper.getActiveUser().getUsername(), chosen.getName(), selectedCategory);
            FacesContext.getCurrentInstance().getExternalContext().redirect("post?id=" + post.getId());
        }

    }

}
