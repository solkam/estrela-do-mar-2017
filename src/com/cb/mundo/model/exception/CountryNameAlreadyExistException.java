package com.cb.mundo.model.exception;

public class CountryNameAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = -170421131758739423L;

	public CountryNameAlreadyExistException(String countryName) {
		super("msg_country_name_alread_exist", new Object[]{countryName} );
	}

}
