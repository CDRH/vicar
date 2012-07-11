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

		m_dispose = 0;
		m_msg = null;
		m_value = getIntFromString((String)m_session.getAttribute("monitor_value"));

		m_pathname = (String)m_session.getAttribute("monitor_pathname");
		m_pathsize = getIntFromString((String)m_session.getAttribute("monitor_pathsize"));
		System.out.println("PATH NAME<"+m_pathname+"> SIZE<"+m_pathsize+">");
		m_msg = m_pathname;
		long currsize = getFileSize(m_pathname);
		m_value = (int)((100.0*currsize)/m_pathsize);

		System.out.println("V<"+m_value+">");
/****/
		//m_value +=10;
/****/
		if(m_value > 100){
			m_value = 0;
			m_dispose = 1;
		}
		m_session.setAttribute("monitor_value",""+m_value);
	}

	public void generate() throws SAXException, ProcessingException {
		System.out.println("MonitorServer:generate()");
		generateResponseXML(contentHandler,m_value,m_msg,m_dispose);
	}

	public void generateResponseXML(ContentHandler contentHandler,int the_value,String the_msg,int the_dispose) throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();

			AttributesImpl monitorAttr = new AttributesImpl();
			monitorAttr.addAttribute("","value","value","CDATA",""+the_value);
			monitorAttr.addAttribute("","dispose","dispose","CDATA",""+the_dispose);
			contentHandler.startElement("","Monitor","Monitor",monitorAttr);
			if(the_msg!=null){
				AttributesImpl msgAttr = new AttributesImpl();
				contentHandler.startElement("","msg","msg",msgAttr);
				contentHandler.characters(the_msg.toCharArray(),0,the_msg.length());
				contentHandler.endElement("","msg","msg");
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

