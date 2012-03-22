//FileManager.java

package Core;

import edu.unl.abbot.Abbot;

import java.util.Vector;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
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

private String m_OwnerID;
private String m_msg = null;
private int m_msgcode = 0;

public static String BASE_USER_DIR = "/tmp/vicar/";
private String m_DirStr = null;
private String m_ActStr = null;
private String m_RenStr = null;
private String m_FilenameStr = null;
private String m_performStr = null;
private int m_isnew = 0;

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
		Session session = request.getSession();
		m_OwnerID = (String)session.getAttribute("userid");
		if(m_OwnerID==null){
			m_OwnerID = "FRANK"; //If not set in OpenSignin.  This is to enable testing outside of Simple or OpenID
			session.setAttribute("userid",m_OwnerID);
		}
		m_isnew = 0;

		m_DirStr = request.getParameter("dir");
		m_ActStr = request.getParameter("act");
		m_RenStr = request.getParameter("ren");
		m_FilenameStr = request.getParameter("fn");
		m_performStr = request.getParameter("perform");

		//if(m_performStr==null){
		//}else{
			m_filePart = (Part)request.get("file_upload");
		//}
	}

	public void generate() throws SAXException, ProcessingException {
		String msg = null;
		//GET commands
		if(m_ActStr==null){
		}else if(m_ActStr.equalsIgnoreCase("del")){
			if(m_DirStr!=null){
				String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
				String resp = removeDir(dirpath);
				if(resp==null){
				}else{
					//System.out.println("DELETED<"+resp+">");
					m_DirStr = null; //GO TO LISTING
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("unzip")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)){
				String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
				if(zu.unzip(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(dirpath+m_FilenameStr);
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("untar")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)&&(m_FilenameStr.endsWith(".tar"))){
				String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
				if(zu.untar(dirpath+m_FilenameStr,dirpath)>=0){
					String resp = removeFile(dirpath+m_FilenameStr);
				}
			}
		}else if(m_ActStr.equalsIgnoreCase("untargz")){
			if((m_DirStr!=null)&&(m_FilenameStr!=null)&&(m_FilenameStr.endsWith(".tar.gz"))){
				String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				ZipUtil zu = new ZipUtil();
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
				String indir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/";
				String outdir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/";
				//String config = "/Users/franksmutniak/Desktop/Github/CDRH/abbot/config/abbot_config.xml";
				//String config = "/Users/franksmutniak/Desktop/Github/CDRH/abbot/target/tei-xl.rng";
				//String config = "http://abbot.unl.edu/tei-xl.rng";
				Abbot abbot = new Abbot();
				abbot.convert(indir,outdir);
				//abbot.convert(indir,outdir,config);
			}
		}else if(m_ActStr.equalsIgnoreCase("zip")){
			if(m_DirStr!=null){
				String outdir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/";
				ZipUtil zu = new ZipUtil();
				zu.zip(outdir,"xml",outdir+"/"+m_DirStr+".zip");
			}
		}else if(m_ActStr.equalsIgnoreCase("targz")){
			if(m_DirStr!=null){
				String outdir = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/";
				ZipUtil zu = new ZipUtil();
				String newtar = outdir+"/"+m_DirStr+".tar";
				zu.tar(outdir,".xml",newtar);
				if(zu.gzip(newtar,newtar+".gz")>=0){
					System.out.println("REMOVAL OF INTERMEDIATE TAR<"+newtar+"> HAPPENING?");
					String resp = removeFile(outdir+newtar);
				}
			}
		}

		//PRODUCE OUTPUT
		Vector<FileData> mydirs = null;
		Vector<String> inputfiles = null;
		Vector<String> outputfiles = null;
		if(m_DirStr==null){ //LIST ALL DIRECTORIES FOR USER OwnerID
			mydirs = listDirs(BASE_USER_DIR+"/"+m_OwnerID);
		}else if(m_DirStr.equalsIgnoreCase("new")){
			//System.out.println("DIR<"+m_DirStr+"> REN<"+m_RenStr+"> OWNER<"+m_OwnerID+">");
			if((m_RenStr==null)||(m_RenStr.length() < 1)){
				if(m_performStr==null){
					String dirpath = BASE_USER_DIR+"/"+m_OwnerID;
					createDir(dirpath+"/NEW");
					dirpath = dirpath+"/"+m_DirStr+"/";
					inputfiles = listFiles(dirpath+"input");
					outputfiles = listFiles(dirpath+"output");
					m_isnew = 1;
				}else{
					//System.out.println("CANCELLING NEW COLLECTiON");
					String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
					String resp = removeDir(dirpath+"/"+m_DirStr);
					mydirs = listDirs(dirpath);
					m_DirStr = null;
				}
			}else{
				//System.out.println("PERFORM<"+m_performStr+">");
				if(m_performStr==null){
				}else if(m_performStr.equalsIgnoreCase("Save")){ //POST command
					if(m_RenStr.matches("[a-zA-Z]+$")){ //FILTERING FOR NON DIR COMPATIBLE OR NEFARIOUS (i.e.'../' OR ESCAPE) CHAR.
//NEED TO CHECK THAT RenStr IS UNIQUE SO IT DOESNT WRITE OVER AN EXISTING COLLECTION
						String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_RenStr+"/";
						//System.out.println("DIRPATH<"+dirpath+">");
						if(isDir(dirpath) == false){
							//System.out.println("\tDOESNT Exist already - being renamed");
							renameDir(BASE_USER_DIR+"/"+m_OwnerID,"NEW",m_RenStr);
							m_DirStr = m_RenStr;
							dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
							m_isnew = 0;
						}else{
							//System.out.println("\tExists already");
							msg = "A collection by that name already exists.";
							m_isnew = 1;
						}
						inputfiles = listFiles(dirpath+"input");
						outputfiles = listFiles(dirpath+"output");
					}else{
						msg = "Names can only contain letters a-z and A-Z with no spaces.";
						m_DirStr = "new";
						String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
						inputfiles = listFiles(dirpath+"input");
						outputfiles = listFiles(dirpath+"output");
						m_isnew = 1;
					}
				}else{
					String dirpath = BASE_USER_DIR+"/"+m_OwnerID;
					String resp = removeDir(dirpath+"/"+m_DirStr);
					mydirs = listDirs(dirpath);
					m_DirStr = null;
				}
			}
		}else{ //LIST FILES IN THE DIRECTORY
			String dirpath = BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/";
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
						File outfile = new File(dirpath+"/input/"+fileName);
						FileOutputStream fos = new FileOutputStream(outfile);
						while((len=fis.read(buf))>0){
							fos.write(buf,0,len);
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}else{
					System.out.println("NULL");
				}
			}
			inputfiles = listFiles(dirpath+"input");
			outputfiles = listFiles(dirpath+"output");
		}

		int maxcount = 10;
		try {
			contentHandler.startDocument();
				AttributesImpl simpleAttr = new AttributesImpl();
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
						dirAttr.addAttribute("","count","count","CDATA",""+userdir.getInputCount());
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
					//THIS DIR ONLY CONTAINS ABBOT OUTPUT SO SHOULD ALWAYS BE .xml FILES OR TAR/TAR.GZ/ZIP FILES OF .xml FILES
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

/**
* Needs error checking!
*/
	public String createDir(String the_dirpath){

		new File(the_dirpath).mkdirs();
		new File(the_dirpath+"/input/").mkdirs();
		new File(the_dirpath+"/output/").mkdirs();
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
						dir.add(new FileData(m_OwnerID,userdir.getName(),inputcnt));
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
		System.out.println("DELETE<"+the_dirpath+">");
		File f = new File(the_dirpath);
		if(f!=null){
			if(f.isDirectory()){
				//DIR MUST BE EMPTY BEFORE DELETION
				System.out.println("DELETE DIR<"+f.getName()+">");
				File dirlist[] = f.listFiles();
//CURRENTLY NOT RECURSIVE FOR SAFETY
				for (File userdir : dirlist){
					if(userdir.isDirectory()){
						File subdirlist[] = userdir.listFiles();
						System.out.println("LEN<"+subdirlist.length+">");
						for (File subdir : subdirlist){
							boolean df = subdir.delete();
							//System.out.println("\t\t\tDEL SUBDIR?"+df+">");
						}
					}else{
						File filelist[] = userdir.listFiles();
						for (File sf : filelist){
							boolean df = sf.delete();
							//System.out.println("\t\t\tDEL SUBDIR?"+df+">");
						}
					}
					boolean ddf = userdir.delete();
					//System.out.println("\tDEL USRDIR?"+ddf+">");
				}
			}
			boolean dddf = f.delete();
			System.out.println("PATH DEL?"+dddf+">");
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


