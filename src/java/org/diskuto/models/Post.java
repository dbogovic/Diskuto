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
    private String owner;
    private String diskuto;
    private String category;
    private List<String> upvote;
    private List<String> downvote;

    public Post() {
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
            this.owner = helper.makeValue("owner", object);
            this.diskuto = helper.makeValue("diskuto", object);
            this.category = helper.makeValue("category", object);
            this.upvote = helper.makeRawValue("/post/upvote/user");
            this.downvote = helper.makeRawValue("/post/downvote/user");

            return true;
        } else {
            return false;
        }
    }

    public void save() throws Exception {
        Database db = new Database();
        this.id = generateId();
        this.created = System.currentTimeMillis() / 1000L;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(this.owner);
        db.xquery("update insert <post><id>" + this.id + "</id><headline>" + this.headline
                + "</headline><description>" + this.description + "</description><created>" + created
                + "</created><owner>" + this.owner + "</owner><diskuto>" + this.diskuto
                + "</diskuto><category>" + this.category + "</category><upvote><user>" + this.owner
                + "</user></upvote><downvote/><comments/></post> into /posts");
        db.close();
    }

    public void addUpvote(String newUpvote) throws Exception {
        this.upvote.add(newUpvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update insert <user>" + newUpvote + "</user> into $x/upvote");
        db.close();
    }

    public void dropUpvote(String upvote) throws Exception {
        this.upvote.remove(upvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update delete $x/upvote/user[.=\"" + upvote + "\"]");
        db.close();
    }

    public void addDownvote(String newDownvote) throws Exception {
        this.downvote.add(newDownvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update insert <user>" + newDownvote + "</user> into $x/downvote");
        db.close();
    }

    public void dropDownvote(String downvote) throws Exception {
        this.downvote.remove(downvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.id
                + "\" return update delete $x/downvote/user[.=\"" + downvote + "\"]");
        db.close();
    }

    private int generateId() throws Exception {
        Database db = new Database();
        ResourceSet rs = db.xquery("max(/posts/post/id)");
        db.close();

        if (rs.getSize() > 0) {
            return Integer.parseInt(rs.getResource(0).getContent().toString()) + 1;
        } else {
            return 1;
        }
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
