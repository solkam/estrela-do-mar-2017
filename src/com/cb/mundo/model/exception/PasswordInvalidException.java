package com.cb.mundo.model.exception;

/**
 * Indica que uma senha nao bate com a senha atual do usuario autenticado
 * @author Solkam
 * @since 12 nov 12
 */
public class PasswordInvalidException extends BusinessException {
	private static final long serialVersionUID = -2299103623583677748L;

	public PasswordInvalidException() {
		super("msg_current_password_invalid", null);
	}

}
