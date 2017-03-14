package com.cb.mundo.model.exception;

public class CreditProratingNotAllowedForNoPendentValueException extends BusinessException implements SeverityWarningExceptionType {
	private static final long serialVersionUID = 2891083507496249135L;

	public CreditProratingNotAllowedForNoPendentValueException() {
		super("msg_credit_prorating_not_allowed_for_no_pendent_value", null);
	}
	
	

}
