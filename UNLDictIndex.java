package org.apache.nutch.LuceneSearchingScore;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.nutch.ntunlp.NTU_Medical;
//import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;

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
public class UNLDictIndex 
{
 public org.apache.lucene.document.Document indexed_doc= null;
 UNLPack cp = new UNLPack();
 public UNLDictIndex()//Constructor
 { }
 public ArrayList process(String s)
 {
 ArrayList al = new ArrayList();
 al.add(s);
 return al;
 } 
//Read the CPG.xml file and assign the value to the fields before adding to the Lucene Index 
public void UNLDict_index(String filename) throws Exception { 
 
  
//cnl.clear();
 try {
 /*
 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
 org.w3c.dom.Document doc1 = dBuilder.parse(filename);
 doc1.getDocumentElement().normalize();
 NodeList nList = doc1.getElementsByTagName("guideline");
 */
 //indexConcept(outputindex);
 TreeSet ts=new TreeSet();
//System.out.println("----------------------------+nList.getLength()); 

String str="";
String outputindex="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/UNLDict-Index";
File fs=new File(outputindex);

 Directory dir = FSDirectory.open(fs);

 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

 IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);

 iwc.setUseCompoundFile(false);

 boolean create = true;
 IndexWriter indexWriter = new IndexWriter(dir, iwc);
  long l1 = System.currentTimeMillis();


 if (create)

 {

 // Create a new index in the directory, removing any

 // previously indexed documents:

 iwc.setOpenMode(OpenMode.CREATE);

 } else {

 // Add new documents to an existing index: 

iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

 }
FileInputStream fstream = new FileInputStream(filename);
BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

while ((str = br.readLine()) != null) {
 
  
  String[] values = str.split("\\t");
  
  	cp.root=values[0];
	cp.features=values[1];
	cp.UniversalWords=values[2];
	cp.definition=values[3];
     cp.example=values[4];
     cp.postag=values[5];
     cp.entrytag=values[6];
	
 cp.Construct(cp.root,cp.features,cp.UniversalWords,cp.definition,cp.example,cp.postag,cp.entrytag);
  

 
 Object[] getDetails = DocNodeDetails();
  
  
  for (int l=0;l<getDetails.length;l++)  {


 if(ts.add(getDetails[0].toString()+getDetails[1].toString()+getDetails[2].toString()+getDetails[3].toString()+getDetails[4].toString()+getDetails[5].toString())){
 indexed_doc=new org.apache.lucene.document.Document();

 indexed_doc.add(new Field("Root", getDetails[0].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Features", getDetails[1].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("UniversalWords", getDetails[2].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Definitions", getDetails[3].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Examples", getDetails[4].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Postags", getDetails[5].toString(), Field.Store.YES, Field.Index.ANALYZED));
 indexed_doc.add(new Field("Entrytag", getDetails[6].toString(), Field.Store.YES, Field.Index.ANALYZED));

// System.out.println("Adding File"+i+getDetails[9].toString());
 indexWriter.addDocument(indexed_doc);
 }//if
 }//for loop

}//while
indexWriter.close();
long l2 = System.currentTimeMillis();

 System.out.println("Time Taken for search"+(l2 - l1));  
}catch(Exception e)
{e.printStackTrace();
}
}//cpg method index ends
 public static void main(String[] s) throws Exception {
 UNLDictIndex mi=new UNLDictIndex();
String filename1="/home/vumamaheswari/Universal-Dictionary-of-Concepts-master/data/csv/dict-nl-eng.csv";
 mi.UNLDict_index(filename1);

 }//main ends
/*public void indexConcept(String IndexDir) throws Exception { 

 }//Method ends*/

public Object[] DocNodeDetails() 
{   
 Object[] docDetail_bnode = new Object[7]; 
    docDetail_bnode[0] = UNLPack.root;
    docDetail_bnode[1] =UNLPack.features;
    docDetail_bnode[2] =UNLPack.UniversalWords;
    docDetail_bnode[3] =UNLPack.definition;
    docDetail_bnode[4] =UNLPack.example;
    docDetail_bnode[5] =UNLPack.postag;
    docDetail_bnode[6] =UNLPack.entrytag;
   
    return docDetail_bnode;
 }}//class ends/*Method to load the concept in Lucene Index***/

//This class contains the Clinical-Index Fields (11 fields Included)

class UNLPack 
{ 
  public static String root;
  public static String features;
  public static String UniversalWords;
  public static String definition;
  public static String example;
  public static String postag;
  public static String entrytag;
	
	public UNLPack()
 	{
	root="";
	features="";
	UniversalWords="";
	definition="";
     example="";
     postag="";
     entrytag="";
	
	     
	}
 
 
 public void Construct(String root1,String features1,String UniversalWords1, String definition1, String examples1,String postag1,String entrytag1)
{ 
	root=root1;
	features=features1;
	UniversalWords=UniversalWords1;
	definition=definition1;
     example=examples1;
     postag=postag1;
     entrytag=entrytag1;
 }
 
}//class ends
