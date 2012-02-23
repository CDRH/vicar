//FileUpload.java

package Core;

//NEED TO CLEAN OUT OLD IMPORTS

import java.util.Map;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.activity.Disposable;

import org.apache.cocoon.servlet.multipart.Part;

/**
* Upload single files pending integration of existing multiple file upload servlet using HTML5.
* Not intended to be more than a temporary solution to facilitate testing.  It has no error checking nor reporting back to the user.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/
public class FileUpload extends ServiceableGenerator implements Disposable {

private String m_OwnerID;
private Part m_filePart;
private Session m_session;
private String m_DirStr;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
	}

/**
* Receives dir name and file for upload.  This is the only http POST command in all of Vicar.
*/
	public void setup(SourceResolver resolver, Map objectModel,
			String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_DirStr = request.getParameter("dir");
		m_filePart = (Part)request.get("uploaded_file");
	}

	public void generate() throws SAXException, ProcessingException {
		String fileName = "";
		String fileType = "";
		if(m_DirStr==null){
			//System.out.println("NEW DIR");
		}else{
			if(m_filePart!=null){ //ADD_IMAGE
				try {
					InputStream fis = m_filePart.getInputStream();
					fileName = m_filePart.getFileName();
					fileType = m_filePart.getMimeType();
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
}
