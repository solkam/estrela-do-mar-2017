package com.cb.mundo.model.exception;

public class UserWithoutAllowedCitiesException extends BusinessException {
	private static final long serialVersionUID = 1343435259141486671L;

	public UserWithoutAllowedCitiesException() {
		super("msg_user_without_allowed_cities", null);
	}

}
