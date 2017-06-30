package org.apache.nutch.LuceneSearchingScore;
import java.lang.*;
import java.util.*;
import java.io.*;

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

public class UNLRules_en {



	
	ArrayList no;
	ArrayList tuid;
	ArrayList cuid;
	ArrayList hw;
	ArrayList root;
	ArrayList uw;
	ArrayList rootrel;
	ArrayList pos;
	ArrayList rootnew;
	ArrayList word_pos;
	ArrayList<String> UnknownList;
	
	
	int cobindex;
	int total, j = 0;

	String verbno2;
	String verbno1;

	String verb1;
	String currword;
	String rel_label;
	String u_word=" ";
	String pos_word;
	String result = "";
	String attrib = "";
	ArrayList unl_attr;
     String sni_did; 
    
     String h_word=" ";
	public UNLRules_en() {
	

	}

	public void init() {

		root = new ArrayList ();
		uw = new ArrayList ();
		no = new ArrayList();
		rootrel = new ArrayList ();
		pos = new ArrayList ();
		rootnew = new ArrayList ();
		hw = new ArrayList ();
		
		word_pos = new ArrayList ();
		
		unl_attr= new ArrayList();	
	
		
		cuid=new ArrayList();
	
		UnknownList = new ArrayList<String>();

		verbno1 = "";
		verbno2 = "";

		verb1 = "";
		u_word = "";
		pos_word = "";
		rel_label = "";
		result = "[s]#";
	}

