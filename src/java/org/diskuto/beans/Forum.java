/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;

/**
 *
 * @author dario
 */
@Named(value = "forum")
@ViewScoped
public class Forum implements Serializable {

    private org.diskuto.models.Forum chosen;
    
    /**
     * Creates a new instance of Forum
     */
    public Forum() throws Exception {
        this.chosen = new org.diskuto.models.Forum().getForum(AppHelper.param("name"));
    }

    public org.diskuto.models.Forum getChosen() {
        return chosen;
    }

    public void setChosen(org.diskuto.models.Forum chosen) {
        this.chosen = chosen;
    }
    
}
