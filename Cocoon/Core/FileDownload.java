//FileDownload.java

package Core;

//import General.*;

import java.io.*;
import java.util.Vector;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.*;
import javax.servlet.ServletOutputStream;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

//import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.generation.ServletGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.http.HttpResponse;

import org.apache.avalon.framework.service.ServiceManager;
//import org.apache.avalon.framework.service.ServiceSelector;
//import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.ComponentException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import org.apache.cocoon.servlet.multipart.Part;

//public class FileDownload extends ServiceableGenerator implements Disposable {
public class FileDownload extends ServletGenerator implements Disposable {

//private DataSourceComponent m_dataSource;
private ComponentSelector m_selector;

private String m_OwnerID;
private Session m_session;
private String m_DirStr;
private String m_FilenameStr;

	public void dispose() {
		super.dispose();
		//manager.release(m_dataSource);
		//m_dataSource = null;
	}

	public void recycle() {
		super.recycle();
	}

	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
		m_selector = (ComponentSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		System.out.println("\nCALLING FileDownload SETUP");
		Request request = ObjectModelHelper.getRequest(objectModel);
		response = ObjectModelHelper.getResponse(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_DirStr = request.getParameter("dir");
		m_FilenameStr = request.getParameter("fn");
	}

	public void generate() throws SAXException, ProcessingException {
		System.out.println("FileDownload:generate()");

	try {
		HttpResponse res = (HttpResponse)response;
		ServletOutputStream sos = res.getOutputStream();

		if(m_DirStr==null){
			res.setContentType("text/html");
			sos.println("stuff");
		}else if(m_FilenameStr==null){
		}else{
			res.setContentType("xml");
			File f = new File(FileManager.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/output/"+m_FilenameStr);
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int avail = bis.available();
			byte[] b = new byte[avail];
			bis.read(b);
			sos.write(b);
		}
		sos.flush();
		sos.close();
	}catch(Exception ex){
	}
	}
}
/****

			if(m_filePart!=null){ //ADD_IMAGE
				try {
					InputStream fis = m_filePart.getInputStream();
					fileName = m_filePart.getFileName();
					fileType = m_filePart.getMimeType();
					System.out.println("FN<"+fileName+"> FT<"+fileType+"> PUT IN DIR<"+m_DirStr+">");
					int len = 0;
					byte buf[] = new byte[1024];
					File outfile = new File(FileManager.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr+"/input/"+fileName);
					FileOutputStream fos = new FileOutputStream(outfile);
					while((len=fis.read(buf))>0){
						fos.write(buf,0,len);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}

		try {
			contentHandler.startDocument();
				AttributesImpl uploadAttr = new AttributesImpl();
				uploadAttr.addAttribute("","filename","filename","CDATA",""+fileName);
				uploadAttr.addAttribute("","filetype","filetype","CDATA",""+fileType);
				if(m_DirStr==null){
					uploadAttr.addAttribute("","dir","dir","CDATA","null");
				}else{
					uploadAttr.addAttribute("","dir","dir","CDATA",""+m_DirStr);
				}
				contentHandler.startElement("","fileupload","fileupload",uploadAttr);
				contentHandler.endElement("","fileupload","fileupload");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

	public long generateKey() {
		// Default non-caching behaviour. We will implement this later.
		return 0;
	}
****/
