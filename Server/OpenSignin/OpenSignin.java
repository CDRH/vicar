//OpenSignin.java

package Server.OpenSignin;

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

//JOpenId
import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

/**
* Requires JOpenId-1.08.jar from http://code.google.com/p/jopenid/
* This file is based in part on OpenIdServlet.java from the sample code.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/
public class OpenSignin extends ServiceableGenerator implements Disposable {

static final long ONE_HOUR = 3600000L;
static final long TWO_HOUR = ONE_HOUR * 2L;
static final long SIX_HOUR = ONE_HOUR * 6L;
static final long TEN_HOUR = ONE_HOUR * 10L;
static final String ATTR_MAC = "openid_mac";
static final String ATTR_ALIAS = "openid_alias";

private String URL_LOGIN_SFX = "/vicar/OpenSignin/OpenSignin.html";
private String URL_APPL = "../Core/FileManager.html?mode=1";
private String URL_APPL_LOGOUT = "../Core/FileManager.html";

private Request m_request;
private Session m_session;

private String m_OwnerID;
private String m_SessionID = "";

private int m_delay = 0;
private String m_msg = null;

private OpenIdManager oimanager;
private String m_op = null;
private int m_loginstatus = 0;


	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);

		//OPENIDMANAGER
		oimanager = new OpenIdManager();

		oimanager.setRealm(Global.URL_BASE);
		oimanager.setReturnTo(Global.URL_BASE+URL_LOGIN_SFX);
	}


	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		m_session = m_request.getSession();
		m_op = m_request.getParameter("op");
		m_OwnerID = (String)m_session.getAttribute("userid");
		m_loginstatus = 0;
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
			m_loginstatus = 0;
			if(m_op==null){
				//System.out.println("NONCE<"+m_nonce+">");
				int nn = checkNonce(m_request.getParameter("openid.response_nonce"));
				if(nn>0){
					String op_id = m_request.getParameter("openid.identity"); //CONSISTENT ACROSS ALL LOGINS
					String op_sig = m_request.getParameter("openid.sig");	//CHANGES EACH SESSION
					String op_signed = m_request.getParameter("openid.signed"); //VALUES RETURNED FROM IDENTITY PROVIDER
					//System.out.println("OPID<"+op_id+"> OPSIG<"+op_sig+"> OPSIGNED<"+op_signed+">");
					String alias = (String) m_session.getAttribute(ATTR_ALIAS);
					String op_firstname = m_request.getParameter("openid."+alias+".value.firstname");
					String op_lastname = m_request.getParameter("openid."+alias+".value.lastname");
					String op_email = m_request.getParameter("openid."+alias+".value.email");
					String op_lang = m_request.getParameter("openid."+alias+".value.language");

					if(op_email==null){
						m_OwnerID = "ANONYMOUS";
					}else{
						m_OwnerID = op_email.replace("@","__");
					}
					m_session.setAttribute("userid",m_OwnerID);

					String fn = "";
					if(op_firstname!=null){
						fn += op_firstname;
					}
					if(op_lastname!=null){
						fn += " "+op_lastname;
					}
					m_session.setAttribute("personname",fn);
					m_session.setAttribute("personemail",op_email);
					m_loginstatus = 1;
					m_url = URL_APPL;
					LogWriter.msg(RemoteAddr,"LOGIN,"+op_email);
				}
			}else if(m_op.equals("Test")){
					//System.out.println("RA<"+RemoteAddr+"> RH<"+RemoteHost+">");
					if(isValidIP(RemoteAddr)){
						//m_OwnerID = "ANONYMOUS";
						m_OwnerID = RemoteAddr;
						m_url = URL_APPL;
						m_loginstatus = 1;
						m_session.setAttribute("personname","anonymous");
						//m_session.setAttribute("personemail","");
						m_session.setAttribute("personemail",m_OwnerID);
						m_session.setAttribute("userid",m_OwnerID);
						m_session.setAttribute("openid","anonymous");
						LogWriter.msg(RemoteAddr,"LOGIN,anonymous");
					}else{
						m_msg = "Not yet available.";
						m_loginstatus = 0;
						m_OwnerID = null;
						m_url = URL_APPL;
						m_delay = 5;
					}
			}else if(m_op.equals("Google")||m_op.equals("Yahoo")){
				try {
					Endpoint endpoint = oimanager.lookupEndpoint(m_op);
					Association association = oimanager.lookupAssociation(endpoint);
					m_session.setAttribute(ATTR_MAC, association.getRawMacKey());
					m_session.setAttribute(ATTR_ALIAS, endpoint.getAlias());
					m_session.setAttribute("openid",m_op.toLowerCase());
					m_url = oimanager.getAuthenticationUrl(endpoint, association);
					m_msg = null;
				}catch(Exception ex){
					m_loginstatus = 0;
					m_OwnerID = null;
					m_session.setAttribute("userid",null);
					m_session.invalidate();
					m_url = URL_APPL;
					m_delay = 5;
					m_msg = "Unable to connect to ";
					if(m_op.equals("Google")){
						m_msg += "Google";
					}else if(m_op.equals("Yahoo")){
						m_msg += "Yahoo";
					}
				}
			}
		}else{
			//System.out.println("LOGGED IN");
			m_loginstatus = 1;
			if(m_op==null){
			}else if(m_op.equalsIgnoreCase("logout")){
				//System.out.println("LOGGING OUT");
				m_OwnerID = null;
				m_session.setAttribute("userid",null);
				String openid = (String)m_session.getAttribute("openid");
				m_session.invalidate();
				m_loginstatus = 0;
				LogWriter.msg(RemoteAddr,"LOGOUT,"+openid);
				if(openid==null){
				}else if(openid.equals("anonymous")){
					m_url = URL_APPL_LOGOUT;
				}else if(openid.equals("google")){
					m_url = URL_APPL_LOGOUT+"?mode=-1";
				}else if(openid.equals("yahoo")){
					m_url = URL_APPL_LOGOUT+"?mode=-2";
				}
			}
		}
		OpenLoginXML(contentHandler,m_loginstatus,RemoteAddr,SessionID,m_url,m_msg,m_delay);
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

	public long generateKey() {
		// Default non-caching behaviour. We will implement this later.
		return 0;
	}

	public void OpenLoginXML(ContentHandler contentHandler,int the_loginstatus,String the_RemoteAddr,String the_SessionID,String the_url,String the_msg,int the_delay)
			throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl openidAttr = new AttributesImpl();
			openidAttr.addAttribute("","IP","IP","CDATA",the_RemoteAddr);
			openidAttr.addAttribute("","SessionID","SessionID","CDATA",""+the_SessionID);
			openidAttr.addAttribute("","loginstatus","loginstatus","CDATA",""+the_loginstatus);
			contentHandler.startElement("","openid","openid",openidAttr);


			AttributesImpl urlAttr = new AttributesImpl();
			urlAttr.addAttribute("","delay","delay","CDATA",""+the_delay);
			contentHandler.startElement("","url","url",urlAttr);
			if(the_url!=null){
				contentHandler.characters(the_url.toCharArray(),0,the_url.length());
			}
			contentHandler.endElement("","url","url");
			//contentHandler.endElement("","msg","msg");

			AttributesImpl msgAttr = new AttributesImpl();
			contentHandler.startElement("","msg","msg",msgAttr);
			if(the_msg!=null){
				contentHandler.characters(the_msg.toCharArray(),0,the_msg.length());
			}
			contentHandler.endElement("","msg","msg");

			contentHandler.endElement("","openid","openid");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

/**
* Modified from sample code.
*/
	private int checkNonce(String nonce) {
		// check response_nonce to prevent replay-attack:
		if (nonce==null || nonce.length()<20){
			return -1;
		}
		//throw new OpenIdException("Verify failed.");
		// make sure the time on server is correct:
		long nonceTime = getNonceTime(nonce);
		//System.out.println("INCOMING NONCE LONG<"+nonceTime+">");
		long diff = Math.abs(System.currentTimeMillis() - nonceTime);
		if (diff > SIX_HOUR){
			//throw new OpenIdException("Bad nonce time.");
			return -2;
		}
		if (isNonceExist(nonce)){
			//throw new OpenIdException("Verify nonce failed.");
			return -3;
		}
		//storeNonce(nonce, nonceTime + TWO_HOUR);
		storeNonce(nonce, nonceTime + TEN_HOUR); //BECAUSE OF ABBOTS OFF CLOCK
		return 1;
	}

	// simulate a database that stores all nonce:
	private Set<String> nonceDb = new HashSet<String>();

	// check if nonce exists in database:
	boolean isNonceExist(String nonce) {
		return nonceDb.contains(nonce);
	}

	// store nonce in database:
	void storeNonce(String nonce, long expires) {
		nonceDb.add(nonce);
	}

	long getNonceTime(String nonce) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(nonce.substring(0, 19) + "+0000").getTime();
		} catch(ParseException e) {
			return 0;
			//throw new OpenIdException("Bad nonce time.");
		}
	}
}

