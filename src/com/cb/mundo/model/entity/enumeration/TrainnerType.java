package com.cb.mundo.model.entity.enumeration;

/**
 * Representa o tipo de treinador conforme nova regra em 2013
 * @author Solkam
 */
public enum TrainnerType {
	
	TIERRA("TRAINNER_TYPE_TIERRA")
	,
	AGUA("TRAINNER_TYPE_AGUA")
	,
	AIRE("TRAINNER_TYPE_AIRE")
	,
	FUEGO("TRAINNER_TYPE_FUEGO")
	;
	
	private final String key;
	
	private TrainnerType(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	
	
	
	

}
