//StreamServer.java

package Server.Stream;

import Server.Global;
import Server.Core.SessionSaver;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Map;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import org.apache.cocoon.environment.Response;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.generation.ServletGenerator;
import org.apache.cocoon.environment.http.HttpResponse;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;

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

public class StreamServer extends ServletGenerator implements Disposable {

private ServiceSelector m_selector;
private Session m_session;

private String m_OwnerID;
private String m_ConvStr;
private String m_DirStr;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
	}

	public void setup(SourceResolver resolver, Map objectModel,String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		response = ObjectModelHelper.getResponse(objectModel);
		m_session = request.getSession();

		m_OwnerID = (String)m_session.getAttribute("userid");
		//if(m_OwnerID==null){
		//	m_session.setAttribute("userid",m_OwnerID);
		//}

		m_ConvStr = request.getParameter("conv");
		m_DirStr = request.getParameter("dir");
		System.out.println("DIR<"+m_DirStr+"> CONV<"+m_ConvStr+">");
	}

	public void generate() throws SAXException, ProcessingException {
		try {
			ServletOutputStream sos = ((HttpResponse)response).getOutputStream();

			if(m_ConvStr==null){
				m_ConvStr = (String)m_session.getAttribute("SAVE:schema:"+m_DirStr);
				//System.out.println("GET ATTR<"+m_ConvStr+">");
			}else{
				m_session.setAttribute("SAVE:schema:"+m_DirStr,m_ConvStr);
			}
			if((m_DirStr==null)||(m_ConvStr==null)){
				sos.println("<datastream>");
				sos.println("<values name='Please Select a conversion schema'>");
				sos.println("</values>");
				sos.println("</datastream>");
				sos.flush();
				sos.close();
				return;
			}
			AbbotConvert ac = new AbbotConvert(m_session,m_DirStr,m_ConvStr);
			System.out.println("THREAD STARTS");
			ac.start();
			int pauseval = 200;
			sos.println("<datastream>");
			sos.flush();
			Thread.sleep(pauseval);

			String userdir = Global.BASE_USER_DIR+"/"+m_OwnerID+"/"+m_DirStr;
			int inputcount = fileCount(userdir+"/input/");
			System.out.println("TOTAL FILES TO CONVERT<"+inputcount+">");
			//PROGRESS BAR(s) FOR UPLOAD
			/*****/
			int totalpct = 0;
			String topline = "File Conversion with "+m_ConvStr.substring(1);
			sos.println("<values name='"+topline+"|Abbot'>");
			sos.flush();
			int pct = 0;
			while(pct<100){
				//System.out.println("PCT<"+pct+">");
				int outputcount = fileCount(userdir+"/output/");
				pct = (int)(100*outputcount)/inputcount;
				totalpct = pct/2;
				sos.println(""+totalpct+"|"+pct);
				sos.flush();
				Thread.sleep(pauseval);
			}
			pct = 0;
			sos.println("</values>");
			sos.println("<values name='"+topline+"|Validation'>");
			while(pct<100){
				//System.out.println("PCT<"+pct+">");
				int validcount = fileCount(userdir+"/valid/");
				pct = (int)(100*validcount)/inputcount;
				totalpct = 50+pct/2;
				sos.println(""+totalpct+"|"+pct);
				sos.flush();
				Thread.sleep(pauseval);
			}
			sos.println("100|100");
			sos.flush();
			sos.println("</values>");
			sos.flush();
			/*****/

			//PROGRESS BAR(s) FOR UPLOAD
			/*****
			int totalpct = getIntFromString((String)m_session.getAttribute("upload_totalpct"));
			int filepct = getIntFromString((String)m_session.getAttribute("upload_filepct"));
			String fn = (String)m_session.getAttribute("upload_filename");
			//sos.println("<values name='TOTAL|"+fn+"'>");
			sos.println("<values name='"+fn+"'>");
			//System.out.println("<values name='TOTAL|"+fn+"'>");
			System.out.println("<values name='"+fn+"'>");
			sos.flush();
			Thread.sleep(pauseval);
			int prevtotalpct = totalpct;
			int prevfilepct = filepct;
			String prevfn = fn;
			while(totalpct<100){
				totalpct = getIntFromString((String)m_session.getAttribute("upload_totalpct"));
				filepct = getIntFromString((String)m_session.getAttribute("upload_filepct"));
				fn = (String)m_session.getAttribute("upload_filename");
				System.out.println("TPCT<"+totalpct+"> FPCT<"+filepct+"> FN<"+fn+">");
				if(!(fn.equals(prevfn))){
					sos.println("</values>");
					//sos.println("<values name='TOTAL|"+fn+"'>");
					sos.println("<values name='"+fn+"'>");
					System.out.println("</values>");
					//System.out.println("<values name='TOTAL|"+fn+"'>");
					System.out.println("<values name='"+fn+"'>");
					sos.flush();
					prevfn = fn;
				}
				if((totalpct>prevtotalpct)||(filepct>prevfilepct)){
					sos.println(""+totalpct+"|"+filepct);
					System.out.println(totalpct+"|"+filepct);
					sos.flush();
					prevtotalpct = totalpct;
					prevfilepct = filepct;
				}
				Thread.sleep(pauseval);
			}
			m_session.setAttribute("upload_totalpct","0");
			m_session.setAttribute("upload_filepct","0");
			m_session.setAttribute("upload_filename","");
			sos.println("100|100");
			sos.println("</values>");
			System.out.println("100|100");
			System.out.println("</values>");
			sos.flush();
			//sos.println("<next id='reload'/>");
			//sos.flush();
			****/
			/****
			Thread.sleep(pauseval);
			long startts = (new Date()).getTime();
			long nowts = startts;
			while(nowts <= startts+10*pauseval){
				Thread.sleep(pauseval);
				int pct = (int)((nowts-startts)/200.0);
				for(int i=0;i<=100;i+=10){
					Thread.sleep(50);
					sos.println(""+(pct+(i/10))+"|"+i);
					sos.flush();
				}
				System.out.println(""+pct);
				nowts = (new Date()).getTime();
			}
			Thread.sleep(pauseval);
			****/

			sos.println("</datastream>");
			System.out.println("</datastream>");
			sos.flush();
			sos.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private int fileCount(String the_path){
		int count = 0;
		try {
			File f = new File(the_path);
			if(f!=null){
				count = f.listFiles().length;
			}
		} catch(Exception ex){
		}
		return count;
	}

	private int getIntFromString(String the_str){
		int ret = 0;
		if(the_str!=null){
			try {
				ret = Integer.decode(the_str).intValue();
			}catch(NumberFormatException nfex){
			}
		}
		return ret;
	}
}


