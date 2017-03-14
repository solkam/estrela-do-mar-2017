package com.cb.mundo.model.exception;

public class CityNameAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = -8284717987974469716L;

	public CityNameAlreadyExistException(String cityName) {
		super("msg_city_name_already_exist", new Object[] {cityName} );
	}
	

}
