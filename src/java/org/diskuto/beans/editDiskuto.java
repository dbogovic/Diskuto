/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author dario
 */
@Named(value = "editDiskuto")
@ViewScoped
public class editDiskuto {

    private String name;
    private String description;
    private String rules;
    
    /**
     * Creates a new instance of editDiskuto
     */
    public editDiskuto() {
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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
    
}
