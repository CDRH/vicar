//ZipUtil.java


package Server.Core;

import org.junit.*;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.InvalidPathException;
import java.nio.charset.Charset;

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Adler32;
import java.util.Vector;
import java.io.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveEntry;

/**
* Utilities for zip/unzip, gzip/gunzip, and tar/untar.  Tar/untar relies on commons-compress-1.3.jar from http://commons.apache.org/compress/  
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.1, 2/15/2012
*/

public class ZipUtil {

private static final int BUFFER = 2048;
private static final int MYBUFFER = 65000;
/**
* For testing only.
*/
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

@Test public void testziputil(){
	assertTrue(true);
}

/**
* Zip all files in sourcedir with suffix and place result into destfile.
*/
	public int zip(String the_sourcedir,String the_suffix,String the_destfile){
		Path destpath = Paths.get(the_destfile);
		try(OutputStream dest = Files.newOutputStream(destpath,StandardOpenOption.CREATE);
				CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
				ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(checksum))){
			//char data[] = new char[BUFFER];

			File f = new File(the_sourcedir);
			String files[] = f.list();
			for(String sourcefilename : files){
				if(sourcefilename.endsWith(the_suffix)){
					ZipEntry entry = new ZipEntry(sourcefilename);
					try(InputStream sourcefilestream = Files.newInputStream(Paths.get(the_sourcedir+sourcefilename));
						BufferedInputStream origin = new BufferedInputStream(sourcefilestream, BUFFER)){
					//try(BufferedReader origin = Files.newBufferedReader(Paths.get(the_sourcedir+sourcefilename),Charset.defaultCharset())){
//use readAllLines instead?
//http://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#readAllLines(java.nio.file.Path,%20java.nio.charset.Charset)

						zipout.putNextEntry(entry);
						int count;
						byte data[] = new byte[BUFFER];
						while((count = origin.read(data, 0, BUFFER)) != -1) {
							zipout.write(data, 0, count);
						}
					} catch(IOException ioex) {
						//ioex.printStackTrace();
					}
				}
			}
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

/**
* Return a Vector of all filenames in zip sourcefile.
*/
	public Vector<String> listzip(String the_sourcefile){
		Vector<String> retList = new Vector<String>();
		try(InputStream fis = Files.newInputStream(Paths.get(the_sourcefile));
				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))){
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				if(entry.isDirectory()==false){
					retList.add(entry.getName());
				}
			}
		}catch(IOException ex){
			ex.printStackTrace();	
		}
		return retList;
	}

/**
* Unzip all files in sourcefile and place into destdir.
*/
	public int unzip(String the_sourcefile,String the_destdir){
		int retval = 0;
		try(FileInputStream fis = new FileInputStream(the_sourcefile);
				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))){
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[BUFFER];
				try(FileOutputStream fos = new FileOutputStream(the_destdir+entry.getName());
						BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)){
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
				}catch(IOException ex){
				}
			}
		} catch(IOException ioex) {
			//ioex.printStackTrace();
			retval = -1;
		}
		return retval;
	}

//gzip only compresses a single file, not multiple files like zip

