//FileManager.java

package Server.Core;

import Server.Global;

import edu.unl.abbot.Abbot;

import java.util.Vector;
import java.util.Map;
import java.util.Date;
import java.util.Enumeration;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.activity.Disposable;

import org.apache.cocoon.servlet.multipart.Part;

/**
* Implements the collections structure in a unix directory and provides a way for the user to invoke Abbot.
* Allows creation of individual collections.  Allows user to upload files, uncompress if necessary, convert the files using Abbot, and then download them.  If necessary the user can compress the files before download.
* Requires abbot-0.3.3-standalone.jar
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class FileManager extends ServiceableGenerator implements Disposable {

private Session m_session;

private String m_OwnerID;
private String m_PersonName = null;
private String m_PersonEmail = null;

private String m_mode = "0";
private String m_msg = null;
private int m_msgcode = 0;

//public static String DEFAULT_RNG = "tei_all.rng";
//public static String DEFAULT_RNGDIR = "/Users/franksmutniak/Desktop/abbottestdata/schema/";
//public static String DEFAULT_RNGFN = "DEFAULT";

//public static String BASE_USER_DIR = "/tmp/vicar/";
//public static String CONVERT_SUFFIX = ".rng";
private String m_DirStr = null;
private String m_ActStr = null;
private String m_RenStr = null;
private String m_ConvStr = null;
private String m_FilenameStr = null;
private String m_performStr = null;
private int m_isnew = 0;

private String m_CurrentStr = null;
private Part m_filePart;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
	}

	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();
		m_OwnerID = (String)m_session.getAttribute("userid");
		if(m_OwnerID!=null){
			m_PersonName = (String)m_session.getAttribute("personname");
			m_PersonEmail = (String)m_session.getAttribute("personemail");

			m_DirStr = request.getParameter("dir");
			m_ActStr = request.getParameter("act");
			m_RenStr = request.getParameter("ren");
			m_FilenameStr = request.getParameter("fn");
			m_performStr = request.getParameter("perform");
			m_ConvStr = request.getParameter("conv");
			if(m_ConvStr==null){
				m_CurrentStr = (String)m_session.getAttribute("SAVE:schema:"+m_DirStr);
				//System.out.println("GET ATTR<"+m_CurrentStr+">");
			}else{
				m_CurrentStr = m_ConvStr;
				//System.out.println("SET ATTR<"+m_CurrentStr+">");
				m_session.setAttribute("SAVE:schema:"+m_DirStr,m_CurrentStr);
				SessionSaver.save(m_session,Global.BASE_USER_DIR+"/"+m_OwnerID+"/session.txt");
			}
			m_filePart = (Part)request.get("file_upload");
			m_isnew = 0;
		}
		m_mode = request.getParameter("mode");

		if((m_DirStr!=null)&&(!m_DirStr.equalsIgnoreCase("new"))){
			//PREVENT USERS FROM SELECTING A NON EXISTENT DIRECTORY VIA EDITING THE URL
			String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
			if(!isDir(dirpath)){
				m_DirStr = null;
			}
		}

		if((m_mode!=null)&&(m_mode.equals("1"))){
			System.out.println("NEW LOGIN");
			if(m_OwnerID!=null){
				SessionSaver.load(m_session,Global.BASE_USER_DIR+"/"+m_OwnerID+"/session.txt");
				//SessionSaver.Display(m_session);
			}
		}
	}

	public void generate() throws SAXException, ProcessingException {
		if(m_OwnerID == null){
			contentHandler.startDocument();
				AttributesImpl signinAttr = new AttributesImpl();
				signinAttr.addAttribute("","mode","mode","CDATA",""+m_mode);
				contentHandler.startElement("","signin","signin",signinAttr);
				contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
			return;
		}

		m_msg = null;
		m_msgcode = 0;
		//GET commands
		if(m_ActStr==null){
		}else if(m_ActStr.equalsIgnoreCase("del")){
			if(m_DirStr!=null){
				m_msg = null;//"Delete this collection?";
				m_msgcode = -1;
			}
		}else if(m_ActStr.equalsIgnoreCase("delconfirm")){
			if(m_DirStr!=null){
				String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
				String resp = removeDir(dirpath);
				if(resp==null){
				}else{
					m_DirStr = null; //GO TO LISTING
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("unzip")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)){
				String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
				if(zu.unzip(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(dirpath+m_FilenameStr);
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("untar")){
			//System.out.println("UNTAR FN<"+m_FilenameStr+">");
			if((m_DirStr!=null)&&(m_FilenameStr!=null)&&(m_FilenameStr.endsWith(".tar"))){
				String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				//System.out.println("UNTAR<"+dirpath+"> FN<"+m_FilenameStr+">");
				ZipUtil zu = new ZipUtil();
				if(zu.untar(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(dirpath+m_FilenameStr);
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("untargz")){
			//System.out.println("FN<"+m_FilenameStr+">");
			if((m_DirStr!=null)&&(m_FilenameStr!=null)&&(m_FilenameStr.endsWith(".tar.gz"))){
				String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
				//System.out.println("UNTARGZ<"+dirpath+"> FN<"+m_FilenameStr+">");
				if(zu.ungzip(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(dirpath+m_FilenameStr);
					int indx = m_FilenameStr.indexOf(".gz");
					m_FilenameStr = m_FilenameStr.substring(0,indx);
					if(zu.untar(dirpath+m_FilenameStr,dirpath)>=0){
						resp = removeFile(dirpath+m_FilenameStr);
						resp = removeFile(dirpath+m_FilenameStr+".gz");
					}
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("conv")){
			if(m_DirStr!=null){
				String colldir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
				String indir = colldir+"/input/";
				String convdir = colldir+"/convert/";
				String outdir = colldir+"/output/";
				String validdir = colldir+"/valid/";
				String resp = cleanDir(outdir);
				//System.out.println("ABBOT CLEARED OUTPUT DIRECTORY<"+resp+">");
				resp = cleanDir(validdir);
				//System.out.println("CLEARED VALIDATION DIRECTORY<"+resp+">");
				System.out.println("ABBOT CONVERT BEGIN");
				Abbot abbot = new Abbot();
/****/
				int convtype = 0;
				if(m_ConvStr!=null){
					if(m_ConvStr.startsWith("1")){
						convdir = Global.SCHEMA_DIR;
						m_ConvStr = m_ConvStr.substring(1);
					}else if(m_ConvStr.startsWith("2")){
						convtype = 1;
						m_ConvStr = m_ConvStr.substring(1);
					}
					System.out.println("*****CONVERT USING <"+convdir+m_ConvStr+">");
					try {
						abbot.convert(indir,outdir,convdir+m_ConvStr);
					}catch(Exception ex){
					}
				}
