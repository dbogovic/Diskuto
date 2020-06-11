/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import org.diskuto.helpers.Database;

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

    public void send() throws Exception {
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
