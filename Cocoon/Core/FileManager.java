//FileManager.java

package Core;

import edu.unl.abbot.Abbot;

import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.Map;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;


public class FileManager extends ServiceableGenerator implements Disposable {

private Request m_request;

private String m_OwnerID;
private String m_SessionID = "";
private String m_msg = null;
private int m_msgcode = 0;

public static String BASE_USER_DIR = "/tmp/vicar/";
private String m_DirStr = null;
private String m_ActStr = null;
private String m_RenStr = null;
private String m_FilenameStr = null;
private String m_performStr = null;

private int m_isnew = 0;

	public void dispose() {
		super.dispose();
		//manager.release(m_dataSource);
		//m_dataSource = null;
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
		ServiceSelector selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		Session session = m_request.getSession();
		m_SessionID = session.getId();
		m_OwnerID = (String)session.getAttribute("userid");
		if(m_OwnerID==null){
			m_OwnerID = "FRANK"; //If not set in OpenSignin.  This is to enable testing outside of Simple or OpenID
			session.setAttribute("userid",m_OwnerID);
		}
		System.out.println("FILEMANAGER OWNERID<"+m_OwnerID+">");
		m_isnew = 0;

		m_DirStr = m_request.getParameter("dir");
		m_ActStr = m_request.getParameter("act");
		m_RenStr = m_request.getParameter("ren");
		m_FilenameStr = m_request.getParameter("fn");
		m_performStr = m_request.getParameter("perform");
	}

