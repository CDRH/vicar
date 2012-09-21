//LogWriter.java

package Server;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Date;

public class LogWriter {

private static Path destpath = Paths.get(Global.LOGFILE_PATH);

	public static void main(String args[]){
		LogWriter.msg("IPADDR","hello world!");
		LogWriter.msg("IPADDR","hello world!");
	}

	public LogWriter(){
	}

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
			}
		}else{
			try(BufferedWriter writer = Files.newBufferedWriter(destpath,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.DSYNC)){
				writer.write(the_msg+"\n");
			}catch(IOException ioex){
				ioex.printStackTrace();
			}
		}
		return ts;
	}
}