/****/

				Vector<String> outputfiles = listFiles(outdir,".xml");
				//for(String ofn : outputfiles){
				//	System.out.println("OFN<"+ofn+">");
				//}

				System.out.println("SED CORRECTION BEGIN");
				System.out.println("CHANGE OUTPUT FILES WITH SED");

				ProcMngr pm = new ProcMngr(Global.SEDPATH,"-i_sed",outdir);
				String convURL = "http:\\/\\/abbot.unl.edu\\/"+m_ConvStr;
				if(convtype == 1){
					convURL = m_ConvStr;
				}
				String email = m_OwnerID.replace("__","@");
				String emailsuffix = email;
				if(emailsuffix!=null){
					int suffstrt = emailsuffix.indexOf("@");
					if(suffstrt>0){
						emailsuffix = emailsuffix.substring(0,suffstrt);
					}
				}

				Vector<String> convList = new Vector<String>();
				convList.add("s/http:\\/\\/abbot.unl.edu\\/tei_all.rng/"+convURL+"/g");
				//convList.add("s/bpz/"+email+"/g");
				convList.add("s/bpz/"+emailsuffix+"/g");
				convList.add("s/Pytlik Zillig,/"+email+"/g");
				convList.add("s/[ \t]*B.<\\/name> Conversion to TEI/<\\/name> Conversion to TEI/g");

				pm.cleanup(outputfiles,convList);
				System.out.println("SED CORRECTION END");

				System.out.println("VALIDATE BEGIN");
				JingUtil ju = new JingUtil();
				for(String ofn : outputfiles){
					ju.writeReportToFile(convdir+m_ConvStr,outdir+"/"+ofn,validdir+"/"+ofn.replace(".xml",".html"),m_ConvStr,ofn);
				}
				System.out.println("VALIDATE END");
				Vector<Integer> el = ju.getErrorList();
				m_session.setAttribute("SAVE:validationerrors:"+m_DirStr,el);
				SessionSaver.save(m_session,Global.BASE_USER_DIR+"/"+m_OwnerID+"/session.txt");
			}
		}

		//PRODUCE OUTPUT
		Vector<FileData> mydirs = null;
		Vector<String> inputfiles = null;
