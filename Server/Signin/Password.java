//Password.java

package Server.Signin;

import Server.Global;

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

	public void dispose() {
		super.dispose();
		manager.release(m_dataSource);
		m_dataSource = null;
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING Password SERVICE");
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

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
							SendMailSSL sm = new SendMailSSL(Global.GMAIL_ID,Global.GMAIL_PWD);
							String emailtext = "Your Vicar password has been changed.  If you did not make this change please reply to this email.";
							sm.SendMail(Global.GMAIL_ID+"@gmail.com",m_OwnerID,"Vicar Password Change",emailtext);
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
/****
			if(true){
				m_session.setAttribute("PWDRESET","false");
				if(m_perfStr==null){
				}else if(m_perfStr.equals("Cancel")){
					//m_actStr = "signin";
				}else if(m_perfStr.equals("Reset")){
					if((m_OwnerID==null)||(m_OwnerID.length()<1)){
						messageText = "Please enter your email address and a reset link will be sent to you.";
						messageCode = 1;
					}else if(Utils.isValidEmailFormat(m_OwnerID)){
						AcctMngr am = new AcctMngr();
						AcctData currad = am.getAcct(m_OwnerID);
						if(currad!=null){
							am.Display();
							MD5 md5 = new MD5();
							Date d = new Date();
							String ts = ""+d.getTime();
							System.out.println("TIME<"+ts+">");
							String confirmpwd = md5.getMD5(m_OwnerID.toUpperCase()+ts);
							AcctData newad = new AcctData(m_OwnerID,currad.getPwd(),AcctData.STATUS_ACTIVE,confirmpwd);
							System.out.println("REQUEST FOR LINK SETTING AUX TO <"+confirmpwd+">");
							am.setAcct(newad.getID(),newad);
							SendMailSSL sm = new SendMailSSL(Global.GMAIL_ID,Global.GMAIL_PWD);
							String emailtext="Please click on "+Global.URL_BASE+"/vicar/Password/Password.html?act=resetpwdlink&signinid="+m_OwnerID+"&pwdalt="+confirmpwd+" to reset your password.";
							sm.SendMail(Global.GMAIL_ID+"@gmail.com",m_OwnerID,"Vicar Password Reset",emailtext);
							messageText = "Please check your email for a password reset link.";
							messageCode = 1;
						}else{
							messageText = "No such account. Please try again.";
							messageCode = -1;
						}
					}else{
						messageText = "Invalid email format. Please try again.";
						messageCode = -1;
					}
				}
			}else if(m_perfStr.equalsIgnoreCase("resetpwdlink")){
				if(m_perfStr==null){
					AcctMngr am = new AcctMngr();
					AcctData currad = am.getAcct(m_OwnerID);
					if((currad!=null)&&(m_PasswordNew!=null)&&(m_PasswordNew.equals(m_PasswordNewAgain))){
						//LINKVALID
						m_session.setAttribute("PWDRESET","true");
					}else{
						messageText = "This reset link is now invalid. Please return to the signin screen and try again.";
						messageCode = -1;
						m_session.setAttribute("PWDRESET","false");
					}
				}else if(m_perfStr.startsWith("Cancel")){
					dispose = 1;
				}
			}
****/
		}catch(Exception e){
			System.out.println("PASSWORD ERROR");
			e.printStackTrace();
		}
		generatePasswordXML(contentHandler,m_OwnerID,messageCode,messageText,dispose);
	}


	public static void generatePasswordXML(ContentHandler contentHandler,String the_OwnerID,int the_msgcode,String the_msgtext,int the_dispose)
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

