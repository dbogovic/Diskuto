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

    private org.diskuto.models.Forum diskuto;
    private String headline;
    private String description;
    private String selectedCategory;
    private String errorText;

    /**
     * Creates a new instance of newPost
     */
    public NewPost() throws Exception {
        Retriever retriever = new Retriever(AppHelper.param("on"));
        this.diskuto = retriever.forum();

        if (this.diskuto == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }
    }

    public void save() throws Exception {
        errorText = "";

        if (headline == null || headline.length() == 0) {
            errorText = AppHelper.getOutput("error.headline");
        } else if (description == null || description.length() == 0) {
            errorText = AppHelper.getOutput("error.description");
        } else {
            Post post = new Post();
            post.save(headline, description, AppHelper.getActiveUser().getUsername(), diskuto.getName(), selectedCategory);
            FacesContext.getCurrentInstance().getExternalContext().redirect("post?id=" + post.getId());
        }
    }

    public Forum getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(Forum diskuto) {
        this.diskuto = diskuto;
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

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

}
