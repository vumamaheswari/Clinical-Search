/*Mapping of URLs and Filename in a HashTable */
package org.apache.nutch.LuceneSearchingScore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

public class URLtoFilename {
    
    public static Hashtable<String, String> FileNameToURL = new Hashtable<String, String>();
    public static BufferedReader urlReader= IOHelper.getBufferedReader("./resource/url/url_to_FilenameMap.txt");
    
    public static void main(String[] args)
    {
		URLtoFilename writeURL = new URLtoFilename();
		writeURL.writeURLandFileName();
		writeURL.test();
    }
    public void writeURLandFileName()
    {
      	String currentLine = null;
          int FileName = 0;
       	while ((currentLine = IOHelper.readLineFromBufferedReader(urlReader)) != null) {

		String[] line=currentLine.split("|");		
		String URL=line[0].toString().trim();
		String filename=line[1].toString().trim();
		writeURL(filename, URL);

    }//while
    IOHelper.closeBufferedReader(urlReader);
    serializeHashTable(FileNameToURL, "./medurls/FileNameToURL.ser");
   

    }
    private void writeURL(String FileName, String URL) {

	        FileNameToURL.put(FileName, URL);
    }
    private void serializeHashTable(Hashtable<String, String> hashtable, String fileName) {
    
        ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream(fileName);
        IOHelper.writeObjectToOutputStream(objectOutputStream, hashtable);
        IOHelper.closeObjectOutputStream(objectOutputStream);
    }
    public void test()
    {
    try{
    Hashtable current=null;
     ObjectInputStream serReader = IOHelper.getObjectInputStream("./medurls/FileNameToURL.ser");
     while ((current = (Hashtable)IOHelper.readObjectFromInputStream(serReader)) != null) {
        System.out.println("The content is"+current);
    }
    IOHelper.closeObjectInputStream(serReader);
    }catch(Exception e){}
    }
}
