package com.cb.mundo.model.exception;

/**
 * Representa que um contato nao pode ser removido pois ha um register
 * referenciando-o
 * 
 * @author Solkam
 * @since 04 MAR 2013
 */
public class ContactReferencedByRegisterException extends BusinessException {
	private static final long serialVersionUID = 5304111172097612100L;

	public ContactReferencedByRegisterException() {
		super("msg_contact_referencedby_register", null);
	}

}
