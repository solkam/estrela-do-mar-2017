package com.cb.mundo.model.entity.config;


/**
 * Asign a alignment to a item of configuration in a certificate.
 * 
 * @author solkam
 * @since 02 out 2011
 */
public enum CertificateItemAlign  {
	
	LEFT("|<", "Esquerda")
	,
	CENTER("|", "Centro")
	,
	RIGHT(">|", "Direita")
	;
	
	private final String label;
	private final String tooltip;
	
	private CertificateItemAlign(String label, String tooltip) {
		this.label = label;
		this.tooltip = tooltip;
	}

	public String getLabel() {
		return label;
	}

	public String getTooltip() {
		return tooltip;
	}
	
	

}
