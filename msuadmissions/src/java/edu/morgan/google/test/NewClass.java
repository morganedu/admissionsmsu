package edu.morgan.google.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */


import java.io.FileOutputStream;
/*
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
*/
public class NewClass {

    /**
     * @param args
     
    public static void main(String[] args) {

        try {
            String inFile = "/Users/user/Downloads/Jan 17 2014 (1).pdf";
            System.out.println ("Reading " + inFile);
            PdfReader reader = new PdfReader(inFile);
            int n = reader.getNumberOfPages();
            System.out.println ("Number of pages : " + n);
            int i = 0;            
            while ( i < n ) {
                String outFile = inFile.substring(0, inFile.indexOf(".pdf")) 
                    + "-" + String.format("%03d", i + 1) + ".pdf"; 
                System.out.println ("Writing " + outFile);
                Document document = new Document(reader.getPageSizeWithRotation(1));
                PdfCopy writer = new PdfCopy(document, new FileOutputStream(outFile));
                document.open();
                PdfImportedPage page = writer.getImportedPage(reader, ++i);
                writer.addPage(page);
                document.close();
                writer.close();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        
        /* example : 
            java SplitPDFFile d:\temp\x\tx.pdf
            
            Reading d:\temp\x\tx.pdf
            Number of pages : 3
            Writing d:\temp\x\tx-001.pdf
            Writing d:\temp\x\tx-002.pdf
            Writing d:\temp\x\tx-003.pdf
         */
        
 //   }

}

