package com.cb.mundo.model.exception;

public class ContactReferencedByParticipantException extends BusinessException {
	private static final long serialVersionUID = 4589088973785441751L;

	public ContactReferencedByParticipantException() {
		super("msg_contact_referencedby_participant", null);
	}

}
