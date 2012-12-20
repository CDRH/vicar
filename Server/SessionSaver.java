package Server;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Vector;
import java.io.IOException;

import org.apache.cocoon.environment.Session;

/**
* Saves a user's settings to the file specified by {@link Global#SESSION_FILE} which is stored in the user's path.
* Used by {@link Convert.AbbotConvert} to store the most recently used schema file name and the vector of errors found during validation.  This file is read by {@link Core.Vicar} following sign in.
* Any session attribute prefixed with 'SAVE:' is saved.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class SessionSaver {

private Session m_session;
private static String TOKEN = "$";

	public SessionSaver(){
	}

/**
* Loads the session information from the session file and restores the session attribute values indicated by each data line.
* @param the_session The session for action on its attributes.
* @param the_pathname The path to the session storage file.
*/
	public static void load(Session the_session,String the_pathname){
		try {
			Path p = Paths.get(the_pathname);
			List<String> lines = Files.readAllLines(p,StandardCharsets.UTF_8);
			for(String line : lines){
				//System.out.println(line);
				StringTokenizer stok = new StringTokenizer(line,TOKEN);
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
		}catch(NoSuchFileException nsfex){
			System.out.println("SessionSaver:FILE MISSING ON LOAD");
			//nsfex.printStackTrace();
		}catch(InvalidPathException ipex){
			ipex.printStackTrace();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
	}

/**
* Saves all session attributes prefixed with 'SAVE:' and parsed by TOKEN.
* @param the_session The session for action on its attributes.
* @param the_pathname The path to the session storage file.
*/
	public static void save(Session the_session,String the_pathname){
		Path p = Paths.get(the_pathname);

		try (BufferedWriter writer = Files.newBufferedWriter(p,StandardCharsets.UTF_8,StandardOpenOption.CREATE)){
			Enumeration san = the_session.getAttributeNames();
			while(san.hasMoreElements()){
				String sanName = (String)san.nextElement();
				if(sanName.startsWith("SAVE:")){
					Object sanObj = the_session.getAttribute(sanName);
					//System.out.println("\tATTR<"+sanName+"> VALUE<"+sanObj.toString()+">");
					writer.write(sanName+TOKEN+sanObj.toString()+"\n");
				}
			}
			//writer.flush();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
	}

/**
* Displays all saveable attributes in the current session.
* For debugging purposes.
*/
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


