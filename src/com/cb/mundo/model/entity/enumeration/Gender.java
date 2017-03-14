package com.cb.mundo.model.entity.enumeration;

/**
 * Representa o sexo de um contato
 * 
 * @author vitor
 * @since 04 MAR 2013
 */
public enum Gender  {
	
	F("GENDER_FEMALE") ,
	M("GENDER_MALE") ;
	
	
	private final String key;
	
	private Gender(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
	

}
