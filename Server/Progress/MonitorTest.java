//MonitorTest.java

package Server.Progress;


import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.http.HttpRequest;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;

public class MonitorTest extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;

private String m_name;
private int m_value;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING MonitorTest SERVICE");
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		if(m_OwnerID==null){
			m_session.setAttribute("userid",m_OwnerID);
		}

		m_name = (String)request.getParameter("name");
		m_value = getIntFromString((String)request.getParameter("value"));
		//System.out.println("NAME<"+m_name+"> VALUE<"+m_value+">");
		MonitorData md = new MonitorData(true,m_name,m_value);
		MonitorData md1 = new MonitorData(true,m_name+"more",m_value+20);
		md.setNext(md1);
		m_session.setAttribute("monitor_data",md);
	}

	public void generate() throws SAXException, ProcessingException {
		System.out.println("MonitorTest:generate()");
		try {
			contentHandler.startDocument();
			AttributesImpl monitorAttr = new AttributesImpl();
			monitorAttr.addAttribute("","name","name","CDATA",""+m_name);
			monitorAttr.addAttribute("","value","value","CDATA",""+m_value);
			contentHandler.startElement("","MonitorTest","MonitorTest",monitorAttr);
			contentHandler.endElement("","Monitor","Monitor");
			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("ERROR0");
			e.printStackTrace();
		}

	}

	public static int getIntFromString(String the_str){
		int ret = 0;
		if(the_str==null){
			return ret;
		}
		try{
			ret = Integer.decode(the_str).intValue();
		}catch(Exception ex){
			ret = 0;
		}
		return ret;
	}
}

