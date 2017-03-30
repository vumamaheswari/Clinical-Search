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

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.apache.nutch.LuceneSearchingScore.Parser;
import org.apache.nutch.ntunlp.*;


public class MedicalSearch implements NTU_Medical
{

public MedicalSearch(){}

public void ReadPMRXMLFile()
{
	String filename="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/InputPMR/20111004KTPH(200).xml";
	TreeSet ts=new TreeSet();
	ArrayList PMR_complaints=new ArrayList();
	try{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		org.w3c.dom.Document doc1 = dBuilder.parse(filename);
		doc1.getDocumentElement().normalize();
    
	    MPack mp = new MPack();
	    NodeList nList = doc1.getElementsByTagName("record");
	    NodeList nList_sections= doc1.getElementsByTagName("sections");
	    for (int i = 0; i < nList.getLength(); i++) 
	    { 
	    //get the elements of the PMR
			Node nNode=nList.item(i);
		    // System.out.println("Current Element :" + nNode.getNodeName());
			if("record".equals(nNode.getNodeName()))
			{
		 	if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				 Element eElement = (Element) nNode;
				 mp.VisitIDCode = eElement.getAttribute("VisitIDCode");
				 mp.division = eElement.getAttribute("division");
				 if(ts.add(mp.VisitIDCode+mp.division)){
					mp.Construct1(mp.VisitIDCode,mp.division);
				 }//if ends
			// System.out.println("Mp"+mp.VisitIDCode+"\t"+mp.division);
			}//end of if nNode

			}//if record
			for (int j = 0; j < nList_sections.getLength(); j++) 
			{ 
		    //get the elements of the PMR
			Node nNode_sections=nList_sections.item(j);
			//System.out.println("Current Element2 :" + nNode_sections.getNodeName());
	 
			if("sections".equals(nNode_sections.getNodeName()))
			{
			if (nNode_sections.getNodeType() == Node.ELEMENT_NODE)
			{
			Element eElement_sections = (Element) nNode_sections;
			NodeList fullStringNodeList = eElement_sections.getElementsByTagName("section");


			mp.PastMedicalHistory = fullStringNodeList.item(0).getChildNodes().item(0).getNodeValue();
			mp.PresentingComplaints=fullStringNodeList.item(1).getChildNodes().item(0).getNodeValue();
			mp.DischargeInstructions=fullStringNodeList.item(2).getChildNodes().item(0).getNodeValue();
			mp.OrderedMedications=fullStringNodeList.item(3).getChildNodes().item(0).getNodeValue();



			//System.out.println("PMR"+mp.PastMedicalHistory+"\t"+mp.PresentingComplaints+"\t"+mp.DischargeInstructions+"\t"+mp.OrderedMedications);
			if(ts.add(mp.PastMedicalHistory+mp.PresentingComplaints+mp.DischargeInstructions+mp.OrderedMedications))
			{
			    // ArrayList<Document> arr = new ArrayList<Document>(); 
			    mp.Construct3(mp.PastMedicalHistory,mp.PresentingComplaints,mp.DischargeInstructions,mp.OrderedMedications);
			    PMR_complaints.add(mp.PastMedicalHistory);
				 
			}
	//System.out.println("The result of search string is"+arr.toString());
	}
	}

	}

	}
	//end for nList
	// doSimpleSearch(PMR_complaints); 

	}catch(Exception e)
	{e.printStackTrace();}
}//method ends

public Object[] PMRNodeDetails() //Method to store the PMR in Array of Object
{   
    Object[] PMRNode = new Object[6]; 
    PMRNode[0] =MPack.VisitIDCode;
    PMRNode[1] =MPack.division;
    PMRNode[2] =MPack.PastMedicalHistory;
    PMRNode[3] =MPack.PresentingComplaints;
    PMRNode[4] =MPack.DischargeInstructions;
    PMRNode[5] =MPack.OrderedMedications;
    return PMRNode;
}

