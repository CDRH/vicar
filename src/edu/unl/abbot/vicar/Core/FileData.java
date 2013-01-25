package edu.unl.abbot.vicar.Core;

/**
* Used by Vicar.java for Vicar Collection information.  This information is retrieved from the unix directory structure (which is a mirror of the collection structure but not identical) and placed in a java.util.Vector.  This vector is passed to the xml generation method of Vicar.java. 
* @see Server.Core.Vicar
*
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/

public class FileData {

private String m_OwnerID;
private String m_Name = "";
private int m_InputCount = 0;
private String m_lastModified = null;

/**
* Constructs a FileData object to store information about the user's collections.
* @param the_OwnerID The ID of the signed in user.
* @param the_Name The name of the collection.  This is either NEW or a name selected by the user which is unique to the user account.
* @param the_InputCount The number of input data files stored in the collection represented by tis FileData instance.
* @param the_lastModified the text representation of the date for display under a tooltiptext on the website.
*/
	public FileData(String the_OwnerID,String the_Name,int the_InputCount,String the_lastModified) {
		m_OwnerID = the_OwnerID;
		m_Name = the_Name;
		m_InputCount = the_InputCount;
		m_lastModified = the_lastModified;
	}

/**
* The ID for the user who created the collection.
* @return The ID of the owner of the collection.
*/ 
	public String getOwnerID(){
		return m_OwnerID;
	}

/**
* The vicar collection name given by the owner.
* This name is unique among all of the user's collections.  The name starts as 'NEW' and the user is asked to give it a name.
* @return The name of collection's name.
*/ 
	public String getName(){
		return m_Name;
	}

/**
* The number of input files uploaded by the owner or extracted from owner's uploaded gip, tar, or tar.gz file.
* @return The number of data files in the input subdirectory of this collection.
*/
	public int getInputCount(){
		return m_InputCount;
	}

/**
* The text representation of the last modified date for the collection.
* @return The text of the date when this collection was last modified.
*/ 
	public String lastModified(){
		return m_lastModified;
	}
}

