package edu.unl.abbot.vicar.Core;

import edu.unl.abbot.vicar.Global;

import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.cocoon.ProcessingException;
import org.xml.sax.helpers.AttributesImpl;

/**
* Produces a list of {@link SchemaData} for for population of a select list on the main page by {@link Vicar}.

* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 12/15/2012
* 
* @see SchemaData
* @see Vicar
*/
public class SchemaList {

/**
* Generate the XML representation of the SchemaData list for use in the XML output produced by {@link Vicar}.
*/
	public void generateSchemaXML(ContentHandler contentHandler,Vector<SchemaData> the_schemaList) throws SAXException, ProcessingException {
		try {
			AttributesImpl schemalistAttr = new AttributesImpl();
			contentHandler.startElement("","schemalist","schemalist",schemalistAttr);

			int count = 0;
			for(SchemaData sd : the_schemaList){
				AttributesImpl schemaAttr = new AttributesImpl();
				schemaAttr.addAttribute("","name","name","CDATA",""+sd.getName());
				schemaAttr.addAttribute("","path","path","CDATA",""+sd.getPath());
				schemaAttr.addAttribute("","type","type","CDATA",""+sd.getType());
				schemaAttr.addAttribute("","current","current","CDATA",""+sd.getCurrent());
				contentHandler.startElement("","schema","schema",schemaAttr);
				String cmnt = sd.getComment();
				if(cmnt!=null){
					contentHandler.characters(cmnt.toCharArray(),0,cmnt.length());
				}
				contentHandler.endElement("","schema","schema");
				count++;
			}

			contentHandler.endElement("","schemalist","schemalist");
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

/**
* Returns a list of SchemaData objects for all schema files; both system supplied and those uploaded by the user into the current collection.
* @param the_OwnerPath The Owner's subpath.
* @param the_DirStr The user collection name.
* @param the_current The name of the currently selected schema file.
* @return A Vector of SchemaData objects.
*/
	public Vector<SchemaData> getSchemaList(String the_OwnerPath,String the_DirStr,String the_current){
		Vector<SchemaData> schemaList = new Vector<SchemaData>();

		//LIST ALL SYSTEM SUPPLIED SCHEMAS
		Vector<String> standardSchemaList = listFiles(Global.SCHEMA_DIR,".rng");
		//TEMPORARILY FILTER OUT SELECTED SCHEMAS PER BZRIANS APRIL 12 2013 EMAIL
		//for(String schemaName : standardSchemaList){
		for(int i=0;i<standardSchemaList.size();i++){
			String schemaName = (String)standardSchemaList.get(i);
			if((schemaName.equals("tei_its.rng"))||(schemaName.equals("tei_tite.rng"))||(schemaName.equals("tei_odds.rng"))
					||(schemaName.equals("tei_math.rng"))||(schemaName.equals("tei_svg.rng"))||(schemaName.equals("tei_xinclude.rng"))){
				standardSchemaList.remove(i);
			}
		}
		String defaultcomment = "In a future iteration Brian will be able to put an appropriate comment here that tells something about this particular schema";
		String url = "";

		//System.out.println("THERE ARE BETTER WAYS TO DO THIS RATHER THAN LOOK UP THE FILE EACH TIME WE NEED A LIST BUT THIS IS OK FOR NOW");
		//System.out.println("THEN AGAIN THIS IS NOT MUCH WORSE THAN STORING AN IN MEMORY COPY FOR EACH COLLECTION");
		Properties prop = new Properties();
		boolean commentsvalid = true;
		try {
			prop.load(new FileInputStream(Global.SCHEMA_DESC));
		}catch(Exception ex){
			commentsvalid = false;
		}
		for(String schemaName : standardSchemaList){
			int iscurrent = 0;
			if((the_current!=null)&&(the_current.startsWith("1"))){
				if(the_current.substring(1).equals(schemaName)){
					iscurrent = 1;
				}
			}
			String comment = "";
			if(commentsvalid){
				comment = prop.getProperty(schemaName,defaultcomment);
			}
			//PUT tei-xl.rng AT THE TOP OF THE LIST AND SET IT AS DEFAULT FIRST TIME
			if(schemaName.equals("tei-xl.rng")){
				schemaList.insertElementAt(new SchemaData(schemaName,Global.SCHEMA_DIR+schemaName,1,iscurrent,comment,url),0);
			}else{
				schemaList.add(new SchemaData(schemaName,Global.SCHEMA_DIR+schemaName,1,iscurrent,comment,url));
			}
		}

		//LIST ALL USER SUPPLIED SCHEMAS
		if(the_DirStr!=null){
			String userschemaDir = Global.BASE_USER_DIR+"/"+the_OwnerPath+"/"+the_DirStr+"/convert/";
			//System.out.println("SCHEMALIST <"+userschemaDir+">");
			Vector<String> userSchemaList = listFiles(userschemaDir,".rng");
			String comment = "";
			url = "";
			for(String schemaName : userSchemaList){
				int iscurrent = 0;
				if((the_current!=null)&&(the_current.startsWith("2"))){
					if(the_current.substring(1).equals(schemaName)){
						iscurrent = 1;
					}
				}
				schemaList.add(new SchemaData(schemaName,userschemaDir+schemaName,2,iscurrent,comment,url));
			}
		}

		return schemaList;
	}

/**
* List the names of files in a path with a particular suffix.
* @param the_dirpath The path where the files reside.
* @param the_suffix The suffix that a file should have to be listed by this method.
* @return A vector of the names of the files.
*/
	private Vector<String> listFiles(String the_dirpath,String the_suffix){
		Vector<String> dir = new Vector<String>();
		try {
			File f = new File(the_dirpath);
			if(f!=null){
				String files[] = f.list();
				if(files!=null){
					for (int i=0; i<files.length; i++) {
						if((files[i]!=null)&&(the_suffix!=null)&&(files[i].endsWith(the_suffix))){
							dir.add(files[i]);
						}
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return dir;
	}
}

