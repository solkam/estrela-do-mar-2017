package com.cb.mundo.model.exception;


public class ContactCivilNameAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = 7294590787821071401L;
	

	public ContactCivilNameAlreadyExistException(String name) {
		super("msg_contact_civilname_already_exists", new Object[]{name} );
	}

}
