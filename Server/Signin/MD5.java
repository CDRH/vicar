package Server.Signin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* Utilities for using MD5 digests to make unique 40 byte one way encryptions.
*/
public class MD5 {

/**
* Simple main for standalone testing.
*/
	public static void main(String args[]){
		MD5 u = new MD5();
		String s = "Four Score and Seven Years Ago...";
		String md5 = u.getMD5(s);
		System.out.println("MD5<"+md5+">");
	}

//	public MD5(){
//	}

	public String getMD5(String the_message){
		if(the_message==null){
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			//System.out.println("MESSAGE<"+the_message.getBytes()+">");
			md.update(the_message.getBytes());
			MessageDigest mdcl = (MessageDigest)md.clone();
			byte[] mdTxt = mdcl.digest();
			return byteArrayToHexString(mdTxt);
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return null;
	}

/**
* The function byteArrayToHexString is from http://www.devx.com/tips/Tip/13540
*/
	private static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0; 
		if(in == null || in.length <= 0){
			return null;
		}

		String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8","9", "A", "B", "C", "D", "E","F"};
		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F);    // must do this is high order bit is on!
			out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble 
			out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
			i++;
		}
		String rslt = new String(out);
		return rslt;
	}
}

