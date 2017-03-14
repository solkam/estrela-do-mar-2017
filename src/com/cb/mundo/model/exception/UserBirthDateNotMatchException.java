package com.cb.mundo.model.exception;

import java.util.Date;

/**
 * Exception para reset de senha quando a data de nascimento nao confere.
 * 
 * @author Solkam
 * @since 23 DEZ 2013
 */
public class UserBirthDateNotMatchException extends BusinessException {
	private static final long serialVersionUID = -8885690375268726062L;

	public UserBirthDateNotMatchException(Date birthDate) {
		super("msg_user_birthdate_not_match", null);
	}

}
