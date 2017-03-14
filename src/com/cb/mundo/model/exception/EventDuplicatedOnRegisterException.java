package com.cb.mundo.model.exception;

/**
 * Exception que representa que o mesmo evento tem varios detalhes 
 * na mesma inscricao.
 * 
 * @author Solkam
 * @since 13 ABR 2014
 */
public class EventDuplicatedOnRegisterException extends BusinessException {

	private static final long serialVersionUID = 1211208732314784501L;

	public EventDuplicatedOnRegisterException() {
		super("msg_event_duplicated_on_register", null);
	}
	

}
