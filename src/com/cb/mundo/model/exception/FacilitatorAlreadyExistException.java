package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa que, ao salvar, um facilitator ja existe com mesmo contato e escola
 * 
 * @author Solkam
 * @since 02 MAR 2013
 */
public class FacilitatorAlreadyExistException extends BusinessException {
	private static final long serialVersionUID = -4198214436413822394L;

	public FacilitatorAlreadyExistException(School school, Contact contact) {
		super("msg_facilitator_already_exists", new Object[]{school, contact.getName()} );
	}

}
