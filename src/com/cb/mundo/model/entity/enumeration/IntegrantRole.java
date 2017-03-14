package com.cb.mundo.model.entity.enumeration;

/**
 * Papeis que um integrante (membro da equipe de producao)
 * pode desempenhar.
 *  
 * @author Solkam
 * @since 25 set 2011
 */
public enum IntegrantRole {
	
	LEAD("PRODUCTION_TEAM_ROLE_LEADER_PRODUCTOR", "Líder Produtor")
	,
	PROD("PRODUCTION_TEAM_ROLE_PRODUCTOR", "Produtor")
	,
	COPR("PRODUCTION_TEAM_ROLE_COPRODUCTOR", "Co-produtor")
	,
	APPR("PRODUCTION_TEAM_ROLE_APPRENTICE", "Aprendiz")
	;
	
	private final String key;
	private final String description;

	private IntegrantRole(String k, String d) {
		this.key = k;
		this.description = d;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}
	
}
