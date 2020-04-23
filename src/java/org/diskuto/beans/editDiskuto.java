/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Database;
import org.diskuto.models.Forum;
import org.xmldb.api.base.ResourceSet;

/**
 *
 * @author dario
 */
@Named(value = "editDiskuto")
@ViewScoped
public class EditDiskuto implements Serializable {

    private String name;
    private String description;
    private String rules;
    private String nameCategory;
    private List<String> categories = new ArrayList();
    private String nameModerator;
    private List<String> moderators = new ArrayList();
    private List<String> errorText = new ArrayList();
    private Forum existing;

    /**
     * Creates a new instance of editDiskuto
     */
    public EditDiskuto() throws Exception {
        existing = new org.diskuto.models.Forum().getForum(AppHelper.param("name"));

        if (existing != null) {
            this.name = existing.getName();
            this.description = existing.getDescription();
            this.rules = existing.getRules();
            this.categories = existing.getCategories();
            this.moderators = existing.getModerators();
        }
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

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getNameModerator() {
        return nameModerator;
    }

    public void setNameModerator(String nameModerator) {
        this.nameModerator = nameModerator;
    }

    public List<String> getModerators() {
        return moderators;
    }

    public void setModerators(List<String> moderators) {
        this.moderators = moderators;
    }

    public List<String> getErrorText() {
        return errorText;
    }

    public void setErrorText(List<String> errorText) {
        this.errorText = errorText;
    }

    public Forum getExisting() {
        return existing;
    }

    public void addCategory() {
        errorText.clear();

        if (nameCategory == null || nameCategory.length() == 0) {
            errorText.add("Niste unijeli naziv kategorije");
        } else if (categories.contains(nameCategory)) {
            errorText.add("Već ste unijeli tu kategoriju");
        } else {
            Matcher matcher = Pattern.compile("^[\\w\\d\\s]+$", Pattern.CASE_INSENSITIVE).matcher(name);
            if (!matcher.find()) {
                errorText.add("Naziv kategorije smije imati samo brojeve i slova");
            } else {
                categories.add(nameCategory);
            }
        }
        
        nameCategory = "";
    }

    public void dropCategory(Object category) {
        errorText.clear();
        categories.remove(category);
    }

    public void addModerator() throws Exception {
        errorText.clear();

        Database db = new Database();
        ResourceSet result = db.xquery("for $x in /users/user where $x/name=\"" + nameModerator + "\" return $x");
        db.close();

        if (AppHelper.getActiveUser().getUsername().equals(nameModerator)) {
            errorText.add("Vi ste već vlasnik Diskuta");
        } else if (result.getSize() == 0) {
            errorText.add("Korisnik pod tim korisničkim imenom ne postoji");
        } else if (moderators.contains(nameModerator)) {
            errorText.add("Već ste unijeli tog moderatora");
        } else {
            moderators.add(nameModerator);
        }
        
        nameModerator = "";
    }

    public void dropModerator(Object moderator) {
        errorText.clear();
        moderators.remove(moderator);
    }

    public void save() throws Exception {
        errorText.clear();
        if (check()) {

            if (existing == null) {
                Database db = new Database();
                ResourceSet resultDiskuto = db.xquery("for $x in /forums/forum where $x/name=\"" + name + "\" return $x");
                db.close();

                if (resultDiskuto.getSize() > 0) {
                    errorText.add("Diskuto pod tim nazivom već postoji");
                } else {
                    Forum forum = new Forum(name, description, categories, moderators, rules);
                    forum.register();
                }
            } else {
                existing.setDescription(description);
                existing.setRules(rules);
                existing.setCategories(categories);
                existing.setModerators(moderators);
                
                existing.update();
            }

            FacesContext.getCurrentInstance().getExternalContext().redirect("myDiskuto");
        }
    }

    private boolean check() throws Exception {

        if (name == null || description == null || rules == null || name.length() == 0
                || description.length() == 0 || rules.length() == 0 || categories.isEmpty()) {
            errorText.add("Niste unijeli sve podatke");
            return false;
        } else {

            Matcher matcher = Pattern.compile("^[\\w\\d]+$", Pattern.CASE_INSENSITIVE).matcher(name);
            if (!matcher.find()) {
                errorText.add("Naziv smije imati samo brojeve i slova bez razmaka");
                return false;
            }

        }

        return true;
    }
}
