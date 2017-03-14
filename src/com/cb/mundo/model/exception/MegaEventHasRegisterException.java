package com.cb.mundo.model.exception;

/**
 * Representa a RN onde nao se pode remover um MegaEvento quando este
 * possui Inscricoes associadas
 * @author Solkam
 * @since 11 out 2012
 */
public class MegaEventHasRegisterException extends BusinessException {
	private static final long serialVersionUID = -6553534447998123849L;

	public MegaEventHasRegisterException() {
		super("msg_error_megaevent_has_registers", null);
	}

}
