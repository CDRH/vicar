//MonitorServer.java

package Server.Upload;

import Server.Global;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class MonitorServer extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private int m_totalsize = 0;
private String m_dirname = "";

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_totalsize = getIntFromString(request.getParameter("totsz"));
		m_dirname = request.getParameter("dir");

		String destdir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_dirname+"/input/";
		System.out.println("\tSTART<"+Thread.currentThread().getName()+"> DIRNAME<"+destdir+"> TOTALSIZE<"+m_totalsize+">");

		DirMonitor dm = new DirMonitor(destdir,(long)m_totalsize);
		dm.giterdone();
		System.out.println("\tEND");
	}

	public void generate() throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl respAttr = new AttributesImpl();
			contentHandler.startElement("","Response","Response",respAttr);
			contentHandler.endElement("","Response","Response");
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



