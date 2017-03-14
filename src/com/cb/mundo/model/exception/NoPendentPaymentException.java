package com.cb.mundo.model.exception;

public class NoPendentPaymentException extends BusinessException {
	private static final long serialVersionUID = -9059760455683172755L;

	public NoPendentPaymentException() {
		super("msg_no_pendent_payment", null);
	}

}
