package com.cb.mundo.model.exception;

public class UsernameAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = 2514789117621008772L;

	public UsernameAlreadyExistException(String username) {
		super("msg_username_already_exist", new Object[] {username});
	}

}
