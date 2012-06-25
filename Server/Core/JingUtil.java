//JingUtil.java

package Server.Core;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.validate.SchemaReader;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.ValidationDriver;
import com.thaiopensource.validate.rng.SAXSchemaReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import java.util.Vector;

public class JingUtil {


	public static void main(String args[]){
		String filebase = "/Users/franksmutniak/Desktop/abbottestdata/";
		String schemafile = filebase+"tei_all.rng";
		String datafile = filebase+"output/all/A00002.xml";
		JingUtil jt = new JingUtil();
		String resp = jt.getReport(schemafile,datafile);
		System.out.println("RESP <"+resp+">");
	}

	public JingUtil(){
	}

	public void writeReportToFile(String the_schemafile,String the_xmlfile,String the_reportfile,String the_schemafn,String the_xmlfn){
		System.out.println("WRTF SF<"+the_schemafile+"> RF<"+the_reportfile+"> SCHEMAFN<"+the_schemafn+"> XML<"+the_xmlfn+">");
		String s = getReport(the_schemafile,the_xmlfile);
		try {
			Vector<String> errList = new Vector<String>();
			if(s!=null){
				StringTokenizer stok = new StringTokenizer(s,"\n");
				while(stok.hasMoreTokens()){
					String msg = stok.nextToken();
					errList.add(msg);
				}
			}
			File f = new File(the_reportfile);
			FileWriter fw = new FileWriter(f,false);
			fw.write("<html>\n");
			fw.write("<head><title>Validation Report for "+the_xmlfn+" translated with "+the_schemafn+" </title></head>\n");
			fw.write("<body style=\"color:black;font-size:100%;\">\n");
			fw.write("<div style=\"color:blue;font-size:130%;\">Validation Report for "+the_xmlfn+" translated with "+the_schemafn+" : "+errList.size()+" errors. </div>\n");
			for(String errmsg : errList){
				fw.write("<div>"+errmsg+"</div>\n");
			}
			fw.write("</body>\n");
			fw.write("</html>\n");
/****
			fw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			fw.write("<validationreport>\n");
			if(s!=null){
				StringTokenizer stok = new StringTokenizer(s,"\n");
				while(stok.hasMoreTokens()){
					String msg = stok.nextToken();
					fw.write("<msg>"+msg+"</msg>\n");
				}
			}
			fw.write("</validationreport>\n");
****/
			fw.flush();
			fw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String getReport(String the_schemafile,String the_xmlfile){
		try {
			SchemaReader schemaReader = SAXSchemaReader.getInstance();

			StringWriter sw = new StringWriter();
			ErrorHandler eh = new ErrorHandlerImpl(sw);
			PropertyMapBuilder  builder = new PropertyMapBuilder();
			builder.put(ValidateProperty.ERROR_HANDLER, eh);

			ValidationDriver vd = new ValidationDriver(builder.toPropertyMap(),schemaReader);

			vd.loadSchema(new InputSource(new FileReader(the_schemafile)));
			vd.validate(new InputSource(new FileReader(the_xmlfile)));

			return sw.toString();
		}catch(Exception ex){
			System.out.println("ERROR");
			ex.printStackTrace();
			return "error";
		}
	}

}
