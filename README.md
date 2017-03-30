# Clinical-Search


Clinical Guidelines Search
=========

A semantic based search engine for clinical data would be a substantial aid for hospitals to provide support for clinical practitioners. Since electronic medical records
of patients contain a variety of information, there is a need to extract meaningful patterns from the Patient Medical Records (PMR). The proposed work matches patients to relevant clinical practical guidelines (CPGs) by matching their medical records with the CPGs.

Software Used
=======
apache-nutch-1.4-bin 
Lucene-4.10.3
apache-tomcat-8.0.32
luke-with-deps.jar (to check the Lucene index)


Steps to run:
=======
1.Install apache-tomcat-8.0.32
2.Inside ./apache-tomcat-8.0.32/webapps extract medSearch.zip
3.Extract CPG-Index.zip (The respective path needs to be changed in medicalsearch.jsp)
4.Extract SNOMED-Index.zip (The respective path needs to be changed in medicalsearch.jsp)
5.Go to browser "http:localhost:8077/medSearch" 
6.Basic Search button control is active now
