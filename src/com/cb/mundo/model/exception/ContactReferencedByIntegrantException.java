package com.cb.mundo.model.exception;

public class ContactReferencedByIntegrantException extends BusinessException {
	private static final long serialVersionUID = 5732834675927170373L;

	public ContactReferencedByIntegrantException() {
		super("msg_contact_referencedby_integrant", null);
	}

}
