//Global.java

package Server;

/**
* Installation specific settings.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class Global {


/****
//LOCAL
public static String BASE_USER_DIR = "/var/vicar";
public static String SCHEMA_DIR = "/users/franksmutniak/Desktop/abbottestdata/schema/";
public static String SED_PATH = "/usr/bin/sed";
public static String URL_BASE = "http://127.0.0.1:8888";
****/

/****/
//ABBOT
public static String BASE_USER_DIR = "/var/vicar";
public static String SCHEMA_DIR = "/var/www/localhost/htdocs/";
public static String SED_PATH = "/bin/sed";
public static String URL_BASE = "http://abbot.unl.edu:8080/cocoon";
/****/

public static String LOGFILE_PATH = BASE_USER_DIR+"/log.txt";

//SIGNIN
public static String ACCTFILE_PATH = BASE_USER_DIR+"/map.ser";

//public static String URL_SIGNIN = "../Signin/Signin.html";
//public static String URL_APPL = "../Core/FileManager.html";

public static String TITLE = "Vicar - Gateway to Abbot";
public static String URL_SIGNIN = "Signin.html";
public static String URL_APPL = "Vicar.html";

//FSS - UNFORTUNATELY THIS IS A HARDCODED LINK - SEE IF IT CAN BE MADE RELATIVE!
//public static String URL_LOGIN_SFX = "/vicar/Signin/OpenSignin.html";
public static String URL_LOGIN_SFX = "/vicar/OpenSignin.html";

public static String GMAIL_ID = "[username]";
public static String GMAIL_PWD = "[pwd]";

	public Global(){
	}
}


