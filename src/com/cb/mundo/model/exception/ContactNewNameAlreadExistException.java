package com.cb.mundo.model.exception;

public class ContactNewNameAlreadExistException extends BusinessException {
	private static final long serialVersionUID = -5958049979604996412L;

	public ContactNewNameAlreadExistException() {
		super("msg_contact_newname_already_exists", null);
	}

}
