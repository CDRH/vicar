//MonitorData.java

package Server.Progress;

/**
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class MonitorData {

private boolean m_isnew = false;;
private String m_name = null;
private int m_value = 0;
private MonitorData m_md;

	public MonitorData(boolean the_isnew,String the_name,int the_value){
		m_isnew = the_isnew;
		m_name = the_name;
		m_value = the_value;
	}

	public boolean isnew(){
		return m_isnew;
	}

	public void setnew(boolean the_new){
		m_isnew = the_new;
	}

	public String getname(){
		return m_name;
	}

	public int getvalue(){
		return m_value;
	}

	public void setvalue(int the_value){
		m_value = the_value;
	}

	public void setNext(MonitorData the_md){
		m_md = the_md;
	}

	public MonitorData getNext(){
		return m_md;
	}

	public void Display(){
		System.out.println("NAME<"+m_name+"> VALUE<"+m_value+">");
	}
}

