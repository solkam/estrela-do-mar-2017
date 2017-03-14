package com.cb.mundo.model.service.email;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.cb.mundo.model.entity.Contact;

/**
 * Adaptador para Messagem de Mail
 * 
 * @author Solkam
 * @since 03 NOV 2013
 */
public class MailAdapter {
	
	private Set<Contact> normalContactRecipients = new TreeSet<>();
	private Set<Contact> blindContactRecipients = new TreeSet<>();
	private String replyTo;
	
	private String subject;
	
	private String content;


	
//construtores...	
	
	/**
	 * Construtor 1
	 * @param normalContactRecipient
	 * @param subject
	 * @param content
	 */
	public MailAdapter(Contact normalContactRecipient,
			String replyTO,
			String subject, 
			String content) {
		super();
		this.normalContactRecipients.add(normalContactRecipient);
		this.replyTo = replyTO;
		this.subject = subject;
		this.content = content;
	}
	

	/**
	 * Construtor 2
	 * @param normalContactRecipients
	 * @param subject
	 * @param content
	 */
	public MailAdapter(Set<Contact> normalContactRecipients, 
			String subject, 
			String content) {
		super();
		this.normalContactRecipients = normalContactRecipients;
		this.subject = subject;
		this.content = content;
	}
	
	
	/**
	 * Construtor 3
	 * @param normalContactRecipients
	 * @param blindContactRecipients
	 * @param subject
	 * @param content
	 */
	public MailAdapter(Set<Contact> normalContactRecipients, 
			Set<Contact> blindContactRecipients,
			String subject, 
			String content) {
		super();
		this.normalContactRecipients = normalContactRecipients;
		this.blindContactRecipients = blindContactRecipients;
		this.subject = subject;
		this.content = content;
	}
	
	/**
	 * Construtor 4: o mais completo
	 * @param normalContactRecipients
	 * @param blindContactRecipients
	 * @param replyTo
	 * @param subject
	 * @param content
	 */
	public MailAdapter(Set<Contact> normalContactRecipients, 
			Set<Contact> blindContactRecipients,
			String replyTo,
			String subject, 
			String content) {
		super();
		this.normalContactRecipients = normalContactRecipients;
		this.blindContactRecipients = blindContactRecipients;
		this.replyTo = replyTo;
		this.subject = subject;
		this.content = content;
	}
	

	
//acessores...		
	public Set<Contact> getNormalContactRecipients() {
		return normalContactRecipients;
	}
	public void setNormalContactRecipients(Set<Contact> normalContactRecipients) {
		this.normalContactRecipients = normalContactRecipients;
	}
	public Set<Contact> getBlindContactRecipients() {
		return blindContactRecipients;
	}
	public void setBlindContactRecipients(Set<Contact> blindContactRecipients) {
		this.blindContactRecipients = blindContactRecipients;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


/* ****************************
 * Adaptador para ApacheCommons
 ******************************/
//	public HtmlEmail adapt(HtmlEmail email) throws EmailException {
//		//1.seta o assunto
//		email.setSubject( this.subject );
//		
//		//2.seta o conteudo
//		email.setHtmlMsg( content );
//		
//		//3.seta os destinatarios normais
//		for (Contact contact : this.normalContactRecipients) {
//			email.addTo( contact.getEmail(), contact.getShortDesc() );
//		}
//		
//		//4.seta os destinatarios ocultos
//		for (Contact contact : this.blindContactRecipients) {
//			email.addBcc( contact.getEmail(), contact.getShortDesc() );
//		}
//		
//		//5.replayTo
//		if (replyTo!=null) {
//			email.addReplyTo( replyTo );
//		}
//		
//		return email;
//	}
	

/* **************************
 * Adapatadores para JavaMail	
 ****************************/
	public InternetAddress[] getTOInternetAddressArray() throws UnsupportedEncodingException {
		return getInternetAddressArray( getNormalContactRecipients() );
	}

	public boolean isFillBlindRecipients() {
		return getBlindContactRecipients()!=null && !getBlindContactRecipients().isEmpty();
	}
	
	public InternetAddress[] getBCCInternetAddressArray() throws UnsupportedEncodingException {
		return getInternetAddressArray( getBlindContactRecipients() );
	}
	
	public boolean isFillReplyTo() {
		return getReplyTo()!=null && !getReplyTo().trim().isEmpty();
	}
	
	public InternetAddress[] getReplayToInternetAddressArray() throws AddressException {
		return new InternetAddress[] { new InternetAddress(getReplyTo()) };
	}
	

	//util
	private InternetAddress[] getInternetAddressArray(Set<Contact> contacts) throws UnsupportedEncodingException {
		if (contacts==null || contacts.isEmpty()) {//curto-circuito
			return null;
		}
		
		InternetAddress[] array = new InternetAddress[ contacts.size() ];
		int i=0;
		for (Contact contact : contacts ) {
			array[i++] = new InternetAddress(contact.getEmail(), contact.getCivilName() );
		}
		return array;
	}
	
	
	

}
