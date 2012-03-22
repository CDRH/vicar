//FileDownload.java

package Core;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Map;

import javax.servlet.http.*;
import javax.servlet.ServletOutputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.apache.cocoon.generation.ServletGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.http.HttpResponse;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;


/**
* Reads a specified data file from the directory structure into a ServletOutputStream for delivery to a browser via http.
* This allows a user to click on the name of an output file in a collection and have that file downloaded to their local computer.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class FileDownload extends ServletGenerator implements Disposable {

private String m_OwnerID;
private Session m_session;
private String m_DirStr;
private String m_FilenameStr;

@Test
	public void testFileDownload(){
		assertTrue(true);
	}

@Override
	public void dispose() {
		super.dispose();
		//manager.release(m_dataSource);
		//m_dataSource = null;
	}

@Override
	public void recycle() {
		super.recycle();
	}

@Override
	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
		//m_selector = (ComponentSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

/**
* Gets userid from session and directory and filename information from Request.
*/
@Override
	public void setup(SourceResolver resolver,Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		response = ObjectModelHelper.getResponse(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_DirStr = request.getParameter("dir");
		m_FilenameStr = request.getParameter("fn");
	}

/**
* Determines the data file to be read and packages into a ServletOutputStream for delivery to a browser via http.
* If the the directory or the filename is not specified then an html error message is returned.
*/
@Override
	public void generate() throws SAXException, ProcessingException {
		try {
			HttpResponse res = (HttpResponse)response;
			ServletOutputStream sos = res.getOutputStream();
	
			if(m_DirStr==null){
				res.setContentType("text/html");
				sos.println("<html><head><title>Directory Needed</title></head><body><h2>Directory Needed</h2></body></html>");
			}else if(m_FilenameStr==null){
				res.setContentType("text/html");
				sos.println("<html><head><title>Filename Needed</title></head><body><h2>Filename Needed</h2></body></html>");
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

