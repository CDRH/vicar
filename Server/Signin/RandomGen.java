//RandomGen.java

package Server.Signin;

import java.io.*;
import java.text.*;
import java.util.*;

public class RandomGen {

private static String ALPHA = "BCDFGHJKLMNPQRSTVWXYZ";
private static String BASE64= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
private static String BASE62= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
private static String ZEROS = "000000000";

	public static void main(String args[]){
		for(int i=0;i<40;i++){
			//System.out.println("RAND<"+RandomGen.generatePublicID(1000)+">");
			System.out.println("RAND<"+RandomGen.generatePublicIDString(20000,6)+">");
		}
	}

//RETURNS ODD NUMBER
	public static int generatePublicID(int the_max){
		long l = Calendar.getInstance().getTimeInMillis();
		Random r = new Random(l);
		int n = r.nextInt(the_max);
		if(n%2==0){
			n+=1;
		}
		if(n>the_max){
			return the_max;
		}
		return n;
	}

	public static String generatePublicIDString(int the_max,int the_len){
		long l = Calendar.getInstance().getTimeInMillis();
		Random r = new Random(l);
		int n = r.nextInt(the_max);
		if(n%2==0){
			n+=1;
		}
		if(n>the_max){
			n = the_max;
		}
		String s = ""+n;
		int sl = s.length();
		//System.out.println("SL<"+sl+"> LEN<"+the_len+">");
		if(sl<the_len){
			s = ZEROS.substring(0,the_len-sl)+s;
		}
		return s;
	}

/****
	public static int generateID(int the_max){
		long l = Calendar.getInstance().getTimeInMillis();
		int seed = (int)(l%(the_max-1));
		Random r = new Random(seed);
		int n = r.nextInt();
		if(n%2==0){
			n+=1;
		}
		return n;
	}
****/

	public static String generatePassword(int the_length){
		String retVal = "";
		long l = Calendar.getInstance().getTimeInMillis();
		int seed = (int)(l%1000000);
		Random r = new Random(seed);
		int n = 0;
		for(int i=0;i<the_length;i++){
			n = r.nextInt();
			if(n<0){n = -n;}
			retVal += ALPHA.charAt(n%21);
		}
		return retVal;
	}

	public static String generateB64ID(int the_length){
		String retVal = "";
		long l = Calendar.getInstance().getTimeInMillis();
		int seed = (int)(l%1000000);
		Random r = new Random(seed);
		int n = 0;
		for(int i=0;i<the_length;i++){
			n = r.nextInt();
			if(n<0){n = -n;}
			retVal += BASE64.charAt(n%63);
		}
		return retVal;
	}

	public static String generateB62ID(int the_length){
		String retVal = "";
		long l = Calendar.getInstance().getTimeInMillis();
		int seed = (int)(l%1000000);
		Random r = new Random(seed);
		int n = 0;
		for(int i=0;i<the_length;i++){
			n = r.nextInt();
			if(n<0){n = -n;}
			retVal += BASE62.charAt(n%61);
		}
		return retVal;
	}
}

