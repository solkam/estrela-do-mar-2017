package com.cb.mundo.model.exception;

/**
 * Exception que representa que um contato NAO tem inscricao 
 * num dado MegaEvento
 * 
 * @author Solkam
 * @since 23 ABR 2014
 */
public class ContactHasNoRegisterException extends BusinessException {
	private static final long serialVersionUID = 2649909531444476515L;

	public ContactHasNoRegisterException() {
		super("msg_contact_has_no_register", null);
	}

}
