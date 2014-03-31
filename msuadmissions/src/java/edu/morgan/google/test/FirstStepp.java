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
import java.util.Set;

/**
 *
 * @author pablohpsilva
 */
public class FirstStepp {

    private GoogleDrive service;
    private HashMap<String, ArrayList<String>> documentsMissing;
    private HashMap<String, ArrayList<String>> documentsFound;
    private HashMap<String, File> studentFoldersJson;
    protected HashMap<String, String> studentFolderWithoutID;

    private ArrayList<File> allFolders;

    public FirstStepp() {
        this.service = new GoogleDrive();
        this.documentsFound = new HashMap<>();
        this.documentsMissing = new HashMap<>();
        this.allFolders = new ArrayList<>();
        this.studentFoldersJson = new HashMap<>();
        this.studentFolderWithoutID = new HashMap<>();
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

    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents) {
        try {
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();

            //Create a folder called PASSED
            File folderPassed = this.getService().GetFolderOrCreate("PASSED");

            for (IncompleteStudent student : incompleteStudents) {
                File studentFolder = this.getService().getFolderByStudentInfo(student.getLastName(), student.getFirstName(), student.getId());

                System.out.println(student.getLastName() + " " + student.getFirstName() + " " + student.getId());

                //private HashMap<String, File> studentFoldersJson;
                if (studentFolder == null) {
                    //this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), list);

                    this.studentFolderWithoutID.put(student.getId(), student.getLastName() + "::" + student.getFirstName());
                } else {
                    //list.add(studentFolder.getTitle());
                    //this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(),list);
                    this.getService().MoveFiles(studentFolder, folderPassed);
                }
            }
            System.out.println(this.documentsFound.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void executePartTwo() {
        try {
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();

            //Create a folder called PASSED
            File folderPassed = this.getService().GetFolderOrCreate("SecondPASSED");
            Set<String> studentIDSets = this.studentFolderWithoutID.keySet();
            Object[] studentIDObjects = studentIDSets.toArray();

            for (int i = 0; i < studentFolderWithoutID.size(); i++) {
                String studentID = (String) studentIDObjects[i].toString();
                String str = studentFolderWithoutID.get(studentID);
                String nameConcat[] = str.split("::");
                String lastName = nameConcat[0];
                String firstName = nameConcat[1];

                File studentFolder = this.getService().getFolderByStudentInfo(lastName, firstName);

                System.out.println(lastName + " " + firstName + " " + studentID);

                //private HashMap<String, File> studentFoldersJson;
                if (studentFolder == null) {
                    //this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), list);
                    File createdFolder = this.getService().CreateFolder(lastName, firstName, studentID);
                    this.getService().MoveFiles(createdFolder, folderPassed);
                } else {
                    //list.add(studentFolder.getTitle());
                    //this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(),list);
                    this.getService().MoveFiles(studentFolder, folderPassed);
                }
            }
            System.out.println(this.documentsFound.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        IncompleteStudents is = new IncompleteStudents();
        FirstStep fs = new FirstStep();
        try {
            System.out.println("  " + fs.getService().GetAuthorizationLink());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            fs.service.setCode(br.readLine());

            //fs.generateHashMapFromFolders();
            ///*
            is.utility();
            fs.executePartOne(is.getStudents());
            fs.executePartTwo();
            //*/
        } catch (Exception ex) {
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
