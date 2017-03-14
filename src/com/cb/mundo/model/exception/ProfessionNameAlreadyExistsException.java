package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Profession;

/**
 * Exception que diz que nome de profissao ja foi cadastrado
 * 
 * @author Solkam
 * @since 31 JUL 2013
 */
public class ProfessionNameAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = 2430110180730330006L;

	public ProfessionNameAlreadyExistsException(Profession profession) {
		super("msg_profession_name_already_exists", new Object[] {profession.getName()} );
	}

}
