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
    private ArrayList<File> allFolders;
    
    public FirstStep(){
        this.service = new GoogleDrive();
        this.documentsFound = new HashMap<>();
        this.documentsMissing = new HashMap<>();
        this.allFolders = new ArrayList<>();
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
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();
            
            //Create a folder called PASSED
            File folderPassed = this.getService().GetFolderOrCreate("PASSED","","");

            for(IncompleteStudent student : incompleteStudents){
                File studentFolder = this.getService().GetFoldersByUserInformation(student.getLastName(), student.getFirstName(), student.getId());
                
                ArrayList<String> list = new ArrayList<>();
                
                if(studentFolder == null){
                    this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), list);
                }else{
                    list.add(studentFolder.getTitle());
                    this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(),list);
                    this.getService().MoveFiles(studentFolder, folderPassed);
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        IncompleteStudents is = new IncompleteStudents();
        FirstStep fs = new FirstStep();
        try{
            System.out.println("  " + fs.getService().GetAuthorizationLink());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            fs.service.setCode(br.readLine());
            
            is.utility();
            fs.executePartOne(is.getStudents());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * @return the service
     */
    public GoogleDrive getService() {
        return service;
    }
}