	public String enconvert(String st,String docid) {
		try {
			////System.out.println("ST:" + st);
			init();
			String getStr = "";
			String getwrd = "";
			String getpos = "";
			String getRoot = "";
			StringTokenizer strToken1 = new StringTokenizer(st);
			StringTokenizer strToken2;
			StringTokenizer strToken3;
			int totwrds = strToken1.countTokens();
			int inlwrds = 0;
			String ID_DOC="";
			// //System.out.println("sen Size:" + totwrds);
			
			
			
			while (inlwrds < totwrds) {
				getStr = strToken1.nextToken().replaceAll("\\[","");
				getStr = getStr.replaceAll("\\]","");
				if(getStr.contains(",")||getStr.contains(".")||getStr.contains(";")||getStr.contains("$")||getStr.contains("?")||getStr.contains("\"")||getStr.contains("--"))
				{
				getStr = getStr.replace(",","");
				getStr = getStr.replace(".","");
				getStr = getStr.replace(";","");
				getStr = getStr.replace("$","");
				getStr=getStr.replace("?","");	
				getStr=getStr.replace("\"","");		
				getStr=getStr.replace("--","");			
				}
				else 
				{
				}
				StringTokenizer st_wordpos=new StringTokenizer(getStr,"/");
				while(st_wordpos.hasMoreTokens())
				{
				getwrd=st_wordpos.nextToken();
				getpos=st_wordpos.nextToken();
				
				
				
				ID_DOC=docid;
				
				getwrd=getwrd.replace("\\s","");
				root.add(getwrd.toLowerCase());
				pos.add(getpos);
				word_pos.add(getpos);
				}

			
				inlwrds++;
			}
			
			addAttributes();			
			findUW();			
			findRelnIndex();

			

		} catch (Exception e) {
		}
		return result;
	}
	public void writeintofile(String docid) {
		try {
			Writer output = null;
			File fn = new File("./resource/unl/UWUnknown/" + docid + ".txt");
			fn.createNewFile();
			output = new BufferedWriter(new FileWriter(fn, false));
			String un_result = UnknownList.toString();
			un_result = un_result.replace(",", "\n");
			un_result = un_result.replace(" ", "");
			un_result = un_result.replace("[", "");
			un_result = un_result.replace("]", "");
			output.write(un_result);
			output.close();
		} catch (Exception e) {

		}
	}
	public void addAttributes() //finding the attributes
	{
	total = root.size();
		j = 0;
		
		try {
			
			String attribute = "";
			while (j < total) {
				String rword = root.get(j).toString();
				String curr_pos = pos.get(j).toString();
				
				boolean flag=false;
				
				if (curr_pos.contains("NNP")) {
					attribute += ".@Noun";
					flag=true;
				}
				if (curr_pos.contains("NNP")&& rword.endsWith("s")) {
					attribute += ".@plural";
					flag=true;
				}
				if (curr_pos.contains("NNP")&& !rword.endsWith("s")) {
					attribute += ".@singular";
					flag=true;
				}
				
				if (curr_pos.contains("VB")|| curr_pos.contains("AUX")&&!curr_pos.contains("VBZ") && !curr_pos.contains("VBD")&& !curr_pos.contains("VBP")&& !curr_pos.contains("VBN")) {
					attribute += ".@Verb";
					flag=true;
				}
				if (curr_pos.contains("VB")&& rword.endsWith("s")) {
					attribute += ".@present";
					flag=true;
				}
				if (curr_pos.contains("VB")&& rword.endsWith("ed")) {
					attribute += ".@past";
					flag=true;
				}
			
				if ((curr_pos.contains("VB")) && (root.get(j-1).toString().contains("will")||root.get(j-1).toString().contains("shall"))) {
					attribute += ".@future";
					flag=true;
				}
				if (curr_pos.contains("ADJP")) {
					attribute += ".@Adjective";
					flag=true;
				}
				if (curr_pos.contains("ADVP")) {
					attribute += ".@Adverb";
					flag=true;
				}				
				if(curr_pos.contains("PP")||curr_pos.contains("RP")||curr_pos.contains("ADJP JJ"))
				{
					attribute += ".@"+rword;
					flag=true;
				}				
				if (rword.equals("and")) {
					attribute += ".@and";
					flag=true;
				}
				if (rword.equals("through")) {
					attribute += ".@through";
					flag=true;
				}
				if(rword.equals("via")){
					attribute += ".@via";
					flag=true;
				}
				if (rword.equals("than")) {
					attribute += ".@than";
					flag=true;
				}
				
				if(rword.equals("since"))
				{
					attribute += ".@since ";
					flag=true;
				}
				if (rword.equals("as")) {
					attribute += ".@as";
					flag=true;
				}
				if (rword.equals("after")){
					attribute += ".@after";
					flag=true;

				}
				if(rword.equals("behind"))
				{
					attribute += ".@behind";
					flag=true;
				}
				//gender
				if(rword.equals("she")||rword.equals("her")||rword.equals("woman")||rword.equals("girl")||rword.equals("Mrs")||rword.equals("Miss"))
				{
					attribute += ".@female";
					flag=true;
				}
				if(rword.equals("he")||rword.equals("his")||rword.equals("man")||rword.equals("boy")||rword.equals("him")||rword.equals("Mr"))
				{
					attribute += ".@male";
					flag=true;
				}
				if(rword.toLowerCase().equals("i")||rword.toLowerCase().equals("me")||rword.toLowerCase().equals("we")||rword.toLowerCase().equals("us"))
				{
					attribute += ".@1";
					flag=true;
				}
				if(rword.toLowerCase().equals("you"))
				{
					attribute += ".@2";
					flag=true;
				}
				if(rword.toLowerCase().equals("he")||rword.toLowerCase().equals("him")||rword.toLowerCase().equals("her")||rword.toLowerCase().equals("it")||
				rword.toLowerCase().equals("they")||rword.toLowerCase().equals("them"))
				{
					attribute += ".@3";
					flag=true;
				}
				if((rword.equals("begin")||rword.equals("began"))&& curr_pos.contains("VB"))
				{
					attribute += ".@begin";
					flag=true;
				} 
				if(flag==false)
				{
					attribute+=".@None";
					flag=true;
				}
				
				unl_attr.add(j, attribute);
				attribute = "";
				j++;
			}
			
		} catch (Exception e) {

		}
			System.out.println("unl_attr"+unl_attr);
		
	}
	
