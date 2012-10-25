//originally from http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/

package Server.Signin;
 
import java.util.Properties;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
 
public class SendMailSSL {
Session m_session;
	public static void main(String[] args) {
		if(args.length>=3){
			String toaddr = args[0];
			String subj = args[1];
			String msg = args[2];
			System.out.println("TO<"+toaddr+"> SUBJ<"+subj+"> MSG<"+msg+">");
			//SendMailSSL s = new SendMailSSL("vicarregister","[pwd]");
			//s.SendMail("vicarregister@gmail.com",toaddr,"test","still testing.");
		}
	}

	public SendMailSSL(final String the_username,final String the_pwd){
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

	public void SendMail(String the_sourceaddr,String the_recipient,String the_subject,String the_text){	
		try {
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

