/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.output.files;

import edu.morgan.users.PrettyStudentPrint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pablohpsilva
 */
public class WriteCVSFile {
    public static void printArray(ArrayList<PrettyStudentPrint> pspArray) throws IOException{
        String content = "";
        for(PrettyStudentPrint psp: pspArray)
            content += psp.getStudentInfo() + ", " + psp.getFoundChecklist() + ", " +psp.getNotFoundChecklist() + "\n";
        
        File file = new File("/Users/pablohpsilva/Desktop/outputFile.csv");
        FileOutputStream fop = new FileOutputStream(file);

        // if file doesnt exists, then create it
        if (!file.exists())
            file.createNewFile();

        // get the content in bytes
        byte[] contentInBytes = content.getBytes();

        fop.write(contentInBytes);
        fop.flush();
        fop.close();
    }
}