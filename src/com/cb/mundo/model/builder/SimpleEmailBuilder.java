package com.cb.mundo.model.builder;

import com.cb.mundo.model.entity.Contact;

/**
 * Construtor de emails simples
 * 
 * @author Solkam
 * @since 29 ABR 2014
 */
public interface SimpleEmailBuilder {
	
	Contact getContactRecipient();

	String getReplyTo();

	String getSubject();

	String getContent();

}
