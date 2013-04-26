package edu.unl.abbot.vicar;

/**
* Installation specific settings.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/

public class Global {

/** The base directory where vicar will store all of its files.  New installations should verify it is writeable by Vicar.*/
public static String BASE_USER_DIR = "/var/vicar/userdata";

/** The directory where system provided Abbot schema files reside.*/
public static String SCHEMA_DIR = "/var/www/localhost/htdocs/";

/** The file which contains descriptions of Abbot schema files.*/
public static String SCHEMA_DESC = "/var/vicar/schemadescription.properties";

/** The path to the <i>sed</i> unix command used by {@link Server.Convert.ProcMngr} for modifying information in abbot generated files.  */
public static String SED_PATH = "/bin/sed";

/**
The base URL at which vicar will be located.  It is used by Signin.java, OpenSignin.java and Password.java to redirect after an OpenID signin or send password/registration information to the user.  If you are running it on your own computer that should likely be <i>http://127.0.0.1:8888</i> for a jetty install and <i>http://127.0.0.1:8080</i> for a tomcat install.
*/
public static String URL_BASE = "http://abbot.unl.edu:8080/cocoon";


/**The file into which log information is written by {@link LogWriter}.*/
public static String LOGFILE_PATH = BASE_USER_DIR+"/log.txt";

/** The file containing usernames and encrypted passwords written by {@link Signin.Signin}.*/
public static String ACCTFILE_PATH = BASE_USER_DIR+"/map.ser";

/** The file containing a particular user's saved settings.  Used by {@link Convert.AbbotConvert} to store the most recently used schema file name and the vector of errors found during validation.  This file is read by {@link Core.Vicar} following sign in. */
public static String SESSION_FILE = "session.txt";

/** The title displayed on the Signin.html main page.*/
public static String TITLE = "Vicar - Access to Abbot TEI-A Conversion!";

/** The URL to which Signout.html redirects.*/
public static String URL_SIGNIN = "Signin.html";

/** The URL of the application directed to by {@link Signin.Signin} or {@link Signin.OpenSignin} upon sign in success.*/
public static String URL_APPL = "Vicar.html";

/** The URL suffix needed by OpenSignin.java for a redirect. This is brittle as the first part is also found in sitemap.xmap and needs to be made less brittle.*/
public static String URL_LOGIN_SFX = "/vicar/OpenSignin.html";

/** The gmail.com user id by which email messages from Vicar will be sent to users.*/
public static String GMAIL_ID = "vicarregister";

/** The gmail.com password for GMAIL_ID by which email messages from Vicar will be send to users.
* This value should be hidden before posting to github or any public location!
*/
public static String GMAIL_PWD = "[pwd]";
}


