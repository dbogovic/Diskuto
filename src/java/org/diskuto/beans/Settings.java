/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author dario
 */
@Named(value = "settings")
@ViewScoped
public class Settings implements Serializable {

    /**
     * Creates a new instance of Settings
     */
    public Settings() {
    }
    
}