//FSS
		Vector<String> convertfiles = null;
		Vector<String> outputfiles = null;
		Vector<String> validfiles = null;
		if(m_DirStr==null){ //LIST ALL COLLECTIONS FOR USER OwnerID
			mydirs = listDirs(Global.BASE_USER_DIR+"/"+m_OwnerID);
		}else if(m_DirStr.equalsIgnoreCase("new")){
			if((m_RenStr==null)||(m_RenStr.length() < 1)){ //CREATE NEW COLLECTION
				if(m_performStr==null){
					String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID;
					createDir(dirpath+"/NEW");
					dirpath = dirpath+"/"+m_DirStr+"/";
					inputfiles = listFiles(dirpath+"input");
//FSS
					convertfiles = listFiles(dirpath+"convert");
					outputfiles = listFiles(dirpath+"output",".xml");
					validfiles = listFiles(dirpath+"valid");
					m_isnew = 1;
				}else{
					//System.out.println("CANCELLING NEW COLLECTiON");
					String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID;
					String resp = removeDir(dirpath+"/"+m_DirStr);
					mydirs = listDirs(dirpath);
					m_DirStr = null;
				}
			}else{ //RENAME NEW COLLECTION
				if(m_performStr==null){
				}else if(m_performStr.equalsIgnoreCase("Save")){ //POST command
					String dirpath = null;
					if(m_RenStr.matches("[a-zA-Z]+$")){ //FILTERING FOR NON DIR COMPATIBLE OR NEFARIOUS (i.e.'../' OR ESCAPE) CHAR.
//NEED TO CHECK THAT RenStr IS UNIQUE SO IT DOESNT WRITE OVER AN EXISTING COLLECTION
						dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_RenStr+"/";
						if(isDir(dirpath) == false){
							//System.out.println("\tDOESNT Exist already - being renamed");
							renameDir(Global.BASE_USER_DIR+"/"+m_OwnerID,"NEW",m_RenStr);
							m_DirStr = m_RenStr;
							dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
							m_isnew = 0;
						}else{
							//System.out.println("\tExists already");
							m_msg = "A collection by that name already exists.";
							m_msgcode = 1;
							m_isnew = 1;
						}
					}else{
						m_msg = "Names can only contain letters a-z and A-Z with no spaces.";
						m_msgcode = 1;
						m_DirStr = "new";
						dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
						m_isnew = 1;
					}
					inputfiles = listFiles(dirpath+"input");
//FSS
					convertfiles = listFiles(dirpath+"convert");
					outputfiles = listFiles(dirpath+"output",".xml");
					validfiles = listFiles(dirpath+"valid");
				}else{ //CANCEL NEW COLLECTION NAME
					String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID;
					String resp = removeDir(dirpath+"/"+m_DirStr);
					mydirs = listDirs(dirpath);
					m_DirStr = null;
				}
			}
		}else{ //LIST FILES IN THE COLLECTION
			String dirpath = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
			//BUT FIRST CHECK FOR NON JAVASCRIPT ENABLED FORM POST OF FILES
			if(m_performStr==null){
			}else if(m_performStr.equalsIgnoreCase("Upload")){ //POST command
				if(m_filePart!=null){ //ADD_IMAGE
					try {
						InputStream fis = m_filePart.getInputStream();
						String fileName = m_filePart.getFileName();
						String fileType = m_filePart.getMimeType();
						System.out.println("UPLOAD FN<"+fileName+"> TYPE<"+fileType+"> TO DIR<"+m_DirStr+">");
						int len = 0;
						byte buf[] = new byte[1024];
						String filedirpath = dirpath+"/input/";
						if((fileName!=null)&&(fileName.toLowerCase().endsWith(".rng"))){
							filedirpath = dirpath+"/convert/";
						}
						File outfile = new File(filedirpath+fileName);
						FileOutputStream fos = new FileOutputStream(outfile);
						while((len=fis.read(buf))>0){
							fos.write(buf,0,len);
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}else{
					//System.out.println("NO FILE UPLOADED");
				}
			}
			inputfiles = listFiles(dirpath+"input");
//FSS
			convertfiles = listFiles(dirpath+"convert");
			outputfiles = listFiles(dirpath+"output",".xml");
			validfiles = listFiles(dirpath+"valid");
		}

		int maxcount = 10;
		try {
			contentHandler.startDocument();
				AttributesImpl filemanagerAttr = new AttributesImpl();
				filemanagerAttr.addAttribute("","personname","personname","CDATA",""+m_PersonName);
				filemanagerAttr.addAttribute("","personemail","personemail","CDATA",""+m_PersonEmail);
				contentHandler.startElement("","filemanager","filemanager",filemanagerAttr);

				AttributesImpl msgAttr = new AttributesImpl();
				msgAttr.addAttribute("","msgcode","msgcode","CDATA",""+m_msgcode);
				contentHandler.startElement("","msg","msg",msgAttr);
				if(m_msg!=null){
					contentHandler.characters(m_msg.toCharArray(),0,m_msg.length());
				}
				contentHandler.endElement("","msg","msg");

				if(mydirs!=null){
					AttributesImpl mydirsAttr = new AttributesImpl();
					mydirsAttr.addAttribute("","maxcount","maxcount","CDATA",""+maxcount);
					mydirsAttr.addAttribute("","count","count","CDATA",""+mydirs.size());
					contentHandler.startElement("","dirs","dirs",mydirsAttr);
					for(FileData userdir : mydirs){
						AttributesImpl dirAttr = new AttributesImpl();
						dirAttr.addAttribute("","name","name","CDATA",""+userdir.getName());
						dirAttr.addAttribute("","count","count","CDATA",""+userdir.getInputCount());
						dirAttr.addAttribute("","date","date","CDATA",""+userdir.lastModified());
						contentHandler.startElement("","dir","dir",dirAttr);
						contentHandler.endElement("","dir","dir");
					}
					contentHandler.endElement("","dirs","dirs");
				}else{
					if(inputfiles!=null){
						AttributesImpl collectionAttr = new AttributesImpl();
						collectionAttr.addAttribute("","dirname","dirname","CDATA",""+m_DirStr);
						collectionAttr.addAttribute("","new","new","CDATA",""+m_isnew);
						contentHandler.startElement("","collection","collection",collectionAttr);

						if(inputfiles!=null){
							AttributesImpl inputfilesAttr = new AttributesImpl();
							inputfilesAttr.addAttribute("","dirname","dirname","CDATA",""+m_DirStr);
							inputfilesAttr.addAttribute("","count","count","CDATA",""+inputfiles.size());
							inputfilesAttr.addAttribute("","new","new","CDATA",""+m_isnew);
							contentHandler.startElement("","inputfiles","inputfiles",inputfilesAttr);
							for(String filename : inputfiles){
								AttributesImpl fileAttr = new AttributesImpl();
								fileAttr.addAttribute("","name","name","CDATA",""+filename);
								fileAttr.addAttribute("","op","op","CDATA","0");
								if(filename==null){
								}else if(filename.endsWith(".xml")){
									fileAttr.addAttribute("","zip","zip","CDATA","1");
								}else if(filename.endsWith(".zip")){
									fileAttr.addAttribute("","zip","zip","CDATA","2");
								}else if(filename.endsWith(".tar")){
									fileAttr.addAttribute("","zip","zip","CDATA","3");
								}else if(filename.endsWith(".tar.gz")){
									fileAttr.addAttribute("","zip","zip","CDATA","4");
								}else{
									fileAttr.addAttribute("","zip","zip","CDATA","0");
								}
								contentHandler.startElement("","file","file",fileAttr);
								contentHandler.endElement("","file","file");
							}
							contentHandler.endElement("","inputfiles","inputfiles");
						}

						if(m_DirStr!=null){
							SchemaList sl = new SchemaList();
							Vector<SchemaData> sdl = sl.getSchemaList(m_OwnerID,m_DirStr,m_CurrentStr);
							sl.generateSchemaXML(contentHandler,sdl);
						}

						//THIS DIR ONLY CONTAINS ABBOT OUTPUT SO SHOULD ALWAYS BE .xml FILES
						if(outputfiles!=null){
							@SuppressWarnings("unchecked")
							Vector<Integer> errorlist = (Vector<Integer>)m_session.getAttribute("SAVE:validationerrors:"+m_DirStr);
							AttributesImpl outputfilesAttr = new AttributesImpl();
							outputfilesAttr.addAttribute("","dirname","dirname","CDATA",""+m_DirStr);
							outputfilesAttr.addAttribute("","count","count","CDATA",""+outputfiles.size());
							outputfilesAttr.addAttribute("","new","new","CDATA",""+m_isnew);
							contentHandler.startElement("","outputfiles","outputfiles",outputfilesAttr);
							int count = 0;
							for(String filename : outputfiles){
								AttributesImpl fileAttr = new AttributesImpl();
								fileAttr.addAttribute("","name","name","CDATA",""+filename);
								fileAttr.addAttribute("","vname","vname","CDATA",""+filename.replace(".xml",".html"));
								fileAttr.addAttribute("","op","op","CDATA","1");
								if((errorlist!=null)&&(count < errorlist.size())){
									fileAttr.addAttribute("","errors","errors","CDATA",""+errorlist.get(count));
								}
								contentHandler.startElement("","file","file",fileAttr);
								contentHandler.endElement("","file","file");
								count++;
							}
							contentHandler.endElement("","outputfiles","outputfiles");
						}

						contentHandler.endElement("","collection","collection");
					}
				}
				contentHandler.endElement("","filemanager","filemanager");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

/**
* Needs error checking!
*/
	public String createDir(String the_dirpath){

		new File(the_dirpath).mkdirs();
		new File(the_dirpath+"/input/").mkdirs();
		new File(the_dirpath+"/convert/").mkdirs();
		new File(the_dirpath+"/output/").mkdirs();
		new File(the_dirpath+"/valid/").mkdirs();
		return the_dirpath;
	}

	public String renameDir(String the_dirpath,String the_oldname,String the_newname){
		String oldpath = the_dirpath+"/"+the_oldname;
		String newpath = the_dirpath+"/"+the_newname;

		File fo = new File(oldpath);
		File fn = new File(newpath);
		boolean fr = fo.renameTo(fn);
		return newpath;
	}

	public boolean isDir(String the_dirpath){
		boolean isdir = false;
		if(the_dirpath!=null){
			try {
				File f = new File(the_dirpath);
				if(f!=null){
					if(f.isDirectory()){
						isdir = true;
					}
				}
			}catch(Exception e){ 
				e.printStackTrace();
			}
		}
		return isdir;
	}

/****
	public boolean isFile(String the_filepath){
		boolean isfile = false;
		if(the_filepath!=null){
			try {
				File f = new File(the_filepath);
				if(f!=null){
					if(!f.isDirectory()){
						isdir = true;
					}
				}
			}catch(Exception e){ 
				e.printStackTrace();
			}
		}
		return isdir;
	}
****/

	public Vector<FileData> listDirs(String the_dirpath){
		Vector<FileData> dir = new Vector<FileData>();
		try {
			File f = new File(the_dirpath);
			if((f!=null)&&(f.isDirectory())){
				File userdirlist[] = f.listFiles();
				for (File userdir: userdirlist){
					if(userdir.isDirectory()){
						//System.out.println("DIR<"+userdir.getName()+">");
						int inputcnt = 0;
						int outputcnt = 0;
						File subdirlist[] = userdir.listFiles();
						for (File subdir : subdirlist){
							//System.out.println("\tSUBDIR<"+subdir.getName()+">");
							String subfiles[] = subdir.list();
							if(subdir.getName().equalsIgnoreCase("input")){
								inputcnt = subfiles.length;
							}else if(subdir.getName().equalsIgnoreCase("output")){
								outputcnt = subfiles.length;
							}
						}
						dir.add(new FileData(m_OwnerID,userdir.getName(),inputcnt,""+(new Date(userdir.lastModified()))));
						//System.out.println("INPUT CNT<"+inputcnt+"> OUTPUT CNT<"+outputcnt+">");
						//userfile.lastModified();
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return dir;
	}

	public String removeDir(String the_dirpath){
		//System.out.println("DELETE<"+the_dirpath+">");
		File f = new File(the_dirpath);
		if(f!=null){
			if(f.isDirectory()){
				//DIR MUST BE EMPTY BEFORE DELETION
				//System.out.println("DELETE DIR X<"+f.getName()+">");
				File dirlist[] = f.listFiles();
//CURRENTLY NOT RECURSIVE FOR SAFETY
				for (File userdir : dirlist){
					//System.out.println("DELETE DIR Y<"+userdir.getName()+">");
					if(userdir.isDirectory()){
						File subdirlist[] = userdir.listFiles();
						//System.out.println("LEN<"+subdirlist.length+">");
						for (File subdir : subdirlist){
							boolean df = subdir.delete();
							//System.out.println("\t\t\tDEL SUBDIR?"+df+">");
						}
					}else{
						File filelist[] = userdir.listFiles();
						for (File sf : filelist){
							//System.out.println("\t\t\tDEL FILE <"+sf.getName()+">");
							boolean df = sf.delete();
							//System.out.println("\t\t\tDEL FILE?"+df+">");
						}
					}
					boolean ddf = userdir.delete();
					//System.out.println("\tDEL USRDIR?"+ddf+">");
				}
			}
			boolean dddf = f.delete();
			//System.out.println("PATH DEL?"+dddf+">");
		}
		return the_dirpath;
	}

	public String cleanDir(String the_dirpath){
		System.out.println("CLEAN<"+the_dirpath+">");
		File f = new File(the_dirpath);
		if(f!=null){
			if(f.isDirectory()){
				//System.out.println("CLEAN DIR X<"+f.getName()+">");
				File dirlist[] = f.listFiles();
				for (File userdir : dirlist){
					//System.out.println("CLEAN DIR Y<"+userdir.getName()+">");
					boolean ddf = userdir.delete();
					//System.out.println("\tDEL USRDIR?"+ddf+">");
				}
			}
		}
		return the_dirpath;
	}

	public Vector<String> listFiles(String the_dirpath){
		Vector<String> dir = new Vector<String>();
		try {
			File f = new File(the_dirpath);
			if(f!=null){
				String files[] = f.list();
				if(files!=null){
					for (int i=0; i<files.length; i++) {
						dir.add(files[i]);
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return dir;
	}

	public Vector<String> listFiles(String the_dirpath,String the_suffix){
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

	public String removeFile(String the_filepath){
		String retval = null;
		try{
			File f = new File(the_filepath);
			if(f.delete()){
				retval = the_filepath;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return retval;
	}
}


