package edu.unl.abbot.vicar.Adorn;


import edu.unl.abbot.vicar.Global;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Map;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import org.apache.cocoon.environment.Response;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServletGenerator;
import org.apache.cocoon.environment.http.HttpResponse;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.http.HttpRequest;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import org.apache.cocoon.servlet.multipart.Part;

/**
* A Generator which launches conversion, correction, and validation of data files via AbbotConvert.java and reports progress.
* <br />
* If no directory path or no conversion schema is provided then this method generates XML containing an error message and returns it via an ServletOutputStream.
* If all necessary information is supplied then {@link AbbotConvert} is called as a separate thread.  This thread continues and monitors the progress of AbbotConvert which includes Abbot and then validation.
* <br />
* Under normal operation with javascript enabled this generator is used only by StreamClient.html.
* <br />
* This thread responds with XML output in a 'long polling' scheme such that it does not terminate until all actions are complete.  The XML stream pauses while there is no change in status and the HTTP connection is kept open.
* <br />
* If javascript is not enabled then Vicar.html will call MAStreamServer.java directly to produce MAStreamServer.html.  This call will use the parameter act=noblock.
* This will launch the AbbotConvert thread and immediately return an XML response which contains an attribute of mode=0.  This 0 mode results in MAStreamServer.html calling itself again with parameter act=join which blocks and waits for the completion of the AbbotConvert thread.  Once the act=join call is complete MAStreamServer.java:generate() returns with an XML response with mode=1 which results in MAStreamServer.html returning the browser to the Vicar.html page.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class MAStreamServer extends ServletGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_OwnerPath;
//private String m_ConvStr;
private String m_DirStr;
private String m_ActStr;

//private String m_AbbotNS;
//private String m_AbbotCustom;

private String m_macorpus;
private String m_mausechoice;
@Override
	public void dispose() {
		super.dispose();
	}

@Override
	public void recycle() {
		super.recycle();
	}

@Override
	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
	}

/**
* Collects session attributes and parameters <i>userid, userpath, Conversion Directory, Data Directory, and Action.</i>
*/
@Override
	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		response = ObjectModelHelper.getResponse(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_OwnerPath = (String)m_session.getAttribute("userpath");
		//m_ConvStr = request.getParameter("conv");
		m_DirStr = request.getParameter("dir");
		m_ActStr = request.getParameter("act");
		//m_AbbotNS = request.getParameter("abbotns");
		//m_AbbotCustom = request.getParameter("abbotcustom");
		m_macorpus = request.getParameter("macorpus");
		m_mausechoice = request.getParameter("mausechoice");
		if((m_mausechoice!=null)&&(m_mausechoice.equals("on"))){
			m_mausechoice = "true";
		}else{
			m_mausechoice = "false";
		}
		//System.out.println("ADORN MACORPUS<"+m_macorpus+"> USECHOICE<"+m_mausechoice+">");
	}

