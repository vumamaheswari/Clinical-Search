package org.apache.nutch.LuceneSearchingScore;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.nutch.ntunlp.NTU_Medical;
//import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

//import java.io.*;
import java.util.*;
//import java.util.Map.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.Version;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
public class MedicalIndex implements NTU_Medical
{
 public org.apache.lucene.document.Document indexed_doc= null;
 public int count_id=0;
 public MedicalIndex()//Constructor
 { }
 public ArrayList process(String s)
 {
 ArrayList al = new ArrayList();
 al.add(s);
 return al;
 } 
//Read the CPG.xml file and assign the value to the fields before adding to the Lucene Index 
public void CPG_index(String filename) throws Exception { 

//cnl.clear();
 try {
 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
 org.w3c.dom.Document doc1 = dBuilder.parse(filename);
 doc1.getDocumentElement().normalize();
 NodeList nList = doc1.getElementsByTagName("guideline");
TreeSet ts=new TreeSet();
//System.out.println("----------------------------+nList.getLength()); 
CPack cp = new CPack();
 String outputindex="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/CPG-Index";
//indexConcept(outputindex);
File fs=new File(outputindex);
 Directory dir = FSDirectory.open(fs);
 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
 IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
 iwc.setUseCompoundFile(false);
 boolean create = true;
 if (create)
 {
 // Create a new index in the directory, removing any
 // previously indexed documents:
 iwc.setOpenMode(OpenMode.CREATE);
 } else {
 // Add new documents to an existing index: 
iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
 }
 IndexWriter indexWriter = new IndexWriter(dir, iwc);
  long l1 = System.currentTimeMillis();
for (int i = 0; i < nList.getLength(); i++) 
{ 
//get the elements of the guidelines
 Node nNode = nList.item(i);
 //System.out.println(";Current Element :" + Node.getNodeName());

 if (nNode.getNodeType() == Node.ELEMENT_NODE)
 {
 Element eElement = (Element) nNode;
 cp.guideline_Category = eElement.getAttribute("Category");
 cp.FileName = eElement.getAttribute("FileName");
 cp.Topic = eElement.getAttribute("Topic");
 cp.Year = eElement.getAttribute("Year");
 count_id = count_id+1;
 cp.gid=eElement.getAttribute("id"); 
 cp.gid=String.valueOf(count_id);
 cp.Construct1(cp.guideline_Category,cp.FileName,cp.Topic,cp.Year,cp.gid);
// System.out.println(cp.guideline_Category+"\t"+cp.FileName+"\t"+cp.Topic+"\t"+cp.Year+"*"+cp.gid);
// cnl.add(cp);
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
 cp.Construct2(cp.session_PageNumber,cp.ReferencePage,cp.SessionTitle); 
// System.out.println(cp.session_PageNumber+"\t"+cp.ReferencePage+"\t"+cp.SessionTitle);
 //cnl.add(cp);
 } // System.out.println("session"+sentenceList.item(j).getTextContent() // .trim());
 }//if
 else if("classification".equals(childNode.getNodeName())) {
 if(childNode.getNodeType() == Node.ELEMENT_NODE){
 Element echildElement2 = (Element)childNode;
 cp.classificationGrade=echildElement2.getAttribute("Grade");
 cp.classificationLevel=echildElement2.getAttribute("Level");
cp.Construct3(cp.classificationGrade,cp.classificationLevel); //System.out.println(cp.classificationGrade+"\t"+cp.classificationLevel); // cnl.add(cp);
 } 
} else if ("fullstring".equals(childNode.getNodeName())) 
{
 cp.fullstring = sentenceList.item(j).getTextContent().trim();
cp.Construct4(cp.fullstring);
 //System.out.println(cp.fullstring); // cnl.add(cp);
 }//else if
  

 Object[] getDetails = DocNodeDetails();
  

  for (int l=0;l<getDetails.length;l++)  {



 indexed_doc=new org.apache.lucene.document.Document();
if(ts.add(getDetails[9].toString()))
{
 indexed_doc.add(new Field("Category", getDetails[0].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("FileName", getDetails[1].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Topic", getDetails[2].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Year", getDetails[3].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("id", getDetails[4].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("PageNumber", getDetails[5].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("ReferencePage", getDetails[6].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("SessionTitle", getDetails[7].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Level", getDetails[8].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("fullstring", getDetails[9].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("classificationLevel",getDetails[10].toString(), Field.Store.YES, Field.Index.ANALYZED));
// System.out.println("Adding File"+i+getDetails[9].toString());
 indexWriter.addDocument(indexed_doc);
}
 }//for loop
 //indexWriter.commit(); 

 
     //
 }//inner for
 }//outer for loop 
indexWriter.close();
long l2 = System.currentTimeMillis();

 System.out.println("Time Taken for search"+(l2 - l1));  
}catch(Exception e)
{e.printStackTrace();
}
}//cpg method index ends
public static void main(String[] s) throws Exception {
 	MedicalIndex mi=new MedicalIndex();
 	String filename1="";
// String filename1="/home/vumamaheswari/apache-nutch-1.4-bin/Input/Guidelines.xml";
	String filepath="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/Input/";


	File folder = new File(filepath);
	File[] listOfFiles = folder.listFiles();
	for (File file : listOfFiles) {
	    if (file.isFile()) {
		   filename1= file.getName();
		   System.out.println(filename1);
		   mi.CPG_index("/home/vumamaheswari/medProject/apache-nutch-1.4-bin/Input/"+filename1);
	    }
	}



 }//main ends
/*public void indexConcept(String IndexDir) throws Exception { 

 }//Method ends*/

public Object[] DocNodeDetails() 
{   
 Object[] docDetail_bnode = new Object[11]; 
    docDetail_bnode[0] = CPack.guideline_Category;
    docDetail_bnode[1] =CPack.FileName;
    docDetail_bnode[2] =CPack.Topic;
    docDetail_bnode[3] =CPack.Year;
    docDetail_bnode[4] =CPack.gid;
    docDetail_bnode[5] =CPack.session_PageNumber;
    docDetail_bnode[6] =CPack.SessionTitle;
    docDetail_bnode[7] =CPack.ReferencePage; 
    docDetail_bnode[8] =CPack.classificationGrade;
    docDetail_bnode[9] =CPack.fullstring;  
    docDetail_bnode[10]=CPack.classificationLevel;
    return docDetail_bnode;
 }}//class ends/*Method to load the concept in Lucene Index***/

//This class contains the Clinical-Index Fields (11 fields Included)

class CPack 
{ 
  public static String guideline_Category;
  public static String FileName ;
  public static String Topic;
  public static String Year;
  public static String gid;
  public static String session_PageNumber;
  public static String SessionTitle;
  public static String ReferencePage;
  public static String fullstring;
  public static String classificationGrade;
  public static String classificationLevel;
	 public CPack()
 	{
	      guideline_Category = "";
	      FileName = "";
	      Topic = "";
	      Year = "";
	      gid = "";
	      session_PageNumber = "";
	      SessionTitle = "";
	      classificationGrade = "";
	      ReferencePage = "";
	      fullstring = "";
	      classificationLevel = ""; 
	 }
 
 
 public void Construct1(String gcat,String filename,String t, String year, String id)
{ 
	 guideline_Category = gcat;
	 FileName = filename; 
	 Topic = t; 
	 Year = year;
	 gid = id;
 }
 public void Construct2(String pno, String stitle, String rp)
 {
	 session_PageNumber = pno;
	 ReferencePage = rp;
	 SessionTitle = stitle;
 }
 public void Construct3(String cg,String cl){ 
	 classificationGrade =cg ;  
	 classificationLevel = cl;
 }
public void Construct4 (String f){   
 	fullstring = f; 
}
}//class ends
