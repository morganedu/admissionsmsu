/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.test;

import com.google.api.services.drive.model.File;
import edu.morgan.google.drive.api.GoogleDrive;
import edu.morgan.output.files.WriteCVSFile;
import edu.morgan.output.files.WriteXMLFile;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import edu.morgan.users.PrettyStudentPrint;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablohpsilva
 */
public class MyMain {
    
    public int test(String args[]){
        return args.length;
    }
    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        
        GoogleDrive gd = new GoogleDrive();
        Execute exec = new Execute();

        ArrayList<IncompleteStudent> studentsProcessed = new ArrayList<>();
        IncompleteStudents incompletestudents = new IncompleteStudents();
        ArrayList<PrettyStudentPrint> prettyPrint = new ArrayList<>();

        try {
            System.out.println(gd.GetAuthorizationLink());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            gd.setCode(br.readLine());
            
            ArrayList<File> googleDriveFolders = gd.getAllFolders();
            
            incompletestudents.utility();
            
            int counter = 0;
            for (IncompleteStudent student : incompletestudents.getStudents()) {
                PrettyStudentPrint psp = new PrettyStudentPrint(student.getLastName() + ", " + student.getFirstName() + ", " + student.getId());
                
                // Get or Create Folder
                File studentFolder = gd.getCreateFolder(googleDriveFolders, student.getLastName(), student.getFirstName(), student.getId());
                
                System.out.println("Debug purposes: " + student.getLastName() + ", " + student.getFirstName() + " - " + ++counter);
                
                if (!student.getChecklist().equals("")) {
                    for(String checklistitem : student.getChecklist().split("::")){
                        ArrayList<File> tempFiles = new ArrayList<>();
                        
                        if (checklistitem.contains("scores")){
                            System.out.println("\t keywords triggered: scores | sat | act");
                            
                            if(checklistitem.contains("sat") )
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat"} );
                            else if (checklistitem.contains("act"))
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "act"} );
                            
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if(checklistitem.contains("recommendation")){
                            System.out.println("\t keywords triggered: recommendation | counselor | teacher");
                            
                            if(checklistitem.contains("counselor"))
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "counselor"} );
                            else if(checklistitem.contains("teacher"))
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "teacher"} );
                            
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("certificate")) {
                            System.out.println("\t keywords triggered: certificate | secondary | school | birth");
                            
                            if (checklistitem.contains("secondary") && checklistitem.contains("school"))
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "secondary", "school"} );
                            else if (checklistitem.contains("birth"))
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "birth"} );
                            
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("essay") && checklistitem.contains("personal")) {
                            System.out.println("\t keywords triggered: essay | personal");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "essay", "personal"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("high") && checklistitem.contains("school") && checklistitem.contains("transcript")) {
                            System.out.println("\t keywords triggered: high | school | transcript");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder); 
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("official") && checklistitem.contains("exam")) {
                            System.out.println("\t keywords triggered: official | exam");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "sssce"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "sce"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "waec"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "cxc"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "gde"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("214") && checklistitem.contains("form")) {
                            System.out.println("\t keywords triggered: 214 | form");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "214", "form"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        
                        else if (checklistitem.contains("resident") && checklistitem.contains("card")) {
                            System.out.println("\t keywords triggered: resident | card");
                            
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "resident", "card"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, checklistitem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                            else
                                exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                        }
                        else
                            exec.organizeArray(prettyPrint, psp, checklistitem, "not");
                    }

                }
            }

            // Generate new JSONFile
            incompletestudents.generateJSON(incompletestudents.convertToUsers(studentsProcessed), "BAFASE_new_min");
            WriteCVSFile.printArray(prettyPrint);
            WriteXMLFile.printArray(prettyPrint);
        } catch (Exception ex) {
            Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
