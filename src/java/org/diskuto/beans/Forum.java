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
import org.diskuto.helpers.Database;
import org.xmldb.api.base.ResourceSet;

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
    private boolean subscribed;

    /**
     * Creates a new instance of Forum
     */
    public Forum() throws Exception {
        this.chosen = new org.diskuto.models.Forum().getForum(AppHelper.param("name"));
        this.cat = AppHelper.param("cat");

        if (this.chosen == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        }
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
        } else if (this.chosen.getOwner().equals(AppHelper.getActiveUser().getUsername())
                || this.chosen.getModerators().contains(AppHelper.getActiveUser().getUsername())) {
            return true;
        }
        return false;
    }

    public boolean isSubscribed() throws Exception {
        Database db = new Database();
        ResourceSet subscribed = db.xquery("/users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                + "\"]/subscriptions[forum=\"" + this.chosen.getName() + "\"]");
        db.close();

        this.subscribed = subscribed.getSize() > 0;
        return this.subscribed;
    }

    public void subscribe() throws Exception {

        Database db = new Database();

        if (!this.subscribed) {
            this.chosen.setSubscribers(this.chosen.getSubscribers() + 1);
            db.xquery("for $x in /users/user where $x/name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\" return update insert <forum>" + this.chosen.getName() + "</forum> into $x/subscriptions");
            this.subscribed = true;
        } else {
            this.chosen.setSubscribers(this.chosen.getSubscribers() - 1);
            db.xquery("for $x in /users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\"]/subscriptions[forum=\"" + this.chosen.getName() + "\"] return update delete $x/forum");
            this.subscribed = false;
        }

        db.xquery("for $x in /forums/forum where $x/name=\"" + this.chosen.getName()
                + "\" return update value $x/subscribers with \"" + this.chosen.getSubscribers() + "\"");

        db.close();
    }
}
