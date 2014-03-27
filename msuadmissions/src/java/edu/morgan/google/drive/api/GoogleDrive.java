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
import java.util.List;

public class GoogleDrive {

    private static final String CLIENT_ID = "892186241167-228o9c5afo7fqrnbciabv81eghdj63f5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "BVuUu5FU8boxFTgkSMtpJwDK";
    private static String REDIRECT_URI;
    private static String CODE_VALIDATION;
    private static String AUTHORIZATION_URI;

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private Drive service;
    
    public GoogleDrive(String redirectUri) {
        REDIRECT_URI = redirectUri;
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                this.httpTransport, this.jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
        AUTHORIZATION_URI = this.flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }
    
    public void setCodeValidation(String code){
        try {
            CODE_VALIDATION = code;
            GoogleTokenResponse response = this.flow.newTokenRequest(CODE_VALIDATION).setRedirectUri(REDIRECT_URI).execute();
            GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
            this.service = new Drive.Builder(this.httpTransport, this.jsonFactory, credential).build();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public File CreateFolder(String LastName, String FirstName, String ID) throws IOException{
        return this.CreateFolder(LastName + " " + FirstName + " " + ID);
    }
    
    public File CreateFolder(String ownerName){
        try{
            File body = new File();
            body.setTitle(ownerName.trim().toUpperCase());
            body.setMimeType("application/vnd.google-apps.folder");
            return this.service.files().insert(body).execute(); 
        }
        catch(IOException ex){
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
    public File MoveFiles(Object fileFrom, Object fileTo) throws IOException{
        File file, target;
        if(fileFrom.getClass().equals("String")){
            file =  this.service.files().get((String) fileFrom).execute();
            target = this.service.files().get((String) fileTo).execute();
        }
        else{
            file =  this.service.files().get(((File) fileFrom).getId()).execute();
            target = this.service.files().get(((File) fileTo).getId()).execute();
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
        
        return this.service.files().update(file.getId(), file).execute();
    }
    
    public File GetFileByTitle(String title){
        try{
            Drive.Files.List list = this.service.files().list();
            FileList fileList = list.execute();
            ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
            for(File file : arrayListFiles)
                if(file.getTitle().equals(title))
                    return file;
            return null;
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public File GetFileById(String fileId){
        try{
            File file = this.service.files().get(fileId).execute();
            return file;
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public ArrayList<File> ListAllFiles(){
        try{
            Drive.Files.List list = this.service.files().list();
            FileList fileList = list.execute();
            ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
            return arrayListFiles;
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    /*
    
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
    
    public String checkFolderExistReturnIdByUser(String user) throws IOException{
        ArrayList<File> userFiles = this.getListFilesTemplate();
        
    }
    
    
    
    public ArrayList<File> listFilesInsideFolderByUser(String firstName, String lastName){
        return null;
    }
    
    public ArrayList<File> listFilesInsideFolderByUser(String ownerName) throws IOException{
        ArrayList<File> userFiles = this.getListFilesTemplate();
        String folderId;
        
        for (File file : userFiles)
            if(!file.getOwnerNames().contains(ownerName.trim()) && file.getMimeType().equals("application/vnd.google-apps.folder")){
                folderId = file.getId();
                break;
            }
        
        userFiles.remove(file);
        return userFiles;
        return null;
    }
    
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
        return this.getFolderByName(firstName + " " + lastName);
    }
    
    public File getFolderByName(String ownerName) throws IOException{
        String folderName = ownerName.trim().toUpperCase();
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
    
    
    
    public void setAuthorizationUrl(String code) {
        this.authorizationUrl = code;
    }
    
    public void setCodeValidation(String code) {
        this.codeValidation = code;
    }
    
    
    
    public void doEverything(String firstName, String lastName, String pathToFile, String fileTitle, String fileDescription, String fileMimeType) throws IOException{
        File folder, file;
        String ownerName = firstName + " " + lastName;
        if(!this.checkFolderExistByUser(ownerName)) // If Folder doesn't exist
            folder = this.createFolder(ownerName); // Create Folder
        else
            folder = this.getFolderByName(ownerName); // Otherwise, Get Folder
        
        if(!this.checkFileExistByName(fileTitle)) // If File doesn't exist.
            this.uploadFile(pathToFile, fileTitle, fileDescription, fileMimeType, folder.getId(), ownerName);
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
        return createFolder(firstName + " " + lastName); 
    }
    
    public File createFolder(String ownerName) throws IOException{
        File body = new File();
        body.setTitle(ownerName.trim().toUpperCase());
        body.setMimeType("application/vnd.google-apps.folder");
        return this.service.files().insert(body).execute(); 
    }
    
    public File uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType) throws IOException {
        return this.service.files().insert(
                    this.prepareFile(fileTitle, fileDescription, fileMimeType),
                    this.newFileMediaContentTemplate(pathToFile, fileMimeType)
            ).execute();
    }
    
    public File uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType, String parentId, String ownerName) throws IOException {
        return this.service.files().insert(
                    this.prepareFile(fileTitle, fileDescription, fileMimeType, parentId, ownerName),
                    this.newFileMediaContentTemplate(pathToFile, fileMimeType)
            ).execute();
    }
    
    
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

    private File prepareFile(String title, String description, String mimeType) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        return body;
    }
    
    private File prepareFile(String title, String description, String mimeType, String parentId, String ownerName) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        List<String> ownerList = new ArrayList<>();
        ownerList.add(ownerName);
        ParentReference newParent = new ParentReference();
        newParent.setId(parentId);
        body.setOwnerNames(ownerList);
        body.setParents(Arrays.asList(newParent));
        return body;
    }
    
    */

    public void test() throws IOException {
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + AUTHORIZATION_URI);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CODE_VALIDATION = br.readLine();
        this.setCodeValidation(CODE_VALIDATION);
        
        /*File a = this.CreateFolder("FulanoA", "Cicrano", "123Asad");
        File b = this.CreateFolder("FulanoB", "Cicrano", "123Asad");
        
        this.MoveFiles(a, b);
        */
        File target = this.GetFileByTitle("FULANOB CICRANO 123ASAD");
        
        for(File file : this.ListAllFiles())
            if(!file.getId().equals(target.getId()))
                this.MoveFiles(file, target);
    }

    public static void main(String args[]) {
        GoogleDrive drive = new GoogleDrive("http://localhost:8084/msuadmissions/XML2GoogleDrive/index.jsp");
        try {
            drive.test();
        } catch (Exception ex) {
            System.out.print("");
        }
    }

}
