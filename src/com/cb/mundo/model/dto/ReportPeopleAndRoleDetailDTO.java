package com.cb.mundo.model.dto;

import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.enumeration.MegaEventRole;

/**
 * DTO para o detalhe do relatorio de pessoas e papeis.
 * Relaciona um register a um paple de mega evento
 * @author Solkam
 * @since 05 ABR 2015
 */
public class ReportPeopleAndRoleDetailDTO {
	
	private final Register register;
	
	private final MegaEventRole role;

	
	public ReportPeopleAndRoleDetailDTO(Register register, MegaEventRole role) {
		this.register = register;
		this.role = role;
	}

	
	
	//acessores...
	public Register getRegister() {
		return register;
	}

	public MegaEventRole getRole() {
		return role;
	}
	
}
