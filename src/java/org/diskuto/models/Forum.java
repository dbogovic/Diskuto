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
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
public class Forum {

    private String name;
    private String description;
    private List<String> categories;
    private List<String> moderators;
    private String rules;
    private User owner;
    private long created;
    private int subscribers;

    public Forum() {
    }

    public boolean retrieveData() throws Exception {

        Database db = new Database();
        ResourceSet info = db.xquery("for $x in/forums/forum where $x/name=\"" + this.name + "\" return $x");
        db.close();

        ResourceIterator iterator = info.getIterator();

        if (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("forum");

            this.description = helper.makeValue("description", objekt);
            this.categories = helper.makeRawValue("/forum/categories/category");
            this.moderators = new ArrayList();
            for (String s : helper.makeRawValue("/forum/moderators/moderator")) {
                moderators.add(s);
            }
            this.rules = helper.makeValue("rules", objekt);
            this.owner = new User();
            this.owner.setUsername(helper.makeValue("owner", objekt));
            this.created = Long.parseLong(helper.makeValue("created", objekt));
            this.subscribers = Integer.parseInt(helper.makeValue("subscribers", objekt));

            return true;
        }

        return false;
    }

    public void register() throws Exception {
        this.created = System.currentTimeMillis() / 1000L;
        owner = AppHelper.getActiveUser();

        Database db = new Database();

        StringBuilder query = new StringBuilder("update insert <forum>");
        query.append("<name>").append(name).append("</name>");
        query.append("<description>").append(description).append("</description>");
        query.append("<categories>");
        for (String category : categories) {
            query.append("<category>").append(category).append("</category>");
        }
        query.append("</categories>");
        query.append("<owner>").append(owner).append("</owner>");
        query.append("<moderators>");
        for (String moderator : moderators) {
            query.append("<moderator>").append(moderator).append("</moderator>");
        }
        query.append("</moderators>");
        query.append("<rules>").append(rules).append("</rules>");
        query.append("<created>").append(created).append("</created>");
        query.append("<subscribers>0</subscribers>");
        query.append("</forum> into /forums");

        db.xquery(query.toString());
        db.close();
    }

    public void update() throws Exception {

        Database db = new Database();
        db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return update value $x/description with \"" + description + "\"");
        db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return update value $x/rules with \"" + rules + "\"");

        StringBuilder query = new StringBuilder("for $x in /forums/forum where $x/name=\"" + name + "\" return update replace $x/categories with <categories>");
        for (String category : categories) {
            query.append("<category>").append(category).append("</category>");
        }
        query.append("</categories>");
        db.xquery(query.toString());

        StringBuilder query2 = new StringBuilder("for $x in /forums/forum where $x/name=\"" + name + "\" return update replace $x/moderators with <moderators>");
        for (String moderator : moderators) {
            query2.append("<moderator>").append(moderator).append("</moderator>");
        }
        query2.append("</moderators>");
        db.xquery(query2.toString());

        db.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getModerators() {
        return moderators;
    }

    public void setModerators(List<String> moderators) {
        this.moderators = moderators;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

}
