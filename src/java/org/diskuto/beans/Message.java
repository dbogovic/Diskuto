/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
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

    private boolean me = false;
    private String chosen;
    private List<org.diskuto.models.Message> messages;
    private HashSet<String> chatting;
    private boolean enabledButton = false;
    private String reply;

    /**
     * Creates a new instance of Message
     */
    public Message() throws Exception {
        String user = AppHelper.param("with");
        if (user == null || user.equals(AppHelper.getActiveUser().getUsername())) {
            me = true;
        } else {
            if (AppHelper.usernameExists(user)) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("message");
            } else {
                Retriever retrieve = new Retriever(user);
                this.messages = retrieve.messages();
            }
        }

        chatting = new HashSet<>();
        fillIn(AppHelper.getResourceSet("for $x in /messages/message where $x/recipient=\"" + AppHelper.getActiveUser().getUsername() + "\" return $x/sender"), "sender");
        fillIn(AppHelper.getResourceSet("for $x in /messages/message where $x/sender=\"" + AppHelper.getActiveUser().getUsername() + "\" return $x/recipient"), "recipient");
    }

    public void send() throws Exception {
        org.diskuto.models.Message message = new org.diskuto.models.Message();
        message.send(this.chosen, AppHelper.getActiveUser().getUsername(), this.reply);
        FacesContext.getCurrentInstance().getExternalContext().redirect("message?with=" + this.chosen);
    }

    private void fillIn(ResourceSet result, String key) throws Exception {
        ResourceIterator iterator = result.getIterator();
        while (iterator.hasMoreResources()) {
            Resource resource = iterator.nextResource();
            XmlHelper helper = new XmlHelper(resource);
            List<String> results = helper.makeListValue("/" + key);

            results.forEach((user) -> {
                chatting.add(user);
            });
        }
    }

    public void enableButton() {
        this.enabledButton = !"".equals(this.reply);
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public List<org.diskuto.models.Message> getMessages() {
        return messages;
    }

    public void setMessages(List<org.diskuto.models.Message> messages) {
        this.messages = messages;
    }

    public HashSet<String> getChatting() {
        return chatting;
    }

    public void setChatting(HashSet<String> chatting) {
        this.chatting = chatting;
    }

    public boolean isEnabledButton() {
        return enabledButton;
    }

    public void setEnabledButton(boolean enabledButton) {
        this.enabledButton = enabledButton;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
