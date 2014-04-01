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
    ArrayList<IncompleteStudent> checkedStudents;
    ArrayList<IncompleteStudent> missingStudents;
    ArrayList<IncompleteStudent> foundStudents;

    public FirstStep() {
        this.service = new GoogleDrive();
        this.checkedStudents = new ArrayList<>();
        this.missingStudents = new ArrayList<>();
        this.foundStudents = new ArrayList<>();
    }
    
    private void printIncompleteStudent(IncompleteStudent student){
        System.out.println();
        System.out.println("Student: "+student.getLastName() + ", " + student.getFirstName());
        System.out.println("Term: " + student.getType());
        System.out.println();
    }

    private void MoveFilesToFolder(IncompleteStudent student, File studentFolder) throws IOException {

        System.out.println("\tMoveFilesToFolder method: Checklist:  ");

        if (!student.getChecklist().equals("")) {
            // Separate checklist by string
            student.setChecklist(student.getChecklist().replaceAll("\\u000b", "::"));
            String[] documents = student.getChecklist().split("::");
            
            // Get all files from student from GoogleDrive
            ArrayList<File> studentFiles = this.getService().GetFileStudentInfo(student.getLastName(), student.getFirstName(), "");

            if (studentFiles.isEmpty()) {
                this.missingStudents.add(student);
            } else {
                // For each string from var documents, do the loop
                for (String documentTitle : documents) {
                    System.out.print(documentTitle + ";");
                    // For each File from var studentFiles, do the loop
                    for (File file : studentFiles) {
                        String title = file.getTitle().replaceAll("_", " ");

                        // If the file contains the same title as the checklist, the last name and first name
                        // add that file to the documentsFound Hashmap, remove that name from var student checklist
                        // and move the file to the student folder.
                        if (title.contains(documentTitle)) {
                            if (!student.getLastName().equals("") && title.contains(student.getLastName())) {
                                if (!student.getFirstName().equals("") && title.contains(student.getFirstName())) {
                                    this.getService().MoveFiles(file, studentFolder);
                                    student.setChecklist(student.getChecklist().replace(documentTitle, ""));
                                    student.setChecklist(student.getChecklist().replace("::", ""));
                                }
                            }
                        }
                    }
                }
                if (student.getChecklist().equals("") || student.getChecklist().contains("^*:*$")){
                    student.setChecklist("COMPLETE");
                    this.foundStudents.add(student);
                }
                else{
                    this.missingStudents.add(student);
                }
            }
        } else {
            student.setChecklist("COMPLETE");
            this.foundStudents.add(student);
        }
    }

    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents) {
        try {
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();

            //Create a folder called PASSED
            File folderPassed1 = this.getService().GetFolderOrCreate("PASSED1");
            File folderPassed2 = this.getService().GetFolderOrCreate("PASSED2");
            File folderPassed3 = this.getService().GetFolderOrCreate("PASSED3");

            for (IncompleteStudent student : incompleteStudents) {

                System.out.println("\n\nStudent: " + student.getLastName() + student.getFirstName() + student.getId());

                File studentFolder = this.getService().getFolderByStudentInfo(student.getLastName(), student.getFirstName(), student.getId());

                if (studentFolder == null) {
                    studentFolder = this.getService().GetFolderOrCreate(student.getLastName() + "_" + student.getFirstName() + "_" + student.getId());
                    this.MoveFilesToFolder(student, studentFolder);
                    this.getService().MoveFiles(studentFolder, folderPassed3);
                } else {
                    this.MoveFilesToFolder(student, studentFolder);

                    if (student.getId().equals("")) {
                        this.getService().MoveFiles(studentFolder, folderPassed2);
                    } else {
                        this.getService().MoveFiles(studentFolder, folderPassed1);
                    }
                }
                this.checkedStudents.add(student);
                System.out.println("Incomplete Students checked: " + this.checkedStudents.size() + " / " + incompleteStudents.size());
            }
        } catch (IOException ex) {
            System.out.println("Back in executePartOne");
            ex.printStackTrace();
        }
    }

    public void runScript(IncompleteStudents is) throws IOException {
        System.out.println(this.getService().GetAuthorizationLink());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.service.setCode(br.readLine());
        
        this.executePartOne(is.getStudents());
    }

    public static void main(String args[]) throws IOException {
        IncompleteStudents is = new IncompleteStudents();
        FirstStep fs = new FirstStep();
        try {
            is.utility();
            fs.runScript(is);
        } catch (Exception ex) {
            System.out.println(ex.toString() + " " + ex.getMessage() + " " + ex.getLocalizedMessage());
            try{
                
             fs.runScript(is);
             }catch(Exception e){
                System.out.println(e.toString() + " " + e.getMessage() + " " + e.getLocalizedMessage());
             }
             System.out.println("Back in main");
        }
    }

    /**
     * @return the service
     */
    public GoogleDrive getService() {
        return service;
    }
}