/**
* Gzip sourcefile and place in destfile.
*/
	public int gzip(String the_sourcefile,String the_destfile){
		byte data[] = new byte[BUFFER];
		try(FileOutputStream dest = new FileOutputStream(the_destfile);
				CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
				GZIPOutputStream out = new GZIPOutputStream(new BufferedOutputStream(checksum));
				FileInputStream fi = new FileInputStream(the_sourcefile);
				BufferedInputStream origin = new BufferedInputStream(fi, BUFFER)){
			int count;
			while((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

/**
* Gunzip the file in sourcefile and place into destdir.
*/
	public int ungzip(String the_sourcefile,String the_destdir){
		int retval = 0;
//PROBLEM WITH USING AUTOCLOSEABLE AND TRY WITH RESOURCES FOR UNGZIP - CAN UNGZIP FILES MADE WITH GZIP METHOD BUT NOT THOSE MADE WITH COMMAND LINE gzip
//REVERTED TO 'OLD WAY'
/****
		try {
			Path p = Paths.get(the_sourcefile);
			String filename = p.getFileName().toString();
			System.out.println("GZIP FILE NAME<"+filename+">");
			if(filename.endsWith(".gz")){
				try(InputStream fis = Files.newInputStream(p);
				//try(FileInputStream fis = new FileInputStream(the_sourcefile);
						GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(fis));
						//GZIPInputStream zis = new GZIPInputStream(fis);
						FileOutputStream fos = new FileOutputStream(the_destdir+"/"+filename);
						BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)){
					int count;
					int total = 0;
					byte data[] = new byte[MYBUFFER];
					while ((count = zis.read(data, 0, MYBUFFER)) != -1) {
					//while ((count = zis.read(data)) > 0) {
						dest.write(data, 0, count);
						total += count;
						System.out.println("TOT<"+total+"> COUNT<"+count+">");
					}
					dest.flush();
				} catch(IOException ioex) {
					ioex.printStackTrace();
					retval = -1;
				}
			}else{
				retval = -2;
			}
		}catch(InvalidPathException ipex){
			ipex.printStackTrace();
			retval = -3;
		}
****/
/****/
                try {
                        FileInputStream fis = new FileInputStream(the_sourcefile);
                        GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(fis));
                        int count;
                        byte data[] = new byte[BUFFER];
                        int indx = the_sourcefile.indexOf(".gz");
                        if(indx>0){
                                the_sourcefile = the_sourcefile.substring(0,indx).trim();
                                indx = the_sourcefile.lastIndexOf("/");
                                //NOT SAFE ASSUMPTION FOR ALL SYSTEMS THOUGH OK FOR ABBOT
                                //IT WOULD BE BETTER TO MAKE THIS SAFER OR US java.nio.vile.Path in java 7
                                the_sourcefile = the_sourcefile.substring(indx+1);
                        }else{
                                //System.out.println("UNGZIP SOURCEFILENAME HAS NO .GZ SUFFIX");
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
/****/
		return retval;
	}

/**
* Tar all files in sourcedir with suffix and place result into destfile.
*/
	public long tar(String the_sourcedir,String the_suffix,String the_destfile){
		if((the_sourcedir==null)||(the_destfile==null)){
			return -1;
		}
		try(OutputStream os = new FileOutputStream(new File(the_destfile));
				ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("tar",os)){
			File fi = new File(the_sourcedir);
			if((fi!=null)&&(fi.isDirectory())){
				File flist[] = fi.listFiles();
				for (File datafile : flist){
					if(datafile.isDirectory()){
						//System.out.println("DIRECTORY IGNORED");
					}else if(datafile.getName().startsWith(".")){
						//System.out.println("HIDDEN FILE IGNORED");
					}else if(datafile.getName().endsWith(the_suffix)){
						//CREATE TAR ENTRY
						TarArchiveEntry entry = new TarArchiveEntry(datafile.getName());
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
						try(FileInputStream fis = new FileInputStream(datafile);
							BufferedInputStream origin = new BufferedInputStream(fis, BUFFER)){
							int count;
							while((count = origin.read(data, 0, BUFFER)) != -1) {
								aos.write(data, 0, count);
							}
							//origin.close();
						}catch(IOException ioex){
						}
						aos.closeArchiveEntry();
					}
				}
			}
			//aos.close();
		}catch(IOException|ArchiveException ex){
		}
		return 0;
	}

/**
* Unzip all files in sourcefile and place into destdir.
*/
	public int untar(String the_sourcefile,String the_destdir){
		int retval = 0;
		try(FileInputStream fis = new FileInputStream(new File(the_sourcefile));
				BufferedInputStream bis = new BufferedInputStream(fis); 
				ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(bis)){
			ArchiveEntry ae = ais.getNextEntry();
			while(ae!=null){
				if(ae.getName().startsWith("./._")){
					//System.out.println("UNTAR IGNORE MAC OSX META FILE");
				}else{
					byte data[] = new byte[BUFFER];
					try (FileOutputStream fos = new FileOutputStream(the_destdir+ae.getName());
							BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)){
						int count;
						while ((count = ais.read(data, 0, BUFFER)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();
					}catch(IOException ioex){
						retval = -1;
						ioex.printStackTrace();
					}
				}
				ae = ais.getNextEntry();
			}
		} catch (IOException|ArchiveException e) {
			e.printStackTrace();
			retval = -2;
		}
		return retval;
	}
}


