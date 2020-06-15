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

    public void retrieve(XmlHelper helper) throws Exception {
        Object object = helper.makeObject("comment");
        this.id = Integer.parseInt(helper.makeValue("id", object));
        this.text = helper.makeValue("text", object);
        this.created = Long.parseLong(helper.makeValue("created", object));
        this.owner = helper.makeValue("owner", object);
        this.deleted = Integer.parseInt(helper.makeValue("deleted", object)) == 1;
        this.upvote = helper.makeListValue("/comment/upvote/user");
        this.downvote = helper.makeListValue("/comment/downvote/user");
    }

    public void save(int post, String text, String owner) throws Exception {
        this.post = post;
        this.id = AppHelper.generateId("max(/posts/post[id=\"" + this.post + "\"]/comments/comment/id)");
        this.text = text;
        this.created = System.currentTimeMillis() / 1000L;
        this.owner = owner;
        this.deleted = false;
        upvote = new ArrayList();
        downvote = new ArrayList();
        upvote.add(owner);

        Database db = new Database();
        db.xquery("update insert <comment><id>" + this.id + "</id><text>" + this.text
                + "</text><created>" + this.created + "</created><owner>" + this.owner
                + "</owner><deleted>0</deleted><upvote><user>" + this.owner
                + "</user></upvote><downvote/></comment> into /posts/post[id=\"" + this.post + "\"]/comments");
        db.close();
    }

    public void addVote(String type, String vote) throws Exception {
        if (type.equals("upvote")) {
            this.upvote.add(vote);
        } else if (type.equals("downvote")) {
            this.downvote.add(vote);
        }
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update insert <user>" + vote
                + "</user> into $x/comments/comment[id=\"" + this.id + "\"]/" + type);
        db.close();
    }

    public void dropVote(String type, String vote) throws Exception {
        if (type.equals("upvote")) {
            this.upvote.remove(vote);
        } else if (type.equals("downvote")) {
            this.downvote.remove(vote);
        }
        Database db = new Database();
        db.xquery("for $x in /posts/post where $x/id=\"" + this.post
                + "\" return update delete $x/comments/comment[id=\"" + this.id
                + "\"]/" + type + "/user[.=\"" + vote + "\"]");
        db.close();
    }

    public void post() throws Exception {
        this.post = Integer.parseInt(new XmlHelper(AppHelper.getResource("for $x in /posts/post where $x/comments/comment/id=\""
                + this.id + "\" return data($x/id)")).rawValue());
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
