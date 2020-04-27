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
import org.diskuto.models.User;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "profile")
@ViewScoped
public class Profile implements Serializable {

    private User chosen;
    private boolean me;
    private String p_created;
    private boolean ignored;

    /**
     * Creates a new instance of Profile
     */
    public Profile() throws Exception {
        String _user = AppHelper.param("name");
        if (_user.equals(AppHelper.getActiveUser().getUsername())) {
            me = true;
            this.chosen = AppHelper.getActiveUser();
        } else {
            me = false;
            this.chosen = new User(_user);
            if (!this.chosen.retrieveData()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
            }
        }
    }

    public User getChosen() {
        return chosen;
    }

    public boolean isMe() {
        return me;
    }

    public String getP_created() {
        return AppHelper.date(chosen.getCreated());
    }

    public boolean isIgnored() throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("/users/user[name=\""+AppHelper.getActiveUser().getUsername()
                + "\"]/ignore/user[\""+this.chosen.getUsername()+"\"]");
        db.close();

        this.ignored = result.getSize() > 0;
        return this.ignored;
    }
    public void ignore() throws Exception {

        Database db = new Database();

        if (!this.ignored) {
            db.xquery("for $x in /users/user where $x/name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\" return update insert <user>" + this.chosen.getUsername() + "</user> into $x/ignore");
            this.ignored = true;
        } else {
            db.xquery("for $x in /users/user[name=\"" + AppHelper.getActiveUser().getUsername()
                    + "\"]/ignore[user=\"" + this.chosen.getUsername() + "\"] return update delete $x/user");
            this.ignored = false;
        }

        db.close();
    }
    

}
