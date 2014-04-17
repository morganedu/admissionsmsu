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
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleDrive {

    private static final String CLIENT_ID = "892186241167-228o9c5afo7fqrnbciabv81eghdj63f5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "BVuUu5FU8boxFTgkSMtpJwDK";
    private static String REDIRECT_URI = "http://localhost:8084/msuadmissions/XML2GoogleDrive/index.jsp";
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

    public File CreateFolder(String LastName, String FirstName, String ID) throws IOException {
        return this.CreateFolder(LastName + "_" + FirstName + "_" + ID + "_ATO");
    }

    public File CreateFolder(String ownerName) {
        try {
            File body = new File();
            body.setTitle(ownerName.trim());
            body.setMimeType("application/vnd.google-apps.folder");
            return (File) this.getService().files().insert(body).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File GetFileById(String fileId) {
        try {
            File file = this.getService().files().get(fileId).execute();
            return file;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<File> ListAllFiles() {
        try {
            Drive.Files.List list = this.getService().files().list();
            FileList fileList = list.execute();
            ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
            return arrayListFiles;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File GetFolderOrCreate(String firstName, String lastName, String userID) {
        try {
            File folder = this.GetFoldersByUserInformation(firstName, lastName, userID);
            if (folder == null) {
                folder = this.CreateFolder(lastName.replaceAll("'", "\\'"), firstName.replaceAll("'", "\\'"), userID);
            }
            return folder;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public File GetFoldersByUserInformation(String firstName, String lastName, String userID) {
        try {
            return this.retrieveAllFiles(this.createQueryString(new String[] {firstName, lastName, userID}, "title") + "and mimeType = 'application/vnd.google-apps.folder'").get(0);

        } catch (Exception ex) {
            System.out.println("GetFoldersByUserInformation: " + ex.toString() + " " + ex.getLocalizedMessage() + " " + ex.getMessage());
            return null;
        }
    }

    public File GetFolderOrCreate(String firstName) {
        File folder = this.GetFoldersByUserInformation(firstName.replaceAll("'", "\\'"));
        if (folder == null) {
            folder = this.CreateFolder(firstName);
        }
        return folder;
    }

    public File GetFoldersByUserInformation(String firstName) {
        try {
            String execution = "";
            execution += "title contains '" + firstName + "' ";
            execution += "and mimeType = 'application/vnd.google-apps.folder'";

            File file = this.retrieveAllFiles(execution).get(0);
            return file;

        } catch (Exception ex) {
            System.out.println("GetFoldersByUserInformation: " + ex.toString() + ex.toString() + " " + ex.getLocalizedMessage() + " " + ex.getMessage());
            return null;
        }
    }

    public ArrayList<File> GetAllFiles() {
        try {
            return this.retrieveAllFiles("mimeType != 'application/vnd.google-apps.folder'");

        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("GetAllFiles: " + ex.toString() + ex.toString() + " " + ex.getLocalizedMessage() + " " + ex.getMessage());
            return null;
        }
    }

    public ArrayList<File> GetFilesByTitle(String fileTitle) {
        try {
            return this.retrieveAllFiles("title contains '" + fileTitle + "' and mimeType != 'application/vnd.google-apps.folder'");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public File GetFileByTitle(String fileTitle) {
        try {
            return this.retrieveAllFiles("title contains '" + fileTitle + "' and mimeType != 'application/vnd.google-apps.folder'").get(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    

    public ArrayList<File> GetFileStudentInfo(String lastName, String firstName, String fileTitle) {
        try {
            return (ArrayList) retrieveAllFiles(this.createQueryString(new String[] {firstName, lastName, fileTitle}, "fullText") + "'and mimeType != 'application/vnd.google-apps.folder'");
        } catch (IOException ex) {
            System.out.println("GetFileStudentInfo: " + ex.toString() + ex.toString() + " " + ex.getLocalizedMessage() + " " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public File getFolderByStudentInfo(String lastName, String firstName, String studentID) {
        try {
            ArrayList<File> files = (ArrayList) retrieveAllFiles("title contains '" + lastName + "' and mimeType = 'application/vnd.google-apps.folder'");
            for (File filed : files) {
                String title = filed.getTitle().replaceAll("_", " ");
                if (!firstName.equals("") && title.contains(firstName) && title.contains(studentID)) {
                    return filed;
                }
            }
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /*
     *
     *  Enhanced methods! Use these! 
     *
    */
    
    public File getCreateFolder(ArrayList<File> folders, String lastName, String firstName, String studentID) throws IOException{
        File folder = this.getStudentFolder(folders, lastName, firstName, studentID);
        if(folder == null)
            folder = this.makeFolder(lastName, firstName, studentID);
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
    
    public File MoveFiles(Object fileFrom, Object fileTo) throws IOException {
        File file, target, copiedFile = new File();
        if (fileFrom.getClass().equals("String")) {
            file = this.getService().files().get((String) fileFrom).execute();
            target = this.getService().files().get((String) fileTo).execute();
        } else {
            file = this.getService().files().get(((File) fileFrom).getId()).execute();
            target = this.getService().files().get(((File) fileTo).getId()).execute();
        }
        
        copiedFile.setTitle(file.getTitle());
        //copiedFile = (File) this.getService().files().copy(file.getId(), copiedFile).execute();

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
    
    /*
     *
     *  Private methods
     *
    */
    
    private File makeFolder(String LastName, String FirstName, String ID) throws IOException {
        File body = new File();
        body.setTitle(LastName.replaceAll("'", "\\'") + "_" + FirstName.replaceAll("'", "\\'") + "_" + ID.replaceAll("'", "\\'") + "_ATO".trim());
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
                    args[index] = args[index].replaceAll("'", "\\'");
                    if(index != 0)
                        execution += " and ";
                    execution += operator + " contains '" + args[index] + "' ";
                }
        return execution;
    }
}