public static String delSpaces(String str){ //Method to remove space from the given PMR text
    StringBuilder sb = new StringBuilder(str);
    ArrayList<Integer> spaceIndexes = new ArrayList<Integer>();
    for ( int i=0; i < sb.length(); i++ ){
        if ( sb.charAt(i) == ' ' && sb.charAt(i-1) == ' '){
            spaceIndexes.add(i);
        }
    }
    for (int i = 0; i < spaceIndexes.size(); i++){
        sb.deleteCharAt(spaceIndexes.get(i)-i);
    }
    return new String(sb.toString());
}
public ArrayList process(String qline)
{

		System.out.println("Inside Process Method");
	    String indexDir="/home/vumamaheswari/medProject/apache-nutch-1.4-bin/CPG-Index";  
         String field = "fullstring";// Full String is considered here
         ArrayList<String> NN=new ArrayList<String>();
         ArrayList result=new ArrayList();
         ArrayList<Object[]> result_sort=new ArrayList<Object[]>();
	    ArrayList ranked_doc =new ArrayList();
         
         TreeSet q_ts=new TreeSet();
         Parser p=new Parser();
         MedicalSearch ms=new MedicalSearch();
	    try {
           // for(int pmn=0;pmn<2;pmn++)
            {
			//String qline=al.get(pmn).toString();
			//ring qline="child bone problem";
            
            qline=delSpaces(qline);
            String[] newString = qline.split("\\.");
            
            
            for(int k=0;k<newString.length;k++)
            {
                String new_token=newString[k].toString().trim();
                String newString1 = new_token.replaceAll("[^a-z A-Z]","");
                System.out.println("After symbol removal"+newString1);
      
                String p1=p.parsenew(newString1);
                //System.out.println("P1 is"+p1);
                StringTokenizer ts=new StringTokenizer(p1);
                while(ts.hasMoreTokens())
                {
                    String s=ts.nextToken();
                    if(s.endsWith("_NN")){
                        s=s.replace("_NN","");
                        s = s.replaceAll("\\b[\\w']{1,3}\\b", "");
                        s = s.replaceAll("\\s{3,}", " ");
                        if(!s.isEmpty()&&q_ts.add(s)){
                            NN.add(s.trim());
                        }//if
                 }//if
                }//while
            }//for
   
    System.out.println("Important Clinical Terms from PMR are"+NN.size()+NN.toString());
    // String queries = null;
    for(int nn=0;nn<NN.size();nn++){
	    String q=NN.get(nn).toString();
	    int repeat = 0;
	    boolean raw = true;
	    String queryString = null;
	    int hitsPerPage = 10;
    	    Directory index=FSDirectory.open(new File(indexDir));
    	    IndexReader reader = DirectoryReader.open(index);
    	    IndexSearcher searcher = new IndexSearcher(reader);
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
	    QueryParser parser1 = new QueryParser(Version.LUCENE_4_10_3, field, analyzer);
	    Query query = parser1.parse(q);
	    searcher.search(query, null, 100);
	 
	    TopDocs results = searcher.search(query, 5 * hitsPerPage);
	    ScoreDoc[] hits = results.scoreDocs;
    
	    int numTotalHits = results.totalHits;
	    System.out.println(numTotalHits + " total matching documents");

	    int start = 0;
	    //int end = Math.min(numTotalHits, hitsPerPage);
	    int end = Math.min(hits.length, start + hitsPerPage);

      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
	     Document doc = searcher.doc(hits[i].doc);         
          String r_obj1=doc.get("Topic").toString();
          String r_obj2=doc.get("fullstring").toString();
          result.add(r_obj2);          
          continue;
        }//if
        }//for ends

	}//for NN arraylist
     
     for(int y=0;y<result.size();y++){
           String fullString_match=result.get(y).toString();       
           int common_w_score=ms.common_words(fullString_match,NN.toString());
           double score=(double)common_w_score;
           System.out.println(common_w_score+fullString_match+"*"+NN.toString());
          
		     Object obj1[] = new Object[2];
		     obj1[0]=score;
		     obj1[1]=fullString_match;
		     if(q_ts.add(obj1[0]+""+obj1[1])){
           		result_sort.add(obj1);
           	}//if
           //map.put(common_w_score,fullString_match);
       }//for
       }//for pmn 
       ranked_doc=ms.Freq_Sorting(result_sort);
        } catch (Exception e) {
            e.printStackTrace();
        }
     System.out.println(ranked_doc);
	return ranked_doc;

}
/*Method to rank the CPGs which has more number of Common terms*/
int common_words(String x, String y) {
    String[] xwords = x.toLowerCase().split("[^a-z]+");
    String[] ywords = y.toLowerCase().split("[^a-z]+");
    Set<String> xset = new HashSet<String>(Arrays.asList(xwords));
    Set<String> yset = new HashSet<String>(Arrays.asList(ywords));
    xset.retainAll(yset);
    //System.out.println("The common words are: " + xset);
    return xset.size();
}
public ArrayList Freq_Sorting(ArrayList al_weight) {


        Object[] bfSort = new Object[al_weight.size()];
        double sumoffreq = 0.0;
        for (int i = 0; i < al_weight.size(); i++) {

            Object docinfo[] = (Object[]) al_weight.get(i);
            Object hit[] = new Object[2];

		    hit[0] = docinfo[0]; //common-term score
		    hit[1] = docinfo[1];//full-string
		  //  hit[2] = docinfo[2];
		   // hit[3] = docinfo[3];
		    //hit[4] = docinfo[4];
		    //hit[5] = docinfo[5];
              bfSort[i] = hit;
        }

        for (int i = 0; i < bfSort.length; i++) {
            Object[] doc1 = (Object[]) bfSort[i];
        
            double freq1 = (Double) doc1[0];

            for (int j = 0; j < bfSort.length; j++) {

                Object[] doc2 = (Object[]) bfSort[j];
                 double freq2 = (Double) doc2[0];

       
                        if (freq1 > freq2) {

                            Object[] o = (Object[]) bfSort[i];
                            bfSort[i] = bfSort[j];
                            bfSort[j] = o;
                        }
          
            }     //for        

        }//for
           
        List list = Arrays.asList(bfSort);
        ArrayList rankresult = new ArrayList(list);

        for (int i = 0; i < bfSort.length; i++) {
            Object o[] = (Object[]) bfSort[i];
            System.out.println("The sorted results"+o[0].toString()+o[1].toString());

        }
        //freqSortingCnt++;
        return rankresult;
}
public static void main(String a[]) throws Exception{

MedicalSearch ms=new MedicalSearch();
//ms.ReadPMRXMLFile();
//ms.doSimpleSearch();
String qline="51 yr old Chinese Female  NKDA    Admitted on 29/09/11  1. Vomiting &amp; Diarrhea x 1/7  - started with vomiting yesterday evening  - non-bloody ? billous (green)  - a/w epigastric pain  - later developed diarrhea, 6-7 episodes at home, &gt;10 episodes in ED  - non-bloody, passage in mucus  - no fever  - recent URTI 1/52 ago,still has cough    - contact hx : son developed, similar symptoms few days ago.pt developed symptoms after washing bedsheets for son  - no travel hx    O/E  Alert, Slight dry  T 37.1 BP 129/84 HR 80 SpO2 100% RA  H S1S2 L Clear A Soft NT, no rebound/guarding. Epigastric tenderness  No neurological deficit    CXR 29/09/11 : The heart size is normal. No active lung lesion is seen. There is no free air under the diaphragm.     XR KUB 29/09/11 : The renal outlines are partially obscured by bowel gas shadows. No radiopaque urinary calculus is detected. The bowel gas pattern is within normal limits.     Initial investigations  TW 11.21 Hb 14.0 Plt 322  Na 137 K 3.4 Cr 41 Urea 3.1  LFTs normal  Amylase 76 Random glucose 5.8    Issues/Progress  1. Gastroenteritis  - Started on IV hydration, symptomatic treatment, oral rehydration salts for diarrhea &amp; vomiting  - Stool Leukocyte : negative  - Stool c/s : negative    upon discharged she was well and comfortable. no fever, no more diarrhea an vomiting.";
System.out.println("Query is"+qline);
ArrayList finalresult=ms.process(qline);
}//main


}//mainclass


/*Class object that contains fields of Patient Medical Records */
class MPack 
{ 
  public static String VisitIDCode;
  public static String division ;
  public static String PastMedicalHistory;
  public static String PresentingComplaints;
  public static String DischargeInstructions;
  public static String OrderedMedications;
  
     public MPack()
     {
          VisitIDCode = "";
          division = "";
          PastMedicalHistory = "";
          PresentingComplaints = "";
          DischargeInstructions = "";
          OrderedMedications = "";
     }
 
 
 	public void Construct1(String vid,String d)
	{ 
		VisitIDCode = vid;
		division =d;    
	    
	}

 	public void Construct3(String pmh,String pc,String Di, String oM){ 
		PastMedicalHistory =pmh ;  
		PresentingComplaints = pc;
		DischargeInstructions=Di;
		OrderedMedications=oM;
 }}//subclass ends
