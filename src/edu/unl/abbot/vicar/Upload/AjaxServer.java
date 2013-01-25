package edu.unl.abbot.vicar.Upload;

//import Server.Global;
import edu.unl.abbot.vicar.Global;

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

/**
* A Generator which handles the multiple file uploads from an HTML5 form or div as called by AjaxUpload.js.
* This generator responds to Ajax calls from AjaxUpload.js which provides Core/Vicar.xsl with it's UploadInit() function which is loaded when the page's body is loaded.
* An upload is triggered by either Vicar.xsl's div with id of <i>upload_msgbox</i> or form with id <i>upload_form</i>.
* AjaxServer gives an XML response containing the number of bytes written so that AjaxUpload.js can calculate percent uploaded and control a progress bar's appearance.
*
* If javascript is not enabled then multiple simultaneous file uploads are not possible and single file uploads are handled by {@link Server.Core.Vicar}.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/

public class AjaxServer extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_OwnerPath;
private String m_filename = "";
private String m_mimetype = "";
private String m_filesize = "";
private int m_totalsize = 0;
private int m_collectionsize = 0;
private String m_dirname = "";

private int m_size = 0;
private String m_msg = "";
private int m_complete = 0;

@Override
	public void dispose() {
		super.dispose();
	}

@Override
	public void recycle() {
		super.recycle();
	}

@Override
	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
		m_selector = (ServiceSelector)manager.lookup(DataSourceComponent.ROLE+"Selector");
	}

/**
* Collects the session attributes and parameters.
* 
*/ 
@Override
	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		m_OwnerPath = (String)m_session.getAttribute("userpath");
		//if(m_OwnerID==null){
		//	m_session.setAttribute("userid",m_OwnerID);
		//}

		m_filename = request.getParameter("fn");
		m_mimetype = request.getParameter("mt");
		m_filesize = request.getParameter("sz");
		m_totalsize = getIntFromString(request.getParameter("totsz"));
		m_dirname = request.getParameter("dir");
		String ful = request.getParameter("file_upload");
		if(ful!=null){
			int indx = ful.lastIndexOf("/");
			if(indx>0){
				ful = ful.substring(indx+1);
			}
			System.out.println("NEED TO USE FILE PATH AS THIS COULD COME FROM A NON UNIX SYSTEM!!!");
		}
		m_complete = 0;
		String destdir = Global.BASE_USER_DIR+"/"+m_OwnerPath+"/"+m_dirname;
		//System.out.println("AjaxServer DESTDIR<"+destdir+">");
		if(m_filename!=null){
			try (ServletInputStream sis = ((HttpRequest)request).getInputStream()){
				if((m_mimetype==null)||(m_mimetype.equals(""))||(m_mimetype.startsWith("application/"))){
					if(m_filename.toLowerCase().endsWith(".rng")){
						m_size = AjaxUtil.writeFileFromInputStream(destdir+"/convert/",m_filename,(InputStream)sis);
						m_msg = "Uploaded conversion file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".gz")){
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded gzip file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".zip")){
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded zip file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".tar")){
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded tar file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".jpg")||(m_filename.toLowerCase().endsWith(".jpeg"))){
						//While no image file uploads are expected for abbot/vicar it is a good sanity check
						//to try at least one other file type to make sure the code works in a more general way.
						//Should we have base64 encoded binary data for other reasons in the future we are ready.
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded image file "+m_filename+"of size "+m_size+".";
					}else{
						m_msg = "File "+m_filename+" of unknown type not uploaded.";
					}
				}else if(m_mimetype.indexOf("text") >= 0){
					m_size = AjaxUtil.writeFileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
					//System.out.println("TEXT SIZE<"+m_size+">");
					m_msg = "Uploaded file "+m_filename+" of type "+m_mimetype+" and size "+m_size+".";
				}else if(m_mimetype.indexOf("image") >= 0){
					//While no image file uploads are expected for abbot/vicar it is a good sanity check
					//to try at least one other file type to make sure the code works in a more general way.
					//Should we have base64 encoded binary data for other reasons in the future we are ready.
					m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
					m_msg = "Uploaded file "+m_filename+" of type "+m_mimetype+" and size "+m_size+".";
				}else{
					m_msg = "File "+m_filename+" of unknown type not uploaded.";
				}
			}catch(IOException ioex){
				//ioex.printStackTrace();
			}
		}else{
			System.out.println("NO JAVASCRIPT VERSION HANDLED IN FileManager.java");
		}
	}


@Override
	public void generate() throws SAXException, ProcessingException {
		generateResponseXML(contentHandler,m_dirname,m_complete,m_filename,m_size,m_msg);
	}


/**
* Provides the XML response returned by generate.
*/
	public void generateResponseXML(ContentHandler contentHandler,String the_dirname,int the_complete,String the_filename,int the_size,String the_msg) throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl respAttr = new AttributesImpl();
			respAttr.addAttribute("","dirname","dirname","CDATA",""+the_dirname);
			respAttr.addAttribute("","complete","complete","CDATA",""+the_complete);
			contentHandler.startElement("","Response","Response",respAttr);
			if(the_msg!=null){
				AttributesImpl msgAttr = new AttributesImpl();
				msgAttr.addAttribute("","filename","filename","CDATA",""+the_filename);
				msgAttr.addAttribute("","filesize","filesize","CDATA",""+the_size);
				contentHandler.startElement("","Msg","Msg",msgAttr);
				contentHandler.characters(the_msg.toCharArray(),0,the_msg.length());
				contentHandler.endElement("","Msg","Msg");
			}
			contentHandler.endElement("","Response","Response");
			contentHandler.endDocument();
		}catch(Exception e){
			System.out.println("AjaxServer ERROR");
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



