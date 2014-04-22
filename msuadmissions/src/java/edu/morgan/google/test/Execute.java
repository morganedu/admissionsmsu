package edu.morgan.google.test;


import edu.morgan.google.drive.api.GoogleDrive;
import edu.morgan.users.IncompleteStudent;
import edu.morgan.users.PrettyStudentPrint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pablohpsilva
 */
public class Execute {
    public void changeChecklist(ArrayList<IncompleteStudent> studentsList, String checklistItem, IncompleteStudent student) throws IOException{
        String aux;
        aux = student.getChecklist().replace(checklistItem + "::", "");
        if(student.getChecklist().equals(aux))
            aux = student.getChecklist().replace(checklistItem, "");

        student.setChecklist(aux);

        if(student.getChecklist().equals(""))
            student.setChecklist("COMPLETE");

        if(!studentsList.contains(student))
            studentsList.add(student);
        else
            studentsList.get(studentsList.indexOf(student)).setChecklist(aux);
    }
    
    public void organizeArray(ArrayList<PrettyStudentPrint> pspArray, PrettyStudentPrint psp, String dataChanged, String token){
        if(pspArray.contains(psp)){
            if(token.equals("found"))
                pspArray.get(pspArray.indexOf(psp)).setFoundChecklist(dataChanged);
            else
                pspArray.get(pspArray.indexOf(psp)).setNotFoundChecklist(dataChanged);
        }
        else{
            if(token.equals("found"))
                psp.setFoundChecklist(dataChanged);
            else
                psp.setNotFoundChecklist(dataChanged);
            pspArray.add(psp);
        }
    }
    
}
