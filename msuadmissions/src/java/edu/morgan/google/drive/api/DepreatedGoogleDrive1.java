/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.drive.api;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
//import edu.morgan.google.drive.GoogleDriveFunctions;

/**
 *
 * @author user
 */
@ManagedBean
@RequestScoped
public class DepreatedGoogleDrive1 {

    /**
     * Creates a new instance of GoogleDrive
     */
    private DeprecatedGoogleDriveFunctions googleFunctions;
    private String URL;
    public DepreatedGoogleDrive1() {
        googleFunctions = new DeprecatedGoogleDriveFunctions("");
    }

    public String getURL() {
        this.URL = googleFunctions.getAuthorizationLink();
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    
    
}
