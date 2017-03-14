package com.cb.mundo.model.exception;

public class UserEmailNotFoundException extends BusinessException {
	private static final long serialVersionUID = 8501044521623176077L;

	public UserEmailNotFoundException(String email) {
		super("msg_user_email_not_found", new Object[]{email} );
	}

}
