package org.apache.nutch.LuceneSearchingScore;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
	public class test {

	    public StanfordCoreNLP pipeline;

	    public test() {
	        // Create StanfordCoreNLP object properties, with POS tagging
	        // (required for lemmatization), and lemmatization
	        Properties props;
	        props = new Properties();
	        props.put("annotators", "tokenize, ssplit, pos, lemma,");

	        /*
	         * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
	         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
	         * and then other sequence model style annotation can be used to add things like lemmas, 
	         * POS tags, and named entities. These are returned as a list of CoreLabels. 
	         * Other analysis components build and store parse trees, dependency graphs, etc. 
	         * 
	         * This class is designed to apply multiple Annotators to an Annotation. 
	         * The idea is that you first build up the pipeline by adding Annotators, 
	         * and then you take the objects you wish to annotate and pass them in and 
	         * get in return a fully annotated object.
	         * 
	         *  StanfordCoreNLP loads a lot of models, so you probably
	         *  only want to do this once per execution
	         */
	        this.pipeline = new StanfordCoreNLP(props);
	    }

	    public List<String> lemmatize(String documentText)
	    {
	        List<String> lemmas = new LinkedList<String>();
	        // Create an empty Annotation just with the given text
	        Annotation document = new Annotation(documentText);
	        // run all Annotators on this text
	        this.pipeline.annotate(document);
	        // Iterate over all of the sentences found
	        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	        for(CoreMap sentence: sentences) {
	            // Iterate over all tokens in a sentence
	            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	                // Retrieve and add the lemma for each word into the
	                // list of lemmas
	            	
	                lemmas.add(token.get(LemmaAnnotation.class));
	                System.out.print(token.get(LemmaAnnotation.class)+" ");
	            }
	        }
	        return lemmas;
	    }


	    public static void main(String[] args) {
	    	 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	        Properties props = new Properties();
                props.setProperty("ner.useSUTime", "false");
	        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	        
	        // read some text in the text variable
	        String text = "All normal risk, asymptomatic women 50-69 years of age should be screened with mammography only, every two years.";// Add your text here!
	        
	        // create an empty Annotation just with the given text
	        Annotation document = new Annotation(text);
	        
	        // run all Annotators on this text
	        pipeline.annotate(document);
	        
	        // these are all the sentences in this document
	        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	        
	        System.out.println("sentences : "+sentences);
	        
	        for(CoreMap sentence: sentences) {
	        	
	        	System.out.println("sentence : "+sentence);
	          // traversing the words in the current sentence
	          // a CoreLabel is a CoreMap with additional token-specific methods
	        	
	        	String words = "";
	          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	            // this is the text of the token
	        	  System.out.println("token : "+token);
	            words += token.get(TextAnnotation.class);
	           
	            // this is the POS tag of the token
	            String pos = token.get(PartOfSpeechAnnotation.class);
	            System.out.println("pos : "+pos);
	            // this is the NER label of the token
	            String ne = token.get(NamedEntityTagAnnotation.class);    
	            System.out.println("ne : "+ne);
	          }
	          
	          Annotation document1 = new Annotation(words);
		        
		        // run all Annotators on this text
		        pipeline.annotate(document1);
		        
		        // these are all the sentences in this document
		        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		        List<CoreMap> sentences1 = document1.get(SentencesAnnotation.class);
		        
		        System.out.println("sentences : "+sentences1);
		        
		        for(CoreMap sentence1: sentences1) {
		        	
		        	System.out.println("sentence : "+sentence1);
		          // traversing the words in the current sentence
		          // a CoreLabel is a CoreMap with additional token-specific methods
		        	
		        	
		          for (CoreLabel token: sentence1.get(TokensAnnotation.class)) {
		        	  System.out.println("token : "+token);
		            String pos = token.get(PartOfSpeechAnnotation.class);
		            System.out.println("pos : "+pos);
		           
		          }
		        
		        }

	          // this is the parse tree of the current sentence
	          Tree tree = sentence.get(TreeAnnotation.class);
	          System.out.println("tree : "+tree);

	          // this is the Stanford dependency graph of the current sentence
	          SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	          System.out.println("dependencies : "+dependencies);
	        }

	        // This is the coreference link graph
	        // Each chain stores a set of mentions that link to each other,
	        // along with a method for getting the most representative mention
	        // Both sentence and token offsets start at 1!
	        Map<Integer, CorefChain> graph = 
	          document.get(CorefChainAnnotation.class);
	        System.out.println("graph : "+graph);
	     
	    }

	    }


