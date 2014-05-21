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
    private final static String CSVFILE = "/Users/pablohpsilva/Desktop/target/outputFile8.8.csv";
    //private final String CSVFILE = "/Users/user/Desktop/outputFile1.csv";
    
    public static void printArray(ArrayList<PrettyStudentPrint> pspArray) throws IOException{
        String content = 
                "Last Name, First Name, Morgan ID, TSTS, S05, SAT, S01, S02, IE11, IE37, IE75, " + 
                "IEW, IEX, APO, APH, AP25, APW, AUD2, AUDE, LRE2, LRE1, SSC, COBC, COMC, FC, CON, " +
                "CER, HST, CLT, UNO, TRNE, D214, RESP, ASG, TREL, AOS, ESSY, AP, BRAC, ARTP, " +
                "COMT, MAD, IEP, ECE1, MDHR, REF3, ISA, IELT, SUPP, GCEA, GCEO, CLEP, PASS, COPS, " +
                "GRR, GRRP, ETR, ESL, DEPA, DEPD, DACA, EAC, IEG, GED, BS, CPE, F1, I797, NEDP, PAC, " +
                "MIDY, MO, COOR, RESU, RSV, WES1, TOEFL, TAXP, TSE, SS, TAXP, PRC, OFEX\n";
        
        String[] tags = {"TSTS", "S05", "SAT", "S01", "S02", "IE11", "IE37", "IE75", "IEW", "IEX", "APO", "APH", "AP25", "APW", "AUD2", "AUDE", "LRE2", "LRE1", "SSC", "COBC", "COMC", "FC", "CON", "CER", "HST", "CLT", "UNO", "TRNE", "D214", "RESP", "ASG", "TREL", "AOS", "ESSY", "AP", "BRAC", "ARTP", "COMT", "MAD", "IEP", "ECE1", "MDHR", "REF3", "ISA", "IELT", "SUPP", "GCEA", "GCEO", "CLEP", "PASS", "COPS", "GRR", "GRRP", "ETR", "ESL", "DEPA", "DEPD", "DACA", "EAC", "IEG", "GED", "BS", "CPE", "F1", "I797", "NEDP", "PAC", "MIDY", "MO", "COOR", "RESU", "RSV", "WES1", "TOEFL", "TAXP", "TSE", "SS", "TAXP", "PRC", "OFEX"};
        
        for(PrettyStudentPrint psp: pspArray){
            content += psp.getStudentInfo();
            for(String tag : tags){
                if(psp.getChecklist().containsKey(tag))
                    content += ", " + psp.getChecklist().get(tag);
                else
                    content += ",";
            }
            content += "\n";
        }
        
        /*
        for(PrettyStudentPrint psp: pspArray)
            content += psp.getStudentInfo() + ", " + psp.getFoundChecklist() + "\n";
        */
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
