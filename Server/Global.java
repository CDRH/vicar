//Global.java

package Server;

/**
* Installation specific settings.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class Global {


/****/
//LOCAL
public static String URL_BASE = "http://127.0.0.1:8888";
public static String BASE_USER_DIR = "/tmp/vicar";
public static String SCHEMA_DIR = "/users/franksmutniak/Desktop/abbottestdata/schema/";
public static String SED_PATH = "/usr/bin/sed";
public static String LOGFILE_PATH = "/tmp/vicar/log.txt";
public static String ACCTFILE_PATH = "/tmp/vicar/map.ser";
/****/

/****
//ABBOT
public static String URL_BASE = "http://abbot.unl.edu:8080/cocoon";
public static String BASE_USER_DIR = "/tmp/vicar";
public static String SCHEMA_DIR = "/var/www/localhost/htdocs/";
public static String SED_PATH = "/bin/sed";
public static String LOGFILE_PATH = "/tmp/vicar/log.txt";
public static String ACCTFILE_PATH = "/tmp/vicar/map.ser";
****/

public static String URL_LOGIN_SFX = "/vicar/OpenSignin/OpenSignin.html";
public static String URL_APPL = "../Core/FileManager.html?mode=1";
public static String URL_APPL_LOGOUT = "../Core/FileManager.html";

	public Global(){
	}
}


