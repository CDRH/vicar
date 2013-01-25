package edu.unl.abbot.vicar.Signin;

import edu.unl.abbot.vicar.Global;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;

/**
* Provides simple operations on a serialized file for storing and retrieving user account information.
* (If this proves to be the best way to proceed then efforts will be made to get the currently commented out try_with_resources versions of the code operational.)
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class FileDB {

private static Path destpath = Paths.get(Global.ACCTFILE_PATH);

private Map<String,Object> m_usermap = new HashMap<String,Object>();

/**
* Simple main for testing.
*/
	public static void main(String args[]){
		FileDB fdb = new FileDB();
		fdb.setEntry("aaa@example.com",new Integer("1"));
		fdb.setEntry("bbb@example.com",new Integer("2"));
		fdb.setEntry("ccc@example.com",new Integer("3"));
		fdb.Display();
		Object x = fdb.getEntry("aaa@example.com");
		System.out.println(x.toString());
	}

	public FileDB(){
		int reccnt = loadDB();
		//System.out.println("RECORDS LOADED<"+reccnt+">");
	}

/**
* Saves current usermap information into the file referenced by destpath.
*/
	public int saveDB(){
		int records = 0;

/****
		try(ObjectOutputStream oos = (ObjectOutputStream)Files.newOutputStream(destpath,StandardOpenOption.APPEND)){
			if(m_usermap!=null){
				oos.writeObject(m_usermap);
				oos.close();
				records = m_usermap.size();
			}
		}catch(FileNotFoundException fnfex){
			fnfex.printStackTrace();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
****/
/****/
		try {
			if(m_usermap!=null){
				FileOutputStream fos = new FileOutputStream(Global.ACCTFILE_PATH);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(m_usermap);
				oos.close();
				records = m_usermap.size();
			}
		}catch(FileNotFoundException fnfex){
		}catch(IOException ioex){
		}
/****/
		System.out.println("SAVED <"+records+"> RECORDS");
		return records;
	}

/**
* Loads account information from the file referenced by destpath into the usermap.
*/
	public int loadDB(){
		int size = 0;
/****
		try {
			FileInputStream fis = new FileInputStream(Global.ACCTFILE_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			Map<String,Object> m = (Map<String,Object>) ois.readObject();
			ois.close();
			m_usermap = m;
			if(m_usermap!=null){
				size = m_usermap.size();
			}
			//System.out.println(m_usermap);
		}catch(FileNotFoundException fnfex){
			fnfex.printStackTrace();
		}catch(ClassNotFoundException cnfex){
			cnfex.printStackTrace();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		System.out.println("LOADDB");
		Display();
		System.out.println("LOADDB");
		return size;
****/
		if(!Files.exists(destpath.getParent())){
			try{
				Path p = Files.createDirectories(destpath.getParent());
			}catch(IOException ioex){
				ioex.printStackTrace();
				return -1;
			}
		}
		if(Files.exists(destpath)){
			try{
				FileInputStream fis = new FileInputStream(Global.ACCTFILE_PATH);
				ObjectInputStream ois = new ObjectInputStream(fis);
				@SuppressWarnings("unchecked")
				Map<String,Object> m = (Map<String,Object>) ois.readObject();
				ois.close();
				m_usermap = m;
				if(m_usermap!=null){
					size = m_usermap.size();
				}
			}catch(FileNotFoundException fnfex){
				fnfex.printStackTrace();
			}catch(ClassNotFoundException cnfex){
				cnfex.printStackTrace();
			}catch(IOException ioex){
				ioex.printStackTrace();
			}
/***
			try(ObjectInputStream ois = (ObjectInputStream)Files.newInputStream(destpath)){
				//ois.write(the_msg+"\n");
				@SuppressWarnings("unchecked")
				Map<String,Object> m = (Map<String,Object>) ois.readObject();
				ois.close();
				m_usermap = m;
				if(m_usermap!=null){
					size = m_usermap.size();
				}
			}catch(FileNotFoundException fnfex){
				fnfex.printStackTrace();
			}catch(ClassNotFoundException cnfex){
				cnfex.printStackTrace();
			}catch(IOException ioex){
				ioex.printStackTrace();
			}
***/
		}else{
			try(BufferedWriter writer = Files.newBufferedWriter(destpath,StandardCharsets.UTF_8,StandardOpenOption.APPEND,StandardOpenOption.DSYNC)){
			}catch(IOException fnfex){
			}
/***
			try {
				FileInputStream fis = new FileInputStream(Global.ACCTFILE_PATH);
			}catch(FileNotFoundException fnfex){
			
				fnfex.printStackTrace();
			}
***/
		}
		return size;
	}

/**
* Updates the usermap with the key/value pair and saves it to the data storage file referenced by destpath.
* @param the_key The unique name by which an entry will be searched.
* @param the_obj The entry to be added or updated in the usermap.
* @return The entry placed in the map.
*/
	public Object setEntry(String the_key,Object the_obj){
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.put(the_key,the_obj);
			saveDB();
		}
		return entryVal;
	}

/**
* Retrieves a key/value pair from the usermap based on the key.
* @param the_key The unique name by which an entry will be searched.
*/
	public Object getEntry(String the_key){
		//Display();
		//System.out.println("GET ENTRY <"+the_key+">");
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.get(the_key);
		}
		return entryVal;
	}

/**
* Removes an key/value pair from the usermap and the data storage file referenced by destpath.
* @param the_key The unique name by which an entry will be searched.
*/
	public Object delEntry(String the_key){
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.remove(the_key);
			saveDB();
		}
		return entryVal;
	}

/**
* Simple display for testing.
*/
	public void Display(){
		Iterator it = m_usermap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			//System.out.println("DISPLAY "+pairs.getKey());
			//((AcctData)pairs.getValue()).Display();
		}
	}
}

