package com.cb.mundo.model.exception;

public class CountryCodeAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = 2313978604270382703L;

	public CountryCodeAlreadyExistException(String code) {
		super("msg_country_code_already_exist", new Object[] {code} );
	}

}
