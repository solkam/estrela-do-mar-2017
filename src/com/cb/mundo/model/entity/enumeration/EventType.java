package com.cb.mundo.model.entity.enumeration;

/**
 * Tipo de evento, sendo seminario de formacao, complementar ou convidado especial
 * 
 * @author Solkam
 * @since 06 nov 2012
 */
public enum EventType {
	
	FORMATION("EVENT_TYPE_FORMATION") 
	,
	STAFF("EVENT_TYPE_STAFF") 
	,
	INVITED("EVENT_TYPE_INVITED") 
	,
	COMPLEMENTAR("EVENT_TYPE_COMPLEMENTAR")	
	,
	DEPENDENT_BABY("EVENT_TYPE_DEPENDENT_BABY") 
	,
	DEPENDENT_CHILD("EVENT_TYPE_DEPENDENT_CHILD") 
	,
	DEPENDENT_TEENAGER("EVENT_TYPE_DEPENDENT_TEENAGER") 
	,
	DEPENDENT_PARTNER("EVENT_TYPE_DEPENDENT_PARTNER") 
	,
	PLAN("EVENT_TYPE_PLAN") 
	,
	CONSULT("EVENT_TYPE_CONSULT") 
	;
	
	
	private final String key;
	
	private EventType(String key) {
		this.key = key;
	}

	
	public String getKey() {
		return key;
	}
	
	
	

}
