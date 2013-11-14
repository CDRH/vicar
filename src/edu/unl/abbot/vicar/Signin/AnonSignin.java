package edu.unl.abbot.vicar.Signin;


import edu.unl.abbot.vicar.Global;
import edu.unl.abbot.vicar.LogWriter;

import java.util.Vector;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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

/**
* Allows anonymous signin for cases where user can accept no persistence of their data between sessions.
* This allows users to perform small jobs or test out the application with little commitment.
* NOT CURRENTLY USED.
* 
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class AnonSignin extends ServiceableGenerator implements Disposable {

private Request m_request;
private Session m_session;

private String m_OwnerID;
private String m_SessionID = "";

private int m_delay = 0;
private int m_msgcode = 0;
private String m_msgtext = null;

private String m_op = null;


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
		m_op = m_request.getParameter("op");
		m_OwnerID = (String)m_session.getAttribute("userid");
		//m_loginstatus = 0;
		m_delay = 0;
	}


	public void generate() throws SAXException, ProcessingException {
		String RemoteAddr = m_request.getRemoteAddr();
		String ForwardFor = m_request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			RemoteAddr = ForwardFor;
		}
		m_session.setAttribute("IPADDR",RemoteAddr);
		//String RemoteHost = m_request.getRemoteHost();
		String SessionID = m_session.getId();
		String m_url = null;

		if(m_OwnerID==null){
			//System.out.println("NOT LOGGED IN");
			//m_loginstatus = 0;
			//System.out.println("OP<"+m_op+">");
			if(m_op==null){
			}else if(m_op.equals("Test")){
				//System.out.println("RA<"+RemoteAddr+"> RH<"+RemoteHost+">");
				if(isValidIP(RemoteAddr)){
					//m_OwnerID = "ANONYMOUS";
					m_OwnerID = RemoteAddr;
					m_url = Global.URL_APPL;
					//m_loginstatus = 1;
					m_session.setAttribute("personname","anonymous");
					//m_session.setAttribute("personemail","");
					m_session.setAttribute("personemail",m_OwnerID);
					m_session.setAttribute("userid",m_OwnerID);
					m_session.setAttribute("userpath",m_OwnerID.replace("@","__"));
					m_session.setAttribute("openid","anonymous");
					m_session.setAttribute("newlogin","1");
					LogWriter.msg(RemoteAddr,"LOGIN,anonymous");
				}else{
					m_msgcode = 1;
					m_msgtext = "Not yet available.";
					//m_loginstatus = 0;
					m_OwnerID = null;
					m_url = Global.URL_APPL;
					m_delay = 5;
				}
			}
		}else{
			//System.out.println("LOGGED IN");
		}
		int dispose = 0;
		SigninXML.generateSigninXML(contentHandler,"ID","ACT",null,m_delay,m_url,m_msgcode,m_msgtext,"",dispose);
	}

	public boolean isValidIP(String the_IP){
		boolean retval = false;
		if(the_IP==null){
		}else if(the_IP.equals("127.0.0.1")){
			retval = true;
		}else if(the_IP.startsWith("129.93.")){
			retval = true;
		}else if(the_IP.startsWith("66.45.131.")){
			retval = true;
		}
		return retval;
	}
}


