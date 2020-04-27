/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.Date;
import java.util.List;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;

/**
 *
 * @author dario
 */
public class Message {

    private long time;
    private User sender;
    private User recipient;
    private String text;
    private boolean seen;

    public Message(long time, User recipient, User sender, String text, boolean seen) {
        this.time = time;
        this.recipient = recipient;
        this.sender = sender;
        this.text = text;
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void send() throws Exception {
        Database db = new Database();
        db.xquery("update insert <message><sender>" + this.sender.getUsername() + "</sender><recipient>" 
                + this.recipient.getUsername() + "</recipient><time>" + this.time + "</time>"
                + "<text>" + this.text + "</text><seen>0</seen></message> into /messages");
        db.close();
    }
}
