package com.cb.mundo.model.service.email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.cb.mundo.model.exception.InfraException;

/**
 * Estrategy que usa o JavaMail como infra-estrutura
 * 
 * @author vitor
 * @since 28 ABR 2014
 */
public class JavaMailSender implements MailSender {

	private static final String TEXT_HTML = "text/html; charset=utf-8";
	
	private final Session mailSession;

	public JavaMailSender(Session s) {
		mailSession = s;
	}


	@Override
	public String send(MailAdapter adapter) {
		try {
			MimeMessage mm = new MimeMessage(mailSession);

			mm.setRecipients(Message.RecipientType.TO,  adapter.getTOInternetAddressArray() );
			
			if (adapter.isFillBlindRecipients()) {
				mm.setRecipients(Message.RecipientType.BCC, adapter.getBCCInternetAddressArray() );
			}
			
			if (adapter.isFillReplyTo()) {
				mm.setReplyTo( adapter.getReplayToInternetAddressArray() );
			}
			
			mm.setSubject( adapter.getSubject() );
			
			mm.setContent( adapter.getContent(), TEXT_HTML );
			
			Transport.send( mm );
			
			return "sucess";
			
		}catch (Exception e) {
			throw new InfraException( e );
		}
	}
	

}
