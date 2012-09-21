//LocalLog.java

package Server.OpenSignin;

/***
import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
****/
import java.io.*;

public class LocalLog {

//private Path m_filepath;
FileWriter m_filepath;

	public static void main(String args[]){
		LocalLog l = new LocalLog("/tmp/vicar/log.txt");
		l.msg("hello world!");
	}

	public LocalLog(String the_pathname){
		try {
/***
			Path p = Paths.get(the_pathname);
			m_filepath = Files.createFile(p);
***/
			File f = new File(the_pathname);
			m_filepath = new FileWriter(f,false);
		}catch(Exception ex){
		}
	}

	public void msg(String the_msg){
		if(m_filepath!=null){
			try {
				m_filepath.write(the_msg);
				m_filepath.flush();
			}catch(Exception ex){
			}
/***
			try (BufferedWriter writer = Files.newBufferedWriter(m_filepath,StandardCharsets.UTF_8,StandardOpenOption.WRITE)){
				writer.write(the_msg);
			}catch(Exception ex){
			}
***/
		}
	}
}

