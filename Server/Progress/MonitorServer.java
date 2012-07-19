//MonitorServer.java

package Server.Progress;


import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServiceableGenerator;
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

public class MonitorServer extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;

private int m_value = 0;
private String m_msg = null;
private int m_dispose = 0;

private String m_pathname = null;
private long m_pathsize = 0;

private int m_iter = 1;
private int m_count = 0;
private boolean m_new = false;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING MonitorServer SERVICE");
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		if(m_OwnerID==null){
			m_session.setAttribute("userid",m_OwnerID);
		}

		m_new = false;
		String newStr = request.getParameter("new");
		if((newStr!=null)&&(newStr.equals("true"))){
			m_new = true; 
		}

		m_dispose = 0;
		m_msg = null;
	}

	public void generate() throws SAXException, ProcessingException {
		System.out.println("MonitorServer:generate()");
		MonitorData md = (MonitorData)m_session.getAttribute("monitor_data");
		if(md!=null){
			//System.out.println("FIRST TEST<"+md.isnew()+">");
			if(m_new){
				md.setnew(true);
			}
		}else{
			System.out.println("FIRST TEST<NULL>");
			md = new MonitorData(true,"___",0);
			m_session.setAttribute("monitor_data",md);
		}
		while((m_count < 10)&&(md.isnew()==false)){
			try {
				Thread.sleep(2000);
				md = (MonitorData)m_session.getAttribute("monitor_data");
				//md.Display();
				m_count++;
				//System.out.println("\tCOUNT<"+m_count+"> SID<"+m_session.getId()+">");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(md.getvalue() >= 100){
			md.setvalue(100);
			m_dispose = 1;
		}
		m_count = 0;
		md.setnew(false);
		m_session.setAttribute("monitor_data",md);
		generateResponseXML(contentHandler,md,m_dispose);
	}

	public void generateResponseXML(ContentHandler contentHandler,MonitorData the_md,int the_dispose) throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();

System.out.println("MonitorServer:");
			AttributesImpl monitorAttr = new AttributesImpl();
			monitorAttr.addAttribute("","dispose","dispose","CDATA",""+the_dispose);
			contentHandler.startElement("","Monitor","Monitor",monitorAttr);
			while(the_md!=null){
				//the_md.Display();
				AttributesImpl progAttr = new AttributesImpl();
				progAttr.addAttribute("","value","value","CDATA",""+the_md.getvalue());
				contentHandler.startElement("","Prog","Prog",progAttr);
				if(the_md.getname()!=null){
					contentHandler.characters(the_md.getname().toCharArray(),0,the_md.getname().length());
				}
				contentHandler.endElement("","Prog","Prog");
				the_md = the_md.getNext();
			}
			contentHandler.endElement("","Monitor","Monitor");

			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("ERROR0");
			e.printStackTrace();
		}
	}

	public long getFileSize(String the_pathname){
		long fs = 0;
		System.out.println("PN<"+the_pathname+">");
		try {
			File f = new File(the_pathname);
			if(f!=null){
				System.out.println("NOTNULL<"+f.getName()+">");
				fs = f.length();
			}
		}catch(Exception ex){
			fs = -1;
		}
		return fs;
	}
	

	public static int getIntFromString(String the_str){
		int ret = 0;
		if(the_str==null){
			return ret;
		}
		try{
			ret = Integer.decode(the_str).intValue();
		}catch(Exception ex){
			ret = 0;
		}
		return ret;
	}
}

