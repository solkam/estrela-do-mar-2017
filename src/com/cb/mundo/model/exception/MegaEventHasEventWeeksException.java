package com.cb.mundo.model.exception;

/**
 * Representa a RN onde nao se pode remover um Mega Evento quando este
 * tem Event Week associados
 * 
 * @author Solkam
 * @since 11 out 2012
 */
public class MegaEventHasEventWeeksException extends BusinessException {
	private static final long serialVersionUID = 5695364157412819459L;

	public MegaEventHasEventWeeksException() {
		super("msg_error_megaevent_has_eventweeks", null);
	}

}
