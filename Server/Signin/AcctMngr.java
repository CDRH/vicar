//AcctMngr.java

package Server.Signin;

public class AcctMngr {

private FileDB m_fDB = new FileDB();

	public static void main(String args[]){
		AcctMngr am = new AcctMngr();
//		am.registerAcct("aaa@example.com","userpwd");
//		am.confirmAcct("aaa@example.com","confirmpwd");
	}

	public AcctMngr(){
	}

	public AcctData getAcct(String the_ID,String the_pwd){
		AcctData ad = (AcctData)m_fDB.getEntry(the_ID);
		if((ad!=null)&&(ad.getPwd().equals(the_pwd))){
			return ad;
		}else{
			return null;
		}
	}

	public AcctData getAcct(String the_ID){
		AcctData ad = (AcctData)m_fDB.getEntry(the_ID);
		return ad;
	}

	public void setAcct(String the_ID,AcctData the_ad){
		m_fDB.setEntry(the_ID,(Object)the_ad);
	}

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

	public int updatePassword(AcctData the_ad,String the_newpwd){
		return 0;
	}

	public void Display(){
		m_fDB.Display();
	}
}

