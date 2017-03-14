package com.cb.mundo.model.entity.config;

import java.awt.Font;

/**
 * Estilo de fonte suportado para impressao de 
 * certificados
 * 
 * @author Solkam
 * @since 14 nov 2011
 */
public enum CertificateItemFontStyle {
	
	BOLD(Font.BOLD, "Negrito")
	,
	PLAIN(Font.PLAIN, "Normal")
	,
	ITALIC(Font.ITALIC, "Italico")
	;
	
	private final int code;
	private final String description;
	
	
	private CertificateItemFontStyle(int code, String desc) {
		this.code = code;
		this.description = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
}
