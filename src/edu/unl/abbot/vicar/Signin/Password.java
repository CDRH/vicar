package edu.unl.abbot.vicar.Signin;

import edu.unl.abbot.vicar.Global;
import edu.unl.abbot.vicar.Private;

import java.io.InputStream;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
import java.text.StringCharacterIterator;

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

/**
* A generator which allows a signed in user to change thier password.
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class Password extends ServiceableGenerator implements Disposable {

private Session m_session;
private ServiceSelector m_selector;
private DataSourceComponent m_dataSource;

private String m_OwnerID = null;
private String m_perfStr = null;
private String m_PasswordCurr = null;
private String m_PasswordNew = null;
private String m_PasswordNewAgain;

private String m_remoteAddr = null;

@Override
	public void dispose() {
		super.dispose();
		manager.release(m_dataSource);
		m_dataSource = null;
	}

@Override
	public void recycle() {
		super.recycle();
	}

@Override
	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING Password SERVICE");
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

@Override
	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession(true);

		m_remoteAddr = request.getRemoteAddr();
		String ForwardFor = request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			m_remoteAddr = ForwardFor;
		}

		m_OwnerID = (String)m_session.getAttribute("userid");

		m_PasswordCurr = Utils.HTMLFilter(request.getParameter("pwd_curr"));
		m_PasswordNew = Utils.HTMLFilter(request.getParameter("pwd_new"));
		m_PasswordNewAgain = Utils.HTMLFilter(request.getParameter("pwd_newagain"));

		m_perfStr = Utils.HTMLFilter(request.getParameter("perform"));
		//System.out.println("OwnerID<"+m_OwnerID+"> PWD<"+m_PasswordCurr+"> PWDNEW<"+m_PasswordNew+"> PWDNEWAGAIN<"+m_PasswordNewAgain+"> PERFORM<"+m_perfStr+">");
	}

/**
* Produces XML output for the password change page Password.html.
* If the session attribute <i>userid</i> is null then it is assumed that the user is not signed in and XML is generated which will cause Password.html to return the user to the signin page. 
*/
@Override
	public void generate() throws SAXException, ProcessingException {
		if(m_OwnerID == null){
			contentHandler.startDocument();
				AttributesImpl signinAttr = new AttributesImpl();
				contentHandler.startElement("","signin","signin",signinAttr);
				contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
			return;
		}

		int messageCode = 0;
		String messageText = "";
		int dispose = 0;
		String m_url = null;
		try {
			if(m_perfStr==null){
				messageText = null;
				messageCode = 0;
			}else if(m_perfStr.equalsIgnoreCase("Change Password")){
				if(Utils.isValidPassword(m_PasswordNew,m_PasswordNewAgain,6,19)){
					AcctMngr am = new AcctMngr();
					AcctData currad = am.getAcct(m_OwnerID);
					MD5 md5 = new MD5();
					String pwdcurr = md5.getMD5(m_PasswordCurr);
					if(currad!=null){
						if(currad.getPwd().equals(pwdcurr)){
							String pwdnew = md5.getMD5(m_PasswordNew);
							AcctData newad = new AcctData(m_OwnerID,pwdnew,AcctData.STATUS_ACTIVE,null);
							am.setAcct(newad.getID(),newad);
							SendMailSSL sm = new SendMailSSL(Private.GMAIL_ID,Private.GMAIL_PWD);
							String emailtext = "Your Vicar password has been changed.  If you did not make this change please reply to this email.";
							sm.SendMail(Private.GMAIL_ID+"@gmail.com",m_OwnerID,"Vicar Password Change",emailtext);
							messageText = "Your password has been changed.";
							messageCode = 1;
						}else{
							messageText = "Error accessing your account.";
							messageCode = -1;
						}
					}else{
						messageText = "The existing password is not valid.";
						messageCode = -1;
					}
				}else{
					messageText = "The new password is either not valid or the two entries do not match.";
					messageCode = -1;
				}
			}else if(m_perfStr.startsWith("Cancel")){
				dispose = 1;
				//System.out.println("CANCEL");
			}
		}catch(Exception e){
			System.out.println("PASSWORD ERROR");
			e.printStackTrace();
		}
		generatePasswordXML(contentHandler,m_OwnerID,messageCode,messageText,dispose);
	}


	private static void generatePasswordXML(ContentHandler contentHandler,String the_OwnerID,int the_msgcode,String the_msgtext,int the_dispose)
			throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl passwordAttr = new AttributesImpl();
			passwordAttr.addAttribute("","ID","ID","CDATA",""+the_OwnerID);
			passwordAttr.addAttribute("","dispose","dispose","CDATA",""+the_dispose);
			contentHandler.startElement("","password","password",passwordAttr);

			AttributesImpl msgAttr = new AttributesImpl();
			msgAttr.addAttribute("","code","code","CDATA",""+the_msgcode);
			contentHandler.startElement("","msg","msg",msgAttr);
			if(the_msgtext!=null){
				contentHandler.characters(the_msgtext.toCharArray(),0,the_msgtext.length());
			}
			contentHandler.endElement("","msg","msg");

			contentHandler.endElement("","password","password");
			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("ERROR0");
			e.printStackTrace();
		}
	}
}

