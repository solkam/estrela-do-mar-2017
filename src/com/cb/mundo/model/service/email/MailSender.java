package com.cb.mundo.model.service.email;


/**
 * Contrato para Enviador de Emails
 * 
 * @author Solkam
 * @since 20 fev 2014
 */
public interface MailSender {

	public abstract String send(MailAdapter adapter);

}