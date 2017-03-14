package com.cb.mundo.model.exception;

public class ContactReferencedByFacilitatorException extends BusinessException {
	private static final long serialVersionUID = 5400234445603447117L;

	public ContactReferencedByFacilitatorException() {
		super("msg_contact_referencedby_facilitator", null);
	}

}
