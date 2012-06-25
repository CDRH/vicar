//AjaxServer.java

package Server.Core;

import Server.Global;

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

import org.apache.cocoon.servlet.multipart.Part;

public class AjaxServer extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_filename = "";
private String m_mimetype = "";
private String m_filesize = "";
private String m_dirname = "";

private int m_size = 0;
private String m_msg = "";

private Part m_filePart;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		//System.out.println("CALLING AjaxServer SERVICE");
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

		m_filename = request.getParameter("fn");
		m_mimetype = request.getParameter("mt");
		m_filesize = request.getParameter("sz");
		m_dirname = request.getParameter("dir");
		String ful = request.getParameter("file_upload");
		if(ful!=null){
			int indx = ful.lastIndexOf("/");
			if(indx>0){
				ful = ful.substring(indx+1);
			}
			System.out.println("NEED TO USE FILE PATH AS THIS COULD COME FROM A NON UNIX SYSTEM!!!");
		}
		System.out.println("FILENAME<"+m_filename+"> DECL MIMETYPE<"+m_mimetype+"> DECLARED SIZE<"+m_filesize+"> DIR<"+m_dirname+">");

		String destdir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_dirname;

		m_session.setAttribute("monitor_pathsize",""+m_filesize);

System.out.println("DOES NOT CHECK FOR CONVERT FILES");
		m_session.setAttribute("monitor_pathname",destdir+"/input/"+m_filename);

		try {
			System.out.println("CODE IS MESSY - BETTER TO HAVE ONE AjaxUtil.write...() WHICH DETECTS TEXT OR BASE64 AND UNPACKS APPROPRIATELY");
			if(m_filename!=null){
				System.out.println("JAVASCRIPT VERSION");
				System.out.println("JS MIME<"+m_mimetype+">");
				if((m_mimetype==null)||(m_mimetype.equals(""))||(m_mimetype.startsWith("application/"))){
					if(m_filename.toLowerCase().endsWith(".rng")){
						ServletInputStream sis = ((HttpRequest)request).getInputStream();
						m_size = AjaxUtil.writeFileFromInputStream(destdir+"/convert/",m_filename,(InputStream)sis);
						m_msg = "Uploaded conversion file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".gz")){
						ServletInputStream sis = ((HttpRequest)request).getInputStream();
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded gzip file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".zip")){
						ServletInputStream sis = ((HttpRequest)request).getInputStream();
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded zip file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".tar")){
						ServletInputStream sis = ((HttpRequest)request).getInputStream();
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded tar file "+m_filename+" of size "+m_size+".";
					}else if(m_filename.toLowerCase().endsWith(".jpg")||(m_filename.toLowerCase().endsWith(".jpeg"))){
						//While no image file uploads are expected for abbot/vicar it is a good sanity check
						//to try at least one other file type to make sure the code works in a more general way.
						//Should we have base64 encoded binary data for other reasons in the future we are ready.
						ServletInputStream sis = ((HttpRequest)request).getInputStream();
						m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
						m_msg = "Uploaded image file "+m_filename+"of size "+m_size+".";
					}else{
						m_msg = "File "+m_filename+" of unknown type not uploaded.";
					}
				}else if(m_mimetype.indexOf("text") >= 0){
					ServletInputStream sis = ((HttpRequest)request).getInputStream();
					m_size = AjaxUtil.writeFileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
					m_msg = "Uploaded file "+m_filename+" of type "+m_mimetype+" and size "+m_size+".";
				}else if(m_mimetype.indexOf("image") >= 0){
					//While no image file uploads are expected for abbot/vicar it is a good sanity check
					//to try at least one other file type to make sure the code works in a more general way.
					//Should we have base64 encoded binary data for other reasons in the future we are ready.
					ServletInputStream sis = ((HttpRequest)request).getInputStream();
					m_size = AjaxUtil.writeBase64FileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
					m_msg = "Uploaded file "+m_filename+" of type "+m_mimetype+" and size "+m_size+".";
				}else{
					m_msg = "File "+m_filename+" of unknown type not uploaded.";
				}
			}else{
				System.out.println("NO JAVASCRIPT VERSION HANDLED IN FileManager.java");
/****
				m_filePart = (Part)request.get("file_upload");
				if(m_filePart!=null){ //ADD_IMAGE
					InputStream fis = m_filePart.getInputStream();
					m_filename = m_filePart.getFileName();
					String filedestdir = destdir+"/input/";
					if((m_filename!=null)&&(m_filename.toLowerCase().endsWith(FileManager.CONVERT_SUFFIX))){
						filedestdir = destdir+"/convert/";
					}
					m_mimetype = m_filePart.getMimeType();
					System.out.println("NO JS READ MIME<"+m_mimetype+">");
					int len = 0;
					byte buf[] = new byte[1024];
					File outfile = new File(filedestdir+m_filename);
					FileOutputStream fos = new FileOutputStream(outfile);
					m_size = 0;
					while((len=fis.read(buf))>0){
						fos.write(buf,0,len);
						m_size += len;
					}
					if((m_filename!=null)&&(m_filename.toLowerCase().endsWith(FileManager.CONVERT_SUFFIX))){
						m_msg = "Uploaded conversion file "+m_filename+" of size "+m_size+".";
					}else{
						m_msg = "Uploaded file "+m_filename+" of type "+m_mimetype+" and size "+m_size+".";
					}
				}
****/
			}
			System.out.println("MIME<"+m_mimetype+"> FN<"+m_filename+"> SIZE<"+m_size+"> DECL SIZE<"+m_filesize+">");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void generate() throws SAXException, ProcessingException {
		generateResponseXML(contentHandler,m_dirname,m_filename,m_size,m_msg);
	}

	public void generateResponseXML(ContentHandler contentHandler,String the_dirname,String the_filename,int the_size,String the_msg) throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl respAttr = new AttributesImpl();
			respAttr.addAttribute("","dirname","dirname","CDATA",""+the_dirname);
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



