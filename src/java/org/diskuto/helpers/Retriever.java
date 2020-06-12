/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import org.diskuto.models.Forum;
import org.diskuto.models.Post;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;

/**
 *
 * @author dario
 */
public class Retriever {
    
    private final String key;

    public Retriever(String key) {
        this.key = key;
    }
    
    public User user() throws Exception {
        Resource resource = AppHelper.getResource("for $x in /users/user where $x/name=\"" + key + "\" return $x");
        
        if(resource == null) return null;
        else {
            User user = new User();
            user.retrieve(new XmlHelper(resource));
            return user;
        }
    }
    
    public Forum forum() throws Exception {
        Resource resource = AppHelper.getResource("for $x in/forums/forum where $x/name=\"" + key + "\" return $x");
        
        if(resource == null) return null;
        else {
            Forum forum = new Forum();
            forum.retrieve(new XmlHelper(resource));
            return forum;
        }
    }
    
    public Post post() throws Exception {
        Resource resource = AppHelper.getResource("/posts/post[id=\"" + Integer.parseInt(key)+ "\"]");
        
        if(resource == null) return null;
        else {
            Post post = new Post();
            post.retrieve(new XmlHelper(resource));
            return post;
        }
    }
    
}
