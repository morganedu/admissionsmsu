/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.output.files;
 
import edu.morgan.users.PrettyStudentPrint;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class WriteXMLFile {
    
    private final static String XMLFILE = "/Users/pablohpsilva/Desktop/outputFile.xml";
    //private final static String XMLFILE = "/Users/BABATUNDE/outputFile.xml";
 
    public static void printArray(ArrayList<PrettyStudentPrint> pspArray) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element records = doc.createElement("Records");
        doc.appendChild(records);

        Element record = doc.createElement("Record");
        records.appendChild(record);

        for(PrettyStudentPrint psp : pspArray){
            Element row = doc.createElement("Record");

            Element lastname = doc.createElement("LastFirstID");
            lastname.appendChild(doc.createTextNode(psp.getStudentInfo()));
            row.appendChild(lastname);

            Element foundlist = doc.createElement("FoundList");
            foundlist.appendChild(doc.createTextNode(psp.getFoundChecklist()));
            row.appendChild(foundlist);
            /*
            Element notfoundlist = doc.createElement("NotFoundList");
            notfoundlist.appendChild(doc.createTextNode(psp.getNotFoundChecklist()));
            row.appendChild(notfoundlist);
            */
            record.appendChild(row);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(XMLFILE));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("XML file saved!");
    }
}