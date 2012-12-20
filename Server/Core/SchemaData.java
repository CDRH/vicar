package Server.Core;

/**
* Stores information associated with a Relax NG Schema file.  This information is used for system provided schemas as well as user supplied schemas.
* Used by {@link SchemaList} to provide {@link Vicar} with a list to display in a select list.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
* @see SchemaList
*/

public class SchemaData {

private String m_Name = "";
private String m_Path = "";
private int m_Type = 0;
private int m_Current = 0;
private String m_Comment = "";
private String m_URL = "";

/**
* Constructor for initializing all values.
*/
	public SchemaData(String the_Name,String the_Path,int the_Type,int the_Current,String the_Comment,String the_URL) {
		m_Name = the_Name;
		m_Path = the_Path;
		m_Type = the_Type;
		m_Current = the_Current;
		m_Comment = the_Comment;
		m_URL = the_URL;
	}

/**
* Returns the name of the schema file.
* @return The name of the schema file.
*/
	public String getName(){
		return m_Name;
	}

/**
* Returns the path of the schema file.
* @return The path of the schema file.
*/
	public String getPath(){
		return m_Path;
	}

/**
* Returns the type of the schema file.
* The type is 1 for system provided schemas and 2 for user supplied schemas.
* (This information is specific to the implementation of {@link SchemaList}.)
* @return The type of the schema file.
*/
	public int getType(){
		return m_Type;
	}

/**
* Returns the status of this particular Schema.  This information is set by {@link SchemaList}.
* @return 1 if this is the currently selected schema, 0 otherwise.
*/
	public int getCurrent(){
		return m_Current;
	}

/**
* Returns a text comment describing this particular schema.
* This is for use in a tool tip text style comment produced by hovering the mouse above the schema name in the select list.
* @return Text comment describing the schema.
*/
	public String getComment(){
		return m_Comment;
	}

/**
* Reserved for future use in case schemas are accessible via URL.
* @return The URL of the schema file.
*/
	public String getURL(){
		return m_URL;
	}
}

