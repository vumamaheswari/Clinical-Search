# Clinical-Search


Clinical Guidelines Search
=========

A semantic based search engine for clinical data would be a substantial aid for hospitals to provide support for clinical practitioners. Since electronic medical records
of patients contain a variety of information, there is a need to extract meaningful patterns from the Patient Medical Records (PMR). The proposed work matches patients to relevant clinical practical guidelines (CPGs) by matching their medical records with the CPGs.

Software Used
=======
apache-nutch-1.4-bin,
Lucene-4.10.3,
apache-tomcat-8.0.32,
luke-with-deps.jar (to check the Lucene index).

Reference
=======
If you use the code, please cite the following paper.
Vasanthakumar, E., & Bond, F. (2018). A Semantic Multi-Field Clinical Search for Patient Medical Records, Cybernetics and Information Technologies, 18(1), 171-182. doi: https://doi.org/10.2478/cait-2018-0014

Steps to run:
=======
1.Install apache-tomcat-8.0.32.
2.Inside ./apache-tomcat-8.0.32/webapps extract medSearch.zip.
3.Extract CPG-Index.zip (The respective path needs to be changed in medicalsearch.jsp).
4.Extract SNOMED-Index.zip (The respective path needs to be changed in medicalsearch.jsp).
5.Go to browser "http:localhost:8077/medSearch" .
6.Basic Search button control is active now.

Reguired jar files
=======

ant-contrib-0.3.jar,
commons-cli-2.0-SNAPSHOT.jar,
commons-codec-1.3.jar,
commons-codec-1.10.jar,
commons-httpclient-3.0.1.jar,
commons-lang3-3.5.jar,
commons-lang-2.1.jar,
commons-logging-1.0.4.jar,
commons-logging-1.2.jar,
commons-logging-1.2-javadoc.jar,
commons-logging-api-1.0.4.jar,
curvesapi-1.03.jar,
dom4j-1.7-20060614.jar,
ejml-0.23.jar,
englishPCFG.ser.gz,
fontbox-1.8.12.jar,
hadoop-0.12.2-core.jar,
jakarta-oro-2.0.7.jar,
javax.json.jar,
javax.json-1.0.4.jar,
javax.json-api-1.0-sources.jar,
jempbox-1.8.12.jar,
jets3t-0.5.0.jar,
jetty-5.1.4.jar,
jetty-5.1.4.LICENSE.txt,
joda-time.jar,
joda-time-2.9-sources.jar,
jollyday.jar,
jollyday-0.4.7-sources.jar,
junit-4.12.jar,
jxl.jar,
log4j-1.2.13.jar,
lucene-analyzers-common-4.10.3.jar,
lucene-codecs-4.10.3.jar,
lucene-core-4.10.3.jar,
lucene-demo-4.10.3.jar,
lucene-highlighter-4.4.0.jar,
lucene-queryparser-4.10.3.jar,
maven-ant-tasks-2.1.3.jar,
pagination-tag-3.0.jar,
pdfbox-0.6.4.jar,
pdfbox-1.8.12.jar,
protobuf.jar,
servlet-api.jar,
slf4j-api.jar,
slf4j-simple.jar,
solr-solrj-3.4.0.jar,
stanford-corenlp-3.6.0.jar,
stanford-corenlp-3.6.0-javadoc.jar,
stanford-corenlp-3.6.0-models.jar,
stanford-corenlp-3.6.0-sources.jar,
stanford-parser.jar,
taglibs-i18n.jar,
taglibs-i18n.tld,
xercesImpl-2.8.1.jar.zip,
xom-1.2.10.jar,
xom-1.2.10-sources.jar,
xom-1.2.10-src.jar.
