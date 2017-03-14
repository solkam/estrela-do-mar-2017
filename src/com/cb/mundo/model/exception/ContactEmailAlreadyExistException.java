package com.cb.mundo.model.exception;

public class ContactEmailAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = -9159953109984631136L;

	public ContactEmailAlreadyExistException(String email) {
		super("msg_contact_email_already_exists", new Object[]{email});
	}

}
