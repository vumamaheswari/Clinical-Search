package org.apache.nutch.LuceneSearchingScore;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.apache.nutch.LuceneSearchingScore.Parser;
import org.apache.nutch.ntunlp.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.nutch.ntunlp.NTU_Medical;
//import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.regex.*;
//import java.io.*;
import java.util.*;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Dependencies;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.Sentence;

public class UNLconversion implements NTU_Medical
{
//Read the Lucene Index-done
//store the UNL Dictionary (Publically available around 1 lakh) in Lucene - Done
//Access Id and Fullstring and filename-done
//Process the fullstring (set of sentences) take only the sentence constituents and add a sentence id-done
//Use standford parser to tag the sentences with POS-done
//convert into UNL and store them in a multilist datastructure
//Each guidelines are stored in seperate ser file that contains more than one sentences
//During indexing - Take CRCs and its associated <ID>+<Filename> similarly Cs-<GID>-<Filename>
//Final Lucene Index for CRCs and C as a seperate Index that will be mapped with the existing term based index with the guidelines ID
//During Searching - Build UNL graph for Query PMR (Patient Medical Records)- CRC, C and term based search with AND logic.
//For Query expansion with ontology similarity score between the query content with the ontology (Important) to predict exact concepts and extract PMR based fields
//Rank them semantically
public static UNLRules_en en_rules=null;
public static MList[] mlist = new MList[100000];
String encon_out = "";
//To buid DMRS use the same and store in the same structure.
//Use pydmrs - read the lucene index/XML file to retireve guidelines sentences with id and store them in dmrs structure in a XML file
//From the XML file read the DMRS structure and store them in a multilist datastructure.(As we did in UNL)
public UNLconversion(){
en_rules=new UNLRules_en();
}

 public ArrayList process(String s)
 {
 ArrayList al = new ArrayList();
 al.add(s);
 return al;
 }
public String stanfordparser(String query1){

	LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	String[] query=query1.split(" ");
	List<CoreLabel> rawWords = Sentence.toCoreLabelList(query);
	Tree parse = lp.apply(rawWords);
	List taggedWords = parse.taggedYield();
	System.out.println(taggedWords);
	return taggedWords.toString().trim();
}
public void run_UNL(String filename){
try{

	 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
	 org.w3c.dom.Document doc1 = dBuilder.parse(filename);
	 doc1.getDocumentElement().normalize();
	 NodeList nList = doc1.getElementsByTagName("guideline");
	 TreeSet ts=new TreeSet();
	 XMLguidelines cp = new XMLguidelines();
	 String indexDir="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/CPG-Index";  
	 Directory index=FSDirectory.open(new File(indexDir));
	 IndexReader reader = DirectoryReader.open(index);
	 
	 String recv_encon="";
   	  encon_out += "[d]#";
	 for (int i=0; i<reader.maxDoc(); i++) {
	 	Document doc = reader.document(i);
	     String docId = doc.get("id");
	     String fullstring=doc.get("fullstring");//contains set of sentences
          String fname=doc.get("FileName");
	     //System.out.println("docid"+"\t"+docId+"\t"+fullstring+"\t"+fname);
	    // do UNL conversion here
	    fullstring=fullstring.replaceAll("e\\.g\\.","");
	    fullstring=fullstring.replaceAll("i\\.e\\.\\,","");
	    
	    StringTokenizer strToken = new StringTokenizer(fullstring.toString().trim(), ".", false);
	    while(strToken.hasMoreTokens()) 
	    {
		    	String sent = strToken.nextToken();
		    	if(sent.length()==1)
		    	{
		    			
		    	}
		    	else
		    	{
		    		
		    		String sent_pos=stanfordparser(sent);
		    		System.out.println("Sents are"+sent_pos+"\t"+docId);
		    		recv_encon=en_rules.enconvert(sent_pos,docId);
		    		//encon_out += recv_encon;	
		    	}
		    
	    }
			encon_out += "[/d]#";		
						    
				    
	 }
	System.out.println("encon_out"+encon_out);

}catch(Exception e){e.printStackTrace();}


}//method ends

public static void main(String[] s) throws Exception {
 	UNLconversion mi=new UNLconversion();
 	String filename1="";
// String filename1="/home/vumamaheswari/apache-nutch-1.4-bin/Input/Guidelines.xml";
	String filepath="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/Medical/";


	File folder = new File(filepath);
	File[] listOfFiles = folder.listFiles();
	for (File file : listOfFiles) {
	    if (file.isFile()) {
		   filename1= file.getName();
		   System.out.println(filename1);
		   mi.run_UNL("/home/vumamaheswari/medProject/apache-nutch-1.4-bin/Medical/"+filename1);
	    }
	}



 }//main ends



}//class ends

class XMLguidelines 
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
	 public XMLguidelines()
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

