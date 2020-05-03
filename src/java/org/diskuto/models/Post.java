/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.ArrayList;
import java.util.List;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

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
    private List<String> upvote;
    private List<String> downvote;

    public Post(int id) {
        this.id = id;
    }

    public Post(String headline, String description, User owner, Forum diskuto, String category) throws Exception {
        this.id = generateId();
        this.headline = headline;
        this.description = description;
        this.created = System.currentTimeMillis() / 1000L;
        this.owner = owner;
        this.diskuto = diskuto;
        this.category = category;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(owner.getUsername());
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
    
    private int generateId() throws Exception {
        Database db = new Database();
        ResourceSet rs = db.xquery("max(/posts/post/id)");
        db.close();

        return Integer.parseInt(rs.getResource(0).getContent().toString()) + 1;
    }

    public void save() throws Exception {
        Database db = new Database();
        db.xquery("update insert <post><id>" + this.id + "</id><headline>" + this.headline
                + "</headline><description>" + this.description + "</description><created>" + created
                + "</created><owner>" + this.owner.getUsername() + "</owner><diskuto>" + this.diskuto.getName()
                + "</diskuto><category>" + this.category + "</category><upvote><user>" + this.owner.getUsername()
                + "</user></upvote><downvote/></post> into /posts");
        db.close();
    }

    public boolean retrieveData() throws Exception {
        Database db = new Database();
        ResourceSet result = db.xquery("/posts/post[id=\"" + this.id + "\"]");
        db.close();

        if (result.getSize() != 0) {
            ResourceIterator iterator = result.getIterator();
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object object = helper.makeObject("post");

            this.created = Long.parseLong(helper.makeValue("created", object));
            this.headline = helper.makeValue("headline", object);
            this.description = helper.makeValue("description", object);
            this.owner = new User(helper.makeValue("owner", object));
            this.owner.retrieveData();
            this.diskuto = new Forum().getForum(helper.makeValue("diskuto", object));
            this.category = helper.makeValue("category", object);
            this.upvote = helper.makeRawValue("/post/upvote/user");
            this.downvote = helper.makeRawValue("/post/downvote/user");

            return true;
        } else {
            return false;
        }
    }

}
