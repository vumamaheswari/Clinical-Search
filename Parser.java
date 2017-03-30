package org.apache.nutch.LuceneSearchingScore;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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

public class Parser {

    private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";        

    private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
    protected StanfordCoreNLP pipeline;
    
    public Parser()
    {
    	 Properties props;
	        props = new Properties();
	        props.put("annotators", "tokenize, ssplit, pos, lemma,");
	        pipeline = new StanfordCoreNLP(props);
    }

    public Tree parse(String str) {                
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }

    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
            tokenizerFactory.getTokenizer(
                new StringReader(str));    
        return tokenizer.tokenize();
    }

    public static List<String> StanfordParserDependenciesCollapsed(String sentence) { 
//        String str = "Early identification provides opportunity for early referral and intervention, so that the child with ASD may have improved functioning in later life.";
    	
    	List<String> ListParser = new ArrayList<String> ();
    	
        Parser parser = new Parser();
        Tree tree = parser.parse(sentence);  
    //    tree.pennPrint();
        TreebankLanguagePack tlp =  new PennTreebankLanguagePack();
        
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        Collection tdl = gs.typedDependenciesCollapsed();

//        System.out.println(tree.toString());
//        System.out.println("typedDependencies: "+tdl); 
        
        for( Iterator<TypedDependency> iter = tdl.iterator(); iter.hasNext(); ) {
            TypedDependency var = iter.next();
            //var.dep().label().lemma()

            TreeGraphNode dep = new TreeGraphNode(var.dep());
            TreeGraphNode gov = new TreeGraphNode(var.gov());

            // All useful information for a node in the tree
            String reln = var.reln().getShortName();
            String word = var.dep().value().toString();
            String tag = var.gov().value().toString();
            String parserResult = reln+"("+tag+","+word.toLowerCase()+")";
//            String parserResult = reln+"("+gov+","+word.toLowerCase()+"-"+var.dep().index()+")";
            ListParser.add(parserResult);
//            System.out.println(reln+"("+tag+"-"+word.toLowerCase()+")"); 
        }        
//        System.out.println();  
        
        return ListParser;
    }
    
    public static List<String> StanfordParserDependencies(String sentence) { 
//      String str = "Early identification provides opportunity for early referral and intervention, so that the child with ASD may have improved functioning in later life.";
  	
  	List<String> ListParser = new ArrayList<String> ();
  	
      Parser parser = new Parser();
      Tree tree = parser.parse(sentence);  
  //    tree.pennPrint();
      TreebankLanguagePack tlp =  new PennTreebankLanguagePack();
      
      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
      Collection tdl = gs.typedDependencies();

//      System.out.println(tree.toString());
//      System.out.println("typedDependencies: "+tdl); 
      
      for( Iterator<TypedDependency> iter = tdl.iterator(); iter.hasNext(); ) {
          TypedDependency var = iter.next();

          TreeGraphNode dep = new TreeGraphNode(var.dep());
          TreeGraphNode gov = new TreeGraphNode(var.gov());

          // All useful information for a node in the tree
          String reln = var.reln().getShortName();
          String word = var.dep().value().toString();
          String tag = var.gov().value().toString();
          String parserResult = reln+"("+tag+","+word.toLowerCase()+")";
//          String parserResult = reln+"("+gov+","+word.toLowerCase()+"-"+var.dep().index()+")";
          ListParser.add(parserResult);
//          System.out.println(reln+"("+tag+"-"+word.toLowerCase()+")"); 
      }        
//      System.out.println();  
      
      return ListParser;
  }
    
    public static List<String> StanfordParserDependenciesCollapsedText(String sentence) { 
//      String str = "Early identification provides opportunity for early referral and intervention, so that the child with ASD may have improved functioning in later life.";
  	
  	List<String> ListParser = new ArrayList<String> ();
  	
      Parser parser = new Parser();
      Tree tree = parser.parse(sentence);  
      
   //   tree.pennPrint();
      
      TreebankLanguagePack tlp =  new PennTreebankLanguagePack();
      
      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
      Collection tdl = gs.typedDependenciesCollapsed();

      
      for( Iterator<TypedDependency> iter = tdl.iterator(); iter.hasNext(); ) {
          TypedDependency var = iter.next();


          String reln = var.reln().getShortName();
          String word = var.dep().value().toString();
          String tag = var.gov().value().toString();


          if(!tag.equalsIgnoreCase("root"))
          {
          ListParser.add(tag);
          }
          ListParser.add(word.toLowerCase());

      }        

      
      List<String> dedupped = new ArrayList<String>(new LinkedHashSet<String>(ListParser));
      return dedupped;
  }
    
    public static List<String> StanfordParserforPattern(String sentence) { 
//      String str = "Early identification provides opportunity for early referral and intervention, so that the child with ASD may have improved functioning in later life.";
  	
  	List<String> ListParser = new ArrayList<String> ();
  	
      Parser parser = new Parser();
      Tree tree = parser.parse(sentence);  
      tree.pennPrint();
      List taggedWords = tree.taggedYield();  
      TreebankLanguagePack tlp =  new PennTreebankLanguagePack();
      
      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
      Collection tdl = gs.typedDependenciesCollapsed();


      
      for( Iterator<TypedDependency> iter = tdl.iterator(); iter.hasNext(); ) {
          TypedDependency var = iter.next();
         
          TreeGraphNode dep = new TreeGraphNode(var.dep());
          TreeGraphNode gov = new TreeGraphNode(var.gov());
        
          // All useful information for a node in the tree
          String reln = var.reln().getShortName();
          String word = var.dep().value().toString();
          String tag = var.gov().value().toString();
          String parserResult = reln+"("+tag+","+word.toLowerCase()+")";
//          String parserResult = reln+"("+gov+","+word.toLowerCase()+"-"+var.dep().index()+")";
          ListParser.add(parserResult);
         // System.out.println(reln+"("+tag+"-"+word.toLowerCase()+")"); 
      }        

      
      return taggedWords;
  }
    
    public String parsenew(String str)
    {
    // String str = "Women with pregestational diabetes should have their serum creatinine and electrolytes assessed at the first antenatal visit and in the third trimester.";
    	// String str="PMHx  1. Gastritis   - on f/u OPS    2. Cholelithiasis  - diagnosed in TTSH 2006  - s/p cholecystectomy (laparoscopic -&gt; open) in Sep 2010 in KTPH 51 yr old Chinese Female  NKDA    Admitted on 29/09/11  1. Vomiting &amp; Diarrhea x 1/7  - started with vomiting yesterday evening  - non-bloody ? billous (green)  - a/w epigastric pain  - later developed diarrhea, 6-7 episodes at home, &gt;10 episodes in ED  - non-bloody, passage in mucus  - no fever  - recent URTI 1/52 ago,still has cough    - contact hx : son developed, similar symptoms few days ago.pt developed symptoms after washing bedsheets for son  - no travel hx    O/E  Alert, Slight dry  T 37.1 BP 129/84 HR 80 SpO2 100% RA  H S1S2 L Clear A Soft NT, no rebound/guarding. Epigastric tenderness  No neurological deficit    CXR 29/09/11 : The heart size is normal. No active lung lesion is seen. There is no free air under the diaphragm.     XR KUB 29/09/11 : The renal outlines are partially obscured by bowel gas shadows. No radiopaque urinary calculus is detected. The bowel gas pattern is within normal limits.     Initial investigations  TW 11.21 Hb 14.0 Plt 322  Na 137 K 3.4 Cr 41 Urea 3.1  LFTs normal  Amylase 76 Random glucose 5.8    Issues/Progress  1. Gastroenteritis  - Started on IV hydration, symptomatic treatment, oral rehydration salts for diarrhea &amp; vomiting  - Stool Leukocyte : negative  - Stool c/s : negative    upon discharged she was well and comfortable. no fever, no more diarrhea an vomiting. Discharged stable/well on 03/1032011. no TCU";
         //String str_new=str.replaceAll("[^a-z A-Z]", "");
         String new1=str;
        
        // System.out.println("new"+new1);
    	 String afterlemmas = "",exmp = ""; 
    	// System.out.println("B4 Lemma : "+str);
    	 List<String> theresult=StanfordParserforPattern(new1);
        // System.out.println("UMA"+theresult);
    	 List<String> lemmas = new LinkedList<String>();
    	 Annotation document = new Annotation(new1);
    	 test slem = new test();
    	 slem.pipeline.annotate(document);
    	 List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	        for(CoreMap sentence: sentences) {
	        	
	        	 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        		 lemmas.add(token.get(LemmaAnnotation.class));
	        		 
	        		 afterlemmas += token.get(LemmaAnnotation.class)+"_"+token.get(PartOfSpeechAnnotation.class)+" ";
	        		 exmp += token.get(LemmaAnnotation.class)+" ";
	        		
		            }
		        }   
	        
	      
     	
  
         return afterlemmas;
    }
         
}
