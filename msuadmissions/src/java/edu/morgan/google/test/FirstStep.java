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
    private HashMap<IncompleteStudent, ArrayList<String>> documentsMissing;
    private HashMap<IncompleteStudent, ArrayList<String>> documentsFound;
    private HashMap<String, File> studentFoldersJson;
    private ArrayList<File> allFolders;
    
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
    
    
    public void generateHashMapFromFolders(){
        ArrayList<File> folders = this.service.getAllFolders();
        for(File file : folders){
            String key = file.getTitle().replace("_"," ");
            this.studentFoldersJson.put(key, file);
        }
        System.out.println(this.studentFoldersJson.size());
    }
    */
    
    private void MoveFilesToFolder(IncompleteStudent student, File studentFolder) throws IOException{
        ArrayList<String> listMissing = new ArrayList<>();
        ArrayList<String> listFound = new ArrayList<>();

        //Check this... Get the list of documents missing
        for(String doc : student.getChecklist().split("\\u000b")){
            ArrayList<File> studentFile = this.getService().GetFileByStudendInfo(student.getLastName(), student.getFirstName(), student.getId(), doc);
            /*
            for(File file : studentFile){
                String title = file.getTitle().replaceAll("_", " ");
                if (!firstName.equals("") && title.contains(firstName)) {
                    return filed;
                }
            }
            */
            if(studentFile == null)
                listMissing.add(doc);
            else{ 
                listFound.add(doc);
                this.getService().MoveFiles(studentFile, studentFolder);
            }
            this.documentsMissing.put(student, listMissing);
            this.documentsFound.put(student, listFound);
        }
    }
    
    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents){
        try{
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();
            
            //Create a folder called PASSED
            File folderPassed = this.getService().GetFolderOrCreate("PASSED");
            File folderPassed2 = this.getService().GetFolderOrCreate("PASSED2");
            File folderPassed3 = this.getService().GetFolderOrCreate("PASSED3");

            for(IncompleteStudent student : incompleteStudents){
                
                // Debug purposes ONLY \/
                System.out.println(student.getLastName() + student.getFirstName() + student.getId());
                // Debug purposes ONLY /\
                
                
                File studentFolder = this.getService().getFolderByStudentInfo(student.getLastName(), student.getFirstName(), student.getId());
                
                // If The student folder doesn't exist, create it.
                if(studentFolder == null){
                    studentFolder = this.getService().CreateFolder(student.getLastName() + "_" + student.getFirstName()  + "_" +  student.getId() + "_" + student.getTerm());
                    this.MoveFilesToFolder(student, studentFolder);
                    this.getService().MoveFiles(studentFolder, folderPassed3);
                } else{
                    if(student.getId().equals("")){
                        this.MoveFilesToFolder(student, studentFolder);
                        this.getService().MoveFiles(studentFolder, folderPassed2);
                    }
                    else{
                        this.MoveFilesToFolder(student, studentFolder);
                        this.getService().MoveFiles(studentFolder, folderPassed);
                    }
                }
            }
            System.out.println(this.documentsFound.toString());
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
            
            //fs.generateHashMapFromFolders();
            
            ///*
            is.utility();
            fs.executePartOne(is.getStudents());
            //*/
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
