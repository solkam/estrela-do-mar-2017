package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Contact;

public class ContactAlreadyAddedToSeminarComplementarException extends BusinessException {
	private static final long serialVersionUID = 1505140131137677482L;

	public ContactAlreadyAddedToSeminarComplementarException(Contact contact) {
		super("msg_contact_already_added_to_seminar_completar", null);
	}

}
