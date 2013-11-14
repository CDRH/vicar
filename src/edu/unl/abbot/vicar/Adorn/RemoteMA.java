package edu.unl.abbot.vicar.Adorn;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.net.*;
import java.util.Vector;

public class RemoteMA {


final String end = "\r\n";
final String twoHyphens = "--";
final String boundary = "*****++++++************++++++++++++";
private static String DEFAULT_CORPUS = "eme";
//private static String DEFAULT_URL = "http://127.0.0.1:8888/";
//private static String DEFAULT_URL = "http://127.0.0.1:8182/teiadorner";
//public static String DEFAULT_URL = "http://devadorner.northwestern.edu/maserver/teiadorner";
public static String DEFAULT_URL = "http://abbot.unl.edu:8080/maserver/teiadorner";

	public static void main(String[] args) {
		RemoteMA p = new RemoteMA();
		String SrcFilePath = null;
		String DestFilePath = null;
		String UseChoice = "false";
		String CorpusConfig = DEFAULT_CORPUS;
		String URL = DEFAULT_URL;
/****/
		//String USERDIR= "/var/vicar/userdata/fsmutniak__gmail.com/eee/";
		//p.uploadDirs(URL,CorpusConfig,UseChoice,USERDIR+"output/",USERDIR+"adorn/");
		String USERDIR= "/Users/franksmutniak/Desktop/Vicar/abbottestdata/output/all/";
		String OUTPUTDIR="/Users/franksmutniak/Desktop/Adorn/";
		p.uploadDirs(URL,CorpusConfig,UseChoice,USERDIR,OUTPUTDIR);
/****/
/****
		if(args.length<2){
			RemoteMA.DisplayUsage();
			return;
		}else{
			CorpusConfig = args[0].toLowerCase();
			if((CorpusConfig.equals("eme"))||(CorpusConfig.equals("ece"))||(CorpusConfig.equals("ncf"))){
			}else{
				System.out.println("UNKNOWN CORPUS");
				RemoteMA.DisplayUsage();
				return;
			}
			SrcFilePath = args[1];
			if(args.length>2){
				DestFilePath = args[2];
			}
			System.err.println("POSTing data to <"+URL+">");
			if(DestFilePath==null){
				System.err.println("Using corpus <"+CorpusConfig+"> to adorn <"+SrcFilePath+">");
			}else{
				System.err.println("Using corpus <"+CorpusConfig+"> to adorn <"+SrcFilePath+"> and save to <"+DestFilePath+">");
			}
			int result = p.upload(URL,CorpusConfig,UseChoice,SrcFilePath,DestFilePath);
			if(result>=0){
				//System.err.println("OK");
			}else if(result==-1){
				System.err.println("Source File not found!");
			}else{
				System.err.println("Connection Error");
			}
		}
****/
	}

	public static void DisplayUsage(){
		System.out.println("\tUsage:");
		System.out.println("\t\tjava RemoteMA <corpus> <src_file_path>				- sends result to stdout");
		System.out.println("\t\tjava RemoteMA <corpus> <src_file_path> <result_file_path>	- sends result to specified result file");
		System.out.println("\t\t\tWhere <corpus> is eme, ece, or ncf for Early Modern English, Eighteenth Century English, or Nineteenth Century Fiction");
		System.out.println("\tExamples:");
		System.out.println("\t\tjava RemoteMA eme testdata/A00025.xml");
		System.out.println("\t\tjava RemoteMA ncf testdata/A00025.xml result/A00025_adorned.xml");
		System.out.println("\n");
	}

	public void uploadDirs(String the_URL,String the_Corpus,String the_UseChoice,String the_SrcDirPath,String the_DestDirPath){
		Vector<String> dirList = listFiles(the_SrcDirPath,".xml");
		for(String fileName : dirList){
			System.out.println(the_SrcDirPath+fileName);
			upload(the_URL,the_Corpus,the_UseChoice,the_SrcDirPath+fileName,the_DestDirPath+fileName);
		}
	}

	public Vector<String> listFiles(String the_dirpath,String the_suffix){
		Vector<String> dirList = new Vector<String>();
		try {
			File f = new File(the_dirpath);
			if(f!=null){
				String files[] = f.list();
				if(files!=null){
					for (int i=0; i<files.length; i++) {
						if((files[i]!=null)&&(the_suffix!=null)&&(files[i].endsWith(the_suffix))){
							dirList.add(files[i]);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dirList;
	}

	public int upload(String the_URL,String the_Corpus,String the_UseChoice,String the_SrcFilePath,String the_DestFilePath){
		try {
		Path srcPath = Paths.get(the_SrcFilePath);
		if(!Files.exists(srcPath)){
			return -1;
		}

		String srcFN = srcPath.getFileName().toString();

		URL url = new URL(the_URL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		/* setRequestProperty */
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ boundary);

		DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"resultsAsAttachedFile\""+end+end+"false"+end);
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"corpusConfig\""+end+end+the_Corpus+end);
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"useChoice\""+end+end+the_UseChoice+end);
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"adorn\""+end+end+"Adorn"+end);
		ds.writeBytes(twoHyphens + boundary + end);
		//ds.writeBytes("Content-Disposition: form-data; name=\"teifile\";filename=\"" + uploadFile +"\"" + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"teifile\";filename=\"" +srcFN+"\"" + end);
		ds.writeBytes(end);

		//WRITE FILE
		int BUFFER = 1024;
		try(InputStream srcfilestream = Files.newInputStream(srcPath);BufferedInputStream srbs = new BufferedInputStream(srcfilestream,BUFFER)){
			int count = 0;
			byte[] data = new byte[BUFFER];
			while((count= srbs.read(data,0,BUFFER))!=-1){
				ds.write(data,0,count);
			}
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
		//System.out.println(conn.getOutputStream().toString());
		ds.flush();
		ds.close();

		if(conn.getResponseCode()!=200){
			System.err.println("RESPONSE CODE:"+conn.getResponseCode());
			System.err.println("RESPONSE  MSG:"+conn.getResponseMessage());
			return -2;
		}

		//READ RESPONSE
		if(the_DestFilePath==null){
			//SEND TO STDOUT
			StringBuffer b = new StringBuffer();
			try(InputStream respstream = conn.getInputStream();BufferedInputStream srbs = new BufferedInputStream(respstream,BUFFER)){
				int count = 0;
				byte[] data = new byte[BUFFER];
				while((count= srbs.read(data,0,BUFFER))!=-1){
					b.append(new String(data, 0, count));
				}
			}catch(IOException ioex){
				ioex.printStackTrace();
			}
			String result = b.toString();
			String controlM = "\r";
			result = result.replace("\r","");//REPLACE CONTROL_M
			System.out.println(result);
		}else{
			//SEND TO FILE
			try(OutputStream fos = Files.newOutputStream(Paths.get(the_DestFilePath),StandardOpenOption.CREATE);BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)){
				try(InputStream respstream = conn.getInputStream();BufferedInputStream srbs = new BufferedInputStream(respstream,BUFFER)){
					int count = 0;
					byte[] data = new byte[BUFFER];
					while((count = srbs.read(data,0,BUFFER))!=-1){
//System.out.println("COUNT<"+count+">");
						dest.write(data, 0, count);
					}
				}catch(IOException ioex){
					ioex.printStackTrace();
				}
				dest.flush();
			}catch(IOException ioex){
				ioex.printStackTrace();
			}
		}

/***/		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
}

