//Simple.java

package Core;

import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.Map;

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


public class Simple extends ServiceableGenerator implements Disposable {

private Request m_request;

private String m_OwnerID;
private int m_mode = 0;
private String m_SessionID = "";
private String m_PersonName = null;
private String m_PersonEmail = null;
private String m_Password = null;
private String m_msg = null;
private int m_msgcode = 0;

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
		ServiceSelector selector = (ServiceSelector)
				manager.lookup(DataSourceComponent.ROLE+"Selector");
		//m_dataSource = (DataSourceComponent)selector.select("dbname");
	}

	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		Session session = m_request.getSession();
		m_SessionID = session.getId();
		System.out.println("SIMPLE SESSION ID<"+m_SessionID+">");
		m_OwnerID = (String)session.getAttribute("userid");

		m_mode = getIntFromString(m_request.getParameter("mode"));
		m_PersonName = (String)session.getAttribute("personname");
		m_PersonEmail = (String)session.getAttribute("personemail");

		m_msgcode = 0;
		m_msg = null;

		if(m_OwnerID==null){
			//NOT LOGGED IN
			if(m_mode>0){
				m_mode = 0;
			}
		}else{
			//LOGGED IN
			if(m_mode==0){
				m_mode = 1;
			}
		}
	}

	public void generate() throws SAXException, ProcessingException {
		String RemoteAddr = m_request.getRemoteAddr();
		String RemoteHost = m_request.getRemoteHost();
		try {
			contentHandler.startDocument();
				AttributesImpl simpleAttr = new AttributesImpl();
				simpleAttr.addAttribute("","IP","IP","CDATA",RemoteAddr);
				simpleAttr.addAttribute("","host","host","CDATA",""+RemoteHost);
				simpleAttr.addAttribute("","SessionID","SessionID","CDATA",""+m_SessionID);
				//simpleAttr.addAttribute("","emailaddr","emailaddr","CDATA",""+m_EmailAddr);
				simpleAttr.addAttribute("","personname","personname","CDATA",""+m_PersonName);
				simpleAttr.addAttribute("","personemail","personemail","CDATA",""+m_PersonEmail);
				simpleAttr.addAttribute("","mode","mode","CDATA",""+m_mode);
				contentHandler.startElement("","simple","simple",simpleAttr);
				AttributesImpl msgAttr = new AttributesImpl();
				msgAttr.addAttribute("","code","code","CDATA",""+m_msgcode);
				contentHandler.startElement("","msg","msg",msgAttr);
				if(m_msg!=null){
					contentHandler.characters(m_msg.toCharArray(),0,m_msg.length());
				}
				contentHandler.endElement("","msg","msg");
				contentHandler.endElement("","simple","simple");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

	public long generateKey() {
		// Default non-caching behaviour. We will implement this later.
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
}

/***
	public CacheValidity generateValidity() {
		// Default non-caching behaviour. We will implement this later.
		return null;
	}
***/