	public void generate() throws SAXException, ProcessingException {
		System.out.println("***********");
		String RemoteHost = m_request.getRemoteHost();
		String RemoteAddr = m_request.getRemoteAddr();

		String msg = null;

		if(m_ActStr==null){
		}else if(m_ActStr.equalsIgnoreCase("del")){
			if(m_DirStr!=null){
				String resp = removeDir(BASE_USER_DIR,m_OwnerID,m_DirStr);
				if(resp==null){
					System.out.println("NOT DELETED");
				}else{
					System.out.println("DELETED<"+resp+">");
					m_DirStr = null; //GO TO LISTING
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("unzip")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)){
				System.out.println("UNZIP FN<"+m_FilenameStr+">");
				String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
				if(zu.unzip(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(BASE_USER_DIR,m_OwnerID,m_DirStr,"input",m_FilenameStr);
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("conv")){
			if(m_DirStr!=null){
				String indir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				String outdir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/";
				System.out.println("CONVERT FILES IN <"+indir+"> and put in <"+outdir+">");
				Abbot abbot = new Abbot();
				abbot.convert(indir,outdir);
			}
		}else if(m_ActStr.equalsIgnoreCase("zip")){
			if(m_DirStr!=null){
				String outdir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/";
				ZipUtil zu = new ZipUtil();
					//zip(String the_sourcedir,String the_suffix,String the_destfile){
				zu.zip(outdir,"xml",outdir+"/"+m_DirStr+".zip");
			}
		}else if(m_ActStr.equalsIgnoreCase("get")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)){
				System.out.println("DOWNLOAD FN<"+m_FilenameStr+">");
			}
		}

		//PRODUCE OUTPUT
		Vector<FileData> mydirs = null;
		Vector<String> inputfiles = null;
		Vector<String> outputfiles = null;
		if(m_DirStr==null){ //LIST ALL DIRECTORIES FOR USER OwnerID
			mydirs = listDirs(BASE_USER_DIR,m_OwnerID);
		}else if(m_DirStr.equalsIgnoreCase("new")){
			if((m_RenStr==null)||(m_RenStr.length() < 1)){
				if(m_performStr==null){
					createDir(BASE_USER_DIR,m_OwnerID,"NEW");
					inputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"input");
					outputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"output");
					m_isnew = 1;
				}else{
					//System.out.println("CANCELLING NEW COLLECTiON");
					String resp = removeDir(BASE_USER_DIR,m_OwnerID,m_DirStr);
					mydirs = listDirs(BASE_USER_DIR,m_OwnerID);
					m_DirStr = null;
				}
			}else{
				if(m_performStr==null){
				}else if(m_performStr.equalsIgnoreCase("Save")){
//NEED TO FILTER THE TEXT OF RenStr (i.e. no '../' or nefarious escape caracters)
					if(m_RenStr.matches("[a-zA-Z]+$")){
//NEED TO CHECK THAT RenStr IS UNIQUE SO IT DOESNT WRITE OVER EXISTING COLLECTION
						renameDir(BASE_USER_DIR,m_OwnerID,"NEW",m_RenStr);
						m_DirStr = m_RenStr;
						inputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"input");
						outputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"output");
						m_isnew = 0;
					}else{
						msg = "Names can only contain letters a-z and A-Z with no spaces.";
						m_DirStr = "new";
						inputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"input");
						outputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"output");
						m_isnew = 1;
					}
				}else{
					//System.out.println("CANCELLING NEW COLLECTiON");
					String resp = removeDir(BASE_USER_DIR,m_OwnerID,m_DirStr);
					mydirs = listDirs(BASE_USER_DIR,m_OwnerID);
					m_DirStr = null;
				}
			}
		}else{ //LIST FILES IN THE DIRECTORY
			inputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"input");
			outputfiles = listFiles(BASE_USER_DIR,m_OwnerID,m_DirStr,"output");
		}

		int maxcount = 10;
		try {
			contentHandler.startDocument();
				AttributesImpl simpleAttr = new AttributesImpl();
				simpleAttr.addAttribute("","IP","IP","CDATA",RemoteAddr);
				//simpleAttr.addAttribute("","host","host","CDATA",""+RemoteHost);
				simpleAttr.addAttribute("","SessionID","SessionID","CDATA",""+m_SessionID);
				contentHandler.startElement("","simple","simple",simpleAttr);
				AttributesImpl msgAttr = new AttributesImpl();
				if(msg!=null){
					contentHandler.startElement("","msg","msg",msgAttr);
					contentHandler.characters(msg.toCharArray(),0,msg.length());
					contentHandler.endElement("","msg","msg");
				}
				if(mydirs!=null){
					AttributesImpl mydirsAttr = new AttributesImpl();
					mydirsAttr.addAttribute("","maxcount","maxcount","CDATA",""+maxcount);
					mydirsAttr.addAttribute("","count","count","CDATA",""+mydirs.size());
					contentHandler.startElement("","dirs","dirs",mydirsAttr);
					for(FileData userdir : mydirs){
						AttributesImpl dirAttr = new AttributesImpl();
						dirAttr.addAttribute("","name","name","CDATA",""+userdir.getName());
						dirAttr.addAttribute("","count","count","CDATA",""+userdir.getCount());
						contentHandler.startElement("","dir","dir",dirAttr);
						contentHandler.endElement("","dir","dir");
					}
					contentHandler.endElement("","dirs","dirs");
				}else{
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
							}else{
								fileAttr.addAttribute("","zip","zip","CDATA","0");
							}
							contentHandler.startElement("","file","file",fileAttr);
							contentHandler.endElement("","file","file");
						}
						contentHandler.endElement("","inputfiles","inputfiles");
					}
					if(outputfiles!=null){
						AttributesImpl outputfilesAttr = new AttributesImpl();
						outputfilesAttr.addAttribute("","dirname","dirname","CDATA",""+m_DirStr);
						outputfilesAttr.addAttribute("","count","count","CDATA",""+inputfiles.size());
						outputfilesAttr.addAttribute("","new","new","CDATA",""+m_isnew);
						contentHandler.startElement("","outputfiles","outputfiles",outputfilesAttr);
						for(String filename : outputfiles){
							AttributesImpl fileAttr = new AttributesImpl();
							fileAttr.addAttribute("","name","name","CDATA",""+filename);
							fileAttr.addAttribute("","op","op","CDATA","1");
/***
							if(filename==null){
							}else if(filename.endsWith(".xml")){
								fileAttr.addAttribute("","zip","zip","CDATA","1");
							}else if(filename.endsWith(".zip")){
								fileAttr.addAttribute("","zip","zip","CDATA","2");
							}else{
								fileAttr.addAttribute("","zip","zip","CDATA","0");
							}
***/
							contentHandler.startElement("","file","file",fileAttr);
							contentHandler.endElement("","file","file");
						}
						contentHandler.endElement("","outputfiles","outputfiles");
					}
				}
				contentHandler.endElement("","simple","simple");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

