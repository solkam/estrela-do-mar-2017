package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.City;

/**
 * Representa que existem usuarios que referencia a cidade a ser removida
 *  
 * @author vitor
 * @since 04 MAR 2013
 */
public class CityReferencedByUserException extends BusinessException {
	private static final long serialVersionUID = 5918631941427908647L;

	public CityReferencedByUserException(City city) {
		super("msg_city_referencedby_user", new Object[]{ city.getName() });
	}

}
