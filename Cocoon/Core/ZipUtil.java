//ZipUtil.java

//adapted from http://java.sun.com/developer/technicalArticles/Programming/compression/

package Core;

import java.util.zip.*;
import java.util.Vector;
import java.io.*;

public class ZipUtil {

private static final int BUFFER = 2048;

	public static void main(String args[]){
		String dir = "/Users/franksmutniak/Desktop/userdata/tmp/";
		ZipUtil z = new ZipUtil();
		z.zip(dir,".txt",dir+"newlycreated.zip");
		Vector<String> list = z.listzip(dir+"newlycreated.zip");
		for(String filename : list){
			System.out.println(filename);
		}
		//z.unzip(dir+"newlycreated.zip",dir+"new/");
	}

	public ZipUtil(){
	}

	public int zip(String the_sourcedir,String the_suffix,String the_destfile){
		try {
			FileOutputStream dest = new FileOutputStream(the_destfile);
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
			//out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
			// get a list of files from current directory
			File f = new File(the_sourcedir);
			String files[] = f.list();
			for (int i=0; i<files.length; i++) {
				System.out.println("Adding: "+files[i]);
				if(files[i].endsWith(the_suffix)){
					FileInputStream fi = new FileInputStream(the_sourcedir+files[i]);
					BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(files[i]);
					out.putNextEntry(entry);
					int count;
					while((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
			out.close();
			System.out.println("checksum: "+checksum.getChecksum().getValue());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Vector<String> listzip(String the_sourcefile){
		Vector<String> retList = new Vector<String>();
		try {
			FileInputStream fis = new FileInputStream(the_sourcefile);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				if(entry.isDirectory()==false){
					retList.add(entry.getName());
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();	
		}
		return retList;
	}

	public int unzip(String the_sourcefile,String the_destdir){
		int retval = 0;
		try {
			FileInputStream fis = new FileInputStream(the_sourcefile);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " +entry.getName()+" ISDIR<"+entry.isDirectory()+">");
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(the_destdir+entry.getName());
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch(Exception e) {
			retval = -1;
			e.printStackTrace();
		}
		return retval;
	}
}


