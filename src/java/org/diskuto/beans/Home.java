/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.diskuto.helpers.Database;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.listeners.Listener;
import org.diskuto.models.Post;
import org.diskuto.models.User;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "home")
@RequestScoped
public class Home {

    private String username;
    private String password;
    private List<String> errorText = new ArrayList();
    private User user;
    private boolean login;
    private List<org.diskuto.models.Post> items = new ArrayList();
    
    /**
     * Creates a new instance of Index
     */
    public Home() throws Exception {
        Database db = new Database();
        ResourceSet query = db.xquery("/posts/post/id");
        ResourceIterator iterator = query.getIterator();
        while (iterator.hasMoreResources()) {
            Resource r = iterator.nextResource();
            String value = (String) r.getContent();
            XmlHelper helper = new XmlHelper(value);
            List<String> results = helper.makeRawValue("/id");
            for (String s : results) {
                org.diskuto.models.Post post = new org.diskuto.models.Post();
                post.setId(Integer.parseInt(s));
                post.retrieveData();
                items.add(post);
            }
        }
        db.close();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }

    public boolean isLogin() {
        return Listener.getFromSession("user") != null;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public User getUser() {
        return (User) Listener.getFromSession("user");
    }

    public List<Post> getItems(){
        return items;
    }
    
    public void doLogin() throws Exception {
        this.errorText.clear();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(user.login()) {
            user.retrieveData();
            Listener.addToSession("user", user);
            if(user.getConfirmCode() != -1) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration");
            }
            else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("home");
            }
        }
        else {
            this.errorText.add("Neuspjela prijava");
        }
    }
    
    public void logOut() throws Exception {
        Listener.deleteFromSession("user");
        FacesContext.getCurrentInstance().getExternalContext().redirect("home");
    }
}
