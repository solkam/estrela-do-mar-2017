package com.cb.mundo.model.entity.enumeration;

/**
 * Status dos informes de seminarios
 * @author Solkam
 * @since 25 set 2011
 */
public enum InformeStatus {
	
	PENDENT("INFORME_STATUS_PENDENT","icon_informe_status_pendent.png", "Pendente")
	,
	SAVED("INFORME_STATUS_SAVED","icon_informe_status_saved.png", "Salvo")
//	,
//	OK("INFORME_STATUS_OK","icon_informe_status_ok.gif")//qdo este status voltar, descomentar nas classes Informe___Bean
	;
	
	private final String key;
	private final String image;
	private final String tip;

	private InformeStatus(String key, String image, String tip) {
		this.key = key;
		this.image = image;
		this.tip = tip;
	}


	public String getKey() {
		return this.key;
	}


	public String getImage() {
		return image;
	}


	public String getTip() {
		return tip;
	}
	
	
	

}
