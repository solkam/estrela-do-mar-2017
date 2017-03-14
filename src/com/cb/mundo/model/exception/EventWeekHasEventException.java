package com.cb.mundo.model.exception;

/**
 * 
 * Representa a RN onde um EventWeek nao pode ser removido caso ele tenha Events
 * 
 * @author Solkam
 * @since 11 out 2012
 */
public class EventWeekHasEventException extends BusinessException {
	private static final long serialVersionUID = -1607762407006219174L;

	public EventWeekHasEventException() {
		super("msg_error_eventweek_has_events", null);
	}
	
	

}
