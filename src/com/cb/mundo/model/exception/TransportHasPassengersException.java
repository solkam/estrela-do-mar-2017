package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Transport;

public class TransportHasPassengersException extends BusinessException {
	private static final long serialVersionUID = 2281267503368186921L;

	public TransportHasPassengersException(Transport transport) {
		super("msg_transport_has_passengers", null);
	}

}
