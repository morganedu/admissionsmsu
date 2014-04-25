/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.test;

import com.google.api.services.drive.model.File;
import edu.morgan.google.drive.api.GoogleDrive;
import edu.morgan.output.files.WriteCSVFile;
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
            
            for (IncompleteStudent student : incompletestudents.getStudents()) {
                System.out.println(student.getChecklist().trim());
            }
            
            int counter = 0;
            for (IncompleteStudent student : incompletestudents.getStudents()) {
                PrettyStudentPrint psp = new PrettyStudentPrint(student.getLastName() + ", " + student.getFirstName() + ", " + student.getId());
                
                // Get or Create Folder
                File studentFolder = gd.getCreateFolder(googleDriveFolders, student.getLastName(), student.getFirstName(), student.getId());
                
                System.out.println("Debug purposes: " + student.getLastName() + ", " + student.getFirstName() + " - " + ++counter);
                
                if (!student.getChecklist().equals("")) {
                    for(String checklistitem : student.getChecklist().split("::")){
                        ArrayList<File> tempFiles = new ArrayList<>();
                        String codeItem = "";
                        
                        if(checklistitem.contains("act") && checklistitem.contains("sat") && checklistitem.contains("scores")){
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat", "act"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "TSTS", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        else if (checklistitem.contains("scores")){
                            if(checklistitem.contains("sat") ){
                                if(checklistitem.contains("tswe")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat", "tswe"} );
                                    codeItem = "S05";
                                }
                                else{
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat"} );
                                    codeItem = "SAT";
                                }
                                
                                if(!tempFiles.isEmpty()){
                                    exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for(File file : tempFiles)
                                        gd.MoveFiles(file, studentFolder);
                                }
                            }
                            
                            if (checklistitem.contains("act")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "scores", "act"} );
                                if(!tempFiles.isEmpty()){
                                    exec.organizeArray(prettyPrint, psp, "TSTS", "found");
                                    exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for(File file : tempFiles)
                                        gd.MoveFiles(file, studentFolder);
                                }
                            }
                        }
                        else if(checklistitem.contains("sat")){
                            if(checklistitem.contains("verbal")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "sat", "verbal"} );
                                codeItem = "S01";
                            }
                            else if(checklistitem.contains("math")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "sat", "math"} );
                                codeItem = "S02";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if(checklistitem.contains("fee")){
                            if(checklistitem.contains("confirmation")){
                                if(checklistitem.contains("112.50")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "112.50"} );
                                    codeItem = "IE11";
                                } else if(checklistitem.contains("37.50")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "37.50"} );
                                    codeItem = "IE37";
                                } else if(checklistitem.contains("75.00")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "75.00"} );
                                    codeItem = "IE75";
                                } else if(checklistitem.contains("waiver")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "waiver"} );
                                    codeItem = "IEW";
                                } else if(checklistitem.contains("nexus")){
                                    tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "nexus"} );
                                    codeItem = "IEX";
                                }
                            } else if(checklistitem.contains("waiver")){
                                if(checklistitem.contains("house") && checklistitem.contains("open")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "waiver", "house", "open"} );
                                codeItem = "APO";
                                } else if(checklistitem.contains("half")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "waiver", "half"} );
                                codeItem = "APH";
                                }
                            } else if(checklistitem.contains("application") && checklistitem.contains("35")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "fee", "application", "35"} );
                                codeItem = "AP25";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if(checklistitem.contains("waiver") && checklistitem.contains("application")){
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "waiver", "application"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "APW", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if(checklistitem.contains("detailed") && checklistitem.contains("eval")){
                            if(checklistitem.contains("ece")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "detailed", "eval", "ece"} );
                                codeItem = "AUD2";
                            }else if(checklistitem.contains("wes")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "detailed", "eval", "wes"} );
                                codeItem = "AUDE";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if(checklistitem.contains("recommendation")){
                            if(checklistitem.contains("counselor")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "counselor"} );
                                codeItem = "LRE2";
                            }
                            else if(checklistitem.contains("teacher")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "teacher"} );
                                codeItem = "LRE1";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("certificate")) {
                            if (checklistitem.contains("secondary") && checklistitem.contains("school")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "secondary", "school"} );
                                codeItem = "SSC";
                            } else if (checklistitem.contains("birth")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "birth"} );
                                codeItem = "COBC";
                            } else if (checklistitem.contains("marriage")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "marriage"} );
                                codeItem = "COMC";
                            }else if (checklistitem.contains("financial")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "financial"} );
                                codeItem = "FC";
                            }else if (checklistitem.contains("naturalization")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "naturalization"} );
                                codeItem = "CON";
                            }else if (checklistitem.contains("achievement")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "certificate", "achievement"} );
                                codeItem = "CER";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
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
                        
                        else if (checklistitem.contains("transcript")) {
                            codeItem = "";
                            if(checklistitem.contains("high") && checklistitem.contains("school")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"} );
                            }else if(checklistitem.contains("official") && checklistitem.contains("college")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"} );
                            }else if(checklistitem.contains("unofficial")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"} );
                            }else if(checklistitem.contains("evaluation")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"} );
                                codeItem = "TRNE";
                            }
                            if(!tempFiles.isEmpty()){
                                if(!codeItem.equals(""))
                                    exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder); 
                            }
                        }
                        
                        else if (checklistitem.contains("form")) {
                            if(checklistitem.contains("dd") && checklistitem.contains("214")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "dd", "214", "form"} );
                                codeItem = "D214";
                            }else if(checklistitem.contains("residence")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "form", "residence"} );
                                codeItem = "RESP";
                            }else if(checklistitem.contains("asylium-refugee")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "form", "asylium-refugee"} );
                                codeItem = "ASG";
                            }else if(checklistitem.contains("transfer") && checklistitem.contains("eligibility")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "form", "transfer", "eligibility"} );
                                codeItem = "TREL";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("affidavit") && checklistitem.contains("support")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "affidavit", "support"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "AOS", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("advanced") && checklistitem.contains("placement") && checklistitem.contains("board")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "advanced", "affidavit", "support"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "AP", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("civilian") && checklistitem.contains("millitary") && checklistitem.contains("person")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "civilian", "millitary", "person"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "BRAC", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("art") && checklistitem.contains("portfolio")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "art", "portfolio"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "ARTP", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("maryland")) {
                            if(checklistitem.contains("tax") && checklistitem.contains("return")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "maryland", "tax", "return"} );
                                codeItem = "COMT";
                            } else if(checklistitem.contains("adult") && checklistitem.contains("external") && checklistitem.contains("diploma")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "maryland", "adult", "external", "diploma"} );
                                codeItem = "MAD";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        /*
                        "letter", "human", "resource"
"reference", "letter"
                        */
                        
                        else if (checklistitem.contains("letter")) {
                            if(checklistitem.contains("human") && checklistitem.contains("resources")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "letter", "human", "resources"} );
                                codeItem = "MDHR";
                            } else if(checklistitem.contains("reference")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "letter", "reference"} );
                                codeItem = "REF3";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("bank") && checklistitem.contains("statement")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "bank", "statement"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "BS", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("world") && checklistitem.contains("education")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "world", "education"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "WES1", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("toefl")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "toefl"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "TOEFL", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("tax") && checklistitem.contains("return") && checklistitem.contains("personal")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "tax", "return", "personal"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "TAXP", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("test") && checklistitem.contains("spoken") && checklistitem.contains("english")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "test", "spoken", "english"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "TSE", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("social") && checklistitem.contains("security")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "security", "social"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "SS", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("tax") && checklistitem.contains("return") && checklistitem.contains("personal")) {
                            tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "tax", "return", "personal"} );
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, "TAXP", "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("level")) {
                            if(checklistitem.contains("gca") && checklistitem.contains("advance")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "level", "gca", "advance"} );
                                codeItem = "GCEA";
                            }else if(checklistitem.contains("gce") && checklistitem.contains("ordinary")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "level", "ordinary", "gce"} );
                                codeItem = "GCEO";
                            }else if(checklistitem.contains("college") && checklistitem.contains("exam") && checklistitem.contains("program")){
                                tempFiles = gd.getStudentFiles(new String[] {student.getLastName(), student.getFirstName(), student.getId(), "level", "exam", "program", "college"} );
                                codeItem = "CLEP";
                            }
                            if(!tempFiles.isEmpty()){
                                exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                exec.changeChecklist(studentsProcessed, checklistitem, student);
                                for(File file : tempFiles)
                                    gd.MoveFiles(file, studentFolder);
                            }
                        }
                        
                        else if (checklistitem.contains("official") && checklistitem.contains("exam")) {
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
            WriteCSVFile.printArray(prettyPrint);
            WriteXMLFile.printArray(prettyPrint);
        } catch (Exception ex) {
            Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
