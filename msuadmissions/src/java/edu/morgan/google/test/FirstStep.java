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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablohpsilva
 */
public class FirstStep {

    private GoogleDrive service;
    private ArrayList<IncompleteStudent> checkedStudents;
    private ArrayList<IncompleteStudent> backupStudents = null;

    public FirstStep() {
        this.service = new GoogleDrive();
        this.checkedStudents = new ArrayList<>();
    }

    private void printIncompleteStudent(IncompleteStudent student) {
        System.out.println();
        System.out.println("Student: " + student.getLastName() + ", " + student.getFirstName());
        System.out.println("Type: " + student.getType());
    }

    private void MoveFilesToFolder(IncompleteStudent student, File studentFolder) throws IOException {

        System.out.println("\tMoveFilesToFolder method: Checklist:  ");

        if (!student.getChecklist().equals("")) {
            student.setChecklist(student.getChecklist().replaceAll("\\u000b", "::"));
            String[] documents = student.getChecklist().split("::");
            for (String checklist : documents) {
                checklist = checklist.replaceAll("_", " ");
                ArrayList<File> studentFile; //= new File();
                try {
                    studentFile = this.getService().GetFileStudentInfo(student.getLastName(), student.getFirstName(), checklist);
                    for (int i = 0; i < studentFile.size(); i++) {
                        this.getService().MoveFiles(studentFile.get(i), studentFolder);
                        student.setChecklist(student.getChecklist().replace(checklist, ""));
                        student.setChecklist(student.getChecklist().replace("::", ""));
                    }
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("File Not Found" + " " + ex.getMessage());
                    //continue;
                }

            }
            if (student.getChecklist().equals("")) {
                student.setChecklist("COMPLETE");
            }
        } else {
            student.setChecklist("COMPLETE");
        }
        this.getCheckedStudents().add(student);
    }

    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents) {
        try {
            File folderPassed1 = this.getService().GetFolderOrCreate("PASSED1");
            File folderPassed2 = this.getService().GetFolderOrCreate("PASSED2");
            File folderPassed3 = this.getService().GetFolderOrCreate("PASSED3");

            for (IncompleteStudent student : incompleteStudents) {

                this.printIncompleteStudent(student);

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
                this.getCheckedStudents().add(student);
                this.backupStudents.remove(student);
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

        if (this.backupStudents == null) {
            this.backupStudents = new ArrayList<>();
            this.backupStudents = (ArrayList<IncompleteStudent>) is.getStudents().clone();
            this.executePartOne(is.getStudents());
        } else {
            ArrayList<IncompleteStudent> aux = new ArrayList<>();
            aux = (ArrayList<IncompleteStudent>) this.backupStudents.clone();
            this.executePartOne(aux);
        }
    }

    /**
     * @return the service
     */
    public GoogleDrive getService() {
        return service;
    }

    /**
     * @return the checkedStudents
     */
    public ArrayList<IncompleteStudent> getCheckedStudents() {
        return checkedStudents;
    }

    public static void main(String args[]) throws IOException {
        IncompleteStudents is = new IncompleteStudents();
        FirstStep fs = new FirstStep();
        while (true) {
            try {
                is.utility();
                fs.runScript(is);
                is.generateJSON(is.convertToUsers(fs.getCheckedStudents()), "CheckedStudents");

            } catch (Exception ex) {
                System.out.println(ex.toString() + " " + ex.getMessage() + " " + ex.getLocalizedMessage());
                ///*

                return;

                //System.out.println("Back in main");
                //*/
            }
        }
    }
}
