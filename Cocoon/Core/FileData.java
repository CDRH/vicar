//FileData.java

package Core;

public class FileData {

private String m_OwnerID;
private String m_Name = "";
private int m_Count = 0;
private String m_created = null;
private String m_lastModified = null;

	public FileData(String the_OwnerID,String the_Name,int the_Count) {
		m_OwnerID = the_OwnerID;
		m_Name = the_Name;
		m_Count = the_Count;
	}

	public String getOwnerID(){
		return m_OwnerID;
	}

	public String getName(){
		return m_Name;
	}

	public int getCount(){
		return m_Count;
	}
}

