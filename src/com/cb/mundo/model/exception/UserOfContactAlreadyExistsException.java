package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Contact;

/**
 * Exception que nao permite que um novo usuario seja criado se um contato ja tem usuario
 * 
 * @author Solkam
 * @since 29 ABR 2014
 */
public class UserOfContactAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = 438162544150440985L;

	public UserOfContactAlreadyExistsException(Contact contact) {
		super("msg_user_of_contact_already_exists", new Object[] { contact.getMoreFullDesc() });
	}
	
	

}
