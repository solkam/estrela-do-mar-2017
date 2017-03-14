package com.cb.mundo.model.exception;

public class EmailWithoutRecipientsException extends BusinessException {

	private static final long serialVersionUID = -8599410589829240872L;

	public EmailWithoutRecipientsException() {
		super("msg_email_without_recipients", null);
	}
	
	

}
