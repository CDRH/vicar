//FileDownload.java

package Server.Core;

import Server.Global;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.StringTokenizer;

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
private String m_OwnerPath;
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

		String requestURL = request.getRequestURI();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_OwnerPath = (String)m_session.getAttribute("userpath");

		Vector<String> urlpartList = new Vector<String>();
		StringTokenizer stok = new StringTokenizer(requestURL,"/");
		//PUT URL COMPONENTS INTO REVERSE ORDER
		while(stok.hasMoreTokens()){
			String s = stok.nextToken();
			urlpartList.insertElementAt(s,0);
		}

		m_FilenameStr = urlpartList.get(0);
		m_DirStr = urlpartList.get(1);
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
				String downloadfiledir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/"+m_DirStr+"/output/";
				String downloadfilename = downloadfiledir+m_FilenameStr;
				if(m_FilenameStr.endsWith(".zip")){
					if(m_DirStr!=null){
						downloadfilename = downloadfiledir+"/"+m_DirStr+".zip";
						ZipUtil zu = new ZipUtil();
						zu.zip(downloadfiledir,"xml",downloadfilename);
					}
				}else if(m_FilenameStr.endsWith(".tar.gz")){
					ZipUtil zu = new ZipUtil();
					downloadfilename = downloadfiledir+"/"+m_DirStr+".tar";
					zu.tar(downloadfiledir,".xml",downloadfilename);
					if(zu.gzip(downloadfilename,downloadfilename+".gz")>=0){
						downloadfilename+=".gz";
					}
				}else if(m_FilenameStr.endsWith(".html")){
					downloadfiledir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/"+m_DirStr+"/valid/";
					downloadfilename = downloadfiledir+"/"+m_FilenameStr;
					res.setContentType("html");
				}
				File f = new File(downloadfilename);
				if((f!=null)&&(f.exists())){
					try(FileInputStream fis = new FileInputStream(f);BufferedInputStream bis = new BufferedInputStream(fis)){
						int avail = bis.available();
						byte[] b = new byte[avail];
						bis.read(b);
						sos.write(b);
					}catch(IOException ioex){
					}
				}else{
					//System.out.println("NEED TO BASE OUTPUT ON REQUESTED FILE TYPE");
					res.setContentType("text/html");
					sos.println("<html><head><title>File Not Found</title></head><body><h2>File Not Found</h2></body></html>");
				}
			}
			sos.flush();
			sos.close();
		}catch(Exception ex){
		}
	}
}


