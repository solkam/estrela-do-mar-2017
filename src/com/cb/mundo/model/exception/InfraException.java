package com.cb.mundo.model.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class InfraException extends MundoCBException {
	private static final long serialVersionUID = 215029684657250486L;
	
	public InfraException(String key, Object[] params, Throwable cause) {
		super(key, params, cause);
	}

	public InfraException(Throwable cause) {
		super(cause);
	}
	

}
