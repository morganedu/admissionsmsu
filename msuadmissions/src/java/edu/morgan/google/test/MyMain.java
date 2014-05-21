/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.morgan.google.test;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.model.File;
import edu.morgan.google.drive.api.GoogleDrive;
import edu.morgan.output.files.WriteCSVFile;
import edu.morgan.output.files.WriteXMLFile;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.IncompleteStudents;
import edu.morgan.users.PrettyStudentPrint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablohpsilva
 */
public class MyMain {

    public int test(String args[]) {
        return args.length;
    }

    public void Start(String code) {
        Random randomGenerator = new Random();
        GoogleDrive gd = new GoogleDrive();
        //Execute exec = new Execute();

        //ArrayList<IncompleteStudent> studentsProcessed = new ArrayList<>();
        IncompleteStudents incompletestudents = new IncompleteStudents();
        ArrayList<PrettyStudentPrint> prettyPrint = new ArrayList<>();
        for (int n = 0; n < 1; ++n) {

            try {
                /*
                 System.out.println(gd.GetAuthorizationLink());
                 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                 gd.setCode(br.readLine());
                 */
                gd.setCode(code);

                ArrayList<File> googleDriveFolders = gd.getAllFolders();

                incompletestudents.utility();

                int counter = 0;
                File autoFolder = gd.getCreateFolder(googleDriveFolders, "AUTO8.8");

                for (IncompleteStudent student : incompletestudents.getStudents()) {
                    PrettyStudentPrint psp = new PrettyStudentPrint(student.getLastName() + ", " + student.getFirstName() + ", " + student.getId());

                    // Get or Create Folder
                    File studentFolder = gd.getCreateFolder(googleDriveFolders, student.getLastName(), student.getFirstName(), student.getId());

                    //out.println("<li>" + student.getLastName() + ", " + student.getFirstName() + " - " + ++counter + "</li>");
                    if (!student.getChecklist().equals("")) {
                        for (String checklistitem : student.getChecklist().split("::")) {
                            ArrayList<File> tempFiles = new ArrayList<>();
                            String codeItem = "";

                            if (checklistitem.contains("act") && checklistitem.contains("sat") && checklistitem.contains("scores")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat", "act"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "TSTS", "found");
                                    psp.setChecklistItem("TSTS", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "TSTS", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("TSTS", "not");
                            }
                            if (checklistitem.contains("scores")) {
                                if (checklistitem.contains("sat")) {
                                    if (checklistitem.contains("tswe")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat", "tswe"});
                                        codeItem = "S05";
                                    } else {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "scores", "sat"});
                                        codeItem = "SAT";
                                    }

                                    if (!tempFiles.isEmpty()) {
                                        //exec.organizeArray(prettyPrint, psp, codeItem, 
                                        psp.setChecklistItem(codeItem, "found");
                                        //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                        for (File file : tempFiles) {
                                            gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                        }
                                    } else
                                        psp.setChecklistItem(codeItem, "not");
                                }

                                if (checklistitem.contains("act")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "scores", "act"});
                                    if (!tempFiles.isEmpty()) {
                                        //exec.organizeArray(prettyPrint, psp, "TSTS", 
                                        psp.setChecklistItem("TSTS", "found");
                                        //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                        for (File file : tempFiles) {
                                            gd.MoveFiles(file, studentFolder, student, "TSTS", checklistitem);
                                        }
                                    } else
                                        psp.setChecklistItem("TSTS", "not");
                                }
                            }
                            if (checklistitem.contains("sat")) {
                                if (checklistitem.contains("verbal")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "sat", "verbal"});
                                    codeItem = "S01";
                                } else if (checklistitem.contains("math")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "sat", "math"});
                                    codeItem = "S02";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("fee")) {
                                if (checklistitem.contains("confirmation")) {
                                    if (checklistitem.contains("112.50")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "112.50"});
                                        codeItem = "IE11";
                                    } else if (checklistitem.contains("37.50")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "37.50"});
                                        codeItem = "IE37";
                                    } else if (checklistitem.contains("75.00")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "75.00"});
                                        codeItem = "IE75";
                                    } else if (checklistitem.contains("waiver")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "waiver"});
                                        codeItem = "IEW";
                                    } else if (checklistitem.contains("nexus")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "confirmation", "nexus"});
                                        codeItem = "IEX";
                                    }
                                } else if (checklistitem.contains("waiver")) {
                                    if (checklistitem.contains("house") && checklistitem.contains("open")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "waiver", "house", "open"});
                                        codeItem = "APO";
                                    } else if (checklistitem.contains("half")) {
                                        tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "waiver", "half"});
                                        codeItem = "APH";
                                    }
                                } else if (checklistitem.contains("application") && checklistitem.contains("35")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "fee", "application", "35"});
                                    codeItem = "AP25";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("waiver") && checklistitem.contains("application")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "waiver", "application"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "APW", "found");
                                    psp.setChecklistItem("APW", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "APW", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("APW", "not");
                            }
                            if (checklistitem.contains("detailed") && checklistitem.contains("eval")) {
                                if (checklistitem.contains("ece")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "detailed", "eval", "ece"});
                                    codeItem = "AUD2";
                                } else if (checklistitem.contains("wes")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "detailed", "eval", "wes"});
                                    codeItem = "AUDE";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("recommendation")) {
                                if (checklistitem.contains("counselor")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "counselor"});
                                    codeItem = "LRE2";
                                } else if (checklistitem.contains("teacher")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "recommendation", "teacher"});
                                    codeItem = "LRE1";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("certificate")) {
                                if (checklistitem.contains("secondary") && checklistitem.contains("school")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "secondary", "school"});
                                    codeItem = "SSC";
                                } else if (checklistitem.contains("birth")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "birth"});
                                    codeItem = "COBC";
                                } else if (checklistitem.contains("marriage")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "marriage"});
                                    codeItem = "COMC";
                                } else if (checklistitem.contains("financial")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "financial"});
                                    codeItem = "FC";
                                } else if (checklistitem.contains("naturalization")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "naturalization"});
                                    codeItem = "CON";
                                } else if (checklistitem.contains("achievement")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "certificate", "achievement"});
                                    codeItem = "CER";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("transcript")) {
                                codeItem = "";
                                if (checklistitem.contains("high") && checklistitem.contains("school")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "high", "school", "transcript"});
                                    //tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "high", "school"});
                                    codeItem = "HST";
                                } else if (checklistitem.contains("official") && checklistitem.contains("college")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "college", "transcript"});
                                    //tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "college"});
                                    codeItem = "CLT";
                                } else if (checklistitem.contains("unofficial")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "unofficial", "transcript"});
                                    //tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "unofficial"});
                                    codeItem = "UNO";
                                } else if (checklistitem.contains("evaluation")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "evaluation", "transcript"});
                                    //tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "evaluation"});
                                    codeItem = "TRNE";
                                }
                                if (!tempFiles.isEmpty()) {
                                    if (codeItem.equals("TRNE")) {
                                        //exec.organizeArray(prettyPrint, psp, codeItem, 
                                        psp.setChecklistItem(codeItem, "found");
                                    }
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("form")) {
                                if (checklistitem.contains("dd") && checklistitem.contains("214")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "dd", "214", "form"});
                                    codeItem = "D214";
                                } else if (checklistitem.contains("residence")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "form", "residence"});
                                    codeItem = "RESP";
                                } else if (checklistitem.contains("asylium-refugee")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "form", "asylium-refugee"});
                                    codeItem = "ASG";
                                } else if (checklistitem.contains("transfer") && checklistitem.contains("eligibility")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "form", "transfer", "eligibility"});
                                    codeItem = "TREL";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("affidavit") && checklistitem.contains("support")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "affidavit", "support"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "AOS", "found");
                                    psp.setChecklistItem("AOS", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "AOS", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("AOS", "not");
                            }
                            if (checklistitem.contains("essay") && checklistitem.contains("personal")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "essay", "personal"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "ESSY", "found");
                                    psp.setChecklistItem("ESSY", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "ESSY", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("ESSY", "not");
                            }
                            if (checklistitem.contains("advanced") && checklistitem.contains("placement") && checklistitem.contains("board")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "advanced", "affidavit", "support"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "AP", "found");
                                    psp.setChecklistItem("AP", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "AP", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("AP", "not");
                            }
                            if (checklistitem.contains("civilian") && checklistitem.contains("millitary") && checklistitem.contains("person")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "civilian", "millitary", "person"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "BRAC", "found");
                                    psp.setChecklistItem("BRAC", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "BRAC", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("BRAC", "not");
                            }
                            if (checklistitem.contains("art") && checklistitem.contains("portfolio")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "art", "portfolio"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "ARTP", "found");
                                    psp.setChecklistItem("ARTP", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "ARTP", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("ARTP", "not");
                            }
                            if (checklistitem.contains("maryland")) {
                                if (checklistitem.contains("tax") && checklistitem.contains("return")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "maryland", "tax", "return"});
                                    codeItem = "COMT";
                                } else if (checklistitem.contains("adult") && checklistitem.contains("external") && checklistitem.contains("diploma") || checklistitem.contains("diplom")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "maryland", "adult", "external", "diploma"});
                                    codeItem = "MAD";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("educational") || checklistitem.contains("educ")) {
                                if (checklistitem.contains("individual") && checklistitem.contains("plan")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "educational", "tax", "return"});
                                    codeItem = "IEP";
                                } else if (checklistitem.contains("world") && checklistitem.contains("services")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "educational", "adult", "external"});
                                    codeItem = "";
                                } else if (checklistitem.contains("evaluators") && checklistitem.contains("cred")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "educational", "adult", "external"});
                                    codeItem = "ECE1";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("letter")) {
                                if (checklistitem.contains("human") && checklistitem.contains("resources")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "letter", "human", "resources"});
                                    codeItem = "MDHR";
                                } else if (checklistitem.contains("reference")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "letter", "reference"});
                                    codeItem = "REF3";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("international")) {
                                if (checklistitem.contains("student") && checklistitem.contains("application") || checklistitem.contains("app")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "international", "student", "application"});
                                    codeItem = "ISA";
                                } else if (checklistitem.contains("english") && checklistitem.contains("lan") && checklistitem.contains("test")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "international", "english", "lan", "test"});
                                    codeItem = "IELT";
                                } else if (checklistitem.contains("info") && checklistitem.contains("applicant") || checklistitem.contains("app")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "international", "info", "applicant"});
                                    codeItem = "SUPP";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("level")) {
                                if (checklistitem.contains("gca") && checklistitem.contains("advance")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "level", "gca", "advance"});
                                    codeItem = "GCEA";
                                } else if (checklistitem.contains("gce") && checklistitem.contains("ordinary")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "level", "ordinary", "gce"});
                                    codeItem = "GCEO";
                                } else if (checklistitem.contains("college") && checklistitem.contains("exam") && checklistitem.contains("program")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "level", "exam", "program", "college"});
                                    codeItem = "CLEP";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("passport")) {
                                if (checklistitem.contains("non")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "non", "passport"});
                                    codeItem = "PASS";
                                } else if (checklistitem.contains("us")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "us", "passport"});
                                    codeItem = "COPS";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("report") || checklistitem.contains("repo")) {
                                if (checklistitem.contains("grade") && checklistitem.contains("card")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "report", "grade"});
                                    codeItem = "GRR";
                                } else if (checklistitem.contains("secondary") && checklistitem.contains("school")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "report", "secondary", "school"});
                                    codeItem = "GRRP";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("english")) {
                                if (checklistitem.contains("translation") && checklistitem.contains("records")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "english", "translation", "records"});
                                    codeItem = "ETR";
                                } else if (checklistitem.contains("second") && checklistitem.contains("language")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "english", "second", "language"});
                                    codeItem = "ESL";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("approval")) {
                                if (checklistitem.contains("department")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "approval", "department"});
                                    codeItem = "DEPA";
                                } else if (checklistitem.contains("departmental")) {
                                    tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "approval", "departmental"});
                                    codeItem = "DEPD";
                                }
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem(codeItem, "not");
                            }
                            if (checklistitem.contains("deferred") && checklistitem.contains("action") && checklistitem.contains("childhood")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "deferred", "action", "childhood"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "DACA", "found");
                                    psp.setChecklistItem("DACA", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "DACA", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("DACA", "not");
                            }
                            if (checklistitem.contains("employment") && checklistitem.contains("authorizaation")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "employment", "authorization"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "EAC", "found");
                                    psp.setChecklistItem("EAC", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "EAC", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("EAC", "not");
                            }
                            if (checklistitem.contains("confirmation") && checklistitem.contains("incentive")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "confirmation", "incentive"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "IEG", "found");
                                    psp.setChecklistItem("IEG", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "IEG", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("IEG", "not");
                            }
                            if (checklistitem.contains("graduate") && checklistitem.contains("diploma") && checklistitem.contains("equivalency")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "graduate", "diploma", "equivalency"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "GED", "found");
                                    psp.setChecklistItem("GED", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "GED", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("GED", "not");
                            }
                            if (checklistitem.contains("bank") && checklistitem.contains("statement")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "bank", "statement"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "BS", "found");
                                    psp.setChecklistItem("BS", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "BS", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("BS", "not");
                            }
                            if (checklistitem.contains("cambridge") && checklistitem.contains("proficiency") && checklistitem.contains("test")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "cambridge", "proficiency", "test"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "CPE", "found");
                                    psp.setChecklistItem("CPE", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "CPE", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("CPE", "not");
                            }
                            if (checklistitem.contains("i-20") && checklistitem.contains("student") && checklistitem.contains("visa")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "i-20", "student", "visa"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "F1", "found");
                                    psp.setChecklistItem("F1", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "F1", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("F1", "not");
                            }
                            if (checklistitem.contains("dream") && checklistitem.contains("act")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "dream", "act"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "I797", "found");
                                    psp.setChecklistItem("I797", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "I797", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("I797", "not");
                            }
                            if (checklistitem.contains("national") && checklistitem.contains("external") && checklistitem.contains("diploma")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "national", "external", "diploma"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "NEDP", "found");
                                    psp.setChecklistItem("NEDP", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "NEDP", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("NEDP", "not");
                            }
                            if (checklistitem.contains("paternal") && checklistitem.contains("consent")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "consent", "paternal"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "PAC", "found");
                                    psp.setChecklistItem("PAC", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "PAC", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("PAC", "not");
                            }
                            if (checklistitem.contains("midyear") || checklistitem.contains("mid year") || checklistitem.contains("mid-year") && checklistitem.contains("grade")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "grade", "mid-year"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "MIDY", "found");
                                    psp.setChecklistItem("MIDY", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "MIDY", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("MIDY", "not");
                            }
                            if (checklistitem.contains("military") && checklistitem.contains("orders")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "military", "orders"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "MO", "found");
                                    psp.setChecklistItem("MO", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "MO", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("MO", "not");
                            }
                            if (checklistitem.contains("court") && checklistitem.contains("order")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "court", "order"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "COOR", "found");
                                    psp.setChecklistItem("COOR", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "COOR", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("COOR", "not");
                            }
                            if (checklistitem.contains("resume")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "resume"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "RESU", "found");
                                    psp.setChecklistItem("RESU", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "RESU", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("RESU", "not");
                            }
                            if (checklistitem.contains("residence") && checklistitem.contains("verification")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "residence", "verification"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "RSV", "found");
                                    psp.setChecklistItem("RSV", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "RSV", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("RSV", "not");
                            }
                            if (checklistitem.contains("world") && checklistitem.contains("education") || checklistitem.contains("educ")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "world", "education"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "WES1", "found");
                                    psp.setChecklistItem("WES1", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "WES1", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("WES1", "not");
                            } 
                            if (checklistitem.contains("toefl")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "toefl"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "TOEFL", "found");
                                    psp.setChecklistItem("TOEFL", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "TOEFL", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("TOEFL", "not");
                            }
                            if (checklistitem.contains("tax") && checklistitem.contains("return") && checklistitem.contains("personal")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "tax", "return", "personal"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "TAXP", "found");
                                    psp.setChecklistItem("TAXP", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "TAXP", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("TAXP", "not");
                            }
                            if (checklistitem.contains("test") && checklistitem.contains("spoken") && checklistitem.contains("english")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "test", "spoken", "english"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "TSE", "found");
                                    psp.setChecklistItem("TSE", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "TSE", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("TSE", "not");
                            }
                            if (checklistitem.contains("social") && checklistitem.contains("security")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "security", "social"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "SS", "found");
                                    psp.setChecklistItem("SS", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "SS", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("SS", "not");
                            }
                            if (checklistitem.contains("tax") && checklistitem.contains("return") && checklistitem.contains("personal")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "tax", "return", "personal"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "TAXP", "found");
                                    psp.setChecklistItem("TAXP", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "TAXP", checklistitem);
                                    }
                                } else 
                                    psp.setChecklistItem("TAXP", "not");
                            }
                            if (checklistitem.contains("i-551") && checklistitem.contains("permanent") || checklistitem.contains("perm") && checklistitem.contains("residence")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "residence", "permanent", "i-551"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.organizeArray(prettyPrint, psp, "PRC", "found");
                                    psp.setChecklistItem("PRC", "found");
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "PRC", checklistitem);
                                    }
                                } else
                                    psp.setChecklistItem("PRC", "not");
                            }
                            if (checklistitem.contains("official") && checklistitem.contains("exam")) {
                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "sssce"});
                                if (!tempFiles.isEmpty()) {
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, "OFEX", checklistitem);
                                    }
                                }

                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "sce"});
                                if (!tempFiles.isEmpty()) {
                                    codeItem = "OFEX";
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                }

                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "waec"});
                                if (!tempFiles.isEmpty()) {
                                    codeItem = "OFEX";
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                }

                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "cxc"});
                                if (!tempFiles.isEmpty()) {
                                    codeItem = "OFEX";
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                }

                                tempFiles = gd.getStudentFiles(new String[]{student.getLastName(), student.getFirstName(), student.getId(), "official", "exam", "gde"});
                                if (!tempFiles.isEmpty()) {
                                    codeItem = "OFEX";
                                    //exec.changeChecklist(studentsProcessed, checklistitem, student);
                                    for (File file : tempFiles) {
                                        gd.MoveFiles(file, studentFolder, student, codeItem, checklistitem);
                                    }
                                }

                                if (!codeItem.equals("")) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "found");
                                    psp.setChecklistItem(codeItem, "found");
                                } else if (codeItem.equals("")) {
                                    //exec.organizeArray(prettyPrint, psp, codeItem, "not");
                                    psp.setChecklistItem(codeItem, "not");
                                }
                            }
                        }

                    }
                    prettyPrint.add(psp);
                    gd.MoveFiles(studentFolder, autoFolder);
                }

                WriteCSVFile.printArray(prettyPrint);
                // Generate new JSONFile
                //incompletestudents.generateJSON(incompletestudents.convertToUsers(studentsProcessed), "BAFASE_new_min");
                //WriteXMLFile.printArray(prettyPrint);
                //out.println("<li><h3>All students processed. Total of students: " + counter + "</h3></li>");
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 503) {
                    try {
                        // Apply exponential backoff.
                        int sleepTime = (1 << n) * 1000 + randomGenerator.nextInt(1001);
                        System.out.println("I'll be sleeping for " + sleepTime);
                        Thread.sleep(sleepTime);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        ///*
        try {
            MyMain m = new MyMain();
            GoogleDrive gd = new GoogleDrive();
            System.out.println(gd.GetAuthorizationLink());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //gd.setCode(br.readLine());

            m.Start(br.readLine());

        } catch (IOException ex) {
            Logger.getLogger(MyMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        //*/
        
        /*
        HashMap<String,String> aux = new HashMap<>();
        aux.put("1", "1");
        aux.put("2", "2");
        aux.put("3", "3");
        aux.put("4", "4");
        for(String value: aux.values())
            System.out.println(value);
        
        System.out.println();
        System.out.println();
        
        aux.put("3", "outro");
        for(String value: aux.values())
            System.out.println(value);
        */
    }
}
