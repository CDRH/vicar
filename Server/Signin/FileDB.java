//FileDB.java

package Server.Signin;

import Server.Global;

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

public class FileDB {

private static Path destpath = Paths.get(Global.ACCTFILE_PATH);

private Map<String,Object> m_usermap = new HashMap<String,Object>();

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

	public Object setEntry(String the_key,Object the_obj){
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.put(the_key,the_obj);
			saveDB();
		}
		return entryVal;
	}

	public Object getEntry(String the_key){
		//Display();
		//System.out.println("GET ENTRY <"+the_key+">");
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.get(the_key);
		}
		return entryVal;
	}

	public Object delEntry(String the_key){
		Object entryVal = null;
		synchronized(this){
			entryVal = m_usermap.remove(the_key);
			saveDB();
		}
		return entryVal;
	}

	public void Display(){
		Iterator it = m_usermap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			//System.out.println("DISPLAY "+pairs.getKey());
			//((AcctData)pairs.getValue()).Display();
		}
	}
}

/*****
//LogWriter.java

package Server;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Date;

public class LogWriter {

private static Path destpath = Paths.get(Global.LOGFILE_PATH);

	public static void main(String args[]){
		LogWriter.msg("IPADDR","hello world!");
		LogWriter.msg("IPADDR","hello world!");
	}

	public LogWriter(){
	}

	public static long msg(String the_ipaddr,String the_msg){
		Date d = new Date();
		long ts = d.getTime();
		the_msg = the_ipaddr+","+d+","+ts+","+the_msg;
		return ts;
	}
}
*****/
