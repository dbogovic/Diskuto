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
    private String f_created;
    private String cat;
    private boolean boss;

    /**
     * Creates a new instance of Forum
     */
    public Forum() throws Exception {
        this.chosen = new org.diskuto.models.Forum().getForum(AppHelper.param("name"));
        this.cat = AppHelper.param("cat");
    }

    public org.diskuto.models.Forum getChosen() {
        return chosen;
    }

    public String getF_created() {
        return AppHelper.date(chosen.getCreated());
    }

    public String getCat() {
        return cat;
    }

    public boolean isBoss() {
        if (AppHelper.getActiveUser() == null) {
            return false;
        } else if (this.chosen.getName().equals(AppHelper.getActiveUser().getUsername())
                || this.chosen.getModerators().contains(AppHelper.getActiveUser().getUsername())) {
            return true;
        }
        return false;
    }
}
