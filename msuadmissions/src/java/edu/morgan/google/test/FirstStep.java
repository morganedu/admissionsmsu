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
    private IncompleteStudents students = new IncompleteStudents();

    public FirstStep() {
        this.service = new GoogleDrive();
        this.documentsFound = new HashMap<>();
        this.documentsMissing = new HashMap<>();
    }

    public void execute() {
        try {
            //Create a folder called PASSED
            File folderPassed = this.service.CreateFolder("PASSED");
            try {
                students.utility();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ArrayList<IncompleteStudent> incompleteStudents = students.getStudents();
            for (IncompleteStudent student : incompleteStudents) {
                File studentFolder = this.service.GetFolderOrCreate(student.getLastName(), student.getFirstName(), student.getId());
                ArrayList<File> studentFiles = new ArrayList<>();

                ArrayList<String> docFound = new ArrayList<>();
                ArrayList<String> docMissing = new ArrayList<>();

                for (String document : student.getDocuments()) {
                    File aux = this.service.GetFileByTitle(document);
                    if (aux == null) {
                        docMissing.add(document);
                    } else {
                        docFound.add(document);
                        this.service.MoveFiles(aux, studentFolder);
                    }
                }
                this.documentsFound.put(student.getLastName() + student.getFirstName() + student.getId(), docFound);
                this.documentsMissing.put(student.getLastName() + student.getFirstName() + student.getId(), docMissing);

                this.service.MoveFiles(studentFolder, folderPassed);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
