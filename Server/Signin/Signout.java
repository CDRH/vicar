package Server.Signin;

import Server.Global;
import Server.LogWriter;

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
* A generator which signs a user out of their account by clearing session information and returning the user to the Sign in page with messages appropriate to their session termination. 
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class Signout extends ServiceableGenerator implements Disposable {

private Request m_request;
private Session m_session;

private String m_OwnerID;
private String m_SessionID = "";

private int m_delay = 0;
private int m_msgcode = 0;
private String m_msgtext = null;

@Override
	public void dispose() {
		super.dispose();
	}

@Override
	public void recycle() {
		super.recycle();
	}

@Override
	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
	}

@Override
	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		m_session = m_request.getSession();
		m_OwnerID = (String)m_session.getAttribute("userid");
	}

@Override
	public void generate() throws SAXException, ProcessingException {
		String RemoteAddr = m_request.getRemoteAddr();
		String ForwardFor = m_request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			RemoteAddr = ForwardFor;
		}
		m_session.setAttribute("IPADDR",RemoteAddr);
		String SessionID = m_session.getId();

		m_OwnerID = null;
		m_session.setAttribute("userid",null);
		m_session.setAttribute("userpath",null);
		String openid = (String)m_session.getAttribute("openid");
		LogWriter.msg(RemoteAddr,"LOGOUT,"+openid);
		String m_url = null;
		if(openid==null){
			m_url = Global.URL_SIGNIN;
		}else if(openid.equals("none")){
			m_url = Global.URL_SIGNIN;
		}else if(openid.equals("anonymous")){
			m_url = Global.URL_SIGNIN;
		}else if(openid.equals("google")){
			m_url = Global.URL_SIGNIN+"?mode=-1";
		}else if(openid.equals("yahoo")){
			m_url = Global.URL_SIGNIN+"?mode=-2";
		}
		m_session.invalidate();
		SigninXML.generateSigninXML(contentHandler,"ID","ACT",Global.TITLE,m_delay,m_url,m_msgcode,m_msgtext,"",0);
	}
}

