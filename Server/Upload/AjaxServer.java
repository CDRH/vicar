//AjaxServer.java

package Server.Upload;

import Server.Global;
//import Server.Progress.MonitorData;

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

public class AjaxServer extends ServiceableGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_filename = "";
private String m_mimetype = "";
private String m_filesize = "";
private int m_totalsize = 0;
private int m_collectionsize = 0;
private String m_dirname = "";

private int m_size = 0;
private String m_msg = "";
private int m_complete = 0;

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
		//if(m_OwnerID==null){
		//	m_session.setAttribute("userid",m_OwnerID);
		//}

		m_filename = request.getParameter("fn");
		//m_session.setAttribute("upload_filename",""+m_filename);
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
		System.out.println("NAME<"+Thread.currentThread().getName()+"> FILENAME<"+m_filename+"> DECL MIMETYPE<"+m_mimetype+"> DECLARED SIZE<"+m_filesize+"> DIR<"+m_dirname+"> TOTALSIZE<"+m_totalsize+">");

		m_complete = 0;
		String destdir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_dirname;

		try {
			//System.out.println("CODE IS MESSY - BETTER TO HAVE ONE AjaxUtil.write...() WHICH DETECTS TEXT OR BASE64 AND UNPACKS APPROPRIATELY");
			if(m_filename!=null){
				//System.out.println("JAVASCRIPT VERSION");
				//System.out.println("JS MIME<"+m_mimetype+">");
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
					//m_size = AjaxUtil.writeFileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis);
					m_size = AjaxUtil.writeFileFromInputStream(destdir+"/input/",m_filename,(InputStream)sis,m_session,getIntFromString(m_filesize));
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

				System.out.println("\t"+m_msg+"\n");
/****
				m_collectionsize = getIntFromString((String)m_session.getAttribute("upload_collectionsize"));
				System.out.println("NAME<"+Thread.currentThread().getName()+"> FN<"+m_filename+"> COLLECTIONSIZE<"+m_collectionsize+"> PLUS<"+m_size+">");
				m_collectionsize += m_size;
				m_session.setAttribute("upload_collectionsize",""+m_collectionsize);
				int totalpct = (int)(100.0*((float)m_collectionsize/m_totalsize));
				m_session.setAttribute("upload_totalpct",""+totalpct);
				if(totalpct>=100){
					m_complete = 1;
				}
				//MonitorData md = new MonitorData(true,m_filename,pct);
				//m_session.setAttribute("monitor_data",md);
****/
			}else{
				System.out.println("NO JAVASCRIPT VERSION HANDLED IN FileManager.java");
			}
			//System.out.println("MIME<"+m_mimetype+"> FN<"+m_filename+"> SIZE<"+m_size+"> DECL SIZE<"+m_filesize+">");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void generate() throws SAXException, ProcessingException {
		generateResponseXML(contentHandler,m_dirname,m_complete,m_filename,m_size,m_msg);
		System.out.println("FINISH<"+Thread.currentThread().getName()+"> FN<"+m_filename+">");
	}

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



