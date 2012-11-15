//SigninXML.java

package Server.Signin;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.SAXException;
import org.apache.cocoon.ProcessingException;

public class SigninXML {

	public static void generateSigninXML(ContentHandler contentHandler,String the_ID,String the_actStr,String the_title,int the_delay,String the_url,int the_msgcode,String the_msgtext,String the_mode,int the_dispose)
				throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl signinAttr = new AttributesImpl();
			signinAttr.addAttribute("","ID","ID","CDATA",""+the_ID);
			signinAttr.addAttribute("","act","act","CDATA",""+the_actStr);
			signinAttr.addAttribute("","mode","mode","CDATA",""+the_mode);
			signinAttr.addAttribute("","dispose","dispose","CDATA",""+the_dispose);
			contentHandler.startElement("","signin","signin",signinAttr);

			AttributesImpl urlAttr = new AttributesImpl();
			urlAttr.addAttribute("","delay","delay","CDATA",""+the_delay);
			contentHandler.startElement("","url","url",urlAttr);
			if(the_url!=null){
				contentHandler.characters(the_url.toCharArray(),0,the_url.length());
			}
			contentHandler.endElement("","url","url");

			AttributesImpl msgAttr = new AttributesImpl();
			msgAttr.addAttribute("","code","code","CDATA",""+the_msgcode);
			contentHandler.startElement("","msg","msg",msgAttr);
			if(the_msgtext!=null){
				contentHandler.characters(the_msgtext.toCharArray(),0,the_msgtext.length());
			}
			contentHandler.endElement("","msg","msg");

			AttributesImpl titleAttr = new AttributesImpl();
			contentHandler.startElement("","title","title",titleAttr);
			if(the_title!=null){
				contentHandler.characters(the_title.toCharArray(),0,the_title.length());
			}
			contentHandler.endElement("","title","title");

			contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}
}

