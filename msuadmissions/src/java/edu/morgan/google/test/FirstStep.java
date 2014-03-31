/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.test;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
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
    ArrayList<IncompleteStudent> incompleteStudents;
    
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
    
    
    private Object splitOrJoinList(Object list){
        String lista;
        if(list.getClass().equals("String")){
            lista = (String) list;
            return lista.split("\\u000b");
        }
        else{
            for()
        }
    }
    */
    
    private void putInfo(IncompleteStudent student, String documentTitle, HashMap <IncompleteStudent,ArrayList<String>> hashmap){
        ArrayList<String> list;
        
        if(hashmap.containsKey(student))
            list = hashmap.get(student);
        else
            list = new ArrayList<>();
        
        list.add(documentTitle);
        hashmap.put(student, list);
    }
    
    private void MoveFilesToFolder(IncompleteStudent student, File studentFolder) throws IOException{
        
        System.out.println("\tMoveFilesToFolder method: Checklist:  ");
        
        // Separate checklist by string
        String[] documents = student.getChecklist().replaceAll("\\u000b", "::").split("::");
        
        // Get all files from student from GoogleDrive
        //ArrayList<File> studentFiles = this.getService().GetFileByStudentInfo(student.getLastName(), student.getFirstName(), "");
        ArrayList<File> studentFiles = this.getService().GetFileStudentInfo(student.getLastName(), student.getFirstName(), "");
        
        if(studentFiles.isEmpty())
            for(String documentTitle : documents)
                    this.putInfo(student, documentTitle, this.documentsMissing);
        else{
            // For each string from var documents, do the loop
            for(String documentTitle : documents){
                System.out.print(documentTitle + ";");
                // For each File from var studentFiles, do the loop
                for(File file : studentFiles){
                    String title = file.getTitle().replaceAll("_", " ");

                    // If the file contains the same title as the checklist, the last name and first name
                    // add that file to the documentsFound Hashmap, remove that name from var student checklist
                    // and move the file to the student folder.
                    if(title.contains(documentTitle))
                        if(!student.getLastName().equals("") && title.contains(student.getLastName()))
                            if(!student.getFirstName().equals("") && title.contains(student.getFirstName())){
                                this.getService().MoveFiles(file, studentFolder);
                                this.putInfo(student, documentTitle, documentsFound);
                                student.setChecklist(student.getChecklist().replace(documentTitle, ""));
                                student.setChecklist(student.getChecklist().replace("::", ""));
                            }
                    // Otherwise, just add it to the missing documents Hashmap
                    else
                        this.putInfo(student, documentTitle, documentsMissing);
                }
                // If I was able to find all the checklist from a user, mark ir as Complete
                if(student.getChecklist().equals(""))
                    student.setChecklist("COMPLETE");
            }
        }
    }
    
    public void executePartOne(ArrayList<IncompleteStudent> incompleteStudents){
        try{
            //Get all folders from server and use it to save time!
            //this.allFolders = this.service.getAllFolders();
            
            //Create a folder called PASSED
            File folderPassed1 = this.getService().GetFolderOrCreate("PASSED1");
            File folderPassed2 = this.getService().GetFolderOrCreate("PASSED2");
            File folderPassed3 = this.getService().GetFolderOrCreate("PASSED3");

            for(IncompleteStudent student : incompleteStudents){
                
                System.out.println("\n\nStudent: " + student.getLastName() + student.getFirstName() + student.getId());
                
                File studentFolder = this.getService().getFolderByStudentInfo(student.getLastName(), student.getFirstName(), student.getId());
                
                if(studentFolder == null){
                    studentFolder = this.getService().GetFolderOrCreate(student.getLastName() + "_" + student.getFirstName()  + "_" +  student.getId());
                    this.MoveFilesToFolder(student, studentFolder);
                    this.getService().MoveFiles(studentFolder, folderPassed3);
                }
                else{
                    this.MoveFilesToFolder(student, studentFolder);

                    if(student.getId().equals(""))
                        this.getService().MoveFiles(studentFolder, folderPassed2);
                    else
                        this.getService().MoveFiles(studentFolder, folderPassed1);
                }
                this.incompleteStudents.remove(student);
                System.out.println("Missing students: " + this.incompleteStudents.size());
            }
            System.out.println(this.documentsFound.toString());
        }catch(IOException ex){
            System.out.println("Back in executePartOne");
            ex.printStackTrace();
        }
    }
    
    public void runScript(IncompleteStudents is) throws IOException{
        System.out.println(this.getService().GetAuthorizationLink());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.service.setCode(br.readLine());
        
        ArrayList<IncompleteStudent> aux;
        
        if(this.incompleteStudents == null || this.incompleteStudents.size() == 0){
            this.incompleteStudents = (ArrayList<IncompleteStudent>)is.getStudents().clone();
            aux = (ArrayList<IncompleteStudent>)is.getStudents().clone();
        }
        else
            aux = (ArrayList<IncompleteStudent>)this.incompleteStudents.clone();
        
        this.executePartOne(aux);
    }
    
    public static void main(String args[]) throws IOException{
        IncompleteStudents is = new IncompleteStudents();
        FirstStep fs = new FirstStep();
        try{
            is.utility();
            fs.runScript(is);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
            try{
                
                fs.runScript(is);
            }catch(Exception e){
                
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
