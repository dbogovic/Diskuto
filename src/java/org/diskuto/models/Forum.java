/*
* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.List;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;

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
    private String owner;
    private long created;
    private int subscribers;

    public Forum() {
    }

    public void retrieve(XmlHelper helper) throws Exception {
        Object objekt = helper.makeObject("forum");
        this.description = helper.makeValue("description", objekt);
        this.categories = helper.makeListValue("/forum/categories/category");
        this.moderators = helper.makeListValue("/forum/moderators/moderator");
        this.rules = helper.makeValue("rules", objekt);
        this.owner = helper.makeValue("owner", objekt);
        this.created = Long.parseLong(helper.makeValue("created", objekt));
        this.subscribers = Integer.parseInt(helper.makeValue("subscribers", objekt));
    }

    public void save(String name, String description, List<String> categories, List<String> moderators, String rules, String owner) throws Exception {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.moderators = moderators;
        this.rules = rules;
        this.owner = owner;
        this.created = System.currentTimeMillis() / 1000L;
        this.subscribers = 0;

        Database db = new Database();
        StringBuilder query = new StringBuilder("update insert <forum>");
        query.append("<name>").append(name).append("</name>");
        query.append("<description>").append(description).append("</description>");
        query.append("<categories>");
        categories.forEach((category) -> {
            query.append("<category>").append(category).append("</category>");
        });
        query.append("</categories>");
        query.append("<owner>").append(owner).append("</owner>");
        query.append("<moderators>");
        moderators.forEach((moderator) -> {
            query.append("<moderator>").append(moderator).append("</moderator>");
        });
        query.append("</moderators>");
        query.append("<rules>").append(rules).append("</rules>");
        query.append("<created>").append(created).append("</created>");
        query.append("<subscribers>0</subscribers>");
        query.append("</forum> into /forums");
        db.xquery(query.toString());
        db.close();
    }

    public void update(String _description, String _rules, List<String> _categories, List<String> _moderators) throws Exception {
        Database db = new Database();

        if (!_description.equals(description)) {
            db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return update value $x/description with \"" + _description + "\"");
        }
        if (!_rules.equals(rules)) {
            db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return update value $x/rules with \"" + _rules + "\"");
        }
        if (!_categories.equals(categories)) {
            StringBuilder query = new StringBuilder("for $x in /forums/forum where $x/name=\"" + name + "\" return update replace $x/categories with <categories>");
            _categories.forEach((category) -> {
                query.append("<category>").append(category).append("</category>");
            });
            query.append("</categories>");
            db.xquery(query.toString());
        }
        if (!_moderators.equals(moderators)) {
            StringBuilder query2 = new StringBuilder("for $x in /forums/forum where $x/name=\"" + name + "\" return update replace $x/moderators with <moderators>");
            _moderators.forEach((moderator) -> {
                query2.append("<moderator>").append(moderator).append("</moderator>");
            });
            query2.append("</moderators>");
            db.xquery(query2.toString());
        }
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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
