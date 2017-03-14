package com.cb.mundo.model.exception;

public class UserNotAuthenticatedException extends BusinessException {
	private static final long serialVersionUID = 1394794621632928727L;

	public UserNotAuthenticatedException() {
		super("msg_user_not_authenticated", null);
	}
	
	

}
