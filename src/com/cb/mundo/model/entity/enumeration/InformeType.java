package com.cb.mundo.model.entity.enumeration;


/**
 * Tipos de informe de seminarios
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public enum InformeType {

	FINANCIAL("INFORME_TYPE_FINANCIAL", "icon_informe_rendicion.gif")
	,
	FEEDBACK("INFORME_TYPE_FEEDBACK", "icon_informe_feedback3.gif")
	;
	
	private final String key;
	private final String icon;
	
	private InformeType(String key, String icon) {
		this.key = key;
		this.icon = icon;
	}

	public String getKey() {
		return key;
	}

	public String getIcon() {
		return icon;
	}
	

}
