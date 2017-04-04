'''
[protype version]
Automatically extracting the clinical practical guidelines pdf text and tag the document with the 
<Grades and Levels>
<Refered PageNo>
<Title>
<Guidelines_sentences>/<fullstring>
'''

from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from cStringIO import StringIO

import glob 


import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import re
def convert_pdf_to_txt(path):
    rsrcmgr = PDFResourceManager()
    retstr = StringIO()
    codec = 'utf-8'
    laparams = LAParams()
    device = TextConverter(rsrcmgr, retstr, codec=codec, laparams=laparams)
    fp = file(path, 'rb')
    interpreter = PDFPageInterpreter(rsrcmgr, device)
    password = ""
    maxpages = 0
    caching = True
    pagenos=set()
    pno=""
  
    for pagenumber, page in enumerate(PDFPage.get_pages(fp, pagenos, maxpages=maxpages, password=password,caching=caching, check_extractable=True)):
    #for page in PDFPage.get_pages(fp, pagenos, maxpages=maxpages, password=password,caching=caching, check_extractable=True):
    		
        interpreter.process_page(page)
    fp.close()
    device.close()
    str = retstr.getvalue()
    str = " ".join(str.replace('\xa0', ' ').strip().split())+'\n'
    retstr.close()
    return str
    
def write_file (folder, filename, filedata, flags='w'):
   
    result = False
    if os.path.isdir(folder):
        try:
            file_obj = open(os.path.join(folder, filename), flags)
            file_obj.write(filedata)
            file_obj.close()
            result = True
        except IOError:
            pass
    return result

def remove_non_ascii_2(text):
	return re.sub(r'[^\x00-\x7F]',' ', text)
def get_title(pdf_path):
    title=""
    infile = open(pdf_path, 'rb')
    parser = PDFParser(infile)
    document = PDFDocument(parser)

    toc = list()
    info=document.info
    print(info) 
    key='Title'
    for subVal in info:
        if key in subVal:        
            title=str(subVal[key]) #Title of the document Retrieve
	return title            
            
filename1= "/home/vumamaheswari/Medical_corpus-2016/Pharmacy/Dental Implant Summary Card (Revised 181012).pdf"     
filename="/home/vumamaheswari/Medical_corpus-2016/Pharmacy/Dental Implant CPG Booklet (Revised 181012).pdf"      
text=convert_pdf_to_txt(filename1) #Extracts text content from the pdf


def find_properties(sentences,title):
	#print("title is:"+str(title))
	
	#Tags to write the extracted files in XML
	Filexmlformat_str='<?xml version="1.0" encoding="UTF-8" standalone="no"?> <cpgs>' 
	gcatTag='<guideline Category=' #Folder Name
	fname='FileName='  #Actual FileName
	top_fname='Topic=' #Topic "First Page- A Text before "MOH Clinical Practice Guidelines" and after "Ministry of Health Singapore"."
	yea_tag='Year=' #Page 1 Year Regular Expression
	id_tag='id=' #incremental count
	end_tag='</cpgs>'
	end_glines='</guidelines>'
	
	#Session tags
	#<session PageNumber="1" ReferencePage="12" SessionTitle="Definition and diagnostic classification"/>
	start_session='<session'
	pno_tag='PageNumber'
	rpg_no_tag='ReferencePage'
	ses_title_tag='SessionTitle' #Finding <subtitle> regular expression
	
	#Classification Grade and Level
	#<classification Grade="B" Level="2+"/>
	start_class='<classification'
	gtag='Grade'
	ltag='Level'
	
	for stuff in sentences:
	
		m = re.match(grade_level_pattern, stuff)
		n = re.match(pno_pattern,stuff)
		
		gpp_pattern=re.match('GPP',stuff)
		sent_stuff="<fullstring>"+stuff+"."+"</fullstring>"
		Title="<Title>"+str(title)+"</Title>"
		gr_le=""
		pno=""
		buf_pno=[]
		g_str='GPP'
		if m or n or gpp_pattern:#extract the content only it has guidelines based informations <GPP> <Grade #,Level #> and (pg #dd)
			find_grade_level=re.findall(grade_level_pattern,stuff)
			
			#g_level.append(find_grade_level)
			#g_l=m.group(0)
			if len(find_grade_level)>0:
				gr_le="<glevel>"+str(find_grade_level)+"</glevel>"
				g_level.append(gr_le)
				#print("Herecomes"+g_level[len(g_level)-1])
		
				
			find_pg_No=re.findall(pno_pattern,stuff)
			if len(find_pg_No) >0:
				pno="<Reference_Page_No.>"+str(find_pg_No)+"</Reference_Page_No.>"
				if pno == '1':
					yr =re.match(yearReg,stuff) #Find the year	
					print('Year'+yr)
			if gpp_pattern:
				find_gpp=re.findall('GPP',stuff)
				gr_le ="<glevel>"+str(find_gpp)+"</glevel>"
		#str(Filexmlformat_str+"\n"+gcatTag+" "+fname+""+top_fname+""+yea_tag+""+id_tag+">\n"+		
		if not gr_le:
			sent_List.append(str(sent_stuff+pno+gr_le+Title)+'\n')

#print(text)
#sentences = re.split(r' *[\.\?!][\'"\)\]]* *', text)

#List to Store the grades and levels
g_level=[]

pno_pattern=re.compile('(pg \d{1,2}).')#refered pages
grade_level_pattern=re.compile('Grade [A-Z], Level [1-9][\+]*') #Grade_Levl_Pattern

#Remove non_ascii
#text=text.replace('\xc2','')

#text=text.replace("\xe2\x80\x99",'')
text=remove_non_ascii_2(str(text))
#text=text.replace('(cid:','')
#nopat=re.compile('[\)\d{2}]')
#text=re.sub(nopat, '', text)

find_pno=re.findall(pno_pattern,text)
#print(find_pno) #refered pagenos obtained

sentences = re.split(r' *[\.][\'"\)\]]* *', text) 
#sentpattern=re.compile('[\.].*[(pg \d{1,2}).]')
#sentences = re.findall(sentpattern,text)

#print(str(sentences)+"\n")
#To seperate sentences based on "."delimters
#sentences = re.split(grade_level_pattern, text)
#List to store Sentences
sent_List=[]
title=get_title(filename) #Title of the document

find_properties(sentences,title)

	
		
print(str(sent_List))
print("Number of Grades and Levels"+str(len(g_level)))
print("Number of Guidelines"+str(len(sent_List)))


