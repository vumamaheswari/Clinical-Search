<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="org.apache.lucene.search.ScoreDoc"%>
<%@page import="org.apache.lucene.search.TopDocs"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.apache.lucene.index.DirectoryReader"%>
<%@page import="org.apache.lucene.store.FSDirectory"%>
<%@page import="org.apache.lucene.store.Directory"%>
<%@page import="org.apache.lucene.index.IndexReader"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.lucene.document.Document"%>
<%@page import="org.apache.lucene.analysis.standard.StandardAnalyzer"%>
<%@page import="org.apache.lucene.queryparser.classic.QueryParser"%>
<%@page import="org.apache.lucene.search.Query"%>
<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.search.IndexSearcher"%>
<%@page 
import ="java.util.Arrays"
import ="java.util.regex.Matcher"
import ="java.util.regex.Pattern"
import ="java.io.StringReader"
import ="java.util.ArrayList"
import ="java.util.Collection"
import ="java.util.HashSet"
import ="java.util.Iterator"
import ="java.util.LinkedHashSet"
import ="java.util.LinkedList"
import ="java.util.List"
import ="java.util.Properties"
import ="java.util.StringTokenizer"
import ="java.util.Arrays"
import ="java.util.Set"
import ="java.util.TreeSet"
import ="java.io.IOException"
import ="java.io.FileInputStream"
import ="java.math.BigDecimal"
import ="java.io.ObjectInputStream"
import ="java.util.Hashtable"
import ="org.apache.lucene.analysis.TokenStream"
import ="org.apache.lucene.search.TopDocs"
import ="org.apache.lucene.document.Field"
import ="org.apache.lucene.document.TextField"

import ="org.apache.lucene.index.FieldInfo"
import ="org.apache.lucene.queryparser.classic.ParseException"


import ="org.apache.lucene.index.IndexWriterConfig.OpenMode"
import ="org.apache.lucene.index.IndexWriterConfig"
import ="org.apache.lucene.index.IndexWriter"

import ="org.apache.lucene.document.FieldType"
import ="edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation"
import ="edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation"
import ="edu.stanford.nlp.ling.CoreLabel"
import ="edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation"
import ="edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation"
import ="edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation"
import ="edu.stanford.nlp.process.TokenizerFactory"
import ="edu.stanford.nlp.parser.lexparser.LexicalizedParser"
import ="edu.stanford.nlp.pipeline.Annotation"
import ="edu.stanford.nlp.pipeline.StanfordCoreNLP"
import ="edu.stanford.nlp.process.CoreLabelTokenFactory"
import ="edu.stanford.nlp.process.PTBTokenizer"
import ="edu.stanford.nlp.process.Tokenizer"
import ="edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation"
import ="edu.stanford.nlp.trees.Dependencies"
import ="edu.stanford.nlp.trees.GrammaticalStructure"
import ="edu.stanford.nlp.trees.GrammaticalStructureFactory"
import ="edu.stanford.nlp.trees.PennTreebankLanguagePack"
import ="edu.stanford.nlp.trees.Tree"
import ="edu.stanford.nlp.trees.TreeGraphNode"
import ="edu.stanford.nlp.trees.TreebankLanguagePack"
import ="edu.stanford.nlp.trees.TypedDependency"
import ="edu.stanford.nlp.util.CoreMap"
import ="edu.stanford.nlp.ling.Sentence"
%>

<%@page import="java.util.List"%>
<HTML>
    <HEAD>
        <TITLE>Reading Guidelines</TITLE>
    </HEAD>
 <body bgcolor="#ffffe">
<div id=text></div>
<BODY>
  <%! public static TreeSet tssdrem=new TreeSet();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

