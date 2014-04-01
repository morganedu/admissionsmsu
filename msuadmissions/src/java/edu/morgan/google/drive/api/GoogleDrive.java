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
import java.util.List;

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

    //public GoogleDrive(String redirectUri) {
    public GoogleDrive(String codeValidation) {
        //REDIRECT_URI = redirectUri;
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_APPS_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA_READONLY))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
        AUTHORIZATION_URI = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        this.setCode(codeValidation);
    }

    public GoogleDrive() {
        //REDIRECT_URI = redirectUri;
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_APPS_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA_READONLY))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
        AUTHORIZATION_URI = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    public void setCode(String code) {
        try {
            CODE_VALIDATION = code;
            GoogleTokenResponse response = this.flow.newTokenRequest(CODE_VALIDATION).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
            this.setService(new Drive.Builder(this.httpTransport, this.jsonFactory, credential).build());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File CreateFolder(String LastName, String FirstName, String ID) throws IOException {
        return this.CreateFolder(LastName + " " + FirstName + " " + ID);
    }

    public File CreateFolder(String ownerName) {
        try {
            File body = new File();
            body.setTitle(ownerName.trim());
            body.setMimeType("application/vnd.google-apps.folder");
            return (File)this.getService().files().insert(body).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     * File: Document or Folder
     *
     * FileFrom can be used as a Folder OR File!
     * FileTo can only be used as Folder!!!
     *
     */
    public File MoveFiles(Object fileFrom, Object fileTo) throws IOException {
        File file, target;
        if (fileFrom.getClass().equals("String")) {
            file = this.getService().files().get((String) fileFrom).execute();
            target = this.getService().files().get((String) fileTo).execute();
        } else {
            file = this.getService().files().get(((File) fileFrom).getId()).execute();
            target = this.getService().files().get(((File) fileTo).getId()).execute();
        }

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

    public String GetAuthorizationLink() {
        return AUTHORIZATION_URI;
    }

    public String GetRedirectURI() {
        return REDIRECT_URI;
    }
    
    public File GetFolderOrCreate(String firstName, String lastName, String userID){
        try{
            File folder = this.GetFoldersByUserInformation(firstName, lastName, userID);
            if(folder == null)
                folder = this.CreateFolder(lastName.replaceAll("'", "\\'"), firstName.replaceAll("'", "\\'"), userID);
            return folder;
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public File GetFolderOrCreate(String firstName){
        File folder = this.GetFoldersByUserInformation(firstName.replaceAll("'", "\\'"));
        if(folder == null)
            folder = this.CreateFolder(firstName);
        return folder;
    }
    
    public File GetFoldersByUserInformation(String firstName){
        try{
            String execution = "";
            execution += "title contains '" + firstName + "' ";
            execution += "and mimeType = 'application/vnd.google-apps.folder'";
            
            File file = this.retrieveAllFiles(execution).get(0);
            return file;
            
        } catch(Exception ex){
            System.out.println("GetFoldersByUserInformation: "+ex.toString());
            return null;
        }
    }
    
    public ArrayList<File> GetAllFiles(){
        try{
            return this.retrieveAllFiles("mimeType != 'application/vnd.google-apps.folder'");
            
        } catch(Exception ex){
            //ex.printStackTrace();
            System.out.println("GetAllFiles: " + ex.toString());
            return null;
        }
    }
    
    private String createQueryString(String operator, String lastName, String firstName, String userID, String documentTitle){
        String execution = "";
            
            if(!documentTitle.equals(""))
                execution += operator + " contains '" + documentTitle + "' ";
            
            if(!lastName.equals("")){
                if(!documentTitle.equals(""))
                    execution += " and " + operator + " contains '" + lastName + "' ";
                else
                    execution += operator + " contains '" + lastName + "' ";
            }
            
            if(!firstName.equals("")){
                if(!lastName.equals(""))
                    execution += " and "+ operator + " contains '" + firstName + "' ";
                else
                    execution += operator + " contains '" + firstName + "' ";
            }
            
            if(!userID.equals("")){
                if(!lastName.equals("") || !firstName.equals(""))
                    execution += " and "+ operator +" contains '" + userID + "' ";
                else
                    execution += operator + " contains '" + userID + "' ";
            }
            
        return execution;
    }
    
    public File GetFoldersByUserInformation(String firstName, String lastName, String userID){
        try{
            String execution = this.createQueryString("title", firstName, lastName, userID, "");
            
            execution += "and mimeType = 'application/vnd.google-apps.folder'";
            
            File file = this.retrieveAllFiles(execution).get(0);
            return file;
            
        } catch(Exception ex){
            //ex.printStackTrace();
            System.out.println("GetFoldersByUserInformation: " + ex.toString());
            return null;
        }
    }
    
    public ArrayList<File> GetFilesByTitle(String fileTitle){
        try{
            return this.retrieveAllFiles("title contains '" + fileTitle + "' and mimeType != 'application/vnd.google-apps.folder'" );
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public File GetFileByTitle(String fileTitle){
        try{
            return this.retrieveAllFiles("title contains '" + fileTitle + "' and mimeType != 'application/vnd.google-apps.folder'" ).get(0);
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public ArrayList<File> GetFileByStudentInfo(String lastName, String firstName, String fileTitle){
        try{
            String execution = this.createQueryString("fullText", lastName.replaceAll("'", "\\'"), firstName.replaceAll("'", "\\'"), "", fileTitle.replaceAll("'", "\\'"));
            execution += " and mimeType != 'application/vnd.google-apps.folder'";
            return this.retrieveAllFiles(execution);
        } catch(IOException ex){
            System.out.println("GetFileByStudentInfo: "+ex.toString());
            return new ArrayList<>();
        }
    }
    
    public ArrayList<File> GetFileStudentInfo(String lastName, String firstName, String fileTitle){
        try{
            ArrayList<File> files = (ArrayList) retrieveAllFiles("title contains '" + lastName + "' and mimeType != 'application/vnd.google-apps.folder'");
            for (File file : files) {
                String title = file.getTitle().replaceAll("_", " ");
                if (!firstName.equals("") && !title.contains(firstName) && !title.contains(fileTitle))
                    files.remove(file);
            }
            return files;
        } catch(IOException ex){
            System.out.println("GetFileStudentInfo: " + ex.toString());
            return new ArrayList<>();
        }
    }
    
    public File getFolderByStudentInfo(String lastName, String firstName, String studentID){
        try{
            ArrayList<File> files = (ArrayList) retrieveAllFiles("title contains '" + lastName + "' and mimeType = 'application/vnd.google-apps.folder'");
            for (File filed : files) {
                String title = filed.getTitle().replaceAll("_", " ");
                if (!firstName.equals("") && title.contains(firstName) && title.contains(studentID)) {
                    return filed;
                }
            }
            return null;
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public ArrayList<File> getAllFolders(){
        try{
            return this.retrieveAllFiles("mimeType = 'application/vnd.google-apps.folder'");
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    private ArrayList<File> retrieveAllFiles(String queryParameters) throws IOException {
        ArrayList<File> result = new ArrayList<>();
        Drive.Files.List request = this.getService().files().list().setQ(queryParameters);
        result.addAll(request.execute().getItems());
        return result;
    }

    public void test() throws IOException {

        /*
         * Test 1 - create folders and move one into another
         /
         File a = this.CreateFolder("FulanoA", "Cicrano", "123Asad");
         File b = this.CreateFolder("FulanoB", "Cicrano", "123Asad");
         this.MoveFiles(a, b);
         */
        /*
         * Test 2 - get files and move them. Print files received from server.
         /
         File a = this.GetFileByTitle("FulanoA Cicrano 123Asad");
         File b = this.GetFileByTitle("FulanoB Cicrano 123Asad");
        
         System.out.println(a.toString());
         System.out.println(b.toString());
        
         this.MoveFiles(a, b);
         */
        /*
         * Test 3 - Get a folder and move files into it.
         /
         File target = this.GetFileByTitle("FULANOB CICRANO 123ASAD");
         for(File file : this.ListAllFiles())
         if(!file.getId().equals(target.getId()))
         this.MoveFiles(file, target);
         */
        /*
         * Test 4 - Title matching
         */
        
        //System.out.println(this.retrieveAllFiles("mimeType = 'application/vnd.google-apps.folder'"));
        //ArrayList<File> files = this.getAllFoldersLastName("davis", "thomas");
        //System.out.println(files.toString());
        
    }

    public static void main(String args[]) {
        GoogleDrive drive = new GoogleDrive();
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + AUTHORIZATION_URI);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            drive.setCode(br.readLine());
            drive.test();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
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

}
