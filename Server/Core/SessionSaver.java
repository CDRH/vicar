//SessionSaver.java

package Server.Core;

import Server.Global;

import java.util.Vector;
import java.util.Map;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;

import org.apache.cocoon.environment.Session;
/**
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class SessionSaver {

private Session m_session;
private static String TOKEN = "$";

	public SessionSaver(){
	}

	public static void load(Session the_session,String the_pathname){
		try {
			//System.out.println("SESSION LOAD FROM FILE<"+the_pathname+">");
			File f = new File(the_pathname);
			if(f!=null){
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while((s = br.readLine())!=null){
					//System.out.println(s);
					StringTokenizer stok = new StringTokenizer(s,TOKEN);
					String name = "";
					if(stok.hasMoreTokens()){
						name = stok.nextToken();
					}
					String value = "";
					if(stok.hasMoreTokens()){
						value = stok.nextToken();
					}
					//System.out.println("\tATTR<"+name+"> VALUE<"+value+">");
					if(value.startsWith("[")){
						Vector<Integer> erlist = new Vector<Integer>();
						value = value.replace("[","");
						value = value.replace("]","");
						StringTokenizer stokvec = new StringTokenizer(value,", ");
						String svec;
						while(stokvec.hasMoreTokens()){
							svec = stokvec.nextToken();
							erlist.add(new Integer(svec));
						}
						the_session.setAttribute(name,erlist);
					}else{
						the_session.setAttribute(name,value);
					}
				}
				fr.close();
			}
		}catch(FileNotFoundException fnfex){
			System.out.println("FILE NOT FOUND");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void save(Session the_session,String the_pathname){
		try {
			//System.out.println("SESSION SAVED TO FILE<"+the_pathname+">");
			File f = new File(the_pathname);
			FileWriter fw = new FileWriter(f,false);
			Enumeration san = the_session.getAttributeNames();
			while(san.hasMoreElements()){
				String sanName = (String)san.nextElement();
				if(sanName.startsWith("SAVE:")){
					Object sanObj = the_session.getAttribute(sanName);
					//System.out.println("\tATTR<"+sanName+"> VALUE<"+sanObj.toString()+">");
					fw.write(sanName+TOKEN+sanObj.toString()+"\n");
				}
			}
			fw.flush();
			fw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void Display(Session the_session){
		try {
			System.out.println("SESSION ATTRIBUTES:");
			Enumeration san = the_session.getAttributeNames();
			while(san.hasMoreElements()){
				String sanName = (String)san.nextElement();
				if(sanName.startsWith("SAVE:")){
					Object sanObj = the_session.getAttribute(sanName);
					System.out.println("\tATTR<"+sanName+"> VALUE<"+sanObj.toString()+">");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}



