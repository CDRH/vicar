package edu.unl.abbot.vicar.Signin;

import edu.unl.abbot.vicar.LogWriter;
//import Server.Global;
import edu.unl.abbot.vicar.Global;

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
* A generator which implements sign in, registration, registration confirmation, and password reminder operations for local (non OpenID) signins.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class Signin extends ServiceableGenerator implements Disposable {


private Session m_session;
private ServiceSelector m_selector;
private DataSourceComponent m_dataSource;

private String m_actStr = null;
private String m_perfStr = null;
private String m_LoginID = null;
private String m_Password = null;
private String m_PasswordAlt;
private boolean m_consent = false;

private String m_hostPage = null;
private String m_referringPage = null;
private String m_remoteAddr = null;
private String m_remoteHost = null;

private String m_mode = "";

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
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

@Override
	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession(true);

		m_hostPage = request.getHeader("Host");
		m_referringPage = request.getHeader("Referer");
		m_remoteAddr = request.getRemoteAddr();
		m_remoteHost = request.getRemoteHost();
		String ForwardFor = request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			m_remoteHost = ForwardFor;
		}
LogWriter.msg("IP","signin setup");

		if((m_referringPage==null)||(m_referringPage.indexOf("Signin.html")>0)){
			m_referringPage = (String)m_session.getAttribute("source");
		}else{
			m_session.setAttribute("source",m_referringPage);
		}

		m_LoginID = Utils.HTMLFilter(request.getParameter("signinid"));
		if(m_LoginID!=null){
			m_LoginID = m_LoginID.toLowerCase();
		}
		m_Password = Utils.HTMLFilter(request.getParameter("pwd"));
		m_PasswordAlt = Utils.HTMLFilter(request.getParameter("pwdalt"));

		String consentStr = Utils.HTMLFilter(request.getParameter("consent"));
		m_consent = false;
		if((consentStr!=null)&&(consentStr.equalsIgnoreCase("on"))){
			m_consent = true;
		}

		m_actStr = Utils.HTMLFilter(request.getParameter("act"));
		m_perfStr = Utils.HTMLFilter(request.getParameter("perform"));
		//System.out.println("ACTSTR<"+m_actStr+">");

		m_mode = Utils.HTMLFilter(request.getParameter("mode"));
	}

