package com.cb.mundo.model.exception;

/**
 * Representa a RN onde um EventWeek nao pode ser removido caso tenha RegisterDetails associados
 * 
 * @author Solkam
 * @since 11 out 2012
 */
public class EventWeekHasRegisterDetailException extends BusinessException {
	private static final long serialVersionUID = -6957400728233864447L;

	public EventWeekHasRegisterDetailException() {
		super("msg_error_eventweek_has_register_details", null);
	}

}
