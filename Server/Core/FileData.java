//FileData.java

package Server.Core;

/**
* Used by FileManager.java for Vicar Collection information.  This information is retrieved from the unix directory structure (which is a mirror of the collection structure but not identical) and placed in a java.util.Vector.  This vector is passed to the xml generation method of FileManager.java. 
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class FileData {

private String m_OwnerID;
private String m_Name = "";
private int m_InputCount = 0;
//private String m_created = null;
private String m_lastModified = null;

	public FileData(String the_OwnerID,String the_Name,int the_InputCount,String the_lastModified) {
		m_OwnerID = the_OwnerID;
		m_Name = the_Name;
		m_InputCount = the_InputCount;
		m_lastModified = the_lastModified;
	}

/**
* The internal ID for the user who created the collection.
*/ 
	public String getOwnerID(){
		return m_OwnerID;
	}

/**
* The vicar collection name given by the owner.
*/ 
	public String getName(){
		return m_Name;
	}

/**
* The number of input files uploaded by the owner or extracted from owner's uploaded gip, tar, or tar.gz file.
*/
	public int getInputCount(){
		return m_InputCount;
	}

/**
* The last modified date for the collection.
*/ 
	public String lastModified(){
		return m_lastModified;
	}
}

