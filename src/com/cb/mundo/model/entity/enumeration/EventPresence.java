package com.cb.mundo.model.entity.enumeration;

import java.math.BigDecimal;

/**
 * Tipo de presenca em um evento
 * 
 * @author Solkam
 * @since 25 abr 2012
 */
public enum EventPresence {
	
	PARTICIPANT("EVENT_PRESENCE_PARTICIPANT"          , new BigDecimal("3000.00") ,"P"),
	STAFF(      "EVENT_PRESENCE_STAFF"                , new BigDecimal("990.00")  ,"S"),
	CONTRATED(  "EVENT_PRESENCE_CONTRATED"            , new BigDecimal("0.00")    ,"C"),
	DEPENDENT(  "EVENT_PRESENCE_DEPENDENT"            , new BigDecimal("990.00")  ,"D"),
	;
	
	private final String key;
	private final BigDecimal defaulValue;
	private final String initial;
	
	private EventPresence(String key, BigDecimal defaulValue, String initial) {
		this.key = key;
		this.defaulValue = defaulValue;
		this.initial = initial;
	}
	
	public String getKey() {
		return this.key;
	}

	public BigDecimal getDefaulValue() {
		return defaulValue;
	}

	public String getInitial() {
		return initial;
	}
	
}
