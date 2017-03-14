package com.cb.mundo.model.exception;

public class ContactReferencedByStaffException extends BusinessException {
	private static final long serialVersionUID = 2188470866737267861L;

	public ContactReferencedByStaffException() {
		super("msg_contact_referencedby_staff", null);
	}

}
