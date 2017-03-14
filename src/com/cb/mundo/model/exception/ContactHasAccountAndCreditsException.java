package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Contact;


/**
 * Exception para evitar que um contato com creditos seja removido
 * @author Solkam
 * @since 24 DEZ 2013
 */
public class ContactHasAccountAndCreditsException extends BusinessException {
	private static final long serialVersionUID = 5224569885061260342L;

	public ContactHasAccountAndCreditsException(Contact c) {
		super("msg_contact_has_accoun_and_credits", null);
	}

}
