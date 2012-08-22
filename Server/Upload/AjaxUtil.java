//AjaxUtil.java

package Server.Upload;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.apache.cocoon.environment.Session;

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
				System.out.println("\t\tAUFN<"+the_filename+">");
				byteswritten += count;
			}
			fos.close();
		}catch (Exception ex){
			byteswritten = -byteswritten;
			//ex.printStackTrace();
			//System.out.println("BW<"+byteswritten+"> C<"+count+">");
			//System.out.println("Data<"+(new String(data))+">");
		}
		return byteswritten;
	}

	public static int writeFileFromInputStream(String the_path,String the_filename,InputStream the_sis,Session the_session,int the_filesize){
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
				//System.out.println("\t\tTN<"+Thread.currentThread().getName()+"> AUFN<"+the_filename+">");
				byteswritten += count;
				int pct = (int)((100*byteswritten)/the_filesize);
				//the_session.setAttribute("upload_filepct",""+pct);
				//Thread.sleep(500);
			}
			fos.close();
		}catch (Exception ex){
			byteswritten = -byteswritten;
			//ex.printStackTrace();
			//System.out.println("BW<"+byteswritten+"> C<"+count+">");
			//System.out.println("Data<"+(new String(data))+">");
		}
		return byteswritten;
	}


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

			int testlen = 40;//3X+1
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

			byte data[] = new byte[BUFFER];
			int numberofreads = 0;
			int count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				byte[] s = Base64.decode(data,0,count,0);
				fos.write(s,0,s.length);
				byteswritten += s.length;
				numberofreads++;
			}
			fos.close();
			//System.out.println("NOREADS<"+numberofreads+">");
		}catch (Exception ex){
			//ex.printStackTrace();
			byteswritten = -byteswritten;
		}
		return byteswritten;
	}
}

