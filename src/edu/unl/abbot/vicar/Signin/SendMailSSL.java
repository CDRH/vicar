package edu.unl.abbot.vicar.Signin;

 
import java.util.Properties;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;

//import edu.unl.abbot.vicar.LogWriter;

/**
* Based on sample code from http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
* Requires the addition of mail.jar from http://www.oracle.com/technetwork/java/javamail/index.html
* The addition of mail.jar likely requires the removal of geronimo-spec-javamail-1.3.1-rc5.jar which comes in the cocoon 2.1.11 distribution as it collides with mail.jar.
* 
* @author Frank Smutniak, Center for Digital Research in the Humanities, http://cdrh.unl.edu
* @version 0.8, 12/15/2012
*/ 
public class SendMailSSL {
Session m_session;

/**
* Simple main for standalone testing.
*/
	public static void main(String[] args) {
		if(args.length>=3){
			String toaddr = args[0];
			String subj = args[1];
			String msg = args[2];
			System.out.println("TO<"+toaddr+"> SUBJ<"+subj+"> MSG<"+msg+">");
			SendMailSSL s = new SendMailSSL("vicarregister","[pwd]");
			s.SendMail("vicarregister@gmail.com",toaddr,"test","still testing.");
		}
	}

/**
* Constructor to set the username and password.
* Hardcoded for google accounts.
*
* @param the_username The username (without the @gmail.com suffix).
* @param the_pwd The password.
*/
	public SendMailSSL(final String the_username,final String the_pwd){
		//LogWriter.msg("IP","sendmail constructor user<"+the_username+"> pwd<"+the_pwd+">");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		m_session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(the_username,the_pwd);
				}
			});
		//m_session.setDebug(true); 
	}

/**
* Send an email.
* 
* @param the_sourceaddr The full email address that this email should appear to come from.
* @param the_recipient The full email address of the recipient.
* @param the_subject The subject of the message.
* @param the_text The body of the message.
*/
	public void SendMail(String the_sourceaddr,String the_recipient,String the_subject,String the_text){	
		try {
			//System.out.println("IP endmail start SRC<"+the_sourceaddr+"> TO<"+the_recipient+"> SUB<"+the_subject+">");
			Message message = new MimeMessage(m_session);
			message.setFrom(new InternetAddress(the_sourceaddr));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(the_recipient));
			message.setSubject(the_subject);
			message.setText(the_text);
			//message.setContent(the_text,"text/html");
			Transport.send(message);
		} catch (MessagingException e) {
			System.out.println("SENDMAIL ERROR");
			e.printStackTrace();
			//throw new RuntimeException(e);
		}
	}
}

