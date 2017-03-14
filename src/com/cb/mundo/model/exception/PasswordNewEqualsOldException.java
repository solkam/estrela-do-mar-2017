package com.cb.mundo.model.exception;

/**
 * Nova senha eh a mesma que a antiga
 * @author Solkam
 * @since 12 nov 12
 */
public class PasswordNewEqualsOldException extends BusinessException {
	private static final long serialVersionUID = -7071949034474031283L;

	public PasswordNewEqualsOldException() {
		super("msg_new_password_equals_old", null);
	}

}
