package com.cb.mundo.model.entity.enumeration;

/**
 * Enum para os Status de uma producao
 * 
 * @author Solkam
 * @since 16 NOV 2014
 */
public enum ProductionStatus {
	
	PLANNED("PRODUCTION_STATUS_PLANNED", "icon_production_status_planned_48.png", false)
	,
	ACTIVE("PRODUCTION_STATUS_ACTIVE", "icon_production_status_active_48.png", false)
	,
	CONCLUDED("PRODUCTION_STATUS_CONCLUDED", "icon_production_status_concluded_48.png", true)
	,
	POSTERGATED("PRODUCTION_STATUS_POSTERGATED", "icon_production_status_postergated_48.png", true)
	,
	CANCELED("PRODUCTION_STATUS_CANCELED", "icon_production_status_canceled_48.png", true)
	;
	
	private final String key;
	private final String image;
	
	
	/**
	 * Significa ou concluida, cancelada ou postergada
	 */
	private final Boolean flagClosed;

	
	private ProductionStatus(String k, String img, Boolean flagClosed) {
		this.key = k;
		this.image = img;
		this.flagClosed = flagClosed;
	}


	
	
	public String getKey() {
		return key;
	}

	public String getImage() {
		return image;
	}

	public Boolean getFlagClosed() {
		return flagClosed;
	}
	
	
}
