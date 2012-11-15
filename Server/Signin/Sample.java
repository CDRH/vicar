//Sample.java

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
import org.apache.cocoon.environment.Response;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.activity.Disposable;
//import org.apache.avalon.excalibur.datasource.DataSourceComponent;

public class Sample extends ServiceableGenerator implements Disposable {


private Session m_session;
private ServiceSelector m_selector;
//private DataSourceComponent m_dataSource;

private String m_OwnerID;

	public void dispose() {
		super.dispose();
		//manager.release(m_dataSource);
		//m_dataSource = null;
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING Sample SERVICE");
		super.service(manager);
		//m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession(true);

		m_OwnerID = (String)m_session.getAttribute("userid");
	}

	public void generate() throws SAXException, ProcessingException {
		/****
		m_session.setAttribute("newlogin","1");
		m_session.setAttribute("userid",""+ad.getID());
		m_session.setAttribute("personname",""+ad.getID());
		****/

		if(m_OwnerID==null){
			SigninXML.generateSigninXML(contentHandler,"","","",0,Global.URL_SIGNIN,0,null,"",0);
			return;
		}

		String personname = (String)m_session.getAttribute("personname");
		String openid = (String)m_session.getAttribute("openid");
		generateSampleXML(contentHandler,m_OwnerID,personname,openid,0,null);
	}

	public static void generateSampleXML(ContentHandler contentHandler,String the_LoginID,String the_personname,String the_openid,int the_msgCode,String the_msgTxt)
			throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl sampleAttr = new AttributesImpl();
			sampleAttr.addAttribute("","userid","userid","CDATA",the_LoginID);
			sampleAttr.addAttribute("","personname","personname","CDATA",""+the_personname);
			sampleAttr.addAttribute("","openid","openid","CDATA",the_openid);
			contentHandler.startElement("","sample","sample",sampleAttr);

			AttributesImpl msgAttr = new AttributesImpl();
			msgAttr.addAttribute("","code","code","CDATA",""+the_msgCode);
			contentHandler.startElement("","msg","msg",msgAttr);
			if(the_msgTxt!=null){
				contentHandler.characters(the_msgTxt.toCharArray(),0,the_msgTxt.length());
			}
			contentHandler.endElement("","msg","msg");

			contentHandler.endElement("","sample","sample");
			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("ERROR0");
			e.printStackTrace();
		}
	}
}

