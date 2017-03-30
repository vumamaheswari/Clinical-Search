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
import org.apache.lucene.queryparser.classic.QueryParser;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
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
import java.awt.*; 
import java.awt.event.*; 

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.io.IOException;
import java.net.URL;


public class GUIMedical extends JFrame{
    

	JRadioButton RtoLbutton;
	JRadioButton LtoRbutton;
	JTextField query;
	JButton parserb;
	JButton blucene;
	JButton bclear;
	JButton hl;
        JTextArea ta_search;
	FlowLayout experimentLayout = new FlowLayout();
 java.net.URL helpURL = GUIMedical.class.getResource("https://www.moh.gov.sg/content/dam/moh_web/HPP/Doctors/cpg_medical/current/2014/diabetes_mellitus/cpg_Diabetes%20Mellitus%20Summary%20Card%20-%20Jul%202014.pdf");

   
  //  JButton applyButton = new JButton("Apply component orientation");
      	Parser p=new Parser();
	MedicalSearch ms_gui=new MedicalSearch();

    public GUIMedical(String name) {
        super(name);
    }
     
    public void addComponentsToPane(final Container pane) {
 	//JFrame f = new JFrame("Clinical Guidelines");
	  query=new JTextField(20);
	final ArrayList<String> NN=new ArrayList<String>();
	  final TreeSet q_ts=new TreeSet();

	  
        final JPanel compsToExperiment = new JPanel();
	      compsToExperiment.add(query);   
        compsToExperiment.setLayout(experimentLayout);
	//f.add(compsToExperiment);
        experimentLayout.setAlignment(FlowLayout.TRAILING);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
         parserb=new JButton("Apply Parser");
	blucene=new JButton("Search");
	bclear=new JButton("Clear");
	//hl=new JButton("Highlight");


	parserb.setActionCommand("Apply Parser");
	blucene.setActionCommand("Search");
	bclear.setActionCommand("Clear");
	//hl.setActionCommand("Highlight");

	compsToExperiment.add(parserb);
  	compsToExperiment.add(blucene);
	compsToExperiment.add(bclear);
	//compsToExperiment.add(hl);

	final JTextArea ta=new JTextArea("",20,20);
  	compsToExperiment.add(ta);
	
	ta.setLineWrap(true);
    	compsToExperiment.add(new JScrollPane(ta));

ta_search=new JTextArea("",100,100);
//hyperlinkListener hyperlinkListener1 = new hyperlinkListener();
ta_search.setLineWrap(true);
 
compsToExperiment.add(new JScrollPane(ta_search));
  //Left to rigcomponent orientation is selected by default
        compsToExperiment.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
         
        //Add controls to set up the component orientation in the experiment layout

//JEditorPane editorPane = new JEditorPane();
//editorPane.setEditable(false);

 // hyperlinkListener hyperlinkListener = new hyperlinkListener();
     // editorPane.addHyperlinkListener(hyperlinkListener);
//      JScrollPane scrollPane = new JScrollPane(editorPane);
   //  pane.add(scrollPane);

       pane.add(compsToExperiment, BorderLayout.CENTER);
        pane.add(controls, BorderLayout.SOUTH); ;
	

	parserb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String command = parserb.getActionCommand();
                   ta.removeAll();
		   ta_search.removeAll();
		if(command.equals("Apply Parser"))
		{	
			 String text = query.getText();
				text=ms_gui.delSpaces(text);
			 String[] newString = text.split("\\.");
            	
            
            for(int k=0;k<newString.length;k++)
            {
                String new_token=newString[k].toString().trim();
                String newString1 = new_token.replaceAll("[^\\d\\.\\,\\;\\-\\:\\(\\)\\&]","");
                //System.out.println("After symbol removal"+newString1);
      		
                String p1=p.parsenew(newString1);
                //System.out.println("P1 is"+p1);
		ta.append(p1 + "\n");
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
            			
             }// IF Parser Button Action listener ends
               

 }});
        
      blucene.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

		System.out.println("The size of NN is"+NN.size());
	
                String command1 = blucene.getActionCommand();
                ArrayList<Object[]> result=new ArrayList<Object[]>();
                //ArrayList result_sort;
                int repeat = 0;
                boolean raw = true;
                String queryString = null;
                int hitsPerPage = 10;
                ArrayList<Object[]> result_sort=new ArrayList<Object[]>();
		 try{
		if(command1.equals("Search"))
		{	
                   
		    
                    for(int nn=0;nn<NN.size();nn++){
                     String q=NN.get(nn).toString();
    
            String indexDir="/home/vumamaheswari/apache-nutch-1.4-bin/CPG-Index";
    	    Directory index=FSDirectory.open(new File(indexDir));
    	    IndexReader reader = DirectoryReader.open(index);
    	    IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
	    QueryParser parser1 = new QueryParser(Version.LUCENE_4_10_3, "fullstring", analyzer);
	    Query query = parser1.parse(q);
	    searcher.search(query, null, 100);
            TopDocs results = searcher.search(query, 5 * hitsPerPage);
            ScoreDoc[] hits = results.scoreDocs;
            int numTotalHits = results.totalHits;
	    System.out.println(numTotalHits + " total matching documents");

            int start = 0;
           // int end = Math.min(numTotalHits, hitsPerPage);
            int end = Math.min(hits.length, start + hitsPerPage);

      for (int i = start; i < end; i++) {
        if (raw) {                              
            // output raw format
            System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
            Document doc = searcher.doc(hits[i].doc);       
            String r_obj1=doc.get("Topic").toString();
            System.out.println("Topic is"+r_obj1);
            String r_obj2=doc.get("fullstring").toString();
	    String r_obj3=doc.get("FileName").toString();
	    String r_obj4=doc.get("PageNumber").toString();
	    String r_obj5=doc.get("ReferencePage").toString();
	    String r_obj6=doc.get("SessionTitle").toString();

            Object[] result_set=new Object[6];
	    result_set[0]=r_obj1;
	    result_set[1]=r_obj2;
	    result_set[2]=r_obj3;
            result_set[3]=r_obj4;
            result_set[4]=r_obj5;
            result_set[5]=r_obj6;

            result.add(result_set);
            
            continue;
            } //if
          }//for start end
           }//for nn
                }//if
	   
       for(int y=0;y<result.size();y++){
           Object[] fullString_match=(Object[] )result.get(y);
           
           int common_w_score=ms_gui.common_words(fullString_match[1].toString(),NN.toString());
           double score=(double)common_w_score;
           System.out.println(common_w_score+fullString_match[1].toString()+"*"+NN.toString());
          Object[] obj1 = new Object[6];
           obj1[0]=score;
           obj1[1]=fullString_match[1].toString();
           obj1[2]=fullString_match[2].toString();
	   obj1[3]=fullString_match[3].toString();
            obj1[4]=fullString_match[4].toString();
	    obj1[5]=fullString_match[5].toString();
	System.out.println("Assigning "+obj1[3]+"\t"+obj1[4]+"\t"+obj1[5]);
           if(q_ts.add(obj1[0]+""+obj1[1])){
           result_sort.add(obj1);
           }
           //map.put(common_w_score,fullString_match);
      
      }//if
      ArrayList ranked_doc=ms_gui.Freq_Sorting(result_sort);
       for (int i = 0; i < ranked_doc.size(); i++) {
            Object[] o = (Object[]) ranked_doc.get(i);
            System.out.println("The sorted results"+o[0].toString()+o[1].toString()+o[2].toString()+o[3].toString()+o[4].toString());



	System.out.println(o[0].toString()+"*"+o[1].toString()+"\n");
	String res_text=o[0].toString()+"*"+o[1].toString()+"\n";

String url ="<a href=\'https://www.moh.gov.sg/content/dam/moh_web/HPP/Doctors/cpg_medical/current/2014/diabetes_mellitus/cpg_Diabetes%20Mellitus%20Summary%20Card%20-%20Jul%202014.pdf"+"#"+o[3].toString()+">"+"</a>";
String output =  url+ res_text;

          ta_search.append(output);

	
 
	}//for
        }catch(Exception e_1){e_1.printStackTrace();}              

               
     }});

       bclear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
		String c3=bclear.getActionCommand();
		if(c3.equals("Clear"))
		{
			ta.removeAll();
			ta.setText("");
   	                ta_search.removeAll();
			ta_search.setText("");
		}
	}});
	
	/*hl.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
		String c4=hl.getActionCommand();
		if(c4.equals("Highlight"))
		{
			highlight(ta_search,q);
			
		}
	}});*/

    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        GUIMedical frame = new GUIMedical("GUI for Medical");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


 
/*    
public static void highlight(JTextComponent textComp, String pattern) {

    try {
        Highlighter hilite = textComp.getHighlighter();
        javax.swing.text.Document doc = textComp.getDocument();
        String text = doc.getText(0, doc.getLength());
        int pos = 0;

        // Search for pattern
        while ((pos = text.indexOf(pattern, pos)) >= 0) {
            // Create highlighter using private painter and apply around pattern
            hilite.addHighlight(pos, pos + pattern.length(), painter2);
            pos += pattern.length();
        }
    } catch (BadLocationException e) {
    }
}*/

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event dispatchi thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
