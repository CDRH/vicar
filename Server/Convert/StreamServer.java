//StreamServer.java

package Server.Convert;

import Server.Global;
import Server.SessionSaver;

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

public class StreamServer extends ServletGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_ConvStr;
private String m_DirStr;
private String m_ActStr;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		response = ObjectModelHelper.getResponse(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_ConvStr = request.getParameter("conv");
		m_DirStr = request.getParameter("dir");
		m_ActStr = request.getParameter("act");
	}

	public void generate() throws SAXException, ProcessingException {
		try {
			ServletOutputStream sos = ((HttpResponse)response).getOutputStream();
			//((HttpResponse)response).setContentType("xml");

			if(m_ConvStr==null){
				m_ConvStr = (String)m_session.getAttribute("SAVE:schema:"+m_DirStr);
			}else{
				m_session.setAttribute("SAVE:schema:"+m_DirStr,m_ConvStr);
			}
			if((m_DirStr==null)||(m_ConvStr==null)){
				sos.println("<datastream>");
				sos.println("<values name='Please Select a conversion schema'>");
				sos.println("</values>");
				sos.println("</datastream>");
				sos.flush();
				sos.close();
				return;
			}

			if(m_ActStr==null){
				String userdir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
				int inputcount = fileCount(userdir+"/input/");
	
				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr);
				ac.start();
	
				int pauseval = 200;
				sos.println("<datastream>");
				sos.flush();
				Thread.sleep(pauseval);

				int totalpct = 0;
				String topline = "File Conversion with "+m_ConvStr.substring(1);
				sos.println("<values name='"+topline+"|Abbot'>");
				sos.flush();
				int pct = 0;
				while(pct<100){
					int outputcount = fileCount(userdir+"/output/");
					pct = (int)(100*outputcount)/inputcount;
					totalpct = pct/2;
					sos.println(""+totalpct+"|"+pct);
					sos.flush();
					Thread.sleep(pauseval);
				}
				pct = 0;
				sos.println("</values>");
				sos.println("<values name='"+topline+"|Validation'>");
				while(pct<100){
					int validcount = fileCount(userdir+"/valid/");
					pct = (int)(100*validcount)/inputcount;
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
			}else if(m_ActStr.equals("nojs")){
				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr);
				//System.out.println("THREADID<"+ac.getId()+">");
				ac.batchresult();

				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
				dsAttr.addAttribute("","mode","mode","CDATA","1");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}else if(m_ActStr.equals("noblock")){
				AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr);
				ac.start();
				//System.out.println("THREADID<"+ac.getId()+">");
				m_session.setAttribute("THREADID",""+ac.getId());

				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
				dsAttr.addAttribute("","tid","tid","CDATA",""+ac.getId());
				dsAttr.addAttribute("","mode","mode","CDATA","0");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}else if(m_ActStr.equals("join")){
				int tid = getIntFromString((String)m_session.getAttribute("THREADID"));
				Thread findthread = null;
				ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
				ThreadGroup parentGroup;
				while((parentGroup = rootGroup.getParent())!=null){
					rootGroup = parentGroup;
					//System.out.println("RG<"+rootGroup.getName()+">");
					Thread[] threads = new Thread[rootGroup.activeCount()];
					while(rootGroup.enumerate(threads,true)==threads.length){
						threads = new Thread[threads.length*2];
					}
					for(Thread t : threads){
						if(t!=null){
							//System.out.println("TID<"+t.getId()+">");
							if(tid == t.getId()){
								findthread = t;
								break;
							}
						}
					}
				}
				//System.out.println("FINDTHREAD<"+findthread.getId()+">");
				findthread.join();
				//System.out.println("FINDTHREAD JOINED");
				contentHandler.startDocument();
				AttributesImpl dsAttr = new AttributesImpl();
				dsAttr.addAttribute("","dirname","dirname","CDATA",m_DirStr);
				//dsAttr.addAttribute("","tid","tid","CDATA",""+ac.getId());
				dsAttr.addAttribute("","mode","mode","CDATA","1");
				contentHandler.startElement("","datastream","datastream",dsAttr);
				contentHandler.endElement("","datastream","datastream");
				contentHandler.endDocument();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private int fileCount(String the_path){
		int count = 0;
		try {
			File f = new File(the_path);
			if(f!=null){
				count = f.listFiles().length;
			}
		} catch(Exception ex){
		}
		return count;
	}

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
}