	public void findUW() {  // finding the constraint
	try{
		int count = 1;
		total = root.size();
		j = 0;
		int hitsPerPage=1;
		StringBuffer resultuw = new StringBuffer();
		Directory index=FSDirectory.open(new File("/home/vumamaheswari/medProject/apache-nutch-1.4-bin/UNLDict-Index"));
    	    IndexReader reader = DirectoryReader.open(index);
    	    IndexSearcher searcher = new IndexSearcher(reader);
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
	    QueryParser parser1 = new QueryParser(Version.LUCENE_4_10_3, "Root", analyzer);
		//System.out.println("Total"+total);
		while (j < total) {
			String rword = root.get(j).toString();
			String curr_pos = word_pos.get(j).toString();
	
		

	    Query query = parser1.parse(parser1.escape(rword.trim()));
	    searcher.search(query, null, 1);
            
         TopDocs results = searcher.search(query, 5 * hitsPerPage);
         ScoreDoc[] hits = results.scoreDocs;
    
    	     int numTotalHits = results.totalHits;
		//System.out.println("Total number of matching documents"+numTotalHits);
		int start = 0;
	   // int end = Math.min(numTotalHits, hitsPerPage);
	 // int end = Math.min(hits.length, start + hitsPerPage);
		int end=hits.length;
		for (int i = start; i < 1; i++) {
			Document doc = searcher.doc(hits[i].doc); 
			
			
			String uwpos=doc.get("Postags").toString().trim();
			uwpos=uwpos.replaceAll("\\{\\}","");
			
			String uwword=doc.get("UniversalWords").toString().trim();
			
			rootnew.add(rword);
			hw.add(doc.get("Root").toString().trim());
			uw.add(uwword);
			cuid.add(doc.get("Doc.Id").toString().trim());
			no.add(new Integer(count++));
			System.out.println(rootnew+"\t"+hw+"\t"+uw+"\t"+cuid+"\t"+no+"\n");
			
			
		}//for ends
		}//while ends
	
		for (int k = 0; k < uw.size(); k++) 
		{
			////System.out.println("inside");
			if (!(rootnew.get(k).equals("None"))) 
			{
				resultuw.append(rootnew.get(k).toString() + ';');
				resultuw.append(hw.get(k).toString() + ';');
				resultuw.append(uw.get(k).toString() + ';');
				//resultuw.append(unl_attr.get(k).toString() + ';');
				resultuw.append(pos.get(k).toString() + ';');
				resultuw.append(cuid.get(k).toString() + ';');
				resultuw.append(no.get(k).toString() + '#');
			}
		}
		//System.out.println("Resultuw:" + resultuw);
		result += "[w]#";
		result += resultuw;
		result += "[/w]#";
		System.out.println("Resultuw:" + result);
	}catch(Exception e){e.printStackTrace();}
	}

	

	
	public void rule1() {
	//	 //System.out.println("Inside Rule1");
		// rootrel.set(j, "agt");
		// int l = Integer.parseInt(verbno2);
		// //System.out.println("l:"+l);
		int k = j;
		while (k <= root.size()) {
			pos_word = pos.get(k).toString();
			// //System.out.println("FOR:" + k + ":" + l + ":" + pos_word);
			if(pos_word.contains("NN"))
			{
				// //System.out.println("pos_word:"+pos.get(k).toString());
				rel_label = "agt";
				findRelation_Category3(k + 1, rel_label);
				break;
			}
			k++;
		}
	}
// indicates a partner to have conjunctive relation to using "and"

	public void rule2() 
	{
		// //System.out.println("Inside Rule 2");
		rootrel.set(j, "and");
		rel_label = "and";
		findRelation_Category1(j, rel_label);
	}
//	(thing with attribute) indicates a thing that is in s state or has an attribute

	public void rule3() {
		////System.out.println("Inside Rule3");
		rootrel.set(j, "aoj");
		rel_label = "aoj";
		findRelation_Category3(j, rel_label);
		// //System.out.println("After Rule3");
	}
//(basis) indicates a thing used as the basis (standard) of comparison

	public void rule4() {
		// //System.out.println("Inside Rule4");
		rootrel.set(j, "bas");
		rel_label = "bas";
		 findRelation_Category1(j, rel_label);
	}

	public void rule5() {
		// //System.out.println("Inside Rule 5");
		rootrel.set(j, "ben");
		u_word = uw.get(j - 1).toString();
		if (u_word.contains("aoj>thing")) {
			rel_label = "ben";
			findRelation_Category5(j - 1, j + 1, rel_label);
		} else {
			rel_label = "ben";
			findRelation_Category4(j + 1, rel_label);
		}

	}

