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
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
public class Comment {

    private int post;
    private int id;
    private String text;
    private long created;
    private String owner;
    private boolean deleted;
    private List<String> upvote;
    private List<String> downvote;

    public Comment() {
    }

    public void commentFromXML(XmlHelper helper) throws Exception {
        Object object = helper.makeObject("comment");
        this.id = Integer.parseInt(helper.makeValue("id", object));
        this.text = helper.makeValue("text", object);
        this.created = Long.parseLong(helper.makeValue("created", object));
        this.owner = helper.makeValue("owner", object);
        this.deleted = Integer.parseInt(helper.makeValue("deleted", object)) == 1;
        this.upvote = helper.makeRawValue("/comment/upvote/user");
        this.downvote = helper.makeRawValue("/comment/downvote/user");
    }

    public void save() throws Exception {
        Database db = new Database();
        this.id = generateId();
        this.created = System.currentTimeMillis() / 1000L;
        this.deleted = false;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(owner);
        db.xquery("update insert <comment><id>" + this.id + "</id><text>" + this.text
                + "</text><created>" + this.created + "</created><owner>" + this.owner
                + "</owner><deleted>0</deleted><upvote><user>" + this.owner
                + "</user></upvote><downvote/></comment> into /posts/post[id=\"" + this.post + "\"]/comments");
        db.close();
    }

    public void addUpvote(String newUpvote) throws Exception {
        this.upvote.add(newUpvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update insert <user>" + newUpvote
                + "</user> into $x/comments/comment[id=\"" + this.id + "\"]/upvote");
        db.close();
    }

    public void dropUpvote(String upvote) throws Exception {
        this.upvote.remove(upvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update delete $x/comments/comment[id=\"" + this.id
                + "\"]/upvote/user[.=\"" + upvote + "\"]");
        db.close();
    }

    public void addDownvote(String newDownvote) throws Exception {
        this.downvote.add(newDownvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update insert <user>" + newDownvote
                + "</user> into $x/comments/comment[id=\"" + this.id + "\"]/downvote");
        db.close();
    }

    public void dropDownvote(String downvote) throws Exception {
        this.downvote.remove(downvote);
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update delete $x/comments/comment[id=\"" + this.id
                + "\"]/downvote/user[.=\"" + upvote + "\"]");
        db.close();
    }

    private int generateId() throws Exception {
        Database db = new Database();
        ResourceSet rs = db.xquery("max(/posts/post[id=\"" + this.post
                + "\"]/comments/comment/id)");
        db.close();

        if (rs.getSize() > 0) {
            return Integer.parseInt(rs.getResource(0).getContent().toString()) + 1;
        } else {
            return 1;
        }
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
