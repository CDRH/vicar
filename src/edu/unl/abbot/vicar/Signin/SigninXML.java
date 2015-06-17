package edu.unl.abbot.vicar.Signin;


import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.SAXException;
import org.apache.cocoon.ProcessingException;

/**
* Provides a common method for use by {@link Signin}, {@link OpenSignin}, and {@link Signout} to produce XML output for their respective XSLT conversions.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class SigninXML {

	public static void generateSigninXML(ContentHandler contentHandler,String the_ID,String the_actStr,String the_title,int the_delay,String the_url,int the_msgcode,String the_msgtext,String the_mode,int the_dispose,String the_clientID,String the_state)
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

//OAUTH
			AttributesImpl oauthAttr = new AttributesImpl();
			oauthAttr.addAttribute("","clientid","clientid","CDATA",""+the_clientID);
			oauthAttr.addAttribute("","state","state","CDATA",""+the_state);
			contentHandler.startElement("","oauth","oauth",oauthAttr);
			contentHandler.endElement("","oauth","oauth");

			contentHandler.endElement("","signin","signin");
			contentHandler.endDocument();
		}catch(Exception e){ 
			e.printStackTrace();
		}
	}

	public static void generateSigninXML(ContentHandler contentHandler,String the_ID,String the_sessionID,String the_actStr,String the_title,int the_delay,String the_url,int the_msgcode,String the_msgtext,String the_mode,int the_dispose,String the_diag)
				throws SAXException, ProcessingException {
		try {
			contentHandler.startDocument();
			AttributesImpl signinAttr = new AttributesImpl();
			signinAttr.addAttribute("","ID","ID","CDATA",""+the_ID);
			signinAttr.addAttribute("","sessionID","sessionID","CDATA",""+the_sessionID);
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

			AttributesImpl diagAttr = new AttributesImpl();
			contentHandler.startElement("","diag","diag",diagAttr);
			if(the_diag!=null){
				contentHandler.characters(the_diag.toCharArray(),0,the_diag.length());
			}
			contentHandler.endElement("","diag","diag");

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

