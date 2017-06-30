package org.apache.nutch.LuceneSearchingScore;

import java.io.*;
//UNL Node information that holds other index based properties of clinical guidelines

public  class ConceptNode implements Serializable
{
	public String root;
	public String uw;
	//UNL Dictionary based properties
	public int rowct;
	public String entry_tag;
	public String examples;
	public String definitions;
	public String postags;
	public String sentid;
	
	//Lucene index based properties 
	public String med_gid;
	public String fullstring_guidelines;
	public String filename_med;
	
	public ConceptToNode rownext;
	public ConceptNode colnext;
		
		// Node constructor
		
		public ConceptNode()
		{
			root="";
			uw="";
			entry_tag="";
			examples="";
			
			definitions="";
			postags="";
			med_gid="";
			fullstring_guidelines="";
			filename_med="";
			sentid="";
			rowct=0;
			
			rownext = null;
			colnext = null;
			
		}
		public ConceptNode(String rw,String uwcons,String entryid,String exampleid,String definitionid,String pos,String gid,String gstring,String fname,String sid)
		
		{
			
			root=rw;
			uw=uwcons;
			entry_tag=entryid;
			examples=exampleid;
			definitions=definitionid;
			postags=pos;
			med_gid=gid;
			fullstring_guidelines=gstring;
			filename_med=fname;
			sentid=sid;
			rowct=0;
			
			rownext = null;
			colnext = null;
	
			
		}
		public void setData(String rw,String uwcons,String entryid,String exampleid,String definitionid,String pos,String gid,String gstring,String fname,String sid)
		
		{
			root=rw;
			uw=uwcons;
			entry_tag=entryid;
			examples=exampleid;
			definitions=definitionid;
			postags=pos;
			med_gid=gid;
			sentid=sid;
			fullstring_guidelines=gstring;
			filename_med=fname;
			
		}
		
		public ConceptToNode getRowNext()
		{
			return rownext;
		}
		
		public ConceptNode getColNext()
		{
			return colnext;
		}
		
		public void setRowNext(ConceptToNode rwnxt)
		{
			rownext = rwnxt;
		}
		
		public void setColNext(ConceptNode colnxt)
		{
			colnext = colnxt;
		}
}
	
