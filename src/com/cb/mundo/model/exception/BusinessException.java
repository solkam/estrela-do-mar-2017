package com.cb.mundo.model.exception;

import javax.ejb.ApplicationException;

/**
 * Exception de negocio generica
 * 
 * @author Solkam
 * @since 20 abr 2011
 */
@ApplicationException(rollback=false)
public class BusinessException extends MundoCBException {
	private static final long serialVersionUID = 1637535927570839025L;

	public BusinessException(String key, Object[] params, Throwable cause) {
		super(key, params, cause);
	}

	public BusinessException(String key, Object[] params) {
		super(key, params);
	}

	
	

}
