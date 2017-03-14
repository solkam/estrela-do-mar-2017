package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.City;

/**
 * Representa que uma cidade eh referenciado por producoes e nao pode ser
 * removida
 * 
 * @author vitor
 * @since 04 MAR 2013
 */
public class CityReferencedByProductionException extends BusinessException {
	private static final long serialVersionUID = 5075474216300031408L;

	public CityReferencedByProductionException(City city) {
		super("msg_city_referencedby_production", new Object[]{ city.getName() });
	}

}
