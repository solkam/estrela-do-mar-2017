package com.cb.mundo.model.exception;

public class ContactReferencedByOutcomingException extends BusinessException {
	private static final long serialVersionUID = 7571128524578385956L;

	public ContactReferencedByOutcomingException() {
		super("msg_contact_referencedby_outcoming", null);
	}

}