@Override
	public void generate() throws SAXException, ProcessingException {
		String messageText = "";
		int messageCode = 0;
		int dispose = 0;
		String m_url = "NONE";
		try {
			if(m_actStr==null){
				//returns login page
				m_actStr = "signin";
			}else if(m_actStr.equalsIgnoreCase("signin")){
				m_session.setAttribute("PWDRESET","false");
				if(m_LoginID==null){
				}else if(Utils.isValidEmailFormat(m_LoginID)){
					AcctMngr am = new AcctMngr();
					MD5 md5 = new MD5();
					m_Password = md5.getMD5(m_Password);
					AcctData ad = am.getAcct(m_LoginID,m_Password);
					if(ad!=null){
						if(ad.getStatus()==AcctData.STATUS_ACTIVE){
							m_session.setAttribute("newlogin","1");
							m_session.setAttribute("userid",""+ad.getID());
							m_session.setAttribute("userpath",""+ad.getID().replace("@","__"));
							m_session.setAttribute("openid","none");
							m_session.setAttribute("personname","");
							m_url = Global.URL_APPL;
						}else if(ad.getStatus()==AcctData.STATUS_REGISTERED){
							messageText = "Please finish the registration process before logging in.";
							messageCode = -1;
						}
					}else{
						messageText = "This ID and password combination does not match any accounts. Please try again.";
						messageCode = -1;
					}
				}else{
					messageText = "Invalid email format. Please try again.";
					messageCode = -1;
				}
			}else if(m_actStr.equalsIgnoreCase("register")){
LogWriter.msg("IP","register");
				m_session.setAttribute("PWDRESET","false");
LogWriter.msg("IP","perfstr"+m_perfStr);
				if(m_perfStr==null){
				}else if(m_perfStr.equals("Cancel")){
					m_actStr = "signin";
				}else if(m_perfStr.equals("Register")){
LogWriter.msg("IP","login"+m_LoginID);
					if(Utils.isValidEmailFormat(m_LoginID)){
						AcctMngr am = new AcctMngr();
						AcctData currad = am.getAcct(m_LoginID);
						if(currad==null){
							if(Utils.isValidPassword(m_Password,m_PasswordAlt,6,19)){
								MD5 md5 = new MD5();
								m_Password = md5.getMD5(m_Password);
								Date d = new Date();
								String ts = ""+d.getTime();
								String confirmpwd = md5.getMD5(m_LoginID.toUpperCase()+ts);
								AcctData ad = new AcctData(m_LoginID,m_Password,AcctData.STATUS_REGISTERED,confirmpwd);
								am.setAcct(ad.getID(),ad);
								m_actStr = "confirm";

LogWriter.msg("IP","sending ssmail");
								SendMailSSL sm = new SendMailSSL(Global.GMAIL_ID,Global.GMAIL_PWD);
								String emailtext="Please click on "+Global.URL_BASE+"/vicar/Signin.html?act=confirm&signinid="+m_LoginID+"&pwdalt="+confirmpwd+" to complete your registration.";
								sm.SendMail(Global.GMAIL_ID+"@gmail.com",m_LoginID,"Vicar Registration Confirmation",emailtext);
								messageText = "Please check your email for a confirmation link.";
								messageCode = 1;
							}else{
								messageText = "Passwords are either of incorrect length or do not match.";
								messageCode = -1;
							}
						}else if(currad.getStatus()==AcctData.STATUS_ACTIVE){
							messageText = "There is already an account associated with this email address.";
							messageCode = -1;
						}else if(currad.getStatus()==AcctData.STATUS_REGISTERED){
							messageText = "You are already registered.  Please click the confirm link to complete the registration.";
							messageCode = -1;
						}else{
						}
					}
				}
			}else if(m_actStr.equalsIgnoreCase("confirm")){
				if(m_LoginID!=null){
					AcctMngr am = new AcctMngr();
					AcctData ad = am.getAcct(m_LoginID);
					if(ad!=null){
						if(ad.getStatus()==AcctData.STATUS_REGISTERED){
							if((ad.getAux().equals(m_PasswordAlt))&&(m_PasswordAlt!=null)){
								AcctData newad = new AcctData(m_LoginID,ad.getPwd(),AcctData.STATUS_ACTIVE,"");
								am.setAcct(newad.getID(),newad);
								messageText = "Your registration is confirmed.";
								messageCode = 1;
							}
						}else if(ad.getStatus()==AcctData.STATUS_ACTIVE){
							messageText = "Your account is already active.";
							messageCode = 1;
						}
					}else{
						messageText = "The registration for this email address is no longer valid. Please reregister.";
						messageCode = -1;
					}
				}
			}else if(m_actStr.equalsIgnoreCase("resetpwd")){
				m_session.setAttribute("PWDRESET","false");
				if(m_perfStr==null){
				}else if(m_perfStr.equals("Cancel")){
					m_actStr = "signin";
				}else if(m_perfStr.equals("Reset")){
					if((m_LoginID==null)||(m_LoginID.length()<1)){
						messageText = "Please enter your email address and a reset link will be sent to you.";
						messageCode = 1;
					}else if(Utils.isValidEmailFormat(m_LoginID)){
						AcctMngr am = new AcctMngr();
						AcctData currad = am.getAcct(m_LoginID);
						if(currad!=null){
							am.Display();
							MD5 md5 = new MD5();
							Date d = new Date();
							String ts = ""+d.getTime();
							String confirmpwd = md5.getMD5(m_LoginID.toUpperCase()+ts);
							AcctData newad = new AcctData(m_LoginID,currad.getPwd(),AcctData.STATUS_ACTIVE,confirmpwd);
							am.setAcct(newad.getID(),newad);

							SendMailSSL sm = new SendMailSSL(Global.GMAIL_ID,Global.GMAIL_PWD);
							String emailtext="Please click on "+Global.URL_BASE+"/vicar/Signin.html?act=resetpwdlink&signinid="+m_LoginID+"&pwdalt="+confirmpwd+" to reset your password.";
							sm.SendMail(Global.GMAIL_ID+"@gmail.com",m_LoginID,"Vicar Password Reset",emailtext);
							messageText = "Please check your email for a password reset link.";
							messageCode = 1;
							m_actStr = "resetpwddone";
						}else{
							messageText = "No such account. Please try again.";
							messageCode = -1;
						}
					}else{
						messageText = "Invalid email format. Please try again.";
						messageCode = -1;
					}
				}
			}else if(m_actStr.equalsIgnoreCase("resetpwdlink")){
				if(m_perfStr==null){
					AcctMngr am = new AcctMngr();
					AcctData currad = am.getAcct(m_LoginID);
					if((currad!=null)&&(m_PasswordAlt!=null)&&(m_PasswordAlt.equals(currad.getAux()))){
						//LINKVALID
						m_session.setAttribute("PWDRESET","true");
					}else{
						messageText = "This reset link is now invalid. Please return to the signin screen and try again.";
						messageCode = -1;
						m_actStr = "resetpwdlinkdone";
						m_session.setAttribute("PWDRESET","false");
					}
				}else if(m_perfStr.equals("Cancel")){
					m_actStr = "signin";
				}else if(m_perfStr.equals("Reset")){
					AcctMngr am = new AcctMngr();
					AcctData currad = am.getAcct(m_LoginID);
					String resetpwd = (String)m_session.getAttribute("PWDRESET");
					if((currad!=null)&&(resetpwd!=null)&&(resetpwd.equals("true"))){
						if(Utils.isValidPassword(m_Password,m_PasswordAlt,6,19)){
							MD5 md5 = new MD5();
							m_Password = md5.getMD5(m_Password);
							Date d = new Date();
							String ts = ""+d.getTime();
							String confirmpwd = md5.getMD5(m_LoginID.toUpperCase()+ts);
							AcctData newad = new AcctData(m_LoginID,m_Password,AcctData.STATUS_ACTIVE,confirmpwd);
							am.setAcct(currad.getID(),newad);
							messageText = "Your password has been changed.";
							messageCode = 1;
							m_actStr = "resetpwdlinkdone";
							m_session.setAttribute("PWDRESET","false");
						}else{
							messageText = "The passwords were either not valid or did not match.";
							messageCode = -1;
						}
					}else{
						messageText = "No such account.";
						messageCode = -1;
						m_session.setAttribute("PWDRESET","false");
					}
				}
			}else if(m_actStr.equalsIgnoreCase("Cancel")){
				dispose = 1;
			}else if(m_actStr.equalsIgnoreCase("Done")){
				dispose = 1;
			}else{
				m_actStr = "signin";
			}

		}catch(Exception e){
			System.out.println("SIGNIN ERROR");
			e.printStackTrace();
		}
		SigninXML.generateSigninXML(contentHandler,m_LoginID,m_actStr,Global.TITLE,0,m_url,messageCode,messageText,m_mode,dispose);
	}
}

