package com.cb.mundo.model.exception;

public class PasswordNotConfirmedException extends BusinessException {
	private static final long serialVersionUID = -5023188422850926916L;

	public PasswordNotConfirmedException() {
		super("msg_user_password_confirmation_failed", null);
	}

}
