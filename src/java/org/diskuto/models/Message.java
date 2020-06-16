/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;

/**
 *
 * @author dario
 */
public class Message {

    private String sender;
    private String recipient;
    private String text;
    private boolean seen;
    private long time;

    public Message() {
    }

    public void retrieve(XmlHelper helper) throws Exception {
        Object object = helper.makeObject("message");
        this.sender = helper.makeValue("sender", object);
        this.recipient = helper.makeValue("recipient", object);
        this.text = helper.makeValue("text", object);
        this.seen = Integer.parseInt(helper.makeValue("seen", object)) == 1;
        this.time = Long.parseLong(helper.makeValue("time", object));

        if (!seen && this.recipient.equals(AppHelper.getActiveUser().getUsername())) {
            this.seen = true;
        }
    }

    public void send(String sender, String recipient, String text) throws Exception {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.seen = false;
        this.time = System.currentTimeMillis() / 1000L;

        Database db = new Database();
        db.xquery("update insert <message><sender>" + this.sender + "</sender><recipient>"
                + this.recipient + "</recipient><time>" + this.time + "</time>"
                + "<text>" + this.text + "</text><seen>0</seen></message> into /messages");
        db.close();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
