//SchemaList.java

package Server.Core;

import Server.Global;

import java.util.Vector;
import java.util.Map;
import java.util.Date;
import java.util.Properties;
import java.util.Enumeration;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

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
* Does stuff.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class SchemaList extends ServiceableGenerator implements Disposable {

private String m_OwnerID;

private String m_DirStr = null;

	public void dispose() {
		super.dispose();
	}

	public void recycle() {
		super.recycle();
	}

	public void service(ServiceManager manager) throws ServiceException{
		super.service(manager);
	}

	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession();
		m_OwnerID = (String)session.getAttribute("userid");
		m_DirStr = request.getParameter("dir");
	}

	public void generate() throws SAXException, ProcessingException {
		if(m_OwnerID == null){
			contentHandler.startDocument();
				AttributesImpl signinAttr = new AttributesImpl();
				signinAttr.addAttribute("","mode","mode","CDATA","-1");
				contentHandler.startElement("","signin","signin",signinAttr);
				contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
			return;
		}

		Vector<SchemaData> schemaList = getSchemaList(m_OwnerID,m_DirStr,"1tei_bare.rng");

		contentHandler.startDocument();
		generateSchemaXML(contentHandler,schemaList);
		contentHandler.endDocument();
	}

	public void generateSchemaXML(ContentHandler contentHandler,Vector<SchemaData> the_schemaList) throws SAXException, ProcessingException {
		try {
			AttributesImpl schemalistAttr = new AttributesImpl();
			contentHandler.startElement("","schemalist","schemalist",schemalistAttr);

			int count = 0;
			for(SchemaData sd : the_schemaList){
				AttributesImpl schemaAttr = new AttributesImpl();
				schemaAttr.addAttribute("","name","name","CDATA",""+sd.getName());
				schemaAttr.addAttribute("","path","path","CDATA",""+sd.getPath());
				schemaAttr.addAttribute("","type","type","CDATA",""+sd.getType());
				schemaAttr.addAttribute("","current","current","CDATA",""+sd.getCurrent());
				contentHandler.startElement("","schema","schema",schemaAttr);
				String cmnt = sd.getComment();
				if(cmnt!=null){
					contentHandler.characters(cmnt.toCharArray(),0,cmnt.length());
				}
				contentHandler.endElement("","schema","schema");
				count++;
			}

			contentHandler.endElement("","schemalist","schemalist");
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

	public Vector<SchemaData> getSchemaList(String the_OwnerID,String the_DirStr,String the_current){
		Vector<SchemaData> schemaList = new Vector<SchemaData>();
		Vector<String> standardSchemaList = listFiles(Global.SCHEMA_DIR,".rng");
		String comment = "In a future iteration Brian will be able to put an appropriate comment here that tells something about this particular schema";
		String url = "";
		for(String schemaName : standardSchemaList){
			int iscurrent = 0;
			if((the_current!=null)&&(the_current.startsWith("1"))){
				if(the_current.substring(1).equals(schemaName)){
					iscurrent = 1;
				}
			}
			if(schemaName.equals("tei-xl.rng")){
				schemaList.insertElementAt(new SchemaData(schemaName,Global.SCHEMA_DIR+schemaName,1,iscurrent,comment,url),0);
			}else{
				schemaList.add(new SchemaData(schemaName,Global.SCHEMA_DIR+schemaName,1,iscurrent,comment,url));
			}
		}
		if(the_DirStr!=null){
			String userschemaDir = Global.BASE_USER_DIR+"/"+the_OwnerID+"/"+the_DirStr+"/convert/";
			Vector<String> userSchemaList = listFiles(userschemaDir,".rng");
			comment = "";
			url = "";
			for(String schemaName : userSchemaList){
				int iscurrent = 0;
				if((the_current!=null)&&(the_current.startsWith("2"))){
					if(the_current.substring(1).equals(schemaName)){
						iscurrent = 1;
					}
				}
				schemaList.add(new SchemaData(schemaName,userschemaDir+schemaName,2,iscurrent,comment,url));
			}
		}

/***
				Properties prop = new Properties();
				prop.load(new FileInputStream(m_DirStr));
				Enumeration schemaList = prop.propertyNames();

				while(schemaList.hasMoreElements()){
					String s = (String)schemaList.nextElement();
				}
***/
		return schemaList;
	}


	private Vector<String> listFiles(String the_dirpath,String the_suffix){
		Vector<String> dir = new Vector<String>();
		try {
			File f = new File(the_dirpath);
			if(f!=null){
				String files[] = f.list();
				if(files!=null){
					for (int i=0; i<files.length; i++) {
						if((files[i]!=null)&&(the_suffix!=null)&&(files[i].endsWith(the_suffix))){
							dir.add(files[i]);
						}
					}
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
		}
		return dir;
	}
}

