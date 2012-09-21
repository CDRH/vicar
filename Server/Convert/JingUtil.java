//JingUtil.java

package Server.Convert;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.rng.SAXSchemaReader;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;

//import java.io.File;
//import java.io.FileWriter;

import java.io.FileReader;
import java.io.StringWriter;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.Vector;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JingUtil {

private Vector<Integer> m_ErrorList = new Vector<Integer>();

	public static void main(String args[]){
		String filebase = "/Users/franksmutniak/Desktop/abbottestdata/";
		String schemafile = filebase+"tei_all.rng";
		String datafile = filebase+"output/all/A00002.xml";
		JingUtil jt = new JingUtil();
		String resp = jt.getReport(schemafile,datafile);
		System.out.println("RESP <"+resp+">");
	}

	public JingUtil(){
		m_ErrorList = new Vector<Integer>();
	}

	public Vector<Integer> getErrorList(){
		return m_ErrorList;
	}

	public void writeReportToFile(String the_schemafile,String the_xmlfile,String the_reportfile,String the_schemafn,String the_xmlfn){
		String s = getReport(the_schemafile,the_xmlfile);
		Vector<String> errList = new Vector<String>();
		if(s!=null){
			StringTokenizer stok = new StringTokenizer(s,"\n");
			while(stok.hasMoreTokens()){
				String msg = stok.nextToken();
				errList.add(msg);
			}
		}
		Path p = Paths.get(the_reportfile);
		//File f = new File(the_reportfile);
		try (BufferedWriter writer = Files.newBufferedWriter(p,StandardCharsets.UTF_8,StandardOpenOption.CREATE)){
			writer.write("<html>\n");
			writer.write("<head><title>Validation Report for "+the_xmlfn+" translated with "+the_schemafn+" </title></head>\n");
			writer.write("<body style=\"color:black;font-size:100%;\">\n");
			writer.write("<div style=\"color:blue;font-size:130%;\">Validation Report for "+the_xmlfn+" translated with "+the_schemafn+" : "+errList.size()+" errors. </div>\n");
			for(String errmsg : errList){
				writer.write("<div>"+errmsg+"</div>\n");
			}
			m_ErrorList.add(errList.size());
			writer.write("</body>\n");
			writer.write("</html>\n");
			writer.flush();
			writer.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
	}

	public String getReport(String the_schemafile,String the_xmlfile){
			SchemaReader schemaReader = SAXSchemaReader.getInstance();
		try(StringWriter sw = new StringWriter();FileReader schemareader = new FileReader(the_schemafile);FileReader xmlreader = new FileReader(the_xmlfile)){
			ErrorHandler eh = new ErrorHandlerImpl(sw);
			PropertyMapBuilder  builder = new PropertyMapBuilder();
			builder.put(ValidateProperty.ERROR_HANDLER, eh);

			ValidationDriver vd = new ValidationDriver(builder.toPropertyMap(),schemaReader);

			//vd.loadSchema(new InputSource(new FileReader(the_schemafile)));
			//vd.validate(new InputSource(new FileReader(the_xmlfile)));
			vd.loadSchema(new InputSource(schemareader));
			vd.validate(new InputSource(xmlreader));

			return sw.toString();
		}catch(SAXException saxex){
			saxex.printStackTrace();
			return "validation parsing error";
		}catch(IOException ioex){
			ioex.printStackTrace();
			return "validation error";
		}
	}

}
