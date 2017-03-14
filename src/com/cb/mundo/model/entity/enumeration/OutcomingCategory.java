package com.cb.mundo.model.entity.enumeration;

/**
 * Categoria de despesas de seminarios
 * @author Solkam
 * @since 25 set 2011
 */
public enum OutcomingCategory {
	
	ROOM("OUTCOME_CATEGORY_ROOM")
	,
	HOST("OUTCOME_CATEGORY_HOST")
	,
	COFBR("OUTCOME_CATEGORY_COFFEE_BREAK")
	,
	MATER("OUTCOME_CATEGORY_MATERIAL")
	,
	FOOD("OUTCOME_CATEGORY_FOOD")
	,
	TRAVEL("OUTCOME_CATEGORY_TRAVEL")
	,
	OTHER("OUTCOME_CATEGORY_OTHER")
	;
	
	private String key;
	
	private OutcomingCategory(String key) {
		this.key = key;
	}
	
	
	public String getKey() {
		return key;
	}

}
