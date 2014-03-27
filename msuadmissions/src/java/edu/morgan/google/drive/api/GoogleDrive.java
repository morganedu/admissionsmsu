/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.morgan.google.drive.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChildReference;
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
    private static final String REDIRECT_URI = "http://localhost:8084/xml2googledrive/xml2googledriveindex.jsp";

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private Drive service;

    private String codeValidation = null;
    private String authorizationUrl = null;

    public GoogleDrive() {
        this.instantiateDependencies();
    }
    
    /*
     * Checking of Files and Folders
     */
    
    public boolean checkFolderExistByUser(String firstName, String lastName) throws IOException{
        return this.checkFolderExistByUser(firstName + " " + lastName);
    }
    
    public boolean checkFileExistByName(String name) throws IOException{
        for (File file : this.getListFilesTemplate())
            if(file.getTitle().equals(name.trim()))
                return true;
        return false;
    }
    
    public boolean checkFolderExistByUser(String user) throws IOException{
        for (File file : this.getListFilesTemplate())
            if(file.getTitle().equals(user.trim().toUpperCase()) && file.getMimeType().equals("application/vnd.google-apps.folder"))
                return true;
        return false;
    }
    
    /*
     * Listing of Files and Folders
     */
    
    public ArrayList<File> listFilesByUser(String firstName, String lastName) throws IOException{
        return this.listFilesByUser(firstName + " " + lastName);
    }
    
    public ArrayList<File> listFilesByUser(String user) throws IOException{
        ArrayList<File> userFiles = this.getListFilesTemplate();
        for (File file : userFiles)
            if(!file.getOwnerNames().contains(user.trim()))
                userFiles.remove(file);
        return userFiles;
    }

    public ArrayList<File> listAllFiles() throws IOException {
        return this.getListFilesTemplate();
    }
    
    /*
     * Getters
     */
    
    public File getFileById(String fileId) throws IOException{
        for (File file : this.getListFilesTemplate()) {
            if(file.getId().equals(fileId))
                return file;
        }
        return null;
    }
    
    public File getFileByTitle(String fileTitle) throws IOException{
        for (File file : this.getListFilesTemplate()) {
            if(file.getTitle().equals(fileTitle))
                return file;
        }
        return null;
    }
    
    public File getFolderByName(String firstName, String lastName) throws IOException{
        String folderName = firstName.trim() + " " + lastName.trim();
        for (File folder : this.getListFilesTemplate()) {
            if(folder.getTitle().equals(folderName) && folder.getMimeType().equals("application/vnd.google-apps.folder"))
                return folder;
        }
        return null;
    }
    
    public String getAuthorizationUrl(){
        return this.authorizationUrl;
    }
    
    public String getCodeValidation(){
        return this.codeValidation;
    }
    
    // On Progress
    public String getMineType(String pathToFile){
        String[] extention = pathToFile.split(".");
        String ext = extention[extention.length-1];
        switch(ext){
            case "pdf":
                return "application/pdf";
            default:
                break;
        }
        return "";
    }
    
    /*
     * Setters
     */
    
    public void setAuthorizationUrl(String code) {
        this.authorizationUrl = code;
    }
    
    public void setCodeValidation(String code) {
        this.codeValidation = code;
    }
    
    /*
     * Creation of Files and Folders
     */
    
    public void doEverything(String firstName, String lastName, String pathToFile, String fileTitle, String fileDescription, String fileMimeType) throws IOException{
        File folder, file;
        if(!this.checkFolderExistByUser(firstName, lastName)) // If Folder doesn't exist
            folder = this.createFolder(firstName, lastName); // Create Folder
        else
            folder = this.getFolderByName(firstName, lastName); // Otherwise, Get Folder
        
        if(!this.checkFileExistByName(fileTitle)) // If File doesn't exist.
            this.uploadFile(pathToFile, fileTitle, fileDescription, fileMimeType, folder.getId());
        else{ // File exist
            file = this.getFileByTitle(fileTitle);
            if(!file.getParents().get(0).getId().equals(folder.getId())) // File isn't inside the folder
                this.moveFileIntoFolder(folder.getId(), file.getId()); // make a symbolic copy
        }
    }
    
    public File createFolderIfDoesntExist(String firstName, String lastName) throws IOException{
        if(!this.checkFolderExistByUser(firstName, lastName))
            return this.createFolder(firstName, lastName);
        return null;
    }
    
    public void moveFileIntoFolder(String folderId, String fileId) throws IOException{
        ChildReference newChild = new ChildReference();
        newChild.setId(fileId);
        this.service.children().insert(folderId, newChild).execute();
    }
    
    public File createFolder(String firstName, String lastName) throws IOException{
        File body = new File();
        body.setTitle(firstName.toUpperCase() + " " + lastName.toUpperCase());
        body.setMimeType("application/vnd.google-apps.folder");
        return this.service.files().insert(body).execute(); 
    }
    
    public File uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType) throws IOException {
        return this.service.files().insert(
                    this.prepareFile(fileTitle, fileDescription, fileMimeType),
                    this.newFileMediaContentTemplate(pathToFile, fileMimeType)
            ).execute();
    }
    
    public File uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType, String parentId) throws IOException {
        return this.service.files().insert(
                    this.prepareFile(fileTitle, fileDescription, fileMimeType, parentId),
                    this.newFileMediaContentTemplate(pathToFile, fileMimeType)
            ).execute();
    }
    
    /*
     * Use with care. Private functions
     *
     */
    private ArrayList<File> getListFilesTemplate() throws IOException{
        Drive.Files.List list = this.service.files().list();
        FileList fileList = list.execute();
        ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
        return arrayListFiles;
    }

    private FileContent newFileMediaContentTemplate(String pathToFile, String fileMimeType) {
        java.io.File fileContent = new java.io.File(pathToFile);
        FileContent mediaContent = new FileContent(fileMimeType, fileContent);
        return mediaContent;
    }

    private void buildService(String codeValidation) {
        try {
            GoogleTokenResponse response = this.flow.newTokenRequest(codeValidation).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
            this.service = new Drive.Builder(this.httpTransport, this.jsonFactory, credential).build();
            //return service;
        } catch (IOException ex) {
            ex.printStackTrace();
            //return null;
        }
    }

    private File prepareFile(String title, String description, String mimeType) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        return body;
    }
    
    private File prepareFile(String title, String description, String mimeType, String parentId) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        ParentReference newParent = new ParentReference();
        newParent.setId(parentId);
        body.setParents(Arrays.asList(newParent));
        return body;
    }
    
    private void instantiateDependencies() {
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
        this.authorizationUrl = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    /*
     * Testing functions.
     * Remove-it when the project is done.
     */
    
    public void test() throws IOException {
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + this.authorizationUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        this.codeValidation = br.readLine();
        this.buildService(this.codeValidation);

        //this.uploadFile("/Users/pablohpsilva/Desktop/document.txt", "My Document Example", "This is an example of description", "text/plain");
        //this.createFolder("fulano", "cicrano");
        this.doEverything("fulano", "cicrano", "/Users/pablohpsilva/Desktop/document.txt", "My Document Example", "This is an example of description", "text/plain");
    }

    public static void main(String args[]) {
        GoogleDrive drive = new GoogleDrive();
        try {
            drive.test();
        } catch (Exception ex) {
            System.out.print("");
        }
    }

}
