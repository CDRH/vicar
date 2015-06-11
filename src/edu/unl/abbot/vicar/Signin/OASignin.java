package edu.unl.abbot.vicar.Signin;


import edu.unl.abbot.vicar.Global;
import edu.unl.abbot.vicar.Private;
import edu.unl.abbot.vicar.LogWriter;

import java.nio.file.Paths;
import java.util.Vector;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

//import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.generation.ServletGenerator;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.activity.Disposable;

import org.apache.cocoon.environment.http.HttpResponse;

/****/
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;

//import com.google.api.client.http.HttpResponse;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/****/


//import org.json.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletInputStream;
import org.apache.cocoon.environment.http.HttpRequest;


/**
* A generator which implements sign in via OpenID.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
//public class OASignin extends ServiceableGenerator implements Disposable {
public class OASignin extends ServletGenerator implements Disposable {

/****
static final long ONE_HOUR = 3600000L;
static final long TWO_HOUR = ONE_HOUR * 2L;
static final long SIX_HOUR = ONE_HOUR * 6L;
static final long TEN_HOUR = ONE_HOUR * 10L;
static final String ATTR_MAC = "openid_mac";
static final String ATTR_ALIAS = "openid_alias";
****/

private Request m_request;
private Session m_session;

private String m_OwnerID;
private String m_SessionID = "";

private int m_delay = 5;
private int m_msgcode = 0;
private String m_msgtext = null;

//private OpenIdManager oimanager;
//private String m_op = null;
private String m_reqURI = null;

private static final Gson GSON = new Gson();

private static final HttpTransport TRANSPORT = new NetHttpTransport();
private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

private static final String APPLICATION_NAME = "Abbot OpenID Connect";

private static GoogleClientSecrets clientSecrets;

private static String COCOON = "/cocoon";
private static String BASE_URL = "/vicar";
//private static String BASE_URL = "/vicar";
//private static String BASE_URL = "/OIDC";
//private static String BASE_URL = "";

private String m_url = null;
private String m_UserName = null;

/****
//ABBOT
private static final String CLIENT_ID = "590474463783-55v0uvmqg9kbcaqn7rmb7n7mj7lhhnms.apps.googleusercontent.com";
private static final String CLIENT_SECRET = "yYbesCayQWNerqJ4O6JhlPEW";
****/

/****
//LOCAL
private static final String CLIENT_ID = "590474463783-eliu1c1psuvp5t7chsv1ojob8e5pgh31.apps.googleusercontent.com";
private static final String CLIENT_SECRET = "aJqg7FbJ8tDpCPNH_qRwxQSQ";
****/


@Override
	public void dispose() {
		super.dispose();
	}

@Override
	public void recycle() {
		super.recycle();
	}

/****
@Override
	public void service(ServiceManager manager) throws ServiceException{
		//super.service(manager);

		//OPENIDMANAGER
		//oimanager = new OpenIdManager();

		//oimanager.setRealm(Global.URL_BASE);
		//oimanager.setReturnTo(Global.URL_BASE+Global.URL_LOGIN_SFX);
	}
****/
@Override
	public void compose(ComponentManager manager) throws ComponentException{
		super.compose(manager);
	}

@Override
	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		m_request = ObjectModelHelper.getRequest(objectModel);
		m_session = m_request.getSession();

		//m_op = m_request.getParameter("op");
		m_OwnerID = (String)m_session.getAttribute("userid");
		m_reqURI = m_request.getRequestURI();
		System.out.println("OASignin SETUP URI<"+m_reqURI+">");
		response = ObjectModelHelper.getResponse(objectModel);
	}

