//AjaxUtil.java

package Server.Upload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.InvalidPathException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		Path destpath = Paths.get(the_path+the_filename);
		try(OutputStream fos = Files.newOutputStream(destpath,StandardOpenOption.CREATE);
				BufferedInputStream bis = new BufferedInputStream(the_sis,BUFFER)){
			data = new byte[BUFFER];
			count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				fos.write(data,0,count);
				byteswritten += count;
			}
		}catch (InvalidPathException ifex){
			byteswritten = -byteswritten;
			ifex.printStackTrace();
		}catch (IOException ioex){
			byteswritten = -byteswritten;
			ioex.printStackTrace();
		}
		return byteswritten;
	}


/**
* Read base64 data by stripping the preamble, converting the data to raw bytes, and then storing in the specified file.
*/
	public static int writeBase64FileFromInputStream(String the_path,String the_filename,InputStream the_sis){
		int byteswritten = 0;
		Path destpath = Paths.get(the_path+the_filename);
		try(OutputStream fos = Files.newOutputStream(destpath,StandardOpenOption.CREATE);
				BufferedInputStream bis = new BufferedInputStream(the_sis,BUFFER)){
			//The base64 strings made by javascript's FileReader.readAsDataURL() have a preamble
			//that contain a mime type.  Since the mime type can vary in length, the header is read
			//3 char at a time until a base 64 conversion can be verified.

			int testlen = 40;//3X+1
			byte header[] = new byte[3];
			String hs="";
			boolean headerdone = false;
			while((!headerdone)&&(bis.read(header,0,3)!=-1)&&(hs.length()<testlen)){
				hs += new String(header);
				if(hs.indexOf(";ba")>0){
					headerdone = true;
					int yettoget = hs.indexOf(";ba")+8-hs.length();
					header = new byte[yettoget];
					bis.read(header,0,yettoget);
					hs += new String(header);
				}
			}

			byte data[] = new byte[BUFFER];
			int numberofreads = 0;
			int count = 0;
			while((count = bis.read(data,0,BUFFER)) != -1){
				byte[] s = Base64.decode(data,0,count,0);
				fos.write(s,0,s.length);
				fos.flush();
				byteswritten += s.length;
				numberofreads++;
			}
		}catch (InvalidPathException ifex){
			byteswritten = -byteswritten;
			ifex.printStackTrace();
		}catch (IOException ioex){
			byteswritten = -byteswritten;
			ioex.printStackTrace();
		}
		return byteswritten;
	}
}

