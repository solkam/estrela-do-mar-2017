package com.cb.mundo.model.exception;

public class RegisterHasRegisterDetailsException extends BusinessException {
	private static final long serialVersionUID = -4674064423044466552L;

	public RegisterHasRegisterDetailsException() {
		super("msg_error_register_has_registerdetail", null);
	}

	
	
}