	public void rule6() {
		// //System.out.println("Inside Rule 6");
		rootrel.set(j, "cag");
		// pos_word = pos.get(j - 1).toString();
		// if (pos_word.equals("verb")) {
		for (int i = j; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if ((u_word.contains("person"))||(u_word.contains("place"))||(u_word.contains("vehicle"))||(u_word.contains("city"))
					||(u_word.contains("district"))||(u_word.contains("organization"))||(u_word.contains("temple"))||(u_word.contains("facilities"))
					||(u_word.contains("country"))||(u_word.contains("lake"))||(u_word.contains("name"))||(u_word.contains("relative")))
			{
				rel_label = "cag";         //co-agent
				// findRelation_Category4(j, rel_label);
			} 
			else if (u_word.equals("aoj>thing")) 
			{
				rel_label = "cao";        //co-thing with attribute
				// findRelation_Category4(j, rel_label);
			} 
			else if ((u_word.contains("icl>abstract thing"))
					|| (u_word.contains("aoj>thing,obj>thing"))) 
			{
				rel_label = "cob";      //affected co-thing
			}
			findRelation_Category4(j, rel_label);
		}
		// }
	}

	public void rule7() {
		// //System.out.println("Inside Rule 7");
		rootrel.set(j, "cao");
		rel_label = "cao";
		// findRelation_Category2(j, rel_label);
	}

	public void rule8() {
		// //System.out.println("Inside Rule 8");
		rootrel.set(j, "cnt");      //content
		rel_label = "cnt";
		// findRelation_Category2(j, rel_label);
	}

	public void rule9() {
		// //System.out.println("Inside Rule9");
		rootrel.set(j, "cob");           //affected co-thing
		rel_label = "cob";
		// findRelation_Category2(j, rel_label);
	}

	public void rule10() {
		// //System.out.println("Inside Rule10");
		rootrel.set(j, "con");  //condition
		rel_label = "con";
		// findRelation_Category2(j, rel_label);
	}

	public void rule11() {
		// //System.out.println("Inside Rule11");
		rootrel.set(j, "coo");
		rel_label = "coo";
		findRelation_Category1(j, rel_label);
	}

	public void rule12() {
		// //System.out.println("Inside Rule12");
		rootrel.set(j, "dur");
		if ((u_word.contains("event")) || (u_word.contains("period"))
				|| (u_word.contains("state"))) {
			rel_label = "dur";
			findRelation_Category4(j, rel_label);
		} else {
			rel_label = "dur";
			findRelation_Category1(j, rel_label);
		}
	}

	public void rule13() {
		// //System.out.println("Inside Rule13");
		rootrel.set(j, "equ");
		if(u_word.contains("iof>place"))
		{
			rel_label = "equ";
		}
		// findRelation_Category2(j, rel_label);
	}

	public void rule14() {
		// //System.out.println("Inside Rule14");
		rootrel.set(j, "fmt");
		String uw_word = uw.get(j - 1).toString();
		String uw_next = uw.get(j + 1).toString();
		if (uw_word.equals(uw_next)) {
			rel_label = "fmt";
			findRelation_Category1(j, rel_label);
		}

	}

	public void rule15() {
		// //System.out.println("Inside Rule15");
		rootrel.set(j, "icl");
		rel_label = "icl";
		findRelation_Category2(j, rel_label);
	}

	public void rule16() {
		// //System.out.println("Inside Rule16");
		rootrel.set(j, "ins");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("instrument")) || (u_word.contains("stationery"))
				|| (u_word.contains("cutley"))||(u_word.contains("vehicle"))) 
		{
			rel_label = "ins";
			findRelation_Category4(j + 1, rel_label);
		}
	}

	public void rule17() {
		// //System.out.println("Inside Rule17");
		rootrel.set(j, "int");
		rel_label = "int";
		// findRelation_Category2(j, rel_label);
	}

	public void rule18() {
		// //System.out.println("Inside Rule18");
		rootrel.set(j, "iof");
		rel_label = "iof";
		// findRelation_Category2(j, rel_label);
	}

	public void rule19() {// //System.out.println("Inside Rule19");
		rootrel.set(j, "man");
		rel_label = "man";
		// //System.out.println(j + 1 + " " + j + " " + rel_label);
		findRelation_Category2(j, rel_label);
	}

	public void rule20() { ////System.out.println("Inside Rule20");
		rootrel.set(j, "met");
		rel_label = "met";
		 findRelation_Category2(j, rel_label);
	}

	public void rule21() { ////System.out.println("Inside Rule21");
		rootrel.set(j, "mod");
		int i = j + 1;
		while (i < root.size()) {
			if (pos.get(i).toString().contains("NN")) {
				rel_label = "mod";
				findRelation_Category5(j, i, rel_label);
				break;
			} else if (pos.get(i).toString().contains("JJ")) {
				i++;
			} else {
				break;
			}
		}
	}


	public void rule22() { ////System.out.println("Inside Rule22");
		rootrel.set(j, "nam");
		rel_label = "nam";
		 findRelation_Category2(j, rel_label);
	}	
	
