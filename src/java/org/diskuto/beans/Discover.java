/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Forum;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "discover")
@ViewScoped
public class Discover implements Serializable {

    private List<org.diskuto.models.Forum> diskutos = new ArrayList<>();

    /**
     * Creates a new instance of Discover
     */
    public Discover() throws Exception {

        ResourceIterator iterator = AppHelper.getResourceSet("/forums/forum").getIterator();
        while (iterator.hasMoreResources()) {
            org.diskuto.models.Forum diskuto = new org.diskuto.models.Forum();
            diskuto.retrieve(new XmlHelper(iterator.nextResource()));
            diskutos.add(diskuto);
        }
    }

    public List<Forum> getDiskutos() {
        return diskutos;
    }

    public void setDiskutos(List<Forum> diskutos) {
        this.diskutos = diskutos;
    }

}