<div id=text></div>
    <BODY>
        <H1>Reading Guidelines</H1>
           <BR>
           

    <head>
    <style type="text/css">
    .pg-normal {
        color: #0000FF;
        font-weight: normal;
        text-decoration: none;
        cursor: pointer;
    }
    
    .pg-selected {
        color: #800080;
        font-weight: bold;
        text-decoration: underline;
        cursor: pointer;
    }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    </head>
    
    <script type="text/javascript">
    function Pager(tableName, itemsPerPage) {
        this.tableName = tableName;
        this.itemsPerPage = itemsPerPage;
        this.currentPage = 1;
        this.pages = 0;
        this.inited = false;
        
        this.showRecords = function(from, to) {        
            var rows = document.getElementById(tableName).rows;
            // i starts from 1 to skip table header row
            for (var i = 1; i < rows.length; i++) {
                if (i < from || i > to)  
                    rows[i].style.display = 'none';
                else
                    rows[i].style.display = '';
            }
        }
        
        this.showPage = function(pageNumber) {
         if (! this.inited) {
          alert("not inited");
          return;
         }
    
            var oldPageAnchor = document.getElementById('pg'+this.currentPage);
            oldPageAnchor.className = 'pg-normal';
            
            this.currentPage = pageNumber;
            var newPageAnchor = document.getElementById('pg'+this.currentPage);
            newPageAnchor.className = 'pg-selected';
            
            var from = (pageNumber - 1) * itemsPerPage + 1;
            var to = from + itemsPerPage - 1;
            this.showRecords(from, to);
        }   
        
        this.prev = function() {
            if (this.currentPage > 1)
                this.showPage(this.currentPage - 1);
        }
        
        this.next = function() {
            if (this.currentPage < this.pages) {
                this.showPage(this.currentPage + 1);
            }
        }                        
        
        this.init = function() {
            var rows = document.getElementById(tableName).rows;
            var records = (rows.length - 1); 
            this.pages = Math.ceil(records / itemsPerPage);
            this.inited = true;
        }
    
        this.showPageNav = function(pagerName, positionId) {
         if (! this.inited) {
          alert("not inited");
          return;
         }
         var element = document.getElementById(positionId);
         
         var pagerHtml = '<span onclick="' + pagerName + '.prev();" class="pg-normal"> « Prev </span> | ';

            for (var page = 1; page <= this.pages; page++) 
		       pagerHtml += '<span id="pg' + page + '" class="pg-normal" onclick="' + pagerName + '.showPage(' + page + ');">' + page + '</span> | ';
		       pagerHtml += '<span onclick="'+pagerName+'.next();" class="pg-normal"> Next »</span>';            
            
            element.innerHTML = pagerHtml;
        }
    }
    </script>         

