package com.cb.mundo.model.exception;

/**
 * Representa RN onde um Event nao pode ser removido caso possua RegisterDetails associados
 * 
 * @author Solkam
 * @since 11 out 2012
 */
public class EventHasRegisterDetailException extends BusinessException {
	private static final long serialVersionUID = -166778305405186142L;

	public EventHasRegisterDetailException() {
		super("msg_error_event_has_register_details", null);
	}
}
