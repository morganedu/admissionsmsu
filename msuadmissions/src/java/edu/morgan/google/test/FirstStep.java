/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.test;

import edu.morgan.google.drive.api.GoogleDrive;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author pablohpsilva
 */
public class FirstStep {
    private GoogleDrive service;
    
    public FirstStep(){
        this.service = new GoogleDrive();
    }
    
    public void execute(){
        System.out.println("  " + this.service.GetAuthorizationLink());
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            this.service.setCode(br.readLine());
            
            //
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
