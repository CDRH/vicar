//DirMonitor.java

package Server.Upload;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.io.IOException;

public class DirMonitor {

private Path m_path;
private boolean shutdown = false;
private long m_totalsize = 0L;

	public static void main(String args[]){
		String monpath = "/tmp/vicar/127.0.0.1/uuu/output/";
		DirMonitor dm = new DirMonitor(monpath,3000000);
		dm.giterdone();
	}

	public DirMonitor(String the_monpath,long the_totalsize){
		m_path = Paths.get(the_monpath);
		m_totalsize = the_totalsize;
	}

	public void giterdone(){
		HashMap<String,Long> filesizeMap = new HashMap<String,Long>();
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			//WatchKey key = m_path.register(watcher,ENTRY_CREATE,ENTRY_MODIFY,ENTRY_DELETE);
			WatchKey key = m_path.register(watcher,ENTRY_MODIFY);
System.out.println("\t\tGITERDONE<"+m_path.toString()+">");
			
			while(!shutdown){
System.out.print("A");
				key = watcher.take();
				for(WatchEvent<?> event: key.pollEvents()){
					System.out.println(event.kind());
					if(event.kind() == ENTRY_MODIFY){
						Path p = (Path)event.context();
						Path absp = Paths.get(m_path.toString(),p.toString());
						System.out.println("\t"+absp.toString()+" OF SIZE "+Files.size(absp));
						filesizeMap.put(absp.toString(),new Long(Files.size(absp)));
					}
				}
				key.reset();
				Collection<Long> filesizes = filesizeMap.values();
				Iterator fsiter = filesizes.iterator();	
				Long ts = 0L;
				while(fsiter.hasNext()){
					ts += (Long)fsiter.next(); 
				}
				System.out.println("TS<"+ts+">");
				if(ts>=m_totalsize){
					shutdown = true;
				}
			}
		}catch(IOException | InterruptedException ex){
			System.out.println("MY MESSAGE"+ex.getMessage());
			ex.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


