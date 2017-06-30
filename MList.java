package org.apache.nutch.LuceneSearchingScore;

import java.io.*;
import java.lang.*;
import java.util.*;



	
public class MList implements Serializable
{
	// reference to the head node.
	public HeadNode head;
	public ConceptNode concept;
	public ConceptToNode destination;
	public RelationNode relation;
	
	public int ConceptCount;
	public int RelationCount;

	public MList() throws Exception
	{
		// this is an empty list, so the reference to the head node
		// is set to a new node with no data
		head = new HeadNode();
		ConceptCount = 0;
		RelationCount = 0;
	}
	public void addConcept(String rw,String uwcons,String entryid,String exampleid,String definitionid,String pos,String gid,String gstring,String fname,String sid)
	
	{
		
		ConceptNode temp = new ConceptNode(rw, uwcons, entryid, exampleid, definitionid, pos, gid, gstring, fname,sid);
		
		if(head.colnext==null)
			head.setColNext(temp);
		else
		{
			ConceptNode current = head.colnext;
		// starting at the head node, crawl to the end of the list
			while(current.getColNext() != null)
			{
				current = current.getColNext();
			}
			current.setColNext(temp);
		}
		ConceptCount++;// increment the number of elements variable
	}

	
	public ConceptNode getConcept(int index)
	// post: returns the element at the specified position in this list.
	{
		// index must be 1 or higher
		if(index <= 0)
			return null;
		
		ConceptNode current = head.getColNext();
		for(int i = 1; i < index; i++)
		{
			if(current.getColNext() == null)
				return null;
			
			current = current.getColNext();
		}
		return current;
	}
	
	public int Conceptsize()
	{
		return ConceptCount;
	}
	
	public int Relationsize()
	{
		return RelationCount;
	}
	

	public ConceptToNode addCT_Concept(String cfrom,String cto,String rl,String did,String sid)
	{
	
		
		ConceptToNode temp = new ConceptToNode(cfrom,cto,rl,did,sid);
		ConceptNode current = head.colnext;
		ConceptToNode current1;
		
		while(current != null)
		{
				
			if((current.uw.equals(cfrom)) && (current.med_gid.equals(did))&& (current.sentid.equals(sid)))
			{
			
			
				if(current.getRowNext()==null)
					current.setRowNext(temp);
				else
				{
				
					current1=current.getRowNext();
					while(current1.getRowNext()!=null)
					{
				
						current1=current1.getRowNext();
					}
							 
					current1.setRowNext(temp);
				}
				break;
			}
			current = current.getColNext();
			ConceptCount++;
		}
				
		return(temp);
	}
	
	
	public void getCT_Concept()
	
	{
		// index must be 1 or higher
		try{
	
		ConceptNode current = head.colnext;
		ConceptToNode current1;
		
		while(current != null)
		{
				
				if(current.getRowNext()!=null)
				{
					current1 = current.getRowNext();
					
					
						
				while(current1.getRowNext()!= null)
				{
	
					current1= current1.getRowNext();
														
				}
				}
				current=current.getColNext();
					
		}
	
		}catch(Exception e)
		{}
			
	
	}
	
	public void addRelation(String rl)
	{
		if(checkRelationExist(rl)==0)
		{
			RelationNode temp = new RelationNode(rl);
		
			if(head.rownext==null)
				head.setRowNext(temp);
			else
			{
				RelationNode current = head.rownext;
		
				while(current.getRowNext() != null)
				{	
					current = current.getRowNext();
				}
			
				current.setRowNext(temp);
			}
			RelationCount++;
		}
	}
	
	public int checkRelationExist(String relnLab)
	{
		RelationNode current = head.rownext;
		int flag=0;
		while(current != null)
		{
			if(current.relnlabel.equals(relnLab))
			{
				flag++;
			}
			current = current.getRowNext();
		}
		if(flag==0)
			return 0;
		else
			return 1;
	}
	
	public RelationNode getRelation(int index)
	// post: returns the element at the specified position in this list.
	{
	
		
		if(index <= 0)
			return null;
		
		RelationNode current = head.getRowNext();
		for(int i = 1; i < index; i++)
		{
		
			if(current.getRowNext() == null)
			{
			
				return null;
			}
			
			current = current.getRowNext();
		}
		return current;
	}
	
	

	public void addCT_Relation(ConceptToNode cn)
	{
	
		RelationNode current = head.rownext;
		ConceptToNode current1;
		
		while(current != null)
		{
			
			
		
			
			if(current.relnlabel.equals(cn.relnlabel))
			{
				
			
				if(current.getColNext()==null)
					current.setColNext(cn);
				else
				{
				
					current1=current.getColNext();
					while(current1.getColNext()!=null)
					{
				
						current1=current1.getColNext();
					}
							 
					current1.setColNext(cn);
				}
				break;
			}
			current = current.getRowNext();
		}
				
		
	}
	
	public void getCT_Relation()
	{
				
		RelationNode current = head.rownext;
		ConceptToNode current1;
		
		while(current != null)
		{
				
				
				if(current.getColNext()!=null)
				{
					current1 = current.getColNext();
				
					
					
					while(current1.getColNext() != null)
					{
						
						current1= current1.getColNext();
						
					
					
					}
				}
				else
				{
					
				}
			current=current.getRowNext();		
		}
		
			
	
	}
	
	


	public String  getrootword(String cs)
	{
			 ConceptNode temp1;
			 temp1=concept;
			 temp1=head.colnext;
			 while(temp1!=null)
			 {
				 if(cs.equals(temp1.uw) )
			    {
			
				 break;
			     }
				 temp1=temp1.colnext;
			 }
			 return temp1.root;
			 
	}	
	  public String  getconcept_vs_ToConcept(String cs,String se)
	{
		  String to_set = "";
		  try{
		 ConceptNode temp1;
		 temp1=concept;
		 temp1=head.colnext;
		 
		 while(temp1!=null)
		 {	
	
				 if((cs.equals(temp1.uw)) && (se.equals(temp1.sentid)))
				 {
					 //	 System.out.println("STR-------->"+cs+":"+se);
					 break;
				 
				 }
		
			 temp1=temp1.getColNext();
		 }
		// if(!temp1.poscheck.isEmpty()){
			 to_set = temp1.root+"$"+temp1.postags+"$"+temp1.uw;
		// }	
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		 return to_set;
		 
}		 
	
		 
		
	
}	