/****/
//FSS
	public String createDir(String the_base,String the_owner,String the_name){
		String dirpath = the_base+"/"+the_owner+"/"+the_name;

		new File(dirpath).mkdirs();
		new File(dirpath+"/input/").mkdirs();
		new File(dirpath+"/output/").mkdirs();
		return dirpath;
	}

	public String renameDir(String the_base,String the_owner,String the_oldname,String the_newname){
		String oldpath = the_base+"/"+the_owner+"/"+the_oldname;
		String newpath = the_base+"/"+the_owner+"/"+the_newname;

		System.out.println("RENAME<"+oldpath+"> TO <"+newpath+">");
		File fo = new File(oldpath);
		File fn = new File(newpath);
		boolean fr = fo.renameTo(fn);
		return newpath;
	}

	public Vector<FileData> listDirs(String the_base,String the_owner){
		String dirpath = the_base+"/"+the_owner;

		Vector<FileData> dir = new Vector<FileData>();
		try {
			File f = new File(dirpath);
			if((f!=null)&&(f.isDirectory())){
				File userdirlist[] = f.listFiles();
				for (File userdir: userdirlist){
					if(userdir.isDirectory()){
						System.out.println("DIR<"+userdir.getName()+">");
						int inputcnt = 0;
						int outputcnt = 0;
						File subdirlist[] = userdir.listFiles();
						for (File subdir : subdirlist){
							System.out.println("\tSUBDIR<"+subdir.getName()+">");
							String subfiles[] = subdir.list();
							if(subdir.getName().equalsIgnoreCase("input")){
								inputcnt = subfiles.length;
							}else if(subdir.getName().equalsIgnoreCase("output")){
								outputcnt = subfiles.length;
							}
						}
						dir.add(new FileData(m_OwnerID,userdir.getName(),inputcnt));
						System.out.println("INPUT CNT<"+inputcnt+"> OUTPUT CNT<"+outputcnt+">");
						//userfile.lastModified();
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return dir;
	}

	public String removeDir(String the_base,String the_owner,String the_DirStr){
		String dirpath = the_base+"/"+the_owner+"/"+the_DirStr;

		System.out.println("DELETE<"+dirpath+">");
		File f = new File(dirpath);
		if(f!=null){
			if(f.isDirectory()){
				//DIR MUST BE EMPTY BEFORE DELETION
				System.out.println("DELETE DIR<"+f.getName()+">");
				File dirlist[] = f.listFiles();
//CURRENTLY NOT RECURSIVE FOR SAFETY
				for (File userdir : dirlist){
					System.out.println("\tDELETE IN DIR<"+userdir.getName()+"> ISDIR<"+userdir.isDirectory()+">");
					if(userdir.isDirectory()){
						File subdirlist[] = userdir.listFiles();
						System.out.println("LEN<"+subdirlist.length+">");
						for (File subdir : subdirlist){
							System.out.println("\t\tDELETING SUBDIR<"+subdir.getName()+">");
							//File filelist[] = subdir.listFiles();
							//for (File sf : filelist){
							//	System.out.println("\t\t\tDEL SUBDIR FILE?"+sf.delete()+">");
							//}
							System.out.println("\t\tDEL SUBDIR?"+subdir.delete()+">");
							boolean df = subdir.delete();
						}
					}else{
						File filelist[] = userdir.listFiles();
						for (File sf : filelist){
							System.out.println("\t\t\tDEL DIR FILE?"+sf.delete()+">");
						}
					}
					System.out.println("\tDEL USRDIR?"+userdir.delete()+">");
				}
			}
			System.out.println("DEL?"+f.delete()+">");
		}
		return dirpath;
	}

/****
	public void createFile(String the_base,String the_owner,String the_dir,String the_description){
		String dirpath = the_base+"/"+the_owner+"/"+the_dir+"/README.txt";

		try {
			FileOutputStream fos = new FileOutputStream(dirpath);
			fos.write(the_description.getBytes());
			fos.close();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
****/

	public Vector<String> listFiles(String the_base,String the_owner,String the_dir,String the_sub){
		String filepath = the_base+"/"+the_owner+"/"+the_dir+"/"+the_sub+"/";

		Vector<String> dir = new Vector<String>();
		try {
			File f = new File(filepath);
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

	public String removeFile(String the_base,String the_owner,String the_DirStr,String the_sub,String the_Filename){
		String filepath = the_base+"/"+the_owner+"/"+the_DirStr+"/"+the_sub+"/"+the_Filename;

		File f = new File(filepath);
		boolean df = f.delete();
		if(df){
			return filepath;
		}else{
			return null;
		}
	}

}

/***
	public long generateKey() {
		// Default non-caching behavior. We will implement this later.
		return 0;
	}

	public static int getIntFromString(String the_str){
		int ret = 0;
		if(the_str==null){
			return ret;
		}
		try{
			ret = Integer.decode(the_str).intValue();
		}catch(Exception ex){
		}
		return ret;
	}

	public CacheValidity generateValidity() {
		// Default non-caching behaviour. We will implement this later.
		return null;
	}

	public String getTimestampString(){
		//GregorianCalendar now = new GregorianCalendar();
		Date nowDate = new Date();
		//now.setTime(nowDate);
		return ""+nowDate.getTime();
	}

***/


