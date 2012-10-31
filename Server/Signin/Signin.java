//Signin.java

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

public class Signin extends ServiceableGenerator implements Disposable {

private String START = "";
private String FINISH = "";
private String APPL_NAME = "";
private String APPL_PREFIX = "";
private String URL_BASE = "";
private String EMAIL_FROM = "";
private String URL_LOGIN ="appl/protected/Signin/SigninIF.html";
private String URL_CONFIRM ="appl/protected/Signin/SigninIF.html?mode=3";

public int EMAIL_PORT = 25;
public String EMAIL_SMTP = "";
public String EMAIL_HELO = "";

private Session m_session;
private ServiceSelector m_selector;
private DataSourceComponent m_dataSource;

private String m_actStr = null;
private String m_perfStr = null;
private String m_LoginID = null;
private String m_Password = null;
private String m_PasswordAlt;
private boolean m_consent = false;

private int m_OwnerID = 0;

private String m_hostPage = null;
private String m_referringPage = null;
private String m_remoteAddr = null;
private String m_remoteHost = null;

	public void dispose() {
		super.dispose();
		manager.release(m_dataSource);
		m_dataSource = null;
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING Signin SERVICE");
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

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

		if((m_referringPage==null)||(m_referringPage.indexOf("Signin.html")>0)){
			m_referringPage = (String)m_session.getAttribute("source");
		}else{
			m_session.setAttribute("source",m_referringPage);
		}


		//System.out.println("REFPAGE<"+m_referringPage+"> START<"+START+"> FINISH<"+FINISH+">");

		String OwnerStr = (String)m_session.getAttribute("userid");
		m_OwnerID = GenUtil.getIntFromString(OwnerStr);

		m_LoginID = GenUtil.HTMLFilter(request.getParameter("signinid"));
		if(m_LoginID!=null){
			m_LoginID = m_LoginID.toLowerCase();
		}
		m_Password = GenUtil.HTMLFilter(request.getParameter("pwd"));
		m_PasswordAlt = GenUtil.HTMLFilter(request.getParameter("pwdalt"));

		System.out.println("PWD<"+m_Password+"> PWDALT<"+m_PasswordAlt+">");

		String consentStr = GenUtil.HTMLFilter(request.getParameter("consent"));
		m_consent = false;
		if((consentStr!=null)&&(consentStr.equalsIgnoreCase("on"))){
			m_consent = true;
		}

		m_actStr = GenUtil.HTMLFilter(request.getParameter("act"));
		m_perfStr = GenUtil.HTMLFilter(request.getParameter("perform"));
		//System.out.println("ACT<"+m_actStr+"> PERFORM<"+m_perfStr+">");
	}

