package edu.unl.abbot.vicar.Core;

import edu.unl.abbot.vicar.Global;

/****
import org.junit.*;
import static org.junit.Assert.*;
****/

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
//import org.apache.cocoon.generation.ServiceableGenerator;
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
* A Generator which returns a specified data file from the directory structure into a ServletOutputStream for delivery to a browser via http.
* This allows a user to click on the name of an output file and have that file downloaded to their local computer.
*
* This class inherits from ServletGenerator instead of the usual ServiceableGenerator for as it needs access to ServletOutputStream.  The ServletOutputStream can introduce the file contents as a stream rather than XML as would be typical for a ServiceableGenerator.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/

public class FileDownload extends ServletGenerator implements Disposable {
//public class FileDownload extends ServiceableGenerator implements Disposable {

private String m_OwnerID;
private String m_OwnerPath;
private Session m_session;
private String m_DirStr;
private String m_ResultStr;
private String m_FilenameStr;

/****
@Test
	public void testFileDownload(){
		assertTrue(true);
	}
****/

@Override
	public void dispose() {
		super.dispose();
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
* Gets the userid and ownerpath from session and the directory and filename information from Request.
* The ownerpath indicates the subdirectory under the global data location in which this particular user's data files will be stored.
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
		m_ResultStr = "";
		if(m_DirStr==null){
			System.out.println("SHOULD NEVER HAPPEN");
		}else if(m_DirStr.equals("adorn")){
			m_ResultStr = "adorn";
			m_DirStr = urlpartList.get(2);
		}else if(m_DirStr.equals("output")){
			m_ResultStr = "output";
			m_DirStr = urlpartList.get(2);
		}
		System.out.println("FILEDOWNLOAD DIRSTR<"+m_DirStr+"> FILENAME<"+m_FilenameStr+">");
	}

/**
* Determines the data file to be read and packages into a ServletOutputStream for delivery to a browser via http.
* If the the directory or the filename is not specified then a simple html error message is returned.
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
				String downloadfiledir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/";;
				res.setContentType("xml");
				if(m_ResultStr==null){
					downloadfiledir += m_DirStr+"/output/";
				}else if(m_ResultStr.equals("adorn")){
					downloadfiledir += m_DirStr+"/adorn/";
				}else if(m_ResultStr.equals("")){
					downloadfiledir += m_DirStr+"/output/";
				}else if(m_ResultStr.equals("output")){
					downloadfiledir += m_DirStr+"/output/";
				}
System.out.println("DFD<"+downloadfiledir+">");
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


