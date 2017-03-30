/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package org.apache.nutch.LuceneSearchingScore;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class readFromXML {

	 /* Test funcation only*/
	  public static void main(String argv[]) {
	 
		  readFromXML xml = new readFromXML();
		  
		  
	    
	  }

	  
	  
	  public List<List<String>> ReadFullString(String inputCPGFilePath)
	  {
		  List<List<String>> fullstringList = new ArrayList<List<String>> ();
		  
		  try {
				 
System.out.println("inputCPGFilePath inside readfromxml is"+inputCPGFilePath);				
File fXmlFile = new File(inputCPGFilePath);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
			 
				
				doc.getDocumentElement().normalize();
			 
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName("guideline");
			 
				System.out.println("----------------------------"+nList.getLength());
			 
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
			 
					Node nNode = nList.item(temp);
					List<String> stringList = new ArrayList<String> ();
				
					if (nNode.getNodeType() == Node.ELEMENT_NODE) 
					{
			 
						
						Element e = (Element) nNode;
						
		                NodeList fullStringNodeList = e.getElementsByTagName("fullstring");
		                NodeList classificationNodeList = e.getElementsByTagName("classification");
		                Node classificationNode = classificationNodeList.item(0);
		                Element e1 = (Element) classificationNode;
		               	try{
				
		                stringList.add(fullStringNodeList.item(0).getChildNodes().item(0).getNodeValue());
		                System.out.println(fullStringNodeList.item(0).getChildNodes().item(0).getNodeValue());
		                stringList.add(e.getAttribute("Topic"));
		                stringList.add(e.getAttribute("Category"));
		                stringList.add(e1.getAttribute("Grade"));
		                stringList.add(e1.getAttribute("Level"));
		                
		                fullstringList.add(stringList);
				}catch(Exception exe){exe.printStackTrace();}
				System.out.println("Added string list are"+stringList);
		          if(temp == 0)
		          {
		        	  System.out.println(fullStringNodeList.item(0).getChildNodes().item(0)
                              .getNodeValue());
		          }
						
			 
					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
		  System.out.println("Read Ok");
		  return fullstringList;
	  }

	  public List<String> ReadEMR(String inputEMRFilePath)
	  {
		 
		  List<String> ListEMR = new ArrayList<String> ();
		  try {
				 
				File fXmlFile = new File(inputEMRFilePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);			 
				
				doc.getDocumentElement().normalize();
			 
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName("record");
			 
				System.out.println("----------------------------");
			 
				for (int temp = 0; temp < nList.getLength(); temp++) {
			 
					Node nNode = nList.item(temp);				
					if (nNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element e = (Element) nNode;						
		                NodeList fullStringNodeList = e.getElementsByTagName("section");
		         
		                // Change the match EMR tag here
		                
		                // For EMR score with presenting complaints
		                ListEMR.add(fullStringNodeList.item(0).getChildNodes().item(0)
	                              .getNodeValue()); 
		                
		                // For EMR score with the whole EMR
		                /*
		                ListEMR.add(fullStringNodeList.item(0).getChildNodes().item(0)
	                              .getNodeValue()
	                              
	                              +" "+fullStringNodeList.item(1).getChildNodes().item(0)
	                              .getNodeValue()+ " "+fullStringNodeList.item(2).getChildNodes().item(0)
	                              .getNodeValue()); 
		                */
		                if(temp == 0)
				          {
				        	  System.out.println(fullStringNodeList.item(1).getChildNodes().item(0)
		                              .getNodeValue());
				          }
		                
					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
		  System.out.println("Read Ok");
		  System.out.println("length of EMR "+ListEMR.size());
		  return ListEMR;
	  }
	  
}