<script type="text/javascript" src="hilitor.js"></script>
        <%
     		
	boolean onto_check=true;
	String text="";
	String text1="";
	String text2="";
	String text3="";
	String text4="";
	String text_overall="";
		ArrayList<String> resultset=new ArrayList<String>(); //final result will be stored here
		
	 text1=request.getParameter("PastMedicalHistory");
	 text2=request.getParameter("PresentingComplaints");

	 text3=request.getParameter("DischargeInsSearch");
	 text4=request.getParameter("OrderedMedSearch");
	 	text1=text1.replaceAll("-"," ");
	 	text2=text2.replaceAll("-"," ");
	 	text3=text3.replaceAll("-"," ");
	 	text4=text4.replaceAll("-"," ");
	String tag="";
	
	if(!text1.isEmpty() && !text2.isEmpty() && !text3.isEmpty() && !text4.isEmpty())
	{
	text=text1.trim()+"PastMedicalHistory"+":"+text2.trim()+"PresentingComplaints"+":"+text3.trim()+"DischargeInsSearch"+":"+text4.trim()+"OrderedMedications";
	tag="overall";
	text_overall=text1.trim()+" "+text2.trim()+" "+text3.trim()+" "+text4.trim();
	//////////System.out.println("I am inside overall"+text);
	}else{
	
	if(!text1.isEmpty())
	{
		text=text1.replaceAll("[\\(\\)\\?\\:]","");
		text=text.replaceAll("s/p","");
		//text=text.replaceAll("S/B","");
		//text=text.replaceAll("Q/S","");
		text=text.replaceAll("a/w","");
		text=text.replaceAll("c/s","");
		text1=text.replaceAll("&lt","");
	
		tag="PastMedicalHistory";
		
	}else if(!text2.isEmpty()){

		text=text2.replaceAll("[\\(\\)\\?\\:]","");
		text=text.replaceAll("s/p","");
		//text=text.replaceAll("S/B","");
		//text=text.replaceAll("Q/S","");
		text=text.replaceAll("a/w","");
		text2=text.replaceAll("c/s","");
		//text=text.replaceAll("&lt","");
		tag="PresentingComplaints";
	}else if(!text3.isEmpty()){

		text=text3.replaceAll("[\\(\\)\\?\\:]","");
		text=text.replaceAll("s/p","");
		//text=text.replaceAll("S/B","");
		//text=text.replaceAll("Q/S","");
		text=text.replaceAll("a/w","");
		text3=text.replaceAll("c/s","");
		//text=text.replaceAll("&lt","");
		tag="DischargeInsSearch";
	}else if(!text4.isEmpty()){

		text=text4.replaceAll("[\\(\\)\\?\\:]","");
		text=text.replaceAll("s/p","");
		//text=text.replaceAll("S/B","");
		//text=text.replaceAll("Q/S","");
		text=text.replaceAll("a/w","");
		text4=text.replaceAll("c/s","");
		//text=text.replaceAll("&lt","");
		tag="OrderedMedications";
	}
	text=""+text1+""+text2+""+text3+""+text4+"";
	//////////System.out.println("I am inside specific"+text);
	}	
	//out.println("Query is"+text);
	if(!text.isEmpty())
	{

		 ArrayList result_new=new ArrayList();
		 ArrayList<Object[]> result_sort=new ArrayList<Object[]>();
		 ArrayList ranked_doc =new ArrayList();
		 ArrayList<Object[]> final_result=new ArrayList<Object[]>();
		 ArrayList ontology_con=new ArrayList();


		//seperate list to seperate the medsearch results(Ranking purpose)
		ArrayList pmh=new ArrayList();
		ArrayList pcom=new ArrayList();
		ArrayList disInst=new ArrayList();
		ArrayList omedline=new ArrayList();
	

		//Store the ontology based results seperately
		ArrayList pmh_onto=new ArrayList();
		ArrayList pcom_onto=new ArrayList();	
		ArrayList disInst_onto=new ArrayList();
	
		ArrayList omedline_onto=new ArrayList();

		//Overall results
	
		ArrayList Overall_onto=new ArrayList();
	
	
		ArrayList Overall=new ArrayList();
		String onto="";
		String q="";
		int hitsPerPage=20;
		                            


	 try {
                
                String indexDir=new File("CPG-Index/CPG-Index").getAbsolutePath(); 
			 String ontologyindex=new File("SNOMED-Index/SNOMED-Index").getAbsolutePath();
			    
                String field="fullstring";             
                if(tag.equals("overall"))
			 {
				
				StringTokenizer s_medsep=new StringTokenizer(text,":");
				while(s_medsep.hasMoreTokens())
				{
					String str1=s_medsep.nextToken();

					String str2=s_medsep.nextToken();
					String str3=s_medsep.nextToken();
					String str4=s_medsep.nextToken();
					if(str1.contains("PastMedicalHistory"))
					{
						str1.replaceAll("PastMedicalHistory","");
						text=str1.toString().trim();
						q= delSpaces(text.toString().trim());
						q=symbolRemoval(q);
						ontology_con=luceneSearch(q,ontologyindex,"SNOMED_FSN",hitsPerPage, onto_check);
						
						onto=ontology_con.toString().replaceAll("(finding)","");
						onto=onto.replaceAll("(disorder)","");
						onto=onto.replaceAll("(procedure)","");
						onto=onto.replaceAll("(situation)","");
						onto=onto.replaceAll("(regime/therapy)","");
						onto=onto.replaceAll("[\\[,\\],\\(,\\)]","");
						onto=onto.replaceAll("family","");
						onto=onto.replaceAll("history","");
				
						if(ontology_con!=null && onto_check == true &&!ontology_con.isEmpty()){
						pmh_onto=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of PMHonto is"+pmh_onto.size()+pmh_onto.toString().trim());
						}
						else
						{
						pmh=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);		
						//////////System.out.println("The size of PMH is"+pmh.size());			
						
												
						}
							
						


					}
					if(str2.contains("PresentingComplaints"))
					{
						str2.replaceAll("PresentingComplaints","");						
						text=str2.toString().trim();
						q= delSpaces(text.toString().trim());
						q=symbolRemoval(q);
	    		    	
						ontology_con=luceneSearch(q,ontologyindex,"SNOMED_FSN",hitsPerPage, onto_check);
						
						onto=ontology_con.toString().replaceAll("(finding)","");
						onto=onto.replaceAll("(disorder)","");
						onto=onto.replaceAll("(procedure)","");
						onto=onto.replaceAll("(situation)","");
						onto=onto.replaceAll("(regime/therapy)","");
						onto=onto.replaceAll("[\\[,\\],\\(,\\)]","");
						onto=onto.replaceAll("family","");
						onto=onto.replaceAll("history","");
					
				
						if(ontology_con!=null && onto_check == true&&!ontology_con.isEmpty()){
						pcom_onto=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of pcom_onto is"+pcom_onto.size()+pcom_onto.toString().trim());
						}
						else
						{
						pcom=luceneSearch(q,indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of pcom is"+pcom.size());	
						}
						
					}
					if(str3.contains("DischargeInsSearch"))
					{
						str3.replaceAll("DischargeInsSearch","");
						text=str3.toString().trim();
					     q= delSpaces(text.toString().trim());
					     q=symbolRemoval(q);
	    		    	
						ontology_con=luceneSearch(q,ontologyindex,"SNOMED_FSN",hitsPerPage, onto_check);
						
						onto=ontology_con.toString().replaceAll("(finding)","");
						onto=onto.replaceAll("(disorder)","");
						onto=onto.replaceAll("(procedure)","");
						onto=onto.replaceAll("(situation)","");
						onto=onto.replaceAll("(regime/therapy)","");
						onto=onto.replaceAll("family","");
						onto=onto.replaceAll("history","");
						onto=onto.replaceAll("[\\[,\\],\\(,\\)]","");
				
						if(ontology_con!=null && onto_check == true&&!ontology_con.isEmpty()){
						disInst_onto=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of disInst_onto is"+disInst_onto.size()+disInst_onto.toString().trim());
						}
						else
						{
						disInst=luceneSearch(q,indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of disInst is"+disInst.size());	
						}
						
					}
					if(str4.contains("OrderedMedications"))
					{
						str4.replaceAll("OrderedMedications","");
						text=str4.toString().trim();
						q= delSpaces(text.toString().trim());
						q=symbolRemoval(q);
		    		    	
						ontology_con=luceneSearch(q,ontologyindex,"SNOMED_FSN",hitsPerPage, onto_check);
					
						onto=ontology_con.toString().replaceAll("(finding)","");
						onto=onto.replaceAll("(disorder)","");
						onto=onto.replaceAll("(procedure)","");
						onto=onto.replaceAll("(situation)","");
						onto=onto.replaceAll("(regime/therapy)","");
						onto=onto.replaceAll("family","");
						onto=onto.replaceAll("history","");
						onto=onto.replaceAll("[\\[,\\],\\(,\\)]","");
			
						if(ontology_con!=null && onto_check == true&&!ontology_con.isEmpty()){
						omedline_onto=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of omedline_onto is"+omedline_onto.size()+omedline_onto.toString().trim());
						}
						else
						{
						omedline=luceneSearch(q,indexDir,field,hitsPerPage, false);
						//////////System.out.println("The size of omedline is"+omedline.size()+omedline.toString().trim());
						}
						
					}
					
				}
				result_new.addAll(pmh);	
				result_new.addAll(pmh_onto);			  
				result_new.addAll(pcom);	
				result_new.addAll(pcom_onto);		
				result_new.addAll(omedline);
				result_new.addAll(omedline_onto);
				result_new.addAll(disInst);
				result_new.addAll(disInst_onto);
				
			   }//if overall 
			   else
			   {
			    		
			    		  
			    		 
				    	 q=q.replaceAll("PastMedicalHistory"," ");
					 q=q.replaceAll("PresentingComplaints"," ");
					 q=q.replaceAll("DischargeInsSearch"," ");
					 q=q.replaceAll("OrderedMedications"," ");
					 q=delSpaces(text.toString().trim());
					 q=symbolRemoval(q);
					 ontology_con=luceneSearch(q.toLowerCase().trim(),ontologyindex,"SNOMED_FSN",hitsPerPage, true);
					////////////System.out.println("ontocons"+ontology_con.toString().trim());
					onto=ontology_con.toString().replaceAll("(finding)","");
					onto=onto.replaceAll("(disorder)","");
					onto=onto.replaceAll("(procedure)","");
					onto=onto.replaceAll("(situation)","");
					onto=onto.replaceAll("(regime/therapy)","");
					onto=onto.replaceAll("family","");
					onto=onto.replaceAll("history","");
					onto=onto.replaceAll("[\\[,\\],\\(,\\)]","");
					
						if(ontology_con!=null && onto_check == true&&!ontology_con.isEmpty()){
						Overall_onto=luceneSearch(onto.toString().trim().toLowerCase(),indexDir,field,hitsPerPage, false);
						////////////System.out.println("size of ontoresults"+Overall_onto.toString().trim());
						}
						else
						{
						Overall=luceneSearch(q.toLowerCase().trim(),indexDir,field,hitsPerPage, false);
						////////////System.out.println("size of actual"+Overall.size());
						//Overall_field2=luceneSearch(q.toLowerCase(),indexDir,field2,hitsPerPage, false);
						
						}

					result_new.addAll(Overall);
					//result_new.addAll(Overall_field2);
				
					result_new.addAll(Overall_onto);
				
			    }


      

	result_sort=sortResults(result_new, q);
	
	final_result=Freq_Sorting(result_sort);

	File currentDir = new File(".");//current dir
	String path=getServletContext().getRealPath("/FileNameToURL.ser");
	
	FileInputStream fis=new FileInputStream(new File("FileNameToURL.ser").getAbsoluteFile().getAbsolutePath());
	//File Input Stream for reading the URL 
	ObjectInputStream ois=new ObjectInputStream(fis);				
	Hashtable fileList=(Hashtable)ois.readObject();
	ois.close();//closing object stream 
	fis.close();//closing file stream
	String newoutput="";
	String url="";
	long startTime=0;
	 long endTime=0;
	startTime=System.nanoTime();
	out.println("Total Hits:"+final_result.size());
	
	   for (int h = 0; h < final_result.size(); h++) {
            Object[] resultobj= (Object[]) final_result.get(h);
		          
           //////////////System.out.println("The sorted results****"+resultobj[0]);
if(tag.equals("overall")){
 newoutput=Highlightwords(text_overall.toLowerCase(),ontology_con,(String)resultobj[1],"black");
 }else
 {
  newoutput=Highlightwords(text.toLowerCase(),ontology_con,(String)resultobj[1],"black");
 }

 url= (String)fileList.get(resultobj[7]);

		
			if(newoutput!=""){//FixMe
			if(tssdrem.add(newoutput.trim())){
			resultset.add("<p>"+"<a href = " +url+"#page="+resultobj[2]+">"+"FileName:"+url+"\t\t"+"PageNumber="+resultobj[2]+"\t\t"+"</a>"+"CATAGORY:"+resultobj[4] +" "+"TOPIC:"+ resultobj[3]+" "+"FileName:"+resultobj[7]+" "+"YEAR:"+resultobj[5]+" "+"CLASS_LEVEL:"+resultobj[6]+" "+"<font color="+"green"+">"+"GUIDLINES:"+newoutput+"</font>"+" "+" score: " + resultobj[0]+"</p>"+"</br>");}
			}
			else
			{
			if(tssdrem.add(newoutput.trim())){resultset.add("<p>"+"<a href = " +url+"#page="+resultobj[2]+">"+"FileName:"+url+"\t\t"+"PageNumber="+resultobj[2]+"\t\t"+"</a>"+"CATAGORY:"+resultobj[4] +" "+"TOPIC:"+ resultobj[3]+" "+"FileName:"+resultobj[7]+" "+"YEAR:"+resultobj[5]+" "+"CLASS_LEVEL:"+resultobj[6]+" "+"<font color="+"green"+">"+"GUIDLINES:"+(String)resultobj[1]+"</font>"+"</p>"+"</br>");}
			
			}
		
        }//for
	  
      tssdrem.clear();
     endTime=System.nanoTime(); 
     double resulttime = endTime - startTime;		
	resulttime= resulttime / (Math.pow(10, 9)); 

	
	 out.println("\t\t\t"+"Time taken:"+BigDecimal.valueOf(resulttime));
              
     } catch (Exception ee) { out.println("<b><p>Error: " + ee + "</p></b>");ee.printStackTrace();}
	
	
	}//if
	 pageContext.setAttribute("resultset", resultset);

%>
<body>    

    <table border="1" id="results">
        <tr bgcolor="orange">
            <td><strong>Guidelines</strong></td>
           
            
        </tr>
        <%
            for (int i = 0; i < resultset.size(); i++) {
        %>
        <tr>
            <%
               String gdetails = (String) resultset.get(i);
                    out.println("<td>" + gdetails+ "</td>");
                                    
            %>
        </tr>
        <%
            }
        %>
     
    </table>
    <div id="pageNavPosition"></div>
	<script type="text/javascript">
	    		  var pager = new Pager('results', 5); 
		       pager.init(); 
		       pager.showPageNav('pager', 'pageNavPosition'); 
		       pager.showPage(1);           
	</script>
	 
</body>
<%@ include file="footer.html" %>

   




<%! public ArrayList luceneSearch(String q, String indexDir, String field, int hitsPerPage, boolean ontocheck){

		//Add subtitle and topic as fields in the next improvement
	 
		
		//////////////System.out.println("getting query here"+q);
	
    		q=q.replaceAll("OrderedMedications","");
    		q=q.replaceAll("PastMedicalHistory","");
		q=q.replaceAll("PresentingComplaints","");
		q=q.replaceAll("DischargeInsSearch","");
	
	    ArrayList ontology_con=new ArrayList();
	try{
		if(ontocheck == true)	
		{
		hitsPerPage = 3;
		}
		
	    Directory index=FSDirectory.open(new File(indexDir));
    	    IndexReader reader = DirectoryReader.open(index);
    	    IndexSearcher searcher = new IndexSearcher(reader);
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
	    QueryParser parser1 = new QueryParser(Version.LUCENE_4_10_3, field, analyzer);
   
	    Query query = parser1.parse(parser1.escape(q));
	    //////////////System.out.println("Query String is"+query);
	
	    searcher.search(query, null, 100);
            
         TopDocs results = searcher.search(query, 5 * hitsPerPage);
         ScoreDoc[] hits = results.scoreDocs;
      
         
    
    	   int numTotalHits = results.totalHits;
		//////////////System.out.println("Total number of matching documents"+numTotalHits);
	   int start = 0;
	   // int end = Math.min(numTotalHits, hitsPerPage);
	 // int end = Math.min(hits.length, start + hitsPerPage);
	int end=hits.length;
     for (int i = start; i < end; i++) {
		
	Document doc = searcher.doc(hits[i].doc); 
	////////////////System.out.println(doc.get("SNOMED_FSN").toString().trim());
     

	Object hit_result[]= new Object[7];

	if(ontocheck==false)
	{
		int pagenumber = Integer.parseInt(doc.get("PageNumber")); 
	
		hit_result[0] =pagenumber;
		hit_result[1] =doc.get("Topic").toString();
		hit_result[2] =doc.get("Level").toString();
		hit_result[3] =doc.get("fullstring").toString();		
		hit_result[4] =doc.get("Year").toString();
		hit_result[5] =doc.get("classificationLevel").toString();
		hit_result[6] =doc.get("FileName").toString();   
		
		 
			if(tssdrem.add(hit_result[3].toString().trim())){
            	ontology_con.add(hit_result);
            	}
            	//////////////System.out.println("The results inside lucenesearch"+hit_result[3].toString().trim());
	}
	else
	{
		////////////////System.out.println("I am in Else");		
		//Object hit_result[] = new Object[1];
		String fsn=doc.get("SNOMED_FSN").toString().trim();
		////////////System.out.println(fsn);
		//hit_result[0] =fsn;
		
					fsn=fsn.replaceAll("(finding)","");
					fsn=fsn.replaceAll("(disorder)","");
					fsn=fsn.replaceAll("(procedure)","");
					fsn=fsn.replaceAll("(situation)","");
					fsn=fsn.replaceAll("(regime/therapy)","");
					fsn=fsn.replaceAll("[\\[,\\],\\(,\\)]","");
					fsn=fsn.replaceAll("family","");
					fsn=fsn.replaceAll("history","");
					////////////System.out.println("fsn is"+fsn+"\t"+"q is"+q+"\t"+fsn.equalsIgnoreCase(q));
				
		//if(fsn.toString().toLowerCase().trim().equalsIgnoreCase(q.trim()))
		//{
			
		//}
		//else
		//{
			//if(tssdrem.add(hit_result[3].toString().trim())){
			ontology_con.add(fsn.toString().toLowerCase());
			//}
		//}
		
		
		
	}

	
}//for

}catch(Exception e){e.printStackTrace();}

return ontology_con;

}

%>


<%!
public ArrayList sortResults(ArrayList result, String NN){
TreeSet q_ts1=new TreeSet();
      ArrayList<Object[]> result_sort=new ArrayList<Object[]>();
	for(int y=0;y<result.size();y++){
	
	 Object resultinfo[] = (Object[]) result.get(y);
		
           String fullString_match=resultinfo[3].toString().trim();
           
           Object[] common_w_score=common_words(fullString_match,NN);
           double score=Double.parseDouble(common_w_score[0].toString().trim());
	   String com_words=common_w_score[1].toString().trim();
           //////////////System.out.println("cc"+score+fullString_match+"*"+NN);

           Object obj1[] = new Object[8];
           obj1[0]=score;//rankscore
           obj1[1]=fullString_match;//fullstring
	   obj1[2]=resultinfo[0].toString().trim(); //pagenumber
	   obj1[3]=resultinfo[1].toString().trim();//topic
	   obj1[4]=resultinfo[2].toString().trim();//level
	   obj1[5]=resultinfo[4].toString().trim();//Year
	   obj1[6]=resultinfo[5].toString().trim();//classificationLevel
	   obj1[7]=resultinfo[6].toString().trim();//Filename
			
		
           if(q_ts1.add(obj1[1])){
           result_sort.add(obj1);
           }//if

	}//for
	return result_sort;
}
//method ends
%>



<%! public String stanfordparser(String query1){

	LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	String[] query=query1.split(" ");
	List<CoreLabel> rawWords = Sentence.toCoreLabelList(query);
	Tree parse = lp.apply(rawWords);
	List taggedWords = parse.taggedYield();
	////////////////System.out.println(taggedWords);
	return taggedWords.toString().trim();
}%>


<%! public static String delSpaces(String str){
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
%>


<%! public String symbolRemoval(String qline){

String[] newString = qline.split("\\.");
  StringBuilder NN=new StringBuilder();
TreeSet q_ts=new TreeSet();
                    
            
            for(int k=0;k<newString.length;k++)
            {
            	try{
                String new_token=newString[k].toString().trim();
                String newString1 = new_token.replaceAll("[^a-z A-Z]","");
                //////////////System.out.println("After symbol removal"+newString1);
      		String p1=stanfordparser(newString1);
		 p1 = p1.replaceAll("[^a-z A-Z]","");
                //String p1=parsenew(newString1);
              //////////////System.out.println("P1 is"+p1);
                StringTokenizer ts=new StringTokenizer(p1);
                while(ts.hasMoreTokens())
                {
                    String s=ts.nextToken();

			////////////System.out.println("The tokens are"+s+"\n");
                    if(s.endsWith("NNP")|s.endsWith("NNS")|s.endsWith("NN")|s.endsWith("JJ")|s.endsWith("RB")){
               s=s.replace("NNP"," ");
			s=s.replace("NNS"," ");
			s=s.replace("NN"," ");
			s=s.replace("JJ"," ");
			s=s.replace("RB"," ");
			
                        s = s.replaceAll("\\b[\\w']{1,3}\\b", "");
                        s = s.replaceAll("\\s{3,}", "");
                       
                        if(!s.isEmpty()&&q_ts.add(s)){
                            NN.append(s);
                        }//if

                 }//if
                }//while
                }catch(Exception e){e.printStackTrace();}
            }//for
   
    ////////////System.out.println("Important Clinical Terms from PMR are"+NN.toString());

    
return NN.toString().trim();
}
%>

<%! Object[] common_words(String x, String y) {
    Object[] comwords=new Object[2];
    String[] xwords = x.toLowerCase().split("[^a-z]+");
    String[] ywords = y.toLowerCase().split("[^a-z]+");
    Set<String> xset = new HashSet<String>(Arrays.asList(xwords));
    Set<String> yset = new HashSet<String>(Arrays.asList(ywords));
    xset.retainAll(yset);
    //////////////System.out.println("The common words are: " + xset);
    String score=String.valueOf(xset.size());
    String words=xset.toString().trim();	
    comwords[0]=score;
    comwords[1]=words;
    return comwords;
}
%>
<%!  public String Highlightwords(String highlight, ArrayList ontocons,String content, String color) {


	
	////System.out.println("Highlight here is"+highlight);
	highlight=highlight.replaceAll("orderedmedications"," ");
    	highlight=highlight.trim().replaceAll("pastmedicalHistory"," ");
	highlight=highlight.trim().replaceAll("presentingcomplaints"," ");
	highlight=highlight.trim().replaceAll("dischargeInsSearch"," ");
	
	String delimiters = "\\s+|,\\s*|\\.\\s*";
	String[] stopwords ={"with","of","a","an","the","in","on","from","to","as","and","upper","by","for","at","or","be","without","while","what","when","who","where"};
	List <String>list = new ArrayList<String>(Arrays.asList(stopwords)); 
	String newcontent="";
	
	 // analyzing the string 
	String[] tokensVal = highlight.split(delimiters);
	String[] tokensVal_onto=new String[ontocons.size()];
	TreeSet sb=new TreeSet();
 	   // prints the number of tokens
 		
 	if(ontocons.size()>1)
 	{
 		for(int i=0;i<ontocons.size();i++)
 	 	{
 	 		String ontostr=ontocons.get(i).toString().trim();
 	 		ontostr=ontostr.replaceAll("[\\(,\\)]","");
 	 			
 	 		tokensVal_onto[i]=ontostr.toLowerCase();
 	 		
 	 		
 	 	}//for
 

	}//if
	try{
  	if(content!=null|!content.isEmpty()){
	 for(String token : tokensVal) {
	 	
	  if(!list.contains(token)){
	  //////////System.out.println("The content is ******************"+content+token.trim());
	java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b" + token.trim() + "\\b", java.util.regex.Pattern.CASE_INSENSITIVE);
     java.util.regex.Matcher matcher = pattern.matcher(content);
	while (matcher.find()) {
        //////////System.out.println("Match found!!!");
        for (int i = 0; i <= matcher.groupCount(); i++) {
		   //////////System.out.println(matcher.group(i));
		   newcontent = matcher.replaceAll("<B>" +"<font color=green>"+matcher.group(i)+ "</font>"+ "</B>");
        }//for
    	}//while
    	}//if
    	}//for
    	
	if(tokensVal_onto!=null) {
    	for( String token_onto : tokensVal_onto) {
    			//Syste
    		 	String[] newtoken= token_onto.split("\\s+");
    			for (String jj:newtoken){
    				if(!list.contains(jj)&&newcontent!=null){
    				////////////System.out.println("The ontological concepts"+newtoken);
				java.util.regex.Pattern pattern_onto = java.util.regex.Pattern.compile("\\b" + jj + "\\b", java.util.regex.Pattern.CASE_INSENSITIVE);
     			java.util.regex.Matcher matcher_onto = pattern_onto.matcher(newcontent);
    				while (matcher_onto.find()) {
    			     for (int i = 0; i <= matcher_onto.groupCount(); i++) {
				   newcontent = matcher_onto.replaceAll("<B>" +"<font color=green>"+matcher_onto.group(i)+ "</font>"+"</B>");
        			}//for
			    	}//while
			    	}//if
			    	}//for
    		     	}//for
    	  
	}//if
	}
	//////////System.out.println("newcontent is"+newcontent.toString().trim());
	}catch(Exception e){e.printStackTrace();
	
	
	}

        return newcontent;
}//method ends
    
%>


<%!
public ArrayList<Object[]> Freq_Sorting(ArrayList al_weight) {
																																																																																																																																																																																																																												

      																																																																																																																																																																																																																																																																																																																																																																																 																																																																																																																																									 Object[] bfSort = new Object[al_weight.size()];
        double sumoffreq = 0.0;
        for (int i = 0; i < al_weight.size(); i++) {

            Object docinfo[] = (Object[]) al_weight.get(i);
            Object hit[] = new Object[8];

		      hit[0] = docinfo[0]; //common-term score
		      hit[1] = docinfo[1];//full-string
		   	 hit[2]=docinfo[2]; //pagenumber
			 hit[3]=docinfo[3];//topic
			 hit[4]=docinfo[4];//level
			 hit[5]=docinfo[5];//Year
			 hit[6]=docinfo[6];//classificationLevel
		      hit[7]=docinfo[7];//Filename
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
	 ArrayList<Object[]> rankresult_final=new ArrayList<Object[]>();
           
       List list = Arrays.asList(bfSort);
        ArrayList rankresult = new ArrayList(list);

        for (int i = 0; i < bfSort.length; i++) {
            Object o[] = (Object[]) bfSort[i];
	 
         //////////////System.out.println("The sorted resultsfreq_sorting"+o[0].toString()+o[1].toString());
	rankresult_final.add(o);
		

        }
	//return rankresult;
	 //freqSortingCnt++;
        return rankresult_final;
}


%>
</BODY>
</HTML>

