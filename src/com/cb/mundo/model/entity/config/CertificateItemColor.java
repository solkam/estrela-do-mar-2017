package com.cb.mundo.model.entity.config;

import java.awt.Color;

/**
 * Cores suportados para imprimir certificados
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public enum CertificateItemColor {
	
	BLACK(Color.BLACK, "Preto")
	,
	GRAY(Color.GRAY, "Cinza")
	;
	
	private final Color awtColor;
	private final String description;
	
	private CertificateItemColor(Color awtColor, String desc) {
		this.awtColor = awtColor;
		this.description = desc;
	}
	
	public Color getAwtColor() {
		return awtColor;
	}

	public String getDescription() {
		return description;
	}


}
