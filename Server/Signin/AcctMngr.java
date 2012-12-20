package Server.Signin;

/**
* Manages Account operations via FileDB.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class AcctMngr {

private FileDB m_fDB = new FileDB();

/**
* Simple main for testing.
*/
	public static void main(String args[]){
		AcctMngr am = new AcctMngr();
//		am.registerAcct("aaa@example.com","userpwd");
//		am.confirmAcct("aaa@example.com","confirmpwd");
	}

	public AcctMngr(){
	}

/**
* Returns AcctData object if provided ID and password match.
* @param the_ID The user account ID.
* @param the_pwd The user account password.
* @return AcctData containing a users account information.
*/
	public AcctData getAcct(String the_ID,String the_pwd){
		AcctData ad = (AcctData)m_fDB.getEntry(the_ID);
		if((ad!=null)&&(ad.getPwd().equals(the_pwd))){
			return ad;
		}else{
			return null;
		}
	}

/**
* Returns AcctData object for the specified ID.
* Used by {@link Password} to change a password as the user is already signed in.
* @param the_ID The user account ID.
* @return AcctData containing a users account information.
*/
	public AcctData getAcct(String the_ID){
		AcctData ad = (AcctData)m_fDB.getEntry(the_ID);
		return ad;
	}

/**
* Sets AcctData object for the specified ID.
* @param the_ID The user account ID.
* @param the_ad The user account data object.
*/
	public void setAcct(String the_ID,AcctData the_ad){
		m_fDB.setEntry(the_ID,(Object)the_ad);
	}

/***
	public String getAcctID(String the_ID,String the_pwd){
		AcctData ad = (AcctData)m_fDB.getEntry(the_ID);
		if((ad!=null)&&(ad.getPwd().equals(the_pwd))){
			if(ad.getStatus()==AcctData.STATUS_ACTIVE){
				return ad.getID();
			}else{
				return "-"+ad.getStatus();
			}
		}else{
			return null;
		}
	}
***/

/**
* Simple call to {@link FileDB#Display} for testing.
*/ 
	public void Display(){
		m_fDB.Display();
	}
}