@Override
	public void generate() throws SAXException, ProcessingException {
		String RemoteAddr = m_request.getRemoteAddr();
		String ForwardFor = m_request.getHeader("X-Forwarded-For");
		if(ForwardFor!=null){
			RemoteAddr = ForwardFor;
		}
		//m_session.setAttribute("IPADDR",RemoteAddr);
		//m_SessionID = m_session.getId();

		String tokenData = (String)m_session.getAttribute("token");
		String stateData = (String)m_session.getAttribute("state");
		String stateParam = (String)m_request.getParameter("state");


		System.out.println("OASignin GENERATE URI<"+m_reqURI+"> STATE_PARAM<"+stateParam+"> STATE_DATA<"+stateData+"> TOKEN<"+tokenData+">");
		if(m_reqURI==null){
		}else if((m_reqURI.equals(BASE_URL+"/"))||(m_reqURI.equals(COCOON+BASE_URL+"/"))){

		}else if( (m_reqURI.equals(BASE_URL+"/oaapp"))||(m_reqURI.equals(COCOON+BASE_URL+"/oaapp"))||
				(m_reqURI.equals(BASE_URL+"/oaapp.xml"))||(m_reqURI.equals(COCOON+BASE_URL+"/oaapp.xml")) ){
			System.out.println("CALLED OIDC/oauth");
			try {
				HttpResponse res = (HttpResponse)response;
				res.setStatus(HttpServletResponse.SC_OK);
				String state = new BigInteger(130, new SecureRandom()).toString(32);
				m_session.setAttribute("state", state);
				stateData = (String)m_session.getAttribute("state");
				contentHandler.startDocument();
				AttributesImpl oauthAttr = new AttributesImpl();
				contentHandler.startElement("","oauth","oauth",oauthAttr);
					AttributesImpl clientidAttr = new AttributesImpl();
					contentHandler.startElement("","clientid","clientid",clientidAttr);
					if(Private.CLIENT_ID != null){
						contentHandler.characters(Private.CLIENT_ID.toCharArray(),0,Private.CLIENT_ID.length());
					}
					contentHandler.endElement("","clientid","clientid");

					AttributesImpl stateAttr = new AttributesImpl();
					contentHandler.startElement("","state","state",stateAttr);
					if(stateData != null){
						contentHandler.characters(stateData.toCharArray(),0,stateData.length());
					}
					contentHandler.endElement("","state","state");

					AttributesImpl appnameAttr = new AttributesImpl();
					contentHandler.startElement("","appname","appname",appnameAttr);
					if(APPLICATION_NAME != null){
						contentHandler.characters(APPLICATION_NAME.toCharArray(),0,APPLICATION_NAME.length());
					}
					contentHandler.endElement("","appname","appname");

					if(m_OwnerID != null){
						AttributesImpl signoutAttr = new AttributesImpl();
						contentHandler.startElement("","signout","signout",signoutAttr);
						contentHandler.endElement("","signout","signout");
					}else{
						AttributesImpl signinAttr = new AttributesImpl();
						contentHandler.startElement("","signin","signin",signinAttr);
						contentHandler.endElement("","signin","signin");
					}

				contentHandler.endElement("","oauth","oauth");
				contentHandler.endDocument();

			}catch(Exception e){
				e.printStackTrace();
			}

		}else if( (m_reqURI.equals(BASE_URL+"/oauth"))||(m_reqURI.equals(COCOON+BASE_URL+"/oauth"))||
				(m_reqURI.equals(BASE_URL+"/oauth.xml"))||(m_reqURI.equals(COCOON+BASE_URL+"/oauth.xml")) ){
			System.out.println("CALLED OIDC/oauth");
			try {
				HttpResponse res = (HttpResponse)response;
				res.setStatus(HttpServletResponse.SC_OK);
				String state = new BigInteger(130, new SecureRandom()).toString(32);
				m_session.setAttribute("state", state);
				stateData = (String)m_session.getAttribute("state");
				contentHandler.startDocument();
				AttributesImpl oauthAttr = new AttributesImpl();
				contentHandler.startElement("","oauth","oauth",oauthAttr);
					AttributesImpl clientidAttr = new AttributesImpl();
					contentHandler.startElement("","clientid","clientid",clientidAttr);
					if(Private.CLIENT_ID != null){
						contentHandler.characters(Private.CLIENT_ID.toCharArray(),0,Private.CLIENT_ID.length());
					}
					contentHandler.endElement("","clientid","clientid");

					AttributesImpl stateAttr = new AttributesImpl();
					contentHandler.startElement("","state","state",stateAttr);
					if(stateData != null){
						contentHandler.characters(stateData.toCharArray(),0,stateData.length());
					}
					contentHandler.endElement("","state","state");

					AttributesImpl appnameAttr = new AttributesImpl();
					contentHandler.startElement("","appname","appname",appnameAttr);
					if(APPLICATION_NAME != null){
						contentHandler.characters(APPLICATION_NAME.toCharArray(),0,APPLICATION_NAME.length());
					}
					contentHandler.endElement("","appname","appname");

					if(m_OwnerID != null){
						AttributesImpl signoutAttr = new AttributesImpl();
						contentHandler.startElement("","signout","signout",signoutAttr);
						contentHandler.endElement("","signout","signout");
					}else{
						AttributesImpl signinAttr = new AttributesImpl();
						contentHandler.startElement("","signin","signin",signinAttr);
						contentHandler.endElement("","signin","signin");
					}

				contentHandler.endElement("","oauth","oauth");
				contentHandler.endDocument();

			}catch(Exception e){
				e.printStackTrace();
			}
		}else if((m_reqURI.equals(BASE_URL+"/connect"))||(m_reqURI.equals(COCOON+BASE_URL+"/connect"))){
			System.out.println("CONNECT");
			try {
				HttpResponse res = (HttpResponse)response;
				ServletOutputStream out = res.getOutputStream();
				res.setContentType("application/json");
				if(tokenData != null){
					System.out.println("CONNECT TOKENDATA NOT NULL");
					res.setStatus(HttpServletResponse.SC_OK);

					out.print(GSON.toJson("connect tokendata not null."));

					out.flush();
					out.close();
					return;
				}
				if((stateParam == null) || (stateData == null) || !stateParam.equals(stateData)){
					System.out.println("STATE DATA DOES NOT MATCH");
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

					out.print(GSON.toJson("state data does not match."));

					out.flush();
					out.close();
					return;
				}

				ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
				ServletInputStream sis = ((HttpRequest)m_request).getInputStream();
				getContent(sis,resultStream);
				String code = new String(resultStream.toByteArray(), "UTF-8");
				System.out.println("CODE "+code);
				try {
					// Upgrade the authorization code into an access and refresh token.
					GoogleTokenResponse tokenResponse =
							new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
							Private.CLIENT_ID, Private.CLIENT_SECRET, code, "postmessage").execute();
	
					// You can read the Google user ID in the ID token.
					// This sample does not use the user ID.
					GoogleIdToken idToken = tokenResponse.parseIdToken();
					String gplusId = idToken.getPayload().getSubject();
System.out.println("GPLUSID <"+gplusId+">");	
					// Store the token in the session for later use.

					m_session.setAttribute("token",tokenResponse.toString());

					res.setStatus(HttpServletResponse.SC_OK);

					out.print(GSON.toJson("Successfully connected user."));

					out.flush();
					out.close();

				} catch (TokenResponseException e) {
					res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

					out.print(GSON.toJson("Failed to upgrade the authorization code."));

					out.flush();
					out.close();
				} catch (IOException e) {
					res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

					out.print(GSON.toJson("Failed to read token data from Google. "+e.getMessage()));

					out.flush();
					out.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if((m_reqURI.equals(BASE_URL+"/disconnect"))||(m_reqURI.equals(COCOON+BASE_URL+"/disconnect"))){
			try {
				HttpResponse res = (HttpResponse)response;
				ServletOutputStream out = res.getOutputStream();
				res.setContentType("application/json");
				m_OwnerID = null;
				m_url = Global.URL_SIGNIN;
				m_session.setAttribute("userid",m_OwnerID);
				if(tokenData == null){
					System.out.println("TOKENDATA NULL");
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//FSS
					//out.print(GSON.toJson("Current user not connected."));
					out.print(GSON.toJson(m_url));

					out.flush();
					out.close();
					return;
				}

				try {
					// Build credential from stored token data.
					GoogleCredential credential = new GoogleCredential.Builder()
							.setJsonFactory(JSON_FACTORY)
							.setTransport(TRANSPORT)
							.setClientSecrets(Private.CLIENT_ID, Private.CLIENT_SECRET).build()
							.setFromTokenResponse(JSON_FACTORY.fromString(tokenData, GoogleTokenResponse.class));

					com.google.api.client.http.HttpResponse revokeResponse = TRANSPORT.createRequestFactory().buildGetRequest(
							new GenericUrl(String.format("https://accounts.google.com/o/oauth2/revoke?token=%s",credential.getAccessToken()))).execute();
					// Reset the user's session.
					m_session.removeAttribute("token");
					res.setStatus(HttpServletResponse.SC_OK);
					//System.out.println("DISCONNECT "+GSON.toJson("STUFF"));

//FSS
					//out.print(GSON.toJson("Successfully disconnected."));
					out.print(GSON.toJson(m_url));

					out.flush();
					out.close();

				} catch (IOException e) {
					// For whatever reason, the given token was invalid.
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);

					out.print(GSON.toJson("Failed to revoke token for given user."));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if((m_reqURI.equals(BASE_URL+"/vicar_redir"))||(m_reqURI.equals(COCOON+BASE_URL+"/vicar_redir"))){
			System.out.println("CALLED OIDC/vicar_redir");
			try {
				HttpResponse res = (HttpResponse)response;
				ServletOutputStream out = res.getOutputStream();
				res.setContentType("application/json");
				if(tokenData == null){
					System.out.println("TOKENDATA IS NULL");
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

					out.print(GSON.toJson("Current user not connected in redir."));

					out.flush();
					out.close();
					return;
				}
				try {
					// Build credential from stored token data.
					GoogleCredential credential = new GoogleCredential.Builder()
						.setJsonFactory(JSON_FACTORY)
						.setTransport(TRANSPORT)
						.setClientSecrets(Private.CLIENT_ID, Private.CLIENT_SECRET).build()
						.setFromTokenResponse(JSON_FACTORY.fromString(tokenData, GoogleTokenResponse.class));
					// Create a new authorized API client.
					Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(APPLICATION_NAME)
						.build();

					Person User = service.people().get("me").execute();
					String UserName = User.getDisplayName();
					System.out.println("USERNAME "+UserName);

					m_session.setAttribute("newlogin","1");
					if(UserName != null){
						m_session.setAttribute("userid",UserName);
						m_session.setAttribute("userpath",UserName.replace("@","__"));
					}
					m_session.setAttribute("openid","googleplus");
					m_session.setAttribute("personname","");
					m_url = Global.URL_APPL;

					res.setStatus(HttpServletResponse.SC_OK);

//FSS
					out.print(GSON.toJson(m_url));
					//out.print(GSON.toJson("oaapp"));

					//out.flush();
					//out.close();
				} catch (IOException e) {
					res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.print(GSON.toJson("Failed to read data from Google. " + e.getMessage()));
					//out.flush();
					//out.close();
				}
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("OTHER NOT USED");
		}
	}


/*
* Read the content of an InputStream.
*
* @param inputStream the InputStream to be read.
* @return the content of the InputStream as a ByteArrayOutputStream.
* @throws IOException
*/
	static void getContent(InputStream inputStream, ByteArrayOutputStream outputStream) throws IOException {
		// Read the response into a buffered stream
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		int readChar;
		while ((readChar = reader.read()) != -1) {
			outputStream.write(readChar);
		}
		reader.close();
	}

	public long generateKey() {
		// Default non-caching behaviour. We will implement this later.
		return 0;
	}
}


