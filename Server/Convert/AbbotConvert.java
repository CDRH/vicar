//AbbotConvert.java

package Server.Convert;

import Server.Global;
import Server.LogWriter;
import Server.SessionSaver;

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
import java.io.Serializable;

import org.apache.cocoon.environment.Session;

public class AbbotConvert extends Thread {

private String m_OwnerID;
private Session m_session;
private String m_dir;
private String m_ConvStr;

	public AbbotConvert(Session the_session,String the_dir,String the_ConvStr){
		m_session = the_session;
		m_dir = the_dir;
		m_ConvStr = the_ConvStr;
	}

	@Override
	public void run(){
		batchresult();
	}

	public void batchresult(){
		String remoteaddr = (String)m_session.getAttribute("IPADDR");
		m_OwnerID = (String)m_session.getAttribute("userid");
		if(m_dir!=null){
			String colldir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_dir;
			String indir = colldir+"/input/";
			String convdir = colldir+"/convert/";
			String outdir = colldir+"/output/";
			String validdir = colldir+"/valid/";
			String resp = cleanDir(outdir);
			resp = cleanDir(validdir);
			Abbot abbot = new Abbot();

			int convtype = 0;
			if(m_ConvStr==null){
				m_ConvStr = (String)m_session.getAttribute("SAVE:schema:"+m_dir);
			}else{
				m_session.setAttribute("SAVE:schema:"+m_dir,m_ConvStr);
			}
			if(m_ConvStr!=null){
				String wholeConvStr = m_ConvStr;
				long convstart = LogWriter.msg(remoteaddr,"CONVERT_BEGIN,"+m_OwnerID+"/"+m_dir+","+m_ConvStr);
				if(m_ConvStr.startsWith("1")){
					convdir = Global.SCHEMA_DIR;
					m_ConvStr = m_ConvStr.substring(1);
				}else if(m_ConvStr.startsWith("2")){
					convtype = 1;
					m_ConvStr = m_ConvStr.substring(1);
				}
//System.out.println("CONVERT DIR<"+indir+"> TO <"+outdir+"> WITH <"+convdir+m_ConvStr+">");
				try {
					abbot.convert(indir,outdir,convdir+m_ConvStr);
				}catch(Exception ex){
					String errmsg = ex.getMessage();
					if(errmsg!=null){
						errmsg = errmsg.replace(","," ");
					}
					LogWriter.msg(remoteaddr,"CONVERT_ERROR,"+ex.getMessage());
				}
//System.out.println("CONVERT DONE");

				LogWriter.msg(remoteaddr,"CORRECTION_BEGIN");
				Vector<String> outputfiles = listFiles(outdir,".xml");
				ProcMngr pm = new ProcMngr(Global.SED_PATH,"-i_sed",outdir);
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

				LogWriter.msg(remoteaddr,"VALIDATION_BEGIN");
				JingUtil ju = new JingUtil();
				for(String ofn : outputfiles){
					ju.writeReportToFile(convdir+m_ConvStr,outdir+"/"+ofn,validdir+"/"+ofn.replace(".xml",".html"),m_ConvStr,ofn);
				}
				Vector<Integer> el = ju.getErrorList();
				m_session.setAttribute("SAVE:validationerrors:"+m_dir,el);
				int totalerrors = 0;
				for(Integer err : el){
					totalerrors += err.intValue();
				}
				String ininfo = getFileInfo(indir,".xml");
				String outinfo = getFileInfo(outdir,".xml");
				LogWriter.msg(remoteaddr,"VALIDATION_END,"+ininfo+","+outinfo+","+totalerrors+","+m_OwnerID+"/"+m_dir+","+wholeConvStr+","+convstart);

				SessionSaver.save(m_session,Global.BASE_USER_DIR+"/"+m_OwnerID+"/session.txt");
			}
		}
	}


	public String cleanDir(String the_dirpath){
		//System.out.println("CLEAN<"+the_dirpath+">");
		File f = new File(the_dirpath);
		if(f!=null){
			if(f.isDirectory()){
				File dirlist[] = f.listFiles();
				for (File userdir : dirlist){
					boolean ddf = userdir.delete();
				}
			}
		}
		return the_dirpath;
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

	public String getFileInfo(String the_dirpath,String the_suffix){
		int filecount = 0;
		long filesize = 0L;
		try {
			File fpath = new File(the_dirpath);
			if((fpath!=null)&&(fpath.isDirectory())){
				File files[] = fpath.listFiles();
				if(files!=null){
					for (File f : files){
						if((f!=null)&&(f.getName().endsWith(the_suffix))){
							filecount++;
							filesize += f.length();
						}
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		if(filecount>0){
			return filecount+","+filesize;
		}else{
			return ",";
		}
	}
}

