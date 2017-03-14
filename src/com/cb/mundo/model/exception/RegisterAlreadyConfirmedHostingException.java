package com.cb.mundo.model.exception;

/**
 * Exception que disse que um register ja esta confirmado em quarto
 *   
 * @author Solkam
 * @since 24 ABR 2014
 */
public class RegisterAlreadyConfirmedHostingException extends BusinessException {
	private static final long serialVersionUID = 6700220722417179752L;

	public RegisterAlreadyConfirmedHostingException() {
		super("msg_register_already_confirmed_hosting", null);
	}
	
	

}
