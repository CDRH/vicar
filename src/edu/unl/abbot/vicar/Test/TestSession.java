package edu.unl.abbot.vicar.Test;


import java.util.*;

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

public class TestSession extends ServiceableGenerator implements Disposable {

private Request m_request;
private Session m_session;

private String m_SessionID = "";

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		m_session = m_request.getSession();
	}


	public void generate() throws SAXException, ProcessingException {
		String RemoteAddr = m_request.getRemoteAddr();
		String ForwardFor = m_request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			RemoteAddr = ForwardFor;
		}
		String SessionID = m_session.getId();

		try {
			contentHandler.startDocument();
			AttributesImpl openidAttr = new AttributesImpl();
			openidAttr.addAttribute("","IP","IP","CDATA",RemoteAddr);
			openidAttr.addAttribute("","SessionID","SessionID","CDATA",""+SessionID);
			contentHandler.startElement("","test","test",openidAttr);
			contentHandler.endElement("","test","test");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
}


