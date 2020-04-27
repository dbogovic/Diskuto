/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "message")
@ViewScoped
public class Message implements Serializable {

    private User chosen;
    private List<org.diskuto.models.Message> messages;
    private HashSet<String> chatting;
    private boolean me = false, enabledButton = false;
    private String reply;

    /**
     * Creates a new instance of Message
     */
    public Message() throws Exception {
        String _user = AppHelper.param("with");
        if (_user.equals(AppHelper.getActiveUser().getUsername()) || _user == null) {
            me = true;
            this.chosen = AppHelper.getActiveUser();
        } else {
            this.chosen = new User(_user);
            if (!this.chosen.retrieveData()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("message");
            }
        }
    }

    public User getChosen() {
        return chosen;
    }

    public boolean isMe() {
        return me;
    }

    public List<org.diskuto.models.Message> getMessages() throws Exception {

        messages = new ArrayList();

        if (!me) {
            Database db = new Database();
            ResourceSet result = db.xquery("for $x in /messages/message where ($x/recipient=\""
                    + AppHelper.getActiveUser().getUsername() + "\" and $x/sender=\"" + this.chosen.getUsername()
                    + "\") or return $x");
            db.close();

            ResourceIterator iterator = result.getIterator();
            while (iterator.hasMoreResources()) {
                Resource r = iterator.nextResource();
                String value = (String) r.getContent();
                XmlHelper helper = new XmlHelper(value);
                Object object = helper.makeObject("message");

                org.diskuto.models.Message m = new org.diskuto.models.Message(Long.parseLong(helper.makeValue("time", object)),
                        AppHelper.getActiveUser(), this.chosen, helper.makeValue("text", object),
                        Integer.parseInt(helper.makeValue("seen", object)) == 1 ? true : false);
                messages.add(m);
            }
        }

        return messages;
    }

    public HashSet<String> getChatting() throws Exception {
        chatting = new HashSet<>();

        Database db = new Database();
        fillIn(db.xquery("for $x in /messages/message where $x/recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" return $x/sender"), "sender");
        fillIn(db.xquery("for $x in /messages/message where $x/sender=\"" + AppHelper.getActiveUser().getUsername() + "\" return $x/recipient"), "recipient");
        db.close();

        return chatting;
    }

    private void fillIn(ResourceSet result, String key) throws Exception {

        ResourceIterator iterator = result.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            List<String> results = helper.makeRawValue("/" + key);

            for (String s : results) {
                chatting.add(s);
            }
        }
    }

    public String formatted(long time) {
        return AppHelper.date(time);
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String send() throws Exception {
        org.diskuto.models.Message message = new org.diskuto.models.Message(System.currentTimeMillis() / 1000L, 
                this.chosen, AppHelper.getActiveUser(), this.reply, false);
        message.send();
        
        this.reply = "";
        return "";
    }

    public void enableButton() {
        if (this.reply.length() > 0) {
            this.enabledButton = true;
        } else {
            this.enabledButton = false;
        }
    }

    public boolean isEnabledButton() {
        return enabledButton;
    }

    public void setEnabledButton(boolean enabledButton) {
        this.enabledButton = enabledButton;
    }

}
