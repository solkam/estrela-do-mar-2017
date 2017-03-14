package com.cb.mundo.model.entity.enumeration;

/**
 * Categoria de Receita para producao
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public enum IncomingCategory {
	
	P("INCOMING_CATEGORY_PARTICIPANT")
	,
	S("INCOMING_CATEGORY_STAFF")
	;
	
	private String key;
	
	private IncomingCategory(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

}
