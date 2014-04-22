/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.output.files;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class ReadXMLFile {
    private static String xmlPathFile;
    private File xmlFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;
    
    public ReadXMLFile(){}
    
    public ReadXMLFile(String xmlPath){
        this.BuildXMLReader(xmlPath);
    }
    
    private void BuildXMLReader(String xmlPath){
        try{
            xmlPathFile = xmlPath;
            this.xmlFile = new File(xmlPath);
            this.dbFactory = DocumentBuilderFactory.newInstance();
            this.dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(this.xmlFile);
            this.doc.getDocumentElement().normalize();
        }
        catch(ParserConfigurationException | SAXException | IOException ex){
            ex.printStackTrace();
        }
    }
    
    public Document GetXMLDocument(){
        return this.doc;
    }
    
    public NodeList GetInfoByTagName(String tagName){
        return this.doc.getElementsByTagName(tagName);
    }
    
    public String GetFirstLastNames(){
        return this.GetInfoByTagName("firstname").toString() + " " + this.GetInfoByTagName("lastname").toString();
    }
 
  public static void main(String argv[]) {
 
    try {
        ReadXMLFile readxmlfile = new ReadXMLFile("/Users/pablohpsilva/Desktop/staff.xml");
        NodeList nList = readxmlfile.GetInfoByTagName("staff");
 
	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
	for (int temp = 0; temp < nList.getLength(); temp++) {
 
		Node nNode = nList.item(temp);
 
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
			Element eElement = (Element) nNode;
 
			System.out.println("Staff id : " + eElement.getAttribute("id"));
			System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
			System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
			System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
			System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
 
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }
 
}
