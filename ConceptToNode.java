package org.apache.nutch.LuceneSearchingScore;
import java.io.*;
//Toconcept node that contains the semantic link information between the UNL concepts.

public class ConceptToNode implements Serializable
{
	public String uwfrmconcept;
	public String uwtoconcept;
	public String relnlabel;

	public String gid;
     public String sid;
		
	
		
	public ConceptToNode rownext;
	public ConceptToNode colnext;
		
		// Node constructor
		
		public ConceptToNode()
		{
			uwfrmconcept="";
			uwtoconcept="";
			relnlabel="";
			
			gid="";
			sid="";
			
			
			rownext = null;
			colnext = null;
		}
		

		public ConceptToNode(String fcon,String tcon,String rln,String guideid,String sentid)
		{
			uwfrmconcept=fcon;
			uwtoconcept=tcon;
			relnlabel = rln;
		
			gid = guideid;
			sid = sentid;
		
			
			rownext = null;
			colnext = null;
			
		}
		public void setData(String fcon,String tcon,String rln,String guideid,String sentid)
		
		{
			uwfrmconcept=fcon;
			uwtoconcept=tcon;
			relnlabel = rln;
		
			gid = guideid;
			sid = sentid;
			
		}
		
		public ConceptToNode getRowNext()
		{
			return rownext;
		}
		
		public ConceptToNode getColNext()
		{
			return colnext;
		}
		
		public void setRowNext(ConceptToNode rwnxt)
		{
			rownext = rwnxt;
		}
		
		public void setColNext(ConceptToNode colnxt)
		{
			colnext = colnxt;
		}
}
