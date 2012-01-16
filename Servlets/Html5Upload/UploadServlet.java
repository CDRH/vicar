//UploadServlet.java
//based on info from http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import java.util.Collection;
import java.util.Iterator;

//@WebServlet("/upload.html")
//@MultipartConfig(location="/Users/franksmutniak/Desktop/tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
@MultipartConfig()
public class UploadServlet extends HttpServlet {

public static final long serialVersionUID = 1L;
private String m_formresp = null;
private static String FILEDIR = "/Users/franksmutniak/Desktop/tmp/";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String OwnerID = (String)session.getAttribute("userid");
		String usersession = req.getParameter("usersession");
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("\t<title>Multiple File Upload Servlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div>SERVLET SESSION"+session.getId()+"</div>\n");
		out.println("<div>USER SESSION"+usersession+"</div>\n");
		out.println("<div>OWNERID "+OwnerID+"</div>");
		out.println("\t<form method=\"POST\" action=\"upload.html\" enctype=\"multipart/form-data\" >");
		out.println("\t\t<input id=\"upload_files\" name=\"upload_files\" type=\"file\" multiple=\"\" />");
		out.println("\t\t<input name=\"perform\" type=\"submit\" value=\"Upload\" />");
		out.println("\t</form>");
		out.println("\t<hr />");
		if(m_formresp!=null){
			out.println(m_formresp);
		}
		out.println("</body>");
		out.println("</html>");
		m_formresp = null;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append("<div>CONTENTTYPE"+req.getContentType()+"</div>\n");
		Collection<Part> parts = req.getParts();
		sbuf.append("<div>PARTSIZE"+parts.size()+"</div>\n");
		HttpSession session = req.getSession(true);
		sbuf.append("<div>SERVLET SESSION"+session.getId()+"</div>\n");
		for(Part part : parts){
			sbuf.append("<div>");
			sbuf.append("<div>NAME_SIZE_HEADERNAME_"+part.getName()+"_"+part.getSize()+"_"+part.getHeaderNames()+"</div>");
			for(String header : part.getHeaderNames()){
				sbuf.append("<div>HEADERNAME["+header+"] "+part.getHeader(header)+"</div>");
			}
			String fnstr = part.getHeader("content-disposition");
			if((fnstr!=null)&&(fnstr.indexOf("filename=")>0)){
				fnstr = fnstr.substring(fnstr.indexOf("filename=")+9).replace("\"","").trim();
			}else{
				fnstr = null;
			}
			sbuf.append("<span>FN "+fnstr+"</span>");
			sbuf.append("</div>");
			sbuf.append("<hr />");
/***
			InputStream is = part.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			sbuf.append("<pre>");
			while((line=br.readLine())!=null){
				sbuf.append(line+"\n");
			}
			sbuf.append("</pre>");
***/
/***/
			if(fnstr!=null){
				byte buf[] = new byte[1024];
				int len = 0;
				InputStream is = part.getInputStream();
				try {
					File outfile = new File(FILEDIR+fnstr);
					OutputStream outstream = new FileOutputStream(outfile);
					while((len = is.read(buf))>0){
						outstream.write(buf,0,len);
					}
					outstream.close();
				}catch(IOException ioex){
					ioex.printStackTrace();
				} finally{
					is.close();
				}
			}
/***/
		}
		sbuf.append("<div>DONE</div>\n");
		m_formresp = sbuf.toString();
		doGet(req,resp);
	}
}

