package com.cb.mundo.model.exception;

/**
 * Represent RN onde nome de Mega Evento deve ser unico
 * 
 * @author Solkam
 * @since 11 out 2012
 */
public class MegaEventNameAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = -6175749278074991401L;

	public MegaEventNameAlreadyExistsException() {
		super("msg_error_megaevent_name_already_exists", null);
	}

}
