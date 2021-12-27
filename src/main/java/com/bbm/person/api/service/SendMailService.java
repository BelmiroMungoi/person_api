package com.bbm.person.api.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class SendMailService {
	
	private String userName = "belmiroteste@gmail.com";
	private String password = "";

	public void sendMail(String assunto, String destino, String msg) throws Exception {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");//Autorizãcão
		properties.put("mail.smtp.starttls", "true");//Autenticacão
		properties.put("mail.smtp.host", "smtp.gmail.com");//Servidor Google
		properties.put("mail.smtp.port", "465");//Porta do Server
		properties.put("mail.smtp.socketFactory.port", "465");//Porta do socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SocketFactory");//Classe de conexão do socket
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(userName, password);
			}
		});
		
		Address[] toUser = InternetAddress.parse(destino);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName));//Quem esta enviando
		message.setRecipients(Message.RecipientType.TO, toUser);//Quem recebe o email
		message.setSubject(assunto);//Assunto do email
		message.setText(msg);//Conteudo do email
		
		Transport.send(message);//Envia o email
		
	}
}
