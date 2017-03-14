package com.cb.mundo.model.entity.enumeration;

/**
 * Enum que represente nivel de maturidade de um facilitator.
 * Tambem determina os percentuais do valor de facilitator:
 * 1) Condor e Aguila = 60% e 40%
 * 2) Condor e Puma = 70% e 30%
 * 3) Condor e Lobo = 80% e 20%
 * 4) Aguila e Puma = 60% e 40%
 * 5) Aguila e Lobo = 70% e 30%
 * 7) Puma e Lobo = 60% e 40%
 * 8) mesmo nivel = 50% e 50%
 * 
 * @author Solkam
 * @since 03 MAR 2013
 */
public enum FacilitatorCategory {
	
	LOBO("FACILITATOR_CATEGORY_LOBO")
	,
	PUMA("FACILITATOR_CATEGORY_PUMA")
	,
	AGUILA("FACILITATOR_CATEGORY_AGUILA")
	,
	CONDOR("FACILITATOR_CATEGORY_CONDOR")
	;
	
	private final String key;
	
	private FacilitatorCategory(String key) {
		this.key = key;
	}

	
	public String getKey() {
		return key;
	}
	
	

}
