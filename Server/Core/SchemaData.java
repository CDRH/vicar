//SchemaData.java

package Server.Core;

/**
* Stuff
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class SchemaData {

private String m_Name = "";
private String m_Path = "";
private int m_Type = 0;
private String m_Comment = "";
private String m_URL = "";

	public SchemaData(String the_Name,String the_Path,int the_Type,String the_Comment,String the_URL) {
		m_Name = the_Name;
		m_Path = the_Path;
		m_Type = the_Type;
		m_Comment = the_Comment;
		m_URL = the_URL;
	}

	public String getName(){
		return m_Name;
	}

	public String getPath(){
		return m_Path;
	}

	public int getType(){
		return m_Type;
	}

	public String getComment(){
		return m_Comment;
	}

	public String getURL(){
		return m_URL;
	}
}