	public void generate() throws SAXException, ProcessingException {
		int ApplID = 0;
		String nextpage = "";
		String timeoutpage = "";
		String messageText = "";
		int messageCode = 0;
		int respType = 0;
		int dispose = 0;
		String m_url = null;
		try {
			if(m_actStr==null){
				//returns login page
				m_actStr = "signin";
			}else if(m_actStr.equalsIgnoreCase("signin")){
				m_session.setAttribute("PWDRESET","false");
				if(m_LoginID==null){
				}else if(GenUtil.isValidEmailFormat(m_LoginID)){
					AcctMngr am = new AcctMngr();
					MD5 md5 = new MD5();
					m_Password = md5.getMD5(m_Password);
					AcctData ad = am.getAcct(m_LoginID,m_Password);
					if(ad!=null){
						if(ad.getStatus()==AcctData.STATUS_ACTIVE){
							m_session.setAttribute("newlogin","1");
							m_session.setAttribute("userid",""+ad.getID());
							m_session.setAttribute("personname",""+ad.getID());
							m_session.setAttribute("personemail",""+ad.getID());
							m_url = Global.URL_APPL;
						}else if(ad.getStatus()==AcctData.STATUS_REGISTERED){
							messageText = "Please finish the registration process before logging in.";
							messageCode = -1;
							respType = 0;
						}
					}else{
						messageText = "This ID and password combination does not match any accounts.";
						messageCode = -1;
						respType = 0;
					}
				}else{
					messageText = "Invalid email format. Please try again.";
					messageCode = -1;
					respType = 0;
				}
			}else if(m_actStr.equalsIgnoreCase("signout")){
				m_session.setAttribute("PWDRESET","false");
				//String IDStr = (String)m_session.getAttribute("userid");
				nextpage = FINISH;
				m_session.setAttribute("userid",null);
				m_session.setAttribute("email",null);
				m_session.invalidate();
				respType = 0;
			}else if(m_actStr.equalsIgnoreCase("register")){
				m_session.setAttribute("PWDRESET","false");
				if(m_perfStr==null){
				}else if(m_perfStr.equals("Cancel")){
					m_actStr = "signin";
				}else if(m_perfStr.equals("Register")){
					if(GenUtil.isValidEmailFormat(m_LoginID)){
						AcctMngr am = new AcctMngr();
						AcctData currad = am.getAcct(m_LoginID);
						if(currad==null){
							if(isValidPassword(m_Password,m_PasswordAlt,6,19)){
								MD5 md5 = new MD5();
								m_Password = md5.getMD5(m_Password);
								Date d = new Date();
								String ts = ""+d.getTime();
								System.out.println("TIME<"+ts+">");
								String confirmpwd = md5.getMD5(m_LoginID.toUpperCase()+ts);
								AcctData ad = new AcctData(m_LoginID,m_Password,AcctData.STATUS_REGISTERED,confirmpwd);
								am.setAcct(ad.getID(),ad);
								m_actStr = "confirm";
								SendMailSSL sm = new SendMailSSL("vicarregister","[pwd]");
								String emailtext="Please click on http://127.0.0.1:8888/vicar/Signin/Signin.html?act=confirm&signinid="+m_LoginID+"&pwdalt="+confirmpwd+" to complete your registration.";
								sm.SendMail("vicarregister@gmail.com",m_LoginID,"Vicar Registration Confirmation",emailtext);
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
					}else if(GenUtil.isValidEmailFormat(m_LoginID)){
						AcctMngr am = new AcctMngr();
						AcctData currad = am.getAcct(m_LoginID);
						if(currad!=null){
							am.Display();
							MD5 md5 = new MD5();
							Date d = new Date();
							String ts = ""+d.getTime();
							System.out.println("TIME<"+ts+">");
							String confirmpwd = md5.getMD5(m_LoginID.toUpperCase()+ts);
							AcctData newad = new AcctData(m_LoginID,currad.getPwd(),AcctData.STATUS_ACTIVE,confirmpwd);
							System.out.println("REQUEST FOR LINK SETTING AUX TO <"+confirmpwd+">");
							am.setAcct(newad.getID(),newad);
							SendMailSSL sm = new SendMailSSL("vicarregister","[pwd]");
							String emailtext="Please click on http://127.0.0.1:8888/vicar/Signin/Signin.html?act=resetpwdlink&signinid="+m_LoginID+"&pwdalt="+confirmpwd+" to reset your password.";
							sm.SendMail("vicarregister@gmail.com",m_LoginID,"Vicar Password Reset",emailtext);
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
					//System.out.println("LINK USERID<"+m_LoginID+"> ALT PWD<"+m_PasswordAlt+">");
					AcctMngr am = new AcctMngr();
					AcctData currad = am.getAcct(m_LoginID);
					//System.out.println("STORED ALT PWD<"+currad.getAux()+">");
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
						if(isValidPassword(m_Password,m_PasswordAlt,6,19)){
							MD5 md5 = new MD5();
							m_Password = md5.getMD5(m_Password);
							//System.out.println("COMPARE LINK PWD<"+m_Password+"> ALT<"+m_PasswordAlt+"> SERVER PWD<"+currad.getPwd()+"> ALT<"+currad.getAux()+">");
							//if((currad.getAux().equals(m_PasswordAlt))&&(m_PasswordAlt!=null)){
							//	System.out.println("MATCH");
							//}
							Date d = new Date();
							String ts = ""+d.getTime();
							//System.out.println("TIME<"+ts+">");
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
			System.out.println("ERROR1");
			e.printStackTrace();
		}
		generateSigninXML(contentHandler,m_LoginID,messageCode,messageText,nextpage,m_actStr,m_url,dispose);
	}

	public boolean isValidPassword(String the_pwd,String the_pwdalt,int the_min,int the_max){
System.out.println("SIMPLE REQUIREMENTS FOR PASSWORD FOR NOW");
		if((the_pwd!=null)&&(the_pwd.equals(the_pwdalt))&&(the_pwd.length()>=the_min)&&(the_pwd.length()<=the_max)){
			if(the_pwd.length()>=3){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}

	public static void generateSigninXML(ContentHandler contentHandler,String the_LoginID,int the_msgcode,String the_msgtext,String the_nextpage,String the_actStr,String the_url,int the_dispose)
			throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl signinAttr = new AttributesImpl();
			signinAttr.addAttribute("","nextpage","nextpage","CDATA",""+the_nextpage);
			signinAttr.addAttribute("","act","act","CDATA",""+the_actStr);
			signinAttr.addAttribute("","ID","ID","CDATA",""+the_LoginID);
			contentHandler.startElement("","signin","signin",signinAttr);

			AttributesImpl urlAttr = new AttributesImpl();
			//urlAttr.addAttribute("","delay","delay","CDATA","5");
			urlAttr.addAttribute("","delay","delay","CDATA","0");
			contentHandler.startElement("","url","url",urlAttr);
			if(the_url!=null){
				contentHandler.characters(the_url.toCharArray(),0,the_url.length());
			}
			contentHandler.endElement("","url","url");

			AttributesImpl msgAttr = new AttributesImpl();
			msgAttr.addAttribute("","code","code","CDATA",""+the_msgcode);
			contentHandler.startElement("","msg","msg",msgAttr);
			if(the_msgtext!=null){
				contentHandler.characters(the_msgtext.toCharArray(),0,the_msgtext.length());
			}
			contentHandler.endElement("","msg","msg");
			contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("ERROR0");
			e.printStackTrace();
		}
	}
}

