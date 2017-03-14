package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Register;

/**
 * Exception que nao permite que um register seja removido se ele
 * tiver transportes associados.
 * 
 * @author Solkam
 * @since 16 OUT 2013
 */
public class RegisterHasTransportException extends BusinessException {
	private static final long serialVersionUID = 1735855856867623873L;

	public RegisterHasTransportException(Register register) {
		super("msg_register_has_transport", null);
	}
	
}
