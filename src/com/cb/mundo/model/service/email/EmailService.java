package com.cb.mundo.model.service.email;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;

import com.cb.mundo.model.builder.EnhancedEmailBuilder;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.exception.EmailWithoutRecipientsException;

/**
 * EJB para envio de emails de maneira assincrona
 * 
 * @author Solkam
 * @since 04 NOV 2013
 */
@Stateless
public class EmailService {
	
	@Resource(lookup="java:jboss/mail/Default")
	private Session mailSession;
	
	
	private MailSender sender;
	
	@PostConstruct
	void init() {
		sender = new JavaMailSender(mailSession);
	}
	


	/**
	 * Envio de email mais simples: para apenas um destinatario
	 * @param contactRecipient
	 * @param subject
	 * @param htmlContent
	 */
	public void sendEmail( Contact contactRecipient
						 , String replyTo
						 , String subject
						 , String htmlContent) {

		MailAdapter adapter = new MailAdapter(contactRecipient, replyTo, subject, htmlContent);
		sender.send(adapter);
	}
	

	/**
	 * Envia email para um conjunto de destinatarios
	 * @param normalContactRecipients
	 * @param subject
	 * @param htmlContent
	 */
	public void sendEmail( Set<Contact> normalContactRecipients
			             , String subject
			             , String htmlContent) {
		//verificacao...
		verifyRecicipients(normalContactRecipients);
		MailAdapter adapter = new MailAdapter(normalContactRecipients, subject, htmlContent);
		sender.send(adapter);
	}
	

	/**
	 * Envia email para um conjunto de destinatarios e copias ocultas.
	 * @param normalContactRecipients
	 * @param blindContactRecipients
	 * @param subject
	 * @param htmlContent
	 */
	public void sendEmail( Set<Contact> normalContactRecipients
						 , Set<Contact> blindContactRecipients
						 , String subject
	  					 , String htmlContent) {
		//verificacoes...
		verifyRecicipients( normalContactRecipients );

		MailAdapter adapter = new MailAdapter(normalContactRecipients, blindContactRecipients, subject, htmlContent);
		sender.send(adapter);
	}


	/**
	 * Envia email para um conjunto de destinatarios e copias ocultas e replyTO
	 * @param normalContactRecipients
	 * @param blindContactRecipients
	 * @param subject
	 * @param htmlContent
	 */
	public void sendEmail( Set<Contact> normalContactRecipients
						 , Set<Contact> blindContactRecipients
						 , String replyTo
						 , String subject
	  					 , String htmlContent) {
		//verificacoes...
		verifyRecicipients(normalContactRecipients);
		//envio
		MailAdapter adapter = new MailAdapter(normalContactRecipients, blindContactRecipients, replyTo, subject, htmlContent);
		sender.send(adapter);
	}
	
	
	/**
	 * Versao sobrecarregada que recebe um EnhancedEmailBuilder
	 * @param enhancedEmailBuilder
	 */
	public void sendEmail(EnhancedEmailBuilder enhancedEmailBuilder) {
		sendEmail( enhancedEmailBuilder.getNormalContactRecipients()
				 , enhancedEmailBuilder.getBlindContactRecipients()
				 , enhancedEmailBuilder.getReplyTO()
				 , enhancedEmailBuilder.getEmailSubject()
				 , enhancedEmailBuilder.getEmailBody() 
				 );
	}
	
	
	/**
	 * Versao sobrecarrega que recebe SimpleEmailBuilder
	 * @param simpleEmailBuilder
	 */
	public void sendEmail(SimpleEmailBuilder simpleEmailBuilder) {
		sendEmail( simpleEmailBuilder.getContactRecipient()
			     , simpleEmailBuilder.getReplyTo()
				 , simpleEmailBuilder.getSubject()
				 , simpleEmailBuilder.getContent()
				 );
	}
	
	
	
	
    //RN	
	/**
	 * Verificacao de negocio: somente serao enviados emails
	 * se houverem destinatarios
	 * @param recipients
	 */
	private void verifyRecicipients(Set<Contact> recipients) {
		if (recipients==null || recipients.isEmpty()) {
			throw new EmailWithoutRecipientsException();
		}
	}
	

}
