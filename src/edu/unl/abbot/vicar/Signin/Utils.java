package edu.unl.abbot.vicar.Signin;


import java.io.*;

/**
* Miscellaneous utilities for Sign in and other operations.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class Utils {

/**
* Cleans up entered text to make it XML compatible and to prevent SQL injection tricks.
* @param the_txt The raw text as entered into a GET or POST command.
* @return The cleaned text.
*/
	public static String HTMLFilter(String the_txt) {
		if(the_txt==null){
			return null;
		}

		char content[] = new char[the_txt.length()];
		the_txt.getChars(0,the_txt.length(),content,0);
		StringBuffer result = new StringBuffer(content.length+50);
		for(int i=0;i<content.length;i++) {
			switch(content[i]){
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '"':
					result.append("&quot;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				default:
					result.append(content[i]);
			}
		}
		return result.toString();
	}

/**
* Simple server side check to determine if an email address is of a valid format.
*/
	public static boolean isValidEmailFormat(String the_email){
		if(the_email==null){
			return false;
		}else if(the_email.indexOf(",")>=0){
			return false;
		}else if(the_email.indexOf("#")>=0){
			return false;
		}else if(the_email.indexOf(" ")>=0){
			return false;
		}else{
			int len = the_email.length();
			int atInd = the_email.indexOf("@");
			int lastdotInd = the_email.lastIndexOf(".");
			if((atInd>0)&&(atInd<lastdotInd)&&(lastdotInd<len)){
				return true;
			}else{
				return false;
			}
		}
	}

/**
* Simple server side way to determine if a password fulfils simple requirements.
*/
	public static boolean isValidPassword(String the_pwd,String the_pwdalt,int the_min,int the_max){
System.out.println("SIMPLE REQUIREMENTS FOR PASSWORD FOR NOW");
		if((the_pwd!=null)&&(the_pwd.equals(the_pwdalt))&&(the_pwd.length()>=the_min)&&(the_pwd.length()<=the_max)){
			if(the_pwd.length()>=3){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}
}



