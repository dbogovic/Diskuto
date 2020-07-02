/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.models.Forum;

/**
 *
 * @author dario
 */
@Named(value = "editDiskuto")
@ViewScoped
public class EditDiskuto implements Serializable {

    private Forum diskuto;
    private String name;
    private String description;
    private String rules;
    private List<String> categories = new ArrayList();
    private List<String> moderators = new ArrayList();
    private String nameCategory;
    private String nameModerator;
    private String errorText;

    /**
     * Creates a new instance of editDiskuto
     */
    public EditDiskuto() throws Exception {
        Retriever retriever = new Retriever(AppHelper.param("name"));
        this.diskuto = retriever.forum();

        if (diskuto != null) {
            this.name = diskuto.getName();
            this.description = diskuto.getDescription();
            this.rules = diskuto.getRules();
            this.categories = diskuto.getCategories();
            this.moderators = diskuto.getModerators();
        }
    }

    public void addCategory() {
        this.errorText = "";
        if (nameCategory == null || nameCategory.length() == 0) {
            this.errorText = AppHelper.getOutput("error.noCategory");
        } else if (categories.contains(nameCategory)) {
            this.errorText = AppHelper.getOutput("error.alreadyCategory");
        } else {
            if (!AppHelper.regex("^[\\w\\d\\s]+$", nameCategory)) {
                this.errorText = AppHelper.getOutput("error.categorySyntax");
            } else {
                categories.add(nameCategory);
            }
        }
        nameCategory = "";
    }

    public void dropCategory(String category) {
        this.errorText = "";
        categories.remove(category);
    }

    public void addModerator() throws Exception {
        this.errorText = "";
        if (AppHelper.getActiveUser().getUsername().equals(nameModerator)) {
            this.errorText = AppHelper.getOutput("error.alreadyOwner");
        } else if (!AppHelper.usernameExists(nameModerator)) {
            this.errorText = AppHelper.getOutput("error.username3");
        } else if (moderators.contains(nameModerator)) {
            this.errorText = AppHelper.getOutput("error.alreadyModerator");
        } else {
            moderators.add(nameModerator);
        }
        nameModerator = "";
    }

    public void dropModerator(String moderator) {
        this.errorText = "";
        moderators.remove(moderator);
    }

    private boolean check() throws Exception {
        if (name == null || description == null || rules == null || name.length() == 0
                || description.length() == 0 || rules.length() == 0 || categories.isEmpty()) {
            this.errorText = AppHelper.getOutput("error.somethingMissing");
            return false;
        } else {
            if (!AppHelper.regex("^[\\w\\d]+$", name)) {
                this.errorText = AppHelper.getOutput("error.onlyNmbLet");
                return false;
            }
        }
        return true;
    }

    public void save() throws Exception {
        this.errorText = "";
        if (check()) {
            if (diskuto == null) {
                if (AppHelper.forumExists(name)) {
                    this.errorText = AppHelper.getOutput("error.alreadyDiskuto");
                } else {
                    Forum forum = new Forum();
                    forum.save(name, description, categories, moderators, rules, AppHelper.getActiveUser().getUsername());
                }
            } else {
                diskuto.update(description, rules, categories, moderators);
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("myDiskuto");
        }
    }

    public Forum getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(Forum diskuto) {
        this.diskuto = diskuto;
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

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getNameModerator() {
        return nameModerator;
    }

    public void setNameModerator(String nameModerator) {
        this.nameModerator = nameModerator;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
