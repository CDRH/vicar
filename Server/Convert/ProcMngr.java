package Server.Convert;

import java.util.Vector;
import java.io.InputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
* Invokes a system command.  Used by AbbotConvert.java to deliver small fixes to the Abbot output files using 'sed.'
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class ProcMngr {

private ProcessBuilder m_builder = null;
private String m_cmdbase = null;
private String m_cmdflag = null; 
private String m_dir = null; 
private Vector<String> m_fileList = null; 
private Vector<String> m_convList = null; 

/**
* Main for testing and to serve as an example.
*/
	public static void main(String args[]){
		String cmdbase = "/usr/bin/sed";

		String cmdflag = "-i_sed";

		String dir = "/tmp/vicar/ANONYMOUS/ggg/output/";

		Vector<String> schemaList = new Vector<String>();
		schemaList.add("A00002.xml");
		schemaList.add("A00005.xml");
		schemaList.add("A00007.xml");
		schemaList.add("A00008.xml");
		schemaList.add("A00011.xml");

		String convURL = "http:\\/\\/abbot.unl.edu\\/fss.rng";
		String email = "fss@example.com";
		Vector<String> convList = new Vector<String>();
		convList.add("s/http:\\/\\/abbot.unl.edu\\/tei_all.rng/"+convURL+"/g");
		convList.add("s/bpz/"+email+"/g");
		convList.add("s/Pytlik Zillig,/"+email+"/g");
		convList.add("s/[ \t]*B.<\\/name> Conversion to TEI/<\\/name> Conversion to TEI/g");

		ProcMngr pm = new ProcMngr(cmdbase,cmdflag,dir);
		pm.cleanup(schemaList,convList);
	}

/**
* Used by {@link AbbotConvert} to perform <i>sed</i> operations on output files.
*/
	public ProcMngr(String the_cmdbase,String the_cmdflag,String the_dir){
		m_cmdbase = the_cmdbase;
		m_cmdflag = the_cmdflag;
		m_dir = the_dir;
	}

/**
* Used by {@link AbbotConvert} to perform <i>sed</i> operations on output files.
* @param the_schemaList List of schema file names on which an operation is to be performed.
* @param the_convList List of <i>sed</i> conversions to be performed.
*/
	public void cleanup(Vector<String> the_schemaList,Vector<String> the_convList){
		for(String schema : the_schemaList){
			for(String conv : the_convList){
				String[] act = {m_cmdbase,m_cmdflag,conv,schema};
				m_builder = new ProcessBuilder(act);
				m_builder.redirectErrorStream(true);
				m_builder.directory(new File(m_dir));
				try {
					Process process = m_builder.start();
					process.waitFor();
					try(InputStreamReader isr = new InputStreamReader(process.getInputStream());
							BufferedReader reader = new BufferedReader(isr)){
						String line = null;
						while((line = reader.readLine())!=null){
							System.out.println("STDOUT<"+line+">");
						}
					}catch(IOException ioex){
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}


