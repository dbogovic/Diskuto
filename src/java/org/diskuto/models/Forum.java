/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

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
    private String owner;
    private long created;
    private int subscribers;

    public Forum() {
    }

    public Forum(String name, String description, List<String> categories, List<String> moderators, String rules) throws Exception {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.moderators = moderators;
        this.rules = rules;
    }

    public Forum(String name, String description, List<String> categories, List<String> moderators, String rules, String owner, long created, int subscribers) {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.moderators = moderators;
        this.rules = rules;
        this.owner = owner;
        this.created = created;
        this.subscribers = subscribers;
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

    public void register() throws Exception {
        this.created = System.currentTimeMillis() / 1000L;
        owner = AppHelper.getActiveUser().getUsername();

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

    public Forum getForum(String name) throws Exception {

        System.out.println(name);
        
        Database db = new Database();
        ResourceSet info = db.xquery("for $x in/forums/forum where $x/name=\"" + name + "\" return $x");
        db.close();

        ResourceIterator iterator = info.getIterator();

        if (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            Object objekt = helper.makeObject("forum");

            String _categories = helper.makeValue("categories", objekt);
            System.out.println("k:" + _categories);
            
            /*
            return new Forum(helper.makeValue("name", objekt), helper.makeValue("description", objekt),
            /*kategorije*/ /*moderatori*//*, helper.makeValue("rules", objekt),
            helper.makeValue("owner", objekt), Long.parseLong(helper.makeValue("created", objekt)),
            Integer.parseInt(helper.makeValue("subscribers", objekt)));
            */
        }

        return null;
    }

}
