package com.cb.mundo.model.exception;

public class UserWithoutAllowedSchoolsException extends BusinessException {
	private static final long serialVersionUID = 248492727167933312L;

	public UserWithoutAllowedSchoolsException() {
		super("msg_user_without_allowed_schools", null);
	}

	

}
