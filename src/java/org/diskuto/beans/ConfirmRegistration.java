/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author dario
 */
@Named(value = "confirmRegistration")
@RequestScoped
public class ConfirmRegistration {

    /**
     * Creates a new instance of ConfirmRegistration
     */
    public ConfirmRegistration() {
    }
    
}
