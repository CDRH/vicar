package edu.unl.abbot.vicar.Signin;


import java.io.Serializable;

/**
* Data object for user account information.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class AcctData implements Serializable {

private String m_ID;
private String m_Pwd;
private int m_Status = 0;
private String m_Aux = null;

public static int STATUS_REGISTERED = 1;
public static int STATUS_ACTIVE = 2;
public static int STATUS_CLOSED = 3;

private static final long serialVersionUID = 42L;


	public AcctData(String the_ID,String the_Pwd,int the_Status,String the_Aux){
		super();
		m_ID = the_ID;
		m_Pwd = the_Pwd;
		m_Status = the_Status;
		m_Aux = the_Aux;
	}

	public String getID(){
		return m_ID;
	}

	public String getPwd(){
		return m_Pwd;
	}

	public int getStatus(){
		return m_Status;
	}

	public String getAux(){
		return m_Aux;
	}

	public void Display(){
		System.out.println("\tID<"+m_ID+"> PWD<"+m_Pwd+"> STATUS<"+m_Status+"> AUX<"+m_Aux+">");
	}
}

