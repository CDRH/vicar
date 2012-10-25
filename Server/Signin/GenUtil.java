//GenUtil.java

package Server.Signin;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

import java.sql.Timestamp;

public class GenUtil {

//	public static String getDBName(){
//		return "ipmonitor_pool";
//	}

//CALENDAR SPECIFIC
private static SimpleDateFormat m_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
private static SimpleDateFormat m_sdf24 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
private static SimpleDateFormat m_sdffull = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
private static SimpleDateFormat m_sdfdateonly = new SimpleDateFormat("yyyy-MM-dd");
private static SimpleDateFormat m_sdftimeonly = new SimpleDateFormat("hh:mm a");

private static NumberFormat m_currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en","US"));

	public static String formatCurrency(int the_value){
		m_currencyFormat.setParseIntegerOnly(true);
		m_currencyFormat.setMinimumFractionDigits(0);
		m_currencyFormat.setMaximumFractionDigits(0);
		return m_currencyFormat.format(new Double(the_value));
	}

	public static String formatCurrency(float the_value){
		return m_currencyFormat.format(new Double(the_value));
	}

	public static java.util.Date getDateFromString(String the_str){
		java.util.Date ret = m_sdf.parse(the_str.trim(),new ParsePosition(0));
		return ret;
	}

	public static java.util.Date getDate24FromString(String the_str){
		java.util.Date ret = m_sdf24.parse(the_str.trim(),new ParsePosition(0));
		return ret;
	}

	public static java.util.Date getDateOnlyFromString(String the_str){
		if(the_str==null){
			return null;
		}
		java.util.Date ret = m_sdfdateonly.parse(the_str.trim(),new ParsePosition(0));
		return ret;
	}

	public static Timestamp getTimestampFromString(String the_str){
		if(the_str==null){
			return new Timestamp(0);
		}
		java.util.Date d = getDateFromString(the_str);
		Timestamp t = new Timestamp(d.getTime());
		//Timestamp t = new Timestamp(0);
		return t;
	}

	public static java.util.Date getDateFromFullString(String the_str){
		java.util.Date ret = m_sdffull.parse(the_str.trim(),
				new ParsePosition(0));
		return ret;
	}

	public static String getFullStringFromDate(java.util.Date the_date){
		if(the_date==null){
			return null;
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf = m_sdffull.format(the_date,new StringBuffer(),
				new FieldPosition(0));
		return sbuf.toString();
	}

	public static String getStringFromDate(java.util.Date the_date){
		if(the_date==null){
			return null;
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf = m_sdf.format(the_date,new StringBuffer(),
				new FieldPosition(0));
		return sbuf.toString();
	}

	public static String getYMDFromTimestamp(java.util.Date the_date){
		if(the_date==null){
			return null;
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf = m_sdfdateonly.format(the_date,new StringBuffer(),
				new FieldPosition(0));
		return sbuf.toString();
	}

	public static String getTimeFromTimestamp(java.util.Date the_date){
		if(the_date==null){
			return null;
		}
		StringBuffer sbuf = new StringBuffer();
		sbuf = m_sdftimeonly.format(the_date,new StringBuffer(),
				new FieldPosition(0));
		return sbuf.toString();
	}

	public static int getIntFromString(String the_str){
		int ret = 0;
		if(the_str==null){
			return ret;
		}
		try{
			ret = Integer.decode(the_str).intValue();
		}catch(Exception ex){
			//ex.printStackTrace();
		}
		return ret;
	}

	public static float getFloatFromString(String the_str){
		float ret = 0;
		if(the_str==null){
			return ret;
		}
		try{
			ret = Float.valueOf(the_str).floatValue();
		}catch(Exception ex){
		}
		return ret;
	}

	public static String URLEncodeString(String the_str){
		String res = "";
		try{
			res = URLEncoder.encode(the_str,"US-ASCII");
		}catch(Exception ex){
		}
		return res;
	}

	public static String URLDecodeString(String the_str){
		String res = "";
		try{
			res = URLDecoder.decode(the_str,"US-ASCII");
		}catch(Exception ex){
		}
		return res;
	}

	public static String QuoteFilter(String the_str){
		if(the_str!=null){
			the_str = the_str.replace("\"","\'");
		}
		return the_str;
	}

	//Based on code by Craig R. McClanahan, Tim Tye
	//for the Apache Software Foundation
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

	public static String HTMLUnfilter(String the_txt) {
		if(the_txt==null){
			return null;
		}
		the_txt = the_txt.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("&quot;","\"").replace("&apos;","\'");
		return the_txt;
	}

	public static String formatStr(String the_str,
			int the_prewidth,int the_postwidth){
		if(the_str==null){
			String res = "";
			for(int k=0;k<(the_prewidth+the_postwidth+1);k++){
				res += " ";
			}
			return res;
		}
		int len = the_str.length();
		int prelen = the_str.indexOf(".");
		int postlen = len-prelen-1;

		String res = "";
		for(int k=0;k<(the_prewidth-prelen);k++){
			res += " ";
		}
		res+=the_str;
		for(int k=postlen;k<the_postwidth;k++){
			res += "0";
		}
		return res;
	}

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
}

/****
	private String getForumNameFromCookie(HttpServletRequest request){
		String cookieName = "";
		String cookieValue = "";
		Cookie[] cl = request.getCookies();
		for(int i=0;i<cl.length;i++){
			Cookie c = (Cookie)cl[i];
			cookieName = (String)c.getName();
			cookieValue = (String)c.getValue();
			if(cookieName.equals("ForumName")){
				System.out.println("COOKIE NAME<"
					+cookieName+"> VALUE<"
					+cookieValue+">");
				return cookieValue;
			}
		}
		return null;
	}
****/