//	not yet verified
	/*	public void rule22() {
		String w1 = root.get(j + 1).toString().trim();

			if ((w1.equals("place")) || (w1.equals("date")) || (w1.equals("country"))) {
				rootrel.set(j, "nam");
				h_word = hw.get(j + 1).toString();
				
				for (int i = 0; i < uw.size(); i++) {
					u_word = uw.get(i).toString();
					
					if (u_word.indexOf(h_word) != -1) {
						rel_label = "nam";
						findRelation_Category2(j + 1, rel_label);
					}

				}
			}
	}*/

	public void rule23() { ////System.out.println("Inside Rule23");
		rootrel.set(j, "obj");
		rel_label = "obj";
		// //System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
	}

	public void rule24() {// //System.out.println("Inside Rule24");
		rootrel.set(j, "or");
		rel_label = "or";
		findRelation_Category1(j, rel_label);
	}

	public void rule25() { ////System.out.println("Inside Rule25");
		String w1=root.get(j-1).toString().trim();
		String w2=root.get(j+1).toString().trim();
		rootrel.set(j, "per");
		if((w1.contains("hour"))&&w2.equals("day")){
			rel_label = "per";
		}
		 findRelation_Category1(j, rel_label);
	}
 /*   public void rule25()
    {
    	rootrel.set(j, "per");
    	rel_label = "per";
    	 findRelation_Category1(j, rel_label);
    }*/
	public void rule26() {// //System.out.println("Inside Rule26");
		rootrel.set(j, "plc");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country")) || (u_word.contains("region"))) {
			rel_label = "plc";
			// findRelation_Category4(j + 1, rel_label);
		} else if (u_word.contains("event")) {
			rel_label = "scn";
			// findRelation_Category4(j + 1, rel_label);
		} else {
			rel_label = "opl";
			// findRelation_Category4(j + 1, rel_label);
		}
		findRelation_Category4(j + 1, rel_label);
	}

	public void rule27() { ////System.out.println("Inside Rule27");
		rootrel.set(j, "frm");
		u_word = uw.get(j + 1).toString();
		int k = j + 1;
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country")) 
				|| (u_word.contains("land"))
				||(u_word.contains("district"))
				||(u_word.contains("organizantion"))
				||(u_word.contains("temple"))
				||(u_word.contains("facilities")))
		{
			rel_label = "plf";
		} else if (u_word.contains("day")) {
			rel_label = "frm";
		} else if (u_word.contains("time")) {
			rel_label = "tmf";
		} else {
			rel_label = "src";
		}
		findRelation_Category4(k, rel_label);
	}

	public void rule28() {// //System.out.println("Inside Rule28");
		rootrel.set(j, "plt");
		int k = j + 1;
		u_word = uw.get(k).toString();
		// //System.out.println("Inside Rule 1"+u_word);
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country"))
				||(u_word.contains("district"))
				||(u_word.contains("organization"))
				||(u_word.contains("temple"))
				||(u_word.contains("facilities"))
				||(u_word.contains("lake"))
				||(u_word.contains("river"))) 
		{
			rel_label = "plt";
			// //System.out.println("Relation:"+k+":"+rel_label);
		} 
		else if ((u_word.contains("time"))
				||(u_word.contains("month"))) 
		{
			rel_label = "tmt";
		} 
		else if (u_word.contains("gol>thing")) 
		{
			rel_label = "gol";
		} 
		else 
		{
			rel_label = "to";
		}
		findRelation_Category4(k, rel_label);
	}

	public void rule29() { ////System.out.println("Inside Rule29");
		rootrel.set(j, "pof");
		rel_label = "pof";
		// findRelation_Category4(j + 1, rel_label);
	}

	public void rule30() { ////System.out.println("Inside Rule30");
	//	//System.out.println("Inside Rule30");
		rootrel.set(j, "pos");
		rel_label = "pos";
		findRelation_Category5(j - 1, j + 1, rel_label);
	//	//System.out.println("After Rule30");
	}

	public void rule31() { ////System.out.println("Inside Rule31");
		rootrel.set(j, "ptn");
		rel_label = "ptn";
		// findRelation_Category1(j, rel_label);
	}

	public void rule32() { ////System.out.println("Inside Rule32");
		rootrel.set(j, "pur");
		rel_label = "pur";
		// findRelation_Category1(j, rel_label);
	}

	public void rule33() { ////System.out.println("Inside Rule33");
		rootrel.set(j, "qua");
		rel_label = "qua";
		// //System.out.println(j + 1 + " " + j + " " + rel_label);
		findRelation_Category2(j, rel_label);
	}

	public void rule34() { ////System.out.println("Inside Rule34");
		rootrel.set(j, "rsn");
		rel_label = "rsn";
		// //System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
	}

	public void rule35() 
	{
		// //System.out.println("Inside Rule35");
		rootrel.set(j, "seq");
		String w1=root.get(j+1).toString().trim();
		if(w1.equals("then"))
		{
			rel_label = "seq";
		}
		// //System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
		findRelation_Category2(j-1, rel_label);
	}

	public void rule36() { ////System.out.println("Inside Rule36");
		rootrel.set(j, "via");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country"))
				||(u_word.contains("district"))
				||(u_word.contains("organization"))
				||(u_word.contains("temple"))
				||(u_word.contains("facilities"))
				||(u_word.contains("lake"))
				||(u_word.contains("river")))
			
		{
			
			rel_label = "via";
			findRelation_Category4(j + 1, rel_label);
		}
	}

	public void rule37() { ////System.out.println("Inside Rule37");
		rootrel.set(j, "pos");
		if (pos.get(j + 1).toString().contains("NN")) {
			rel_label = "pos";
			// //System.out.println(j + 1 + " " + j + " " + rel_label);
			findRelation_Category5(j, j + 1, rel_label);
		}
	}

	public void rule38() { ////System.out.println("Inside Rule38");
	//	//System.out.println("Inside Rule38");
		rootrel.set(j, "plc");
		// if(uw.get(j-1).toString().contains("aoj>thing")){
		rel_label = "plc";
	//	//System.out.println(j - 1 + " " + j + " " + rel_label + ":"+ uw.get(j - 1).toString() + ":" + uw.get(j + 1).toString());
		findRelation_Category5(j - 1, j + 1, rel_label);
		// }
	}

	public void WriteInToFile(String docid) 
	{
		try 
		{
			Writer FileWrite = null;
			File fn = new File("./resource/unl/UWUnknown_en/" + docid + ".txt");
			fn.createNewFile();
			FileWrite = new BufferedWriter(new FileWriter(fn, false));
			String result = UnknownList.toString();
			result = result.replace(",", "\n");
			result =result.replace(" ", "");
			result = result.replace("[", "");
			result = result.replace("]", "");
			FileWrite.write(result);
			FileWrite.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void findRelnIndex() {    //finding the appropriate rules for each word by 38 rules and relations by 5 categories
		System.out.println("inside findReinindex");
		j = 0;
		total = root.size();
		result += "[r]#";
		while (j < total) {
			try {
				rootrel.add("None");
				currword = root.get(j).toString();
				pos_word = pos.get(j).toString();
				// //System.out.println("pos_word:" + pos_word);
				u_word = uw.get(j).toString();
				// //System.out.println("currword" + currword);
				// //System.out.println("J:" + j);
				if (pos_word.contains("CC")) {
					// //System.out.println("Inside CC");
					if (currword.contains("and")) {
						// //System.out.println("Inside CC");
						////System.out.println("currword" + currword);
						rule2();
						rule35();
					} 
					else if (currword.contains("or")) {
						//System.out.println("currword" + currword);
						rule24();
					}
					// //System.out.println("Rule6:" + j + rel_label);
				}
				if(currword.equals("a"))
				{
					rule25();      //a
				}
				
				if (pos_word.contains("JJ")) {
				//	//System.out.println("currword" + currword);
					rule21();
				}
				if ((pos_word.contains("AUX"))||(pos_word.contains("VBZ"))) {
				//	//System.out.println("currword" + currword);
					// //System.out.println("Inside VBZ");
					rule3();
				}
				if (pos_word.contains("PRP")) {
				//	//System.out.println("currword" + currword);
					rule37();
				}
			//as,like
				if((currword.equals("as"))||(currword.equals("like")))
				{
					rule22();
				}
				if (pos_word.contains("IN")) {
					////System.out.println("currword" + currword);
					////System.out.println("Inside IN"+j);
					if (currword.equals("from")) {
						// //System.out.println("Inside FROM");
						rule27();
					}
					if (currword.equals("by")) {
						
					//	//System.out.println("Inside by");
						rule1();
					}
					
					if ((currword.equals("in")) || (currword.equals("on"))) {
						////System.out.println("Inside IN ON:" + j);
						rule38();
					}
					if (currword.equals("for")) {
					//	//System.out.println("Inside FOR");
						rule5();
						// rule38();
					}
					if (currword.equals("of")) {
					//	//System.out.println("Inside of:");
						//rule15();
						rule30();
					}
					if (currword.equals("with")) {
						rule6();
						rule16();
						rule20();  //with
					}
				}
				if(currword.equals("than"))
				{ 
					rule4();     //than
				}
				if (pos_word.contains("TO")) {
				//	//System.out.println("currword" + currword);
					if (currword.equals("to")) {
						// //System.out.println("Inside TO");
						rule14();
						rule28();
					}
				}
				if (pos_word.contains("CD")) {
				//	//System.out.println("currword" + currword);
					rule33();
				}
				if (currword.equals("while")) {
				//	//System.out.println("currword" + currword);
					rule11();
				}
				if ((currword.equals("during"))) {
				//	//System.out.println("currword" + currword);
					rule12();
				}

			} catch (Exception e) {
			}
			j++;
		}
		j = 0;
		result += "[/r]#[/s]#";
		// //System.out.println("RESULT:" + result);
	}

	public void findRelation_Category1(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int size = no.size();
		// if (size != (j + 1)) {
	//	//System.out.println("Inside Category1:" + j);
		if (pos.get(j - 1).toString().equals(pos.get(j + 1).toString())) {
		//	//System.out.println("Inside Category1:" + pos.get(j - 1).toString()+ ":" + pos.get(j + 1).toString());
			concept1 = no.get(j + 1).toString();
			if ((j - 1) > 0) {
				concept2 = no.get(j - 1).toString();
			} else {
				concept2 = no.get(0).toString();
			}
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		}
		////System.out.println("RESULT:" + result);
	}

	public void findRelation_Category2(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		int tsize = no.size();
		concept2 = no.get(j).toString();
		for (int i = j + 1; i < tsize; i++) {
			if ((pos.get(i).toString().contains("NN"))
					|| (pos.get(i).toString().contains("NN"))) {
				// //System.out.println("Inside Category 2:");
				concept1 = no.get(i).toString();
				break;
			}
		}
		result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		////System.out.println("Inside Category 2:"+result);
	}

	public void findRelation_Category3(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int tsize = no.size();
	//	//System.out.println("INSIDE Category 3");
		concept2 = no.get(j - 1).toString();

		if (root.get(j).toString().equals("be")) {
			concept1 = no.get(j + 1).toString();
		} else {
			concept1 = verbno1;
		}
	//	//System.out.println("concept1 & concept2:" + concept1 + ":" + concept2);
		if (!concept1.equals(concept2)) {
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			////System.out.println("result:"+result);
		}
	}

	public void findRelation_Category4(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int tsize = no.size();
		// //System.out.println("INSIDE Category 4");
		concept2 = no.get(j).toString();
		if ((verbno2 != null) && (j >= Integer.parseInt(verbno2))) {
			concept1 = verbno2;
		} else {
			concept1 = verbno1;
		}
		if (!concept1.equals(concept2)) {
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			////System.out.println("category 4"+result);
		}
	}

	public void findRelation_Category5(int j, int i, String rl) {
		String concept1 = "";
		String concept2 = "";
		concept2 = no.get(j).toString();
		concept1 = no.get(i).toString();
		result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		////System.out.println("category 5"+result);
	}



}

