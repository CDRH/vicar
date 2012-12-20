package Server;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Date;

/**
* Provides a static method for recording various events.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class LogWriter {

/**
* Path of the log file which is specified by {@link Global#LOGFILE_PATH}.
*/
private static Path destpath = Paths.get(Global.LOGFILE_PATH);

/**
* Main for standalone testing.
*/
	public static void main(String args[]){
		LogWriter.msg("IPADDR","hello world!");
		LogWriter.msg("IPADDR","hello world!");
	}

	public LogWriter(){
	}

/**
* Saves, to a log file, a simple message with a prepended IP address and timestamp.
* Used in any location within Vicar source code where it is important to track events for testing and following usage patterns.
* @param the_ipaddr The IP address of the website user.
* @param the_msg The text message.
* @return the timestamp of the message unless there is an error in which case -1 is returned.
*/
	public static long msg(String the_ipaddr,String the_msg){
		Date d = new Date();
		long ts = d.getTime();
		the_msg = the_ipaddr+","+d+","+ts+","+the_msg;
		if(!Files.exists(destpath.getParent())){
			try{
				Path p = Files.createDirectories(destpath.getParent());
			}catch(IOException ioex){
				ioex.printStackTrace();
				return -1;
			}
		}
		if(Files.exists(destpath)){
			try(BufferedWriter writer = Files.newBufferedWriter(destpath,StandardCharsets.UTF_8,StandardOpenOption.APPEND,StandardOpenOption.DSYNC)){
				writer.write(the_msg+"\n");
			}catch(IOException ioex){
				ioex.printStackTrace();
				return -1;
			}
		}else{
			try(BufferedWriter writer = Files.newBufferedWriter(destpath,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.DSYNC)){
				writer.write(the_msg+"\n");
			}catch(IOException ioex){
				ioex.printStackTrace();
				return -1;
			}
		}
		return ts;
	}
}