/**
* Responds with XML based on how it is called.
* See class level description for more information.
*/
@Override
	public void generate() throws SAXException, ProcessingException {
		try {
			ServletOutputStream sos = ((HttpResponse)response).getOutputStream();
			//((HttpResponse)response).setContentType("xml");

//System.out.println("MASERVER TEST <"+m_ActStr+">");
		if((m_ActStr!=null)&&(!m_ActStr.equals("test"))){
			//if(m_ConvStr==null){
			//	m_ConvStr = (String)m_session.getAttribute("SAVE:schema:"+m_DirStr);
			//}else{
			//	m_session.setAttribute("SAVE:schema:"+m_DirStr,m_ConvStr);
			//}
			if(m_DirStr==null){//||(m_ConvStr==null)){
				sos.println("<datastream>");
				sos.println("<values name='Please Select a conversion schema'>");
				sos.println("</values>");
				sos.println("</datastream>");
				sos.flush();
				sos.close();
				return;
			}
		}

			//MAKE ADORN DIRECTORY
			String userdir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/"+m_DirStr;
			new File(userdir+"/adorn/").mkdirs();
			System.out.println("RETROFIT CREATION OF ADORN DIRECTORY");
			if(m_ActStr==null){
				RemoteMA rma = new RemoteMA();
				Vector<String> fileList = rma.listFiles(userdir+"/output/",".xml");
				sos.println("<datastream>");
				String topline = "File Adornment";
				sos.println("<values name='"+topline+"'>");
				sos.flush();
				if(fileList!=null){
					int total = fileList.size();
					int cnt = 0;
					for(String fileName : fileList){
						//System.out.println("STARTING<"+fileName+">");
						rma.upload(RemoteMA.DEFAULT_URL,m_macorpus,m_mausechoice,userdir+"/output/"+fileName,userdir+"/adorn/"+fileName);
						cnt++;
						int pct = (int)((100*cnt)/total);
						//System.out.println("\tPCT<"+pct+">");
						sos.println(pct);
						sos.flush();
					}
				}
				sos.println("</values>");
				sos.flush();
	
				sos.println("</datastream>");
				sos.flush();
				sos.close();
			}else if(m_ActStr.equals("test")){
				//RemoteMA rma = new RemoteMA();
				//rma.upload(RemoteMA.DEFAULT_URL,m_macorpus,userdir+"/output/",userdir+"/adorn/");
				sos.println("<datastream>");
				String topline = "File Adornment with MACORPUS<"+m_macorpus+"> MAUSECHOICE<"+m_mausechoice+">";
				sos.println("<values name='"+topline+"'>");
				sos.flush();
				Thread.sleep(1000);
				sos.println("10");
				sos.flush();
				Thread.sleep(1000);
				sos.println("30");
				sos.flush();
				Thread.sleep(1000);
				sos.println("50");
				sos.flush();
				Thread.sleep(1000);
				sos.println("80");
				sos.flush();
				Thread.sleep(1000);
				sos.println("100");
				sos.flush();
				sos.println("</values>");
				sos.flush();
	
				sos.println("</datastream>");
				sos.flush();
				sos.close();
			}else if(m_ActStr.equals("noblock")){
//FSS				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr,m_AbbotNS,m_AbbotCustom);
//FSS				ac.start();
//FSS				m_session.setAttribute("THREADID",""+ac.getId());
				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
//FSS				dsAttr.addAttribute("","tid","tid","CDATA",""+ac.getId());
				dsAttr.addAttribute("","mode","mode","CDATA","0");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}else if(m_ActStr.equals("join")){
				/**********
				//WAITS FOR COMPLETION OF THE AbbotConvert THREAD AND REPORTS THAT COMPLETION AS WELL AS mode = 1
				//MODE 1 INDICATES THAT MAStreamServer.html SHOULD RETURN TO Vicar.html
				int tid = getIntFromString((String)m_session.getAttribute("THREADID"));
				Thread findthread = null;
				ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
				ThreadGroup parentGroup;
				while((parentGroup = rootGroup.getParent())!=null){
					rootGroup = parentGroup;
					Thread[] threads = new Thread[rootGroup.activeCount()];
					while(rootGroup.enumerate(threads,true)==threads.length){
						threads = new Thread[threads.length*2];
					}
					for(Thread t : threads){
						if(t!=null){
							if(tid == t.getId()){
								findthread = t;
								break;
							}
						}
					}
				}
				findthread.join();
				**********/
				RemoteMA rma = new RemoteMA();
				Vector<String> fileList = rma.listFiles(userdir+"/output/",".xml");
				if(fileList!=null){
					int total = fileList.size();
					int cnt = 0;
					for(String fileName : fileList){
						//System.out.println("STARTING<"+fileName+">");
						rma.upload(RemoteMA.DEFAULT_URL,m_macorpus,m_mausechoice,userdir+"/output/"+fileName,userdir+"/adorn/"+fileName);
						cnt++;
						int pct = (int)((100*cnt)/total);
						//System.out.println("\tPCT<"+pct+">");
					}
				}
	
				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
				dsAttr.addAttribute("","mode","mode","CDATA","1");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}
/****
			if(m_ActStr==null){
				//CALLED BY StreamClient.html WHEN JAVASCRIPT IS ENABLED
				//FOLLOWS A 'long polling' SCHEME TO RETURN INFORMATION ON PROGRESS WHILE KEEPING THE HTTP CONNECTION OPEN
				String userdir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/"+m_DirStr;
				int outputcount = fileCount(userdir+"/output/");
	
//FSS				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr,m_AbbotNS,m_AbbotCustom);
//FSS				ac.start();
				int pauseval = 200;
				sos.println("<datastream>");
				sos.flush();
				Thread.sleep(pauseval);

				int totalpct = 0;
				String topline = "File Adornment with MACORPUS<"+m_macorpus+"> MAUSECHOICE<"+m_mausechoice+">";
				sos.println("<values name='"+topline+"|Abbot'>");
				sos.flush();
				int pct = 0;
				int adorncount = 0;
				while(pct<100){
					adorncount = fileCount(userdir+"/adorn/");
					pct = (int)(100*adorncount)/outputcount;
					totalpct = pct/2;
					sos.println(""+totalpct+"|"+pct);
					sos.flush();
					Thread.sleep(pauseval);
				}
				pct = 0;
				sos.println("</values>");
				sos.println("<values name='"+topline+"|Validation'>");
				while(pct<100){
					int validcount = fileCountValid(userdir+"/valid/");
					pct = (int)(100*validcount)/outputcount;
					totalpct = 50+pct/2;
					sos.println(""+totalpct+"|"+pct);
					sos.flush();
					Thread.sleep(pauseval);
				}
				sos.println("100|100");
				sos.flush();
				sos.println("</values>");
				sos.flush();
	
				sos.println("</datastream>");
				sos.flush();
				sos.close();
			}else if(m_ActStr.equals("test")){
				int pauseval = 200;
				sos.println("<datastream>");
				sos.flush();
				Thread.sleep(pauseval);
				int totalpct = 0;
				for(int part=1;part<5;part++){
					sos.println("<values name='TOTAL TEST|PART TEST"+part+"'>");
					sos.flush();
					for(int partpct=0;partpct<=100;partpct+=5){
						totalpct += partpct/40;
						sos.println(""+totalpct+"|"+partpct);
						sos.flush();
						Thread.sleep(pauseval);
					}
					if(part>=5){	
						sos.println("100|100");
						sos.flush();
					}
					sos.println("</values>");
					sos.flush();
				}

				sos.println("</datastream>");
				sos.flush();
				sos.close();
			}else if(m_ActStr.equals("noblock")){
				//USED BY NON JAVASCRIPT INVOKATION IN Vicar.html OF MAStreamServer.html
				//STARTS THE AbbotConvert THREAD AND RETURNS AN XML RESPONSE IMMEDIATELY
				//RESPONDING WITH MODE '0' RESULTS IN MAStreamServer.html CALLING ITSELF AGAIN WITH ActStr==join
				//WHERE IT BLOCKS WAITING FOR THE COMPLETION OF THE AbbotConvert THREAD
				//AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr);
//FSS				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr,m_AbbotNS,m_AbbotCustom);
//FSS				ac.start();
//FSS				m_session.setAttribute("THREADID",""+ac.getId());

				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
//FSS				dsAttr.addAttribute("","tid","tid","CDATA",""+ac.getId());
				dsAttr.addAttribute("","mode","mode","CDATA","0");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();

			}else if(m_ActStr.equals("join")){
				//WAITS FOR COMPLETION OF THE AbbotConvert THREAD AND REPORTS THAT COMPLETION AS WELL AS mode = 1
				//MODE 1 INDICATES THAT MAStreamServer.html SHOULD RETURN TO Vicar.html
				int tid = getIntFromString((String)m_session.getAttribute("THREADID"));
				Thread findthread = null;
				ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
				ThreadGroup parentGroup;
				while((parentGroup = rootGroup.getParent())!=null){
					rootGroup = parentGroup;
					Thread[] threads = new Thread[rootGroup.activeCount()];
					while(rootGroup.enumerate(threads,true)==threads.length){
						threads = new Thread[threads.length*2];
					}
					for(Thread t : threads){
						if(t!=null){
							if(tid == t.getId()){
								findthread = t;
								break;
							}
						}
					}
				}
				findthread.join();
				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
				dsAttr.addAttribute("","mode","mode","CDATA","1");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}
****/
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

/**
* Returns the number of files in the path.
* @param the_path The path of the directory.
* @return The number of files.
*/
	private int fileCount(String the_path){
		int count = 0;
		try {
			File f = new File(the_path);
			if(f!=null){
				String[] xx = f.list();
				for(int i=0;i<xx.length;i++){
					if(xx[i].endsWith(".xml")&&(!xx[i].startsWith("."))){
						count++;
					}
				}
			}
		} catch(Exception ex){
		}
		return count;
	}

	private int fileCountValid(String the_path){
		int count = 0;
		try {
			File f = new File(the_path);
			if(f!=null){
				String[] xx = f.list();
				for(int i=0;i<xx.length;i++){
					if(xx[i].endsWith("html")){
						count++;
					}
				}
			}
		} catch(Exception ex){
		}
		return count;
	}
/**
* Converts a text representation of an integer into the integer.  If no conversion can be made then 0 is returned.
* @param the_str The text representation of an integer.
* @return The integer represented by the text or 0 if no such representation.
*/
	private int getIntFromString(String the_str){
		int ret = 0;
		if(the_str!=null){
			try {
				ret = Integer.decode(the_str).intValue();
			}catch(NumberFormatException nfex){
			}
		}
		return ret;
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

}


