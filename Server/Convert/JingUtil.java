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

import java.io.FileReader;
import java.io.StringWriter;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.Vector;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
* Provides call to Jing validation for {@link AbbotConvert}.
*
* Jing requires jing-20030619.jar and may work with more recent jars as the become available at http://www.thaiopensource.com/relaxng/jing.html.  This jar comes with cocoon 2.1.11 but may be absent in other versions.
*
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/
public class JingUtil {

private Vector<Integer> m_ErrorList = new Vector<Integer>();

	/**
	* Main for standalone testing.
	*/
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

/**
* Returns Vector of integers indicating the number of errors in the validations performed by writeReportToFile since the JingUtil instance was created.
* This information is displayed next to the output files following a schema conversion.
* @return Vector of Integers corresponding to errors found during validation.
*/
	public Vector<Integer> getErrorList(){
		return m_ErrorList;
	}

/**
* Generates an HTML file to present validation messages.
* 
* @param the_schemafile The full path and name of the schema .rng file.
* @param the_xmlfile The full path and name of the validated .xml output file.
* @param the_reportfile The full path and name of the .html file to be written.
* @param the_schemafn The name of the schema .rng file for display in the output.
* @param the_xmlfn The name of the schema .rng file for display in the output.
*/
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

/**
* Invokes Jing Validation of the output file against the schema file.
*/
	public String getReport(String the_schemafile,String the_xmlfile){
		SchemaReader schemaReader = SAXSchemaReader.getInstance();
		try(StringWriter sw = new StringWriter();
				FileReader schemareader = new FileReader(the_schemafile);
				FileReader xmlreader = new FileReader(the_xmlfile)){
			ErrorHandler eh = new ErrorHandlerImpl(sw);
			PropertyMapBuilder  builder = new PropertyMapBuilder();
			builder.put(ValidateProperty.ERROR_HANDLER, eh);
			ValidationDriver vd = new ValidationDriver(builder.toPropertyMap(),schemaReader);
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


