<HTML>
    <HEAD>
        <TITLE>Submitting PMR query</TITLE>
    </HEAD>
  <body bgcolor="#ffffe">
    <BODY>
        <H1>Submitting PMR query</H1>
        <FORM ACTION="medicalsearch.jsp" METHOD="POST">
            PastMedicalHistory:
            <BR>  
<TEXTAREA NAME="PastMedicalHistory" ROWS="10" ></TEXTAREA>              
<style type="text/CSS">
.style1 { font-size: 16px; background: #CCCCFF; 
          border-width: thin thin thin thin; 
          border-color: #CCCCFF #CCCCCC #CCCCCC #CCCCFF}
.style2 { font-size: 16px; font-weight: bold; 
          background: #CCFFCC; border-width: thin medium medium thin; 
          border-color: #CCFF99 #999999 #999999 #CCFF99}
</style>

	    
<input type="submit" name="Submit" value="PMRHistorySearch" 
       onmouseover="this.className='style2'"
       onmouseout="this.className='style1'" class="style1"> 


            <BR>
	    
	    PresentingComplaints:
	      <BR>
            <TEXTAREA NAME="PresentingComplaints" ROWS="10"></TEXTAREA>
	<style type="text/CSS">	
.style1 { font-size: 16px; background: #CCCCFF; 
          border-width: thin thin thin thin; 
          border-color: #CCCCFF #CCCCCC #CCCCCC #CCCCFF}
.style2 { font-size: 16px; font-weight: bold; 
          background: #CCFFCC; border-width: thin medium medium thin; 
          border-color: #CCFF99 #999999 #999999 #CCFF99}
</style>
	 
<input type="submit" name="Submit" value="PMRComplaintSearch" 
       onmouseover="this.className='style2'"
       onmouseout="this.className='style1'" class="style1"> 

  <BR>
	    
	    Discharge Instruction:
	      <BR>
            <TEXTAREA NAME="DischargeInsSearch" ROWS="5"></TEXTAREA>
	<style type="text/CSS">	
.style1 { font-size: 16px; background: #CCCCFF; 
          border-width: thin thin thin thin; 
          border-color: #CCCCFF #CCCCCC #CCCCCC #CCCCFF}
.style2 { font-size: 16px; font-weight: bold; 
          background: #CCFFCC; border-width: thin medium medium thin; 
          border-color: #CCFF99 #999999 #999999 #CCFF99}
</style>
	 
<input type="submit" name="Submit" value="DischargeInsSearch" 
       onmouseover="this.className='style2'"
       onmouseout="this.className='style1'" class="style1"> 

	  <BR>
	    
	    Ordered Medication:
	      <BR>
            <TEXTAREA NAME="OrderedMedSearch" ROWS="5"></TEXTAREA>
	<style type="text/CSS">	
.style1 { font-size: 16px; background: #CCCCFF; 
          border-width: thin thin thin thin; 
          border-color: #CCCCFF #CCCCCC #CCCCCC #CCCCFF}
.style2 { font-size: 16px; font-weight: bold; 
          background: #CCFFCC; border-width: thin medium medium thin; 
          border-color: #CCFF99 #999999 #999999 #CCFF99}
</style>
	 
<input type="submit" name="Submit" value="OrderedMedSearch" 
       onmouseover="this.className='style2'"
       onmouseout="this.className='style1'" class="style1"> 
           
	   <BR>
	
	  <INPUT TYPE="SUBMIT" VALUE="Overall-Search-Results">       
	  
        </FORM>
    </BODY>
    <%@ include file="footer.html" %>
<HTML>

