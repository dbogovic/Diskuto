/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.models;

import java.util.List;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;

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

    public Forum(String name, String description, List<String> categories, List<String> moderators, String rules) throws Exception {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.moderators = moderators;
        this.rules = rules;
        this.created = System.currentTimeMillis() / 1000L;
        owner = AppHelper.getActiveUser().getUsername();
    }

    public void register() throws Exception {
        Database db = new Database();
        
        StringBuilder query = new StringBuilder("update insert <forum>");
        query.append("<name>").append(name).append("</name>");
        query.append("<description>").append(description).append("</description>");
        query.append("<categories>");
        for(String category : categories) {
            query.append("<category>").append(category).append("</category>");
        }
        query.append("</categories>");
        query.append("<owner>").append(owner).append("</owner>");
        query.append("<moderators>");
        for(String moderator : moderators) {
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
    
}
