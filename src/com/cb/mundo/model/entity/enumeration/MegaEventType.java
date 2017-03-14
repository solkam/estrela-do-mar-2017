package com.cb.mundo.model.entity.enumeration;

/**
 * Tipo de Mega Evento.
 * Estah pendente a imagem para Expeditions
 * 
 * @author Solkam
 * @since 25 abr 2012
 */
public enum MegaEventType  {
	
	CAMPAMENTO("MEGA_EVENT_TYPE_CAMPAMENTO", "logo_megaeventtype_montain.png")	,
	ESTRELA("MEGA_EVENT_TYPE_ESTRELA", "logo_megaeventtype_seastar.png")	,
//	FELINO("MEGA_EVENT_TYPE_FELINO", "logo_megaeventtype_felinoplata.png")	,
//	EXPEDITION("MEGA_EVENT_TYPE_EXPEDITION", "logo_megaeventtype_expedition.png") ,
	;
	
	private final String key;
	private final String image;
	
	private MegaEventType(String key, String image) {
		this.key = key;
		this.image = image;
	}


	public String getKey() {
		return key;
	}

	public String getImage() {
		return image;
	}
}
