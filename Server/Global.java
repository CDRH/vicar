//Global.java

package Server;

/**
* Global settings for a particular installation
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
public static String SEDPATH = "/usr/bin/sed";
/****/

/****
//ABBOT
public static String URL_BASE = "http://abbot.unl.edu:8080/cocoon";
public static String BASE_USER_DIR = "/tmp/vicar";
public static String SCHEMA_DIR = "/var/www/localhost/htdocs/";
public static String SEDPATH = "/bin/sed";
****/


	public Global(){
	}
}


