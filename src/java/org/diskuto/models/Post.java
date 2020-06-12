/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.ArrayList;
import java.util.List;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;

/**
 *
 * @author dario
 */
public class Post {

    private int id;
    private String headline;
    private String description;
    private long created;
    private String owner;
    private String diskuto;
    private String category;
    private List<String> upvote;
    private List<String> downvote;

    public Post() {
    }

    public void retrieve(XmlHelper helper) throws Exception {
        Object object = helper.makeObject("post");
        this.id = Integer.parseInt(helper.makeValue("id", object));
        this.created = Long.parseLong(helper.makeValue("created", object));
        this.headline = helper.makeValue("headline", object);
        this.description = helper.makeValue("description", object);
        this.owner = helper.makeValue("owner", object);
        this.diskuto = helper.makeValue("diskuto", object);
        this.category = helper.makeValue("category", object);
        this.upvote = helper.makeListValue("/post/upvote/user");
        this.downvote = helper.makeListValue("/post/downvote/user");
    }

    public void save(String headline, String description, String owner, String diskuto, String category) throws Exception {
        this.id = AppHelper.generateId("max(/posts/post/id)");
        this.headline = headline;
        this.description = description;
        this.created = System.currentTimeMillis() / 1000L;
        this.owner = owner;
        this.diskuto = diskuto;
        this.category = category;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(this.owner);

        Database db = new Database();
        db.xquery("update insert <post><id>" + this.id + "</id><headline>" + this.headline
                + "</headline><description>" + this.description + "</description><created>" + created
                + "</created><owner>" + this.owner + "</owner><diskuto>" + this.diskuto
                + "</diskuto><category>" + this.category + "</category><upvote><user>" + this.owner
                + "</user></upvote><downvote/><comments/></post> into /posts");
        db.close();
    }

    public void addVote(String type, String vote) throws Exception {
        if (type.equals("upvote")) {
            this.upvote.add(vote);
        } else if (type.equals("downvote")) {
            this.downvote.add(vote);
        }

        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update insert <user>" + vote + "</user> into $x/" + type);
        db.close();
    }

    public void dropVote(String type, String vote) throws Exception {
        if (type.equals("upvote")) {
            this.upvote.remove(vote);
        } else if (type.equals("downvote")) {
            this.downvote.remove(vote);
        }

        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update delete $x/" + type + "/user[.=\"" + vote + "\"]");
        db.close();
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(String diskuto) {
        this.diskuto = diskuto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getUpvote() {
        return upvote;
    }

    public void setUpvote(List<String> upvote) {
        this.upvote = upvote;
    }

    public List<String> getDownvote() {
        return downvote;
    }

    public void setDownvote(List<String> downvote) {
        this.downvote = downvote;
    }

}
