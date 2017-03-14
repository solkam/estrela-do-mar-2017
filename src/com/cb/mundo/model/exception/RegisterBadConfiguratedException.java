package com.cb.mundo.model.exception;


/**
 * Representa que ha algo errada na inscricao de um contato.
 * Por ex: muito registers para o mesmo contato para o mesmo MegaEvent
 * 
 * @author Solkam
 * @since 13 ABR 2014
 */
public class RegisterBadConfiguratedException extends BusinessException {

	private static final long serialVersionUID = 7488101570835040031L;

	public RegisterBadConfiguratedException() {
		super("msg_register_bad_configurated", null);
	}
	
	

}
