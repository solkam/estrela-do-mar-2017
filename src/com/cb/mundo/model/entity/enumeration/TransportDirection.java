package com.cb.mundo.model.entity.enumeration;

/**
 * Enum que representa a direcao do transporte:
 * -entrada: transporte de ingresso ao mega evento
 * -saida: transporte de saida do mega evento
 * 
 * @author Solkam
 * @since 12 OUT 2013
 */
public enum TransportDirection {
	
	INPUT("TRANSPORT_DIRECTION_INPUT", new RegisterStatus[] {
			RegisterStatus.INCOMPLETE, 
			RegisterStatus.REGISTERED, 
			RegisterStatus.CHECKEDIN})
	,
	OUTPUT("TRANSPORT_DIRECTION_OUTPUT", new RegisterStatus[] {
			RegisterStatus.CHECKEDIN, 
			RegisterStatus.CHECKEDOUT})
	;
	
	private final String key;
	
	private final RegisterStatus[] statusArray;
	
	
	private TransportDirection(String key, RegisterStatus[] statusArray) {
		this.key = key;
		this.statusArray = statusArray;
	}

	public String getKey() {
		return key;
	}

	public RegisterStatus[] getStatusArray() {
		return statusArray;
	}
	
	
	
}
