package edu.morgan.google.test;


import edu.morgan.users.IncompleteStudent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
        if(!checklistItem.equals("")){
            aux = student.getChecklist().replace(checklistItem + "::", "");
            if(student.getChecklist().equals(aux))
                aux = student.getChecklist().replace(checklistItem, "");
        }
        else
            aux = student.getChecklist();

        student.setChecklist(aux);

        if(student.getChecklist().equals(""))
            student.setChecklist("COMPLETE");

        if(!studentsList.contains(student))
            studentsList.add(student);
        else
            studentsList.get(studentsList.indexOf(student)).setChecklist(aux);
    }
    
}
