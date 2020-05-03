/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.ArrayList;
import java.util.List;
import org.diskuto.helpers.Database;

/**
 *
 * @author dario
 */
public class Post {

    private int id;
    private String headline;
    private String description;
    private long created;
    private User owner;
    private Forum diskuto;
    private String category;
    private List<User> upvote;
    private List<User> downvote;

    public Post(int id) {
        this.id = id;
    }

    public Post(String headline, String description, User owner, Forum diskuto, String category) {
        this.id = generateId();
        this.headline = headline;
        this.description = description;
        this.created = System.currentTimeMillis() / 1000L;
        this.owner = owner;
        this.diskuto = diskuto;
        this.category = category;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(owner);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Forum getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(Forum diskuto) {
        this.diskuto = diskuto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<User> getUpvote() {
        return upvote;
    }

    public void setUpvote(List<User> upvote) {
        this.upvote = upvote;
    }

    public List<User> getDownvote() {
        return downvote;
    }

    public void setDownvote(List<User> downvote) {
        this.downvote = downvote;
    }

    private int generateId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save() throws Exception {
        Database db = new Database();
        db.xquery("");
        db.close();
    }

    public void retrieveData() {

    }

}
