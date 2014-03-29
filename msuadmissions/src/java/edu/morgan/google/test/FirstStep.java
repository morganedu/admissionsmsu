/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.test;

import com.google.api.services.drive.model.File;
import edu.morgan.google.drive.api.GoogleDrive;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pablohpsilva
 */
public class FirstStep {
    private GoogleDrive service;
    private HashMap<String, ArrayList<String>> documentsMissing;
    private HashMap<String, ArrayList<String>> documentsFound;
    
    public FirstStep(){
        this.service = new GoogleDrive();
        this.documentsFound = new HashMap<>();
        this.documentsMissing = new HashMap<>();
    }
    /*
    public void execute(ArrayList<IncompleteStudent> incompleteStudents){
        try{
            //Create a folder called PASSED
            File folderPassed = this.service.CreateFolder("PASSED");

            for(IncompleteStudent student : incompleteStudents){
                File studentFolder = this.service.GetFolderOrCreate(student.getLastName(), student.getFirstName(), student.getId());
                ArrayList<File> studentFiles = new ArrayList<>();

                ArrayList<String> docFound = new ArrayList<>();
                ArrayList<String> docMissing = new ArrayList<>();

                for(String document : student.getChecklist()){
                    File aux = this.service.GetFileByTitle(document);
                    if(aux == null)
                        docMissing.add(document);
                    else{
                        docFound.add(document);
                        this.service.MoveFiles(aux, studentFolder);
                    }
                }
                this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(), docFound);
                this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), docMissing);
                
                this.service.MoveFiles(studentFolder, folderPassed);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    */
    
    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents){
        try{
            //Create a folder called PASSED
            File folderPassed = this.service.GetFolderOrCreate("PASSED","","");

            for(IncompleteStudent student : incompleteStudents){
                File studentFolder = this.service.GetFoldersByUserInformation(student.getLastName(), student.getFirstName(), student.getId());
                
                ArrayList<String> list = new ArrayList<>();
                
                if(studentFolder == null){
                    this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), list);
                }else{
                    list.add(studentFolder.getTitle());
                    this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(),list);
                    this.service.MoveFiles(studentFolder, folderPassed);
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
