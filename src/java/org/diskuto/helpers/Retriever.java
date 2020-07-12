/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.diskuto.models.Forum;
import org.diskuto.models.Message;
import org.diskuto.models.Post;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

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

        if (resource == null) {
            return null;
        } else {
            User user = new User();
            user.retrieve(new XmlHelper(resource));
            return user;
        }
    }

    public Forum forum() throws Exception {
        Resource resource = AppHelper.getResource("for $x in/forums/forum where $x/name=\"" + key + "\" return $x");

        if (resource == null) {
            return null;
        } else {
            Forum forum = new Forum();
            forum.retrieve(new XmlHelper(resource));
            return forum;
        }
    }

    public Post post() throws Exception {
        Resource resource = AppHelper.getResource("/posts/post[id=\"" + Integer.parseInt(key) + "\"]");

        if (resource == null) {
            return null;
        } else {
            Post post = new Post();
            post.retrieve(new XmlHelper(resource));
            return post;
        }
    }

    public List<Message> messages() throws Exception {
        List<Message> messages = new ArrayList<>();

        ResourceSet resourceSet = AppHelper.getResourceSet("for $x in /messages/message where ($x/recipient=\""
                + AppHelper.getActiveUser().getUsername() + "\" and $x/sender=\"" + key + "\") return $x");
        resourceSet.addAll(AppHelper.getResourceSet("for $x in /messages/message where ($x/sender=\""
                + AppHelper.getActiveUser().getUsername() + "\" and $x/recipient=\"" + key + "\") return $x"));
        ResourceIterator iterator = resourceSet.getIterator();
        while (iterator.hasMoreResources()) {
            Message message = new Message();
            message.retrieve(new XmlHelper(iterator.nextResource()));
            messages.add(message);
        }

        Collections.sort(messages, (Message z1, Message z2) -> {
            if (z1.getTime() > z2.getTime()) {
                return -1;
            }
            if (z1.getTime() < z2.getTime()) {
                return 1;
            }
            return 0;
        });

        Database db = new Database();
        db.xquery("update value /messages/message[recipient=\"" + AppHelper.getActiveUser().getUsername()
                + "\" and sender=\"" + key + "\"]/seen with \"1\"");
        db.close();

        return messages;
    }

    public List<User> searchUsers(String term) throws Exception {
        List<User> users = new ArrayList();

        ResourceIterator iterator = AppHelper.getResourceSet("for $x in /users/user where contains(lower-case($x/name), \""
                + term.toLowerCase() + "\") return $x/name").getIterator();
        while (iterator.hasMoreResources()) {
            for (String name : new XmlHelper(iterator.nextResource()).makeListValue("/name")) {
                Retriever retriever = new Retriever(name);
                User user = retriever.user();
                if (!user.isDisabled()) {
                    users.add(retriever.user());
                }
            }
        }

        return users;
    }

    public List<Forum> searchForum(String term) throws Exception {
        List<Forum> forums = new ArrayList();

        ResourceIterator iterator = AppHelper.getResourceSet("for $x in /forums/forum where contains(lower-case($x/name), \""
                + term.toLowerCase() + "\") return $x/name").getIterator();
        while (iterator.hasMoreResources()) {
            for (String name : new XmlHelper(iterator.nextResource()).makeListValue("/name")) {
                Retriever retriever = new Retriever(name);
                forums.add(retriever.forum());
            }
        }

        return forums;
    }

    public List<Post> searchPosts(String term) throws Exception {
        List<Post> posts = new ArrayList();

        ResourceIterator iterator = AppHelper.getResourceSet("for $x in /posts/post where contains(lower-case($x/headline), \""
                + term.toLowerCase() + "\") return $x/id").getIterator();
        while (iterator.hasMoreResources()) {
            for (String name : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                Retriever retriever = new Retriever(name);
                Post post = retriever.post();
                if (!post.isDeleted()) {
                    posts.add(post);
                }
            }
        }

        return posts;
    }

}
