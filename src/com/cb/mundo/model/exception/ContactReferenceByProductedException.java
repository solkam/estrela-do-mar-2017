package com.cb.mundo.model.exception;

public class ContactReferenceByProductedException extends BusinessException {
	private static final long serialVersionUID = -3538927058216058723L;

	public ContactReferenceByProductedException() {
		super("msg_contact_referencedby_producted_contact", null);
	}

}
