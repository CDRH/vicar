//AjaxUtil.java

package Server.Core;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class AjaxUtil {

private static final int BUFFER = 32*1024;;

	public AjaxUtil () {
	}

/**
* Read raw bytes of a text stream and write the data to file specified by the filename.
*/
	public static int writeFileFromInputStream(String the_path,String the_filename,InputStream the_sis){
		int byteswritten = 0;
		byte data[] = new byte[BUFFER];
		int count = 0;
		try {
			FileOutputStream fos = new FileOutputStream(the_path+the_filename);
			BufferedInputStream bis = new BufferedInputStream(the_sis,BUFFER);
			data = new byte[BUFFER];
			count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				fos.write(data,0,count);
				byteswritten += count;
			}
			fos.close();
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println("BW<"+byteswritten+"> C<"+count+">");
			System.out.println("Data<"+(new String(data))+">");
		}
		return byteswritten;
	}

/**
* Read base64 data by stripping the preamble, converting the data to raw bytes, and then storing in the specified file.
*/
/****
	public static int writeImageFileFromInputStream(String the_path,String the_filename,InputStream the_sis){
		int byteswritten = 0;
		try {
			FileOutputStream fos = new FileOutputStream(the_path+the_filename);
			BufferedInputStream bis = new BufferedInputStream(the_sis,BUFFER);
			//The base64 strings made by javascript's FileReader.readAsDataURL() have a preamble
			//that contain a mime type.  Since the mime type can vary in length, the first 20 bytes
			//are tested for the mime type and then newheader is calculated to end at the beginning
			//of the actual data string.
			byte header[] = new byte[20];
			byte newheader[] = new byte[20];
			if(bis.read(header,0,20)!=-1){
				String hs = new String(header);
				System.out.println("HEADER<"+hs+">");
				if(hs!=null){
					//System.out.println("HS<"+hs+">");
					int indx = hs.indexOf(";ba");
					if(indx>0){
						int more = indx+8-20;
						if(bis.read(newheader,0,more)!=-1){
							String newhs = new String(newheader);
							System.out.println("NEWHS<"+newhs+">");
						}
					}
				}
			}
			byte data[] = new byte[BUFFER];
			int count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				byte[] s = Base64.decode(data,0,count,0);
				fos.write(s,0,s.length);
				byteswritten += s.length;
			}
			fos.close();
		}catch (Exception ex){
			ex.printStackTrace();
			//byteswritten = -byteswritten;
		}
		return byteswritten;
	}
*****/

/**
* Read base64 data by stripping the preamble, converting the data to raw bytes, and then storing in the specified file.
*/
	public static int writeBase64FileFromInputStream(String the_path,String the_filename,InputStream the_sis){
		int byteswritten = 0;
		try {
			FileOutputStream fos = new FileOutputStream(the_path+the_filename);
			BufferedInputStream bis = new BufferedInputStream(the_sis,BUFFER);
			//The base64 strings made by javascript's FileReader.readAsDataURL() have a preamble
			//that contain a mime type.  Since the mime type can vary in length, the header is read
			//3 char at a time until a base 64 conversion can be verified.

			int testlen = 40;
			byte header[] = new byte[3];
			String hs="";
			boolean headerdone = false;
			while((!headerdone)&&(bis.read(header,0,3)!=-1)&&(hs.length()<testlen)){
				hs += new String(header);
				//System.out.println("HS<"+hs+">");
				if(hs.indexOf(";ba")>0){
					headerdone = true;
					int yettoget = hs.indexOf(";ba")+8-hs.length();
					header = new byte[yettoget];
					bis.read(header,0,yettoget);
					hs += new String(header);
					System.out.println("DONEHEADER<"+hs+">");
				}
			}
			//byteswritten+=hs.length();

			byte data[] = new byte[BUFFER];
			int numberofreads = 0;
			int count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				byte[] s = Base64.decode(data,0,count,0);
				fos.write(s,0,s.length);
				byteswritten += s.length;
				numberofreads++;
			}
		System.out.println("NOREADS<"+numberofreads+">");
			fos.close();
		}catch (Exception ex){
			ex.printStackTrace();
			//byteswritten = -byteswritten;
		}
		return byteswritten;
	}
}

