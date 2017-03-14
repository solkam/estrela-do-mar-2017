package com.cb.mundo.model.exception;

public class ContactReferencedByUserException extends BusinessException {
	private static final long serialVersionUID = -9039983951915039363L;

	public ContactReferencedByUserException() {
		super("msg_contact_referencedby_user", null);
	}
	

}
