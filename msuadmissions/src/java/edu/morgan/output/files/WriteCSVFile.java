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
public class WriteCSVFile {
    private final static String CSVFILE = "/Users/user/Desktop/outputFile1.csv";
    //private final String CSVFILE = "/Users/BABATUNDE/outputFile.csv";
    
    public static void printArray(ArrayList<PrettyStudentPrint> pspArray) throws IOException{
        String content = "Last Name, First Name, Morgan ID, Yes, No \n";
        for(PrettyStudentPrint psp: pspArray)
            content += psp.getStudentInfo() + ", " + psp.getFoundChecklist() + "\n";
        
        File file = new File(CSVFILE);
        FileOutputStream fop = new FileOutputStream(file);

        // if file doesnt exists, then create it
        if (!file.exists())
            file.createNewFile();

        // get the content in bytes
        byte[] contentInBytes = content.getBytes();

        fop.write(contentInBytes);
        fop.flush();
        fop.close();
        
        System.out.println("CVS file saved!");
    }
}
