//ZipUtil.java

//zip functionality adapted from http://java.sun.com/developer/technicalArticles/Programming/compression/
//tar functionality from apache commons compress

package Core;

import java.util.zip.*;
import java.util.Vector;
import java.io.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveEntry;


public class ZipUtil {

private static final int BUFFER = 2048;

	public static void main(String args[]){
		String dir = "/Users/franksmutniak/Desktop/userdata/tmp/";
		ZipUtil z = new ZipUtil();
		/****/
		//z.zip(dir,".xml",dir+"ziptest.zip");
		Vector<String> list = z.listzip(dir+"ziptest.zip");
		for(String filename : list){
			System.out.println(filename);
		}
		z.unzip(dir+"ziptest.zip",dir+"new/");
		/****/
		/****
		z.tar(dir,".xml",dir+"newlycreated.tar");
		z.gzip(dir+"newlycreated.tar",dir+"new/"+"newlycreated.tar.gz");
		dir+="new/";
		z.ungzip(dir+"newlycreated.tar.gz",dir);
		z.untar(dir+"newlycreated.tar",dir);
		****/
		/****
		z.ungzip(dir+"all.tar.gz",dir+"new/");
		System.out.println("UNGZIPED");
		z.untar(dir+"new/"+"all.tar",dir+"/new");
		****/
		/****
		dir = "/Users/franksmutniak/Desktop/abbottestdata/input/";
		z.tar(dir,".xml",dir+"all.tar");
		z.gzip(dir+"all.tar",dir+"all.tar.gz");
		****/
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
			for(String fs : files){
				if(fs.endsWith(the_suffix)){
					//FileInputStream fi = new FileInputStream(the_sourcedir+files[i]);
					FileInputStream fi = new FileInputStream(the_sourcedir+fs);
					BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
					//ZipEntry entry = new ZipEntry(files[i]);
					ZipEntry entry = new ZipEntry(fs);
					out.putNextEntry(entry);
					int count;
					while((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
			out.close();
			//System.out.println("checksum: "+checksum.getChecksum().getValue());
			checksum.close();
			dest.close();
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
			zis.close();
			fis.close();
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
				//System.out.println("Extracting: " +entry.getName()+" ISDIR<"+entry.isDirectory()+">");
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
				fos.close();
			}
			zis.close();
			fis.close();
		} catch(Exception e) {
			retval = -1;
			e.printStackTrace();
		}
		return retval;
	}

//FSS
//gzip only compresses a single file, not multiple files like zip

	public int gzip(String the_sourcefile,String the_destfile){
		try {
			FileOutputStream dest = new FileOutputStream(the_destfile);
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			GZIPOutputStream out = new GZIPOutputStream(new BufferedOutputStream(checksum));
			//out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
					FileInputStream fi = new FileInputStream(the_sourcefile);
					BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
					int count;
					while((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
			out.close();
			//System.out.println("checksum: "+checksum.getChecksum().getValue());
			checksum.close();
			dest.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int ungzip(String the_sourcefile,String the_destdir){
		int retval = 0;
		try {
			FileInputStream fis = new FileInputStream(the_sourcefile);
			GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(fis));
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			int indx = the_sourcefile.indexOf(".gz");
			if(indx>0){
				the_sourcefile = the_sourcefile.substring(0,indx).trim();
				indx = the_sourcefile.lastIndexOf("/");
				//NOT SAFE ASSUMPTION FOR ALL SYSTEMS THOUGH OK FOR ABBOT
				//IT WOULD BE BETTER TO MAKE THIS SAFER OR US java.nio.vile.Path in java 7
				the_sourcefile = the_sourcefile.substring(indx+1);
				System.out.println("UNGZIP SOURCEFILENAME<"+the_sourcefile+">");
			}else{
				System.out.println("UNGZIP SOURCEFILENAME HAS NO .GZ SUFFIX");
			}
			FileOutputStream fos = new FileOutputStream(the_destdir+the_sourcefile);
			BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = zis.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			zis.close();
			fis.close();
		} catch(Exception e) {
			retval = -1;
			e.printStackTrace();
		}
		return retval;
	}

	public long tar(String the_sourcedir,String the_suffix,String the_destfile){
		try {
			if((the_sourcedir==null)||(the_destfile==null)){
				return -1;
			}
			File fo = new File(the_destfile);
			OutputStream os = new FileOutputStream(fo);
			ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("tar",os);

			//System.out.println("TARRING <"+the_sourcedir+">");
			File fi = new File(the_sourcedir);
			//System.out.println("TAR ISDIR<"+fi.isDirectory()+">");
			if((fi!=null)&&(fi.isDirectory())){
				File flist[] = fi.listFiles();
				for (File datafile : flist){
					//System.out.println(datafile.getName());
					if(datafile.isDirectory()){
						//System.out.println("DIRECTORY IGNORED");
					}else if(datafile.getName().startsWith(".")){
						//System.out.println("HIDDEN FILE IGNORED");
					//}else if(datafile.getName().endsWith(".tar")){
					//	System.out.println("DONT RECURSE");
					}else if(datafile.getName().endsWith(the_suffix)){
						//CREATE TAR ENTRY
						TarArchiveEntry entry = new TarArchiveEntry(datafile.getName());
						//System.out.println("DATALEN<"+datafile.length()+">");	
						entry.setSize(datafile.length());
						//entry.setModTime(0);
						//entry.setUserId(0);
						//entry.setGroupId(0);
						//entry.setUserName("avalon");
						//entry.setGroupName("excalibur");
						//entry.setMode(0100000);
						entry.setMode(0644);
						aos.putArchiveEntry(entry);

						//READ FILE AND PUT DATA IN TAR
						byte data[] = new byte[BUFFER];
						FileInputStream fis = new FileInputStream(datafile);
						BufferedInputStream origin = new BufferedInputStream(fis, BUFFER);
						int count;
						while((count = origin.read(data, 0, BUFFER)) != -1) {
							aos.write(data, 0, count);
						}
						origin.close();

						aos.closeArchiveEntry();
					}
				}
			}
			aos.close();
		}catch(Exception ex){
		}
		return 0;
	}

	public int untar(String the_sourcefile,String the_destdir){
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			//System.out.println("UNTARRING <"+the_sourcefile+">");
			fis = new FileInputStream(new File(the_sourcefile));
			bis = new BufferedInputStream(fis); 
			ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(bis);
			ArchiveEntry ae = ais.getNextEntry();
			while(ae!=null){
				//System.out.println("Entry<"+ae.getName()+"> DIR?<"+ae.isDirectory()+">");
				if(ae.getName().startsWith("./._")){
					System.out.println("UNTAR IGNORE MAC OSX META FILE");
				}else{
					FileOutputStream fos = new FileOutputStream(the_destdir+ae.getName());
					byte data[] = new byte[BUFFER];
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
					int count;
					while ((count = ais.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					fos.close();
				}
				ae = ais.getNextEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}


