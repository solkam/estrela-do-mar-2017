package com.cb.mundo.model.entity.enumeration;

/**
 * Maturidade de um contato segundo sua idade
 * 
 * @author Solkam
 * @since 17 JAN 2013
 */
public enum ContactMaturity {
	
	UNKNOWN("CONTACT_MATURITY_UNKNOWN")
	,
	CHILD("CONTACT_MATURITY_CHILD")
	,
	TEENAGER("CONTACT_MATURITY_TEENAGER")
	,
	YOUNG("CONTACT_MATURITY_YOUNG")
	,
	ADULT("CONTACT_MATURITY_ADULT")
	,
	ANCIENT("CONTACT_MATURITY_ANCIENT")
	;
	
	private final String key;

	private ContactMaturity(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
	public static ContactMaturity getByAge(int age) {
		if (age <= 0) {
			return ContactMaturity.UNKNOWN;
		}
		
		if (age <= 12) {
			return ContactMaturity.CHILD;
		}
		if (age <= 17) {
			return ContactMaturity.TEENAGER;
		}
		if (age <= 24) {
			return ContactMaturity.YOUNG;
		}
		if (age <= 60) {
			return ContactMaturity.ADULT;
		}
		return ContactMaturity.ANCIENT;
	}
	
	
	
	

}
