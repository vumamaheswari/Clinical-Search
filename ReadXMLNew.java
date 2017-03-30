package org.apache.nutch.LuceneSearchingScore;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;
import java.util.*;
import java.util.Map.*;

public class ReadXMLNew
{
public  static ArrayList<CPack> cnl = new ArrayList<CPack>();
public  static CPack cp = null;

public static ArrayList ReadXML(String filename)
{
try {
	
	  cnl.clear();
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc1 = dBuilder.parse(filename);
	 cp = new CPack();	
	
	doc1.getDocumentElement().normalize();

	//System.out.println("Root element :" + doc1.getDocumentElement().getNodeName());
			
	NodeList nList = doc1.getElementsByTagName("guideline");
			
	//System.out.println("----------------------------"+nList.getLength());
	for (int i = 0; i < nList.getLength(); i++) {
	//get the elements of the guidelines
	Node nNode = nList.item(i);
				
		//System.out.println("\nCurrent Element :" + nNode.getNodeName());
				
	if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			cp.guideline_Category = eElement.getAttribute("Category");
			cp.FileName = eElement.getAttribute("FileName");
			cp.Topic = eElement.getAttribute("Topic");
			cp.Year = eElement.getAttribute("Year");
			cp.gid=eElement.getAttribute("id");
				  
		

		}
		
	 NodeList sentenceList = nList.item(i).getChildNodes();
	//System.out.println("Number of Child nodes"+sentenceList.getLength());
	 for (int j = 0; j < sentenceList.getLength(); j++) {
            Node childNode = sentenceList.item(j);
            if ("session".equals(childNode.getNodeName())) {

		if(childNode.getNodeType() == Node.ELEMENT_NODE){

			Element echildElement1 = (Element) childNode;
			cp.session_PageNumber=echildElement1.getAttribute("PageNumber");
			cp.ReferencePage=echildElement1.getAttribute("ReferencePage");
			cp.SessionTitle=echildElement1.getAttribute("SessionTitle");
	
		}
		
               // System.out.println("session"+sentenceList.item(j).getTextContent()
                       // .trim());
            }//if
	    else if("classification".equals(childNode.getNodeName())) {



		if(childNode.getNodeType() == Node.ELEMENT_NODE){

			Element echildElement2 = (Element) childNode;
			cp.classificationGrade=echildElement2.getAttribute("Grade");
			cp.classificationLevel=echildElement2.getAttribute("Level");
			
			
		}
	    }
	     else if ("fullstring".equals(childNode.getNodeName())) {
			cp.fullstring = sentenceList.item(j).getTextContent().trim();
                
            }//else if
	    
		  cnl.add(cp);
		  
        }//inner for


	}//outer for loop
}catch(Exception e){e.printStackTrace();}
	return cnl;

}
//This class contains the Clinical-Index Fields (11 fields Included)
static class CPack {

    public String guideline_Category = "";
    public String FileName = "";
    public String Topic = "";
    public String Year = "";
    public String gid = "";
    public String session_PageNumber = "";
    public String SessionTitle = "";
    public String classificationGrade = "";
    public String ReferencePage = "";
    public String fullstring = "";
    public String classificationLevel = "";
 
}
	
}//class

