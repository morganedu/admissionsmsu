/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.google.drive.api;

/**
 *
 * @author user
 */
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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DeprecatedGoogleDriveFunctions {

    private static final String CLIENT_ID = "892186241167-2qk5mrb6sj5ltkpv20lirkfjnpcesacn.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "eAWILdYH71N4oIhBXvvjMkRP";
    private static final String REDIRECT_URI = "http://localhost:8084/BannerUpdateService/CodeUseServlet";

    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private Drive service;

    private String codeValidation = null;
    private String authorizationUrl = null;

    public DeprecatedGoogleDriveFunctions(String code) {
        this.instantiateDependencies();
        this.codeValidation = code;
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
    
    public void listFilesByUser(String user) throws IOException{
        for (File file : this.getListFiles()) {
            if(file.getOwnerNames().contains(user)){
                System.out.println(file.getId() + " - " + file.getTitle() + " - " + file.getMimeType() + " - parent: " + file.getParents().get(0).getId());
            }
        }
    }

    public void listAllFiles() throws IOException {
        for (File file : this.getListFiles()) {
                System.out.println("All: " + file.getTitle());
        }
    }

    public void uploadFile(String pathToFile, String fileTitle, String fileDescription, String fileMimeType) {
        try {
            this.service.files().insert(
                        this.prepareFile(fileTitle, fileDescription, fileMimeType),
                        this.newFileMediaContentTemplate(pathToFile, fileMimeType)
                ).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<File> getListFiles() throws IOException{
         
        FileList fileList = service.files().list().execute();
        ArrayList<File> arrayListFiles = (ArrayList) fileList.getItems();
        return arrayListFiles;
    }

    private FileContent newFileMediaContentTemplate(String pathToFile, String fileMimeType) {
        java.io.File fileContent = new java.io.File(pathToFile);
        FileContent mediaContent = new FileContent(fileMimeType, fileContent);
        return mediaContent;
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

    private File prepareFile(String title, String description, String mimeType) {
        // Insert a file
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        return body;
    }

    public String getAuthorizationLink()
    {
        return this.authorizationUrl;
    }
    

}