package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Register;

/**
 * Exception para evitar que se dupliquem inscricoes
 * @author Solkam
 *
 */
public class RegisterAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = -3279416259977323951L;

	public RegisterAlreadyExistException(Register register) {
		super("msg_register_already_exist", null);
	}

}
