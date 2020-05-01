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
import org.diskuto.models.Forum;

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
    private List<Forum> listDiskuto;
    private List<String> listCategory;
    
    /**
     * Creates a new instance of newPost
     */
    public NewPost() throws Exception {
        this.chosen = new org.diskuto.models.Forum().getForum(AppHelper.param("on"));

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

    public List<Forum> getListDiskuto() {
        return listDiskuto;
    }

    public void setListDiskuto(List<Forum> listDiskuto) {
        this.listDiskuto = listDiskuto;
    }

    public List<String> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<String> listCategory) {
        this.listCategory = listCategory;
    }
    
    public void post() throws Exception {
        
    }
    
}
