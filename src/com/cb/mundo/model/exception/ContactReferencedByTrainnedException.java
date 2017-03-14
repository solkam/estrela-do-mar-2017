package com.cb.mundo.model.exception;

public class ContactReferencedByTrainnedException extends BusinessException {
	private static final long serialVersionUID = 3196665364302284904L;

	public ContactReferencedByTrainnedException() {
		super("msg_contact_referencedby_trainned_contact", null);
	}

}
