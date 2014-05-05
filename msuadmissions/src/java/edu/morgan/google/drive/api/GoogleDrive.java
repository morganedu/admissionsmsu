/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.morgan.google.drive.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import edu.morgan.users.IncompleteStudent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleDrive {

    private static final String CLIENT_ID = "892186241167-228o9c5afo7fqrnbciabv81eghdj63f5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "BVuUu5FU8boxFTgkSMtpJwDK";
    private static String REDIRECT_URI = "http://localhost:8084/msuadmissions/MiddleServlet";
    private static String CODE_VALIDATION;
    private static String AUTHORIZATION_URI;

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private Drive service;
    private GoogleCredential credential;

    /*
        CONFIGURATION METHODS
    */
    //public GoogleDrive(String redirectUri) {
    public GoogleDrive(String codeValidation) {
        //REDIRECT_URI = redirectUri;
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_APPS_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA_READONLY))
                .setAccessType("offline")
                .setApprovalPrompt("force").build();
        AUTHORIZATION_URI = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        this.setCode(codeValidation);
    }

    public GoogleDrive() {
        //REDIRECT_URI = redirectUri;
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_APPS_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA_READONLY))
                .setAccessType("offline")
                .setApprovalPrompt("force").build();
        AUTHORIZATION_URI = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

    }
    
    
    /**
     * @return the service
     */
    public Drive getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(Drive service) {
        this.service = service;
    }
    
    public String GetAuthorizationLink() {
        return AUTHORIZATION_URI;
    }

    public String GetRedirectURI() {
        return REDIRECT_URI;
    }
    
    /*
        Public Use of this Class
    */
    
    public void setCode(String code) {
        try {
            CODE_VALIDATION = code;
            GoogleTokenResponse response = this.flow.newTokenRequest(CODE_VALIDATION).setRedirectUri(REDIRECT_URI).execute();
            String accessToken = response.getAccessToken();
            credential = new GoogleCredential().setAccessToken(accessToken);
            this.setService(new Drive.Builder(this.httpTransport, this.jsonFactory, this.credential).setApplicationName("Admissions GoogleDrive Manager").build());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /*
     *
     *  Enhanced methods! Use these! 
     *
    */
    
    public File getCreateFolder(ArrayList<File> folders, String lastName, String firstName, String studentID) throws IOException{
        //File folder = this.getStudentFolder(folders, lastName, firstName, studentID);
        //if(folder == null)
        File folder = this.makeFolder(lastName, firstName, studentID);
        return folder;
    }
    
    public File getCreateFolder(ArrayList<File> folders, String folderName) throws IOException{
        //File folder = this.getStudentFolder(folders, folderName, "", "");
        //if(folder == null)
        File folder = this.makeFolder(folderName, "", "");
        return folder;
    }

    public ArrayList<File> getAllFolders() throws IOException {
        return this.retrieveAllFiles("mimeType = 'application/vnd.google-apps.folder'");
    }
    
    public ArrayList<File> getAllFiles() throws IOException {
        return this.retrieveAllFiles("mimeType != 'application/vnd.google-apps.folder'");
    }
    
    public ArrayList<File> getStudentFiles(String lastName, String firstName, String studentID, String checklist) throws IOException {
        return this.retrieveAllFiles(this.createQueryString(new String[] {lastName, firstName, studentID, checklist}, "fullText") + "and mimeType != 'application/vnd.google-apps.folder'");
    }
    
    public ArrayList<File> getStudentFiles(String args[]) throws IOException {
        return this.retrieveAllFiles(this.createQueryString(args, "fullText") + "and mimeType != 'application/vnd.google-apps.folder'");
    }
    
    public File getStudentFolder(ArrayList<File> folders, String lastName, String firstName, String studentID){
        for(File file : folders){
            File aux = this.getOneFile(file, new String[] {lastName, firstName, studentID});
            if(aux != null)
                return aux;
        }
        return null;
    }
    
    public File MoveFiles(Object fileFrom, Object fileTo, IncompleteStudent student, String codeItem, String checklist) throws IOException {
        File file, target, copiedFile = new File();
        if (fileFrom.getClass().toString().equals("String")) {
            file = this.getService().files().get((String) fileFrom).execute();
            target = this.getService().files().get((String) fileTo).execute();
        } else {
            file = this.getService().files().get(((File) fileFrom).getId()).execute();
            target = this.getService().files().get(((File) fileTo).getId()).execute();
        }
        
        copiedFile.setTitle(this.createFileName(student, codeItem, checklist) + "AUTO");

        ParentReference newParent = new ParentReference();
        newParent.setSelfLink(target.getSelfLink());
        newParent.setParentLink(target.getParents().get(0).getSelfLink());
        newParent.setId(target.getId());
        newParent.setKind(target.getKind());
        newParent.setIsRoot(false);

        ArrayList<ParentReference> parentsList = new ArrayList<>();
        parentsList.add(newParent);
        copiedFile.setParents(parentsList);
        //file.setParents(parentsList);

        //return (File) this.getService().files().update(file.getId(), file).execute();
        return (File) this.getService().files().copy(file.getId(), copiedFile).execute();
    }
    
    public File MoveFiles(Object fileFrom, Object fileTo) throws IOException {
        File file, target = new File();
        file = this.getService().files().get(((File) fileFrom).getId()).execute();
        target = this.getService().files().get(((File) fileTo).getId()).execute();

        ParentReference newParent = new ParentReference();
        newParent.setSelfLink(target.getSelfLink());
        newParent.setParentLink(target.getParents().get(0).getSelfLink());
        newParent.setId(target.getId());
        newParent.setKind(target.getKind());
        newParent.setIsRoot(false);

        ArrayList<ParentReference> parentsList = new ArrayList<>();
        parentsList.add(newParent);
        file.setParents(parentsList);

        return (File) this.getService().files().update(file.getId(), file).execute();
    }
    
    public String createFileName(IncompleteStudent student, String codeItem, String checklist){
        String term, retorno = "";
        if(student.getTerm().toLowerCase().contains("fall")){
            if(student.getTerm().split(" ").length == 2)
                term = "FA" + student.getTerm().split(" ")[1];
            else
                term = "FA";
        } else if(student.getTerm().toLowerCase().contains("summer")){
            if(student.getTerm().split(" ").length == 2)
                term = "SU" + student.getTerm().split(" ")[1];
            else
                term = "SU";
        } else if(student.getTerm().toLowerCase().contains("spring")){
            if(student.getTerm().split(" ").length == 2)
                term = "SP" + student.getTerm().split(" ")[1];
            else
                term = "SP";
        } else{
            if(student.getTerm().split(" ").length == 2)
                term = "WN" + student.getTerm().split(" ")[1];
            else
                term = "WN";
        }
        
        if(!student.getId().equals(""))
            retorno = student.getLastName() + "_" + student.getFirstName() + "_" + student.getId() + "_" + term + "_" + codeItem + "_" + checklist + "_";
        else
            retorno = student.getLastName() + "_" + student.getFirstName() + "_" + term + "_" + codeItem + "_" + checklist + "_";
        
        return retorno;
    }
    
    /*
     *
     *  Private methods
     *
    */
    
    private File makeFolder(String LastName, String FirstName, String ID) throws IOException {
        File body = new File();
        if(LastName.equals("AUTO"))
            body.setTitle(LastName);
        else
            body.setTitle(LastName.replaceAll("'", "\\'") + "_" + FirstName.replaceAll("'", "\\'") + "_" + ID.replaceAll("'", "\\'") + "_AUTO".trim());
        body.setMimeType("application/vnd.google-apps.folder");
        return (File) this.getService().files().insert(body).execute();
    }

    private ArrayList<File> retrieveAllFiles(String queryParameters) throws IOException {
        return (ArrayList<File>) this.getService().files().list().setQ(queryParameters).execute().getItems();
    }
    
    private File getOneFile(File file, String args[]){
        if(args.length != 0)
            for (String arg : args)
                if (!arg.equals("") && !file.getTitle().contains(arg)) {
                    return null;
        }
        return file;
    }
    
    private String createQueryString(String args[], String operator) {
        String execution = "";
        if(args.length != 0)
            for(int index = 0; index < args.length; index++)
                if(!args[index].equals("")){
                    //args[index] = args[index].replaceAll("'", "\'");
                    if(index != 0)
                        execution += "and ";
                    execution += operator + " contains \"" + args[index] + "\" ";
                }
        return execution;
    }
}
