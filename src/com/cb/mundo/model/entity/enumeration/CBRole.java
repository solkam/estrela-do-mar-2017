package com.cb.mundo.model.entity.enumeration;

/**
 * Representa um Papel permanente dentro de CB.
 * Indepentende do que estiver fazendo um MegaEvento,
 * o que vale eh este Role.
 * @author Solkam
 * @since 02 ABR 2015
 */
public enum CBRole {

	DIRECTORIO("Directório")
	,
	DIRECTOR("Director")
	,
	LAMA("Lama")
	,
	YAIKIN("Yaikin")
	,
	MENTOR("Mentor")
	,
	COLABORADOR("Colaborador")//ou administracao
	,
	COLABORADOR_CB("Colaborador CB")//ou contratados
	,
	INSTRUTOR("Instructor")
	;
	
	private final String description;
	
	private CBRole(String d) {
		this.description = d;
	}

	public String getDescription() {
		return description;
	}
	
}
