package com.cb.mundo.model.entity.enumeration;

public enum RegisterStatus {
	
	INCOMPLETE(       "REGISTER_STATUS_INCOMPLETE"       , "icon_register_status_incomplete_36.png"     , false, false)
	,
	REGISTERED(       "REGISTER_STATUS_REGISTERED"       , "icon_register_status_registered_36.png"     , false, true)
	,
//	CHECKEDIN_VIRTUAL("REGISTER_STATUS_CHECKEDIN_VIRTUAL", "icon_register_status_checkin_virtual_36.png", true,  false)
//	,
	CHECKEDIN(        "REGISTER_STATUS_CHECKEDIN"        , "icon_register_status_checkedin_36.png"      , true,  true)
	,
	CHECKEDOUT(       "REGISTER_STATUS_CHECKEDOUT"       , "icon_register_status_checkedout_36.png"     , true,  true)
	,
	MEGAEVENTOUT(     "REGISTER_STATUS_MEGAEVENTOUT"     , "icon_register_status_megaeventout_36.png"   , false, true)
    ,
    CANCELED(         "REGISTER_STATUS_CANCELED"         , "icon_register_status_canceled_36.png"       , false, false)
    ,
    TRANSFERED(       "REGISTER_STATUS_TRANSFERED"       , "icon_register_status_transfered_36.png"     , false, false)
    ,
    EXPIRED(          "REGISTER_STATUS_EXPIRED"          , "icon_register_status_expired_36.png"        , false, false)
	;
	
	/**
	 * key para internacionalizacao
	 */
	private final String key;
	
	/**
	 * nome da image (ou icone) que expressa o status
	 */
	private final String image;
	
	/**
	 * representa que está dentro do mega evento
	 */
	private final Boolean flagInsideMegaEvent;
	
	/**
	 * representa o fluxo basico da inscricao
	 */
	private final Boolean flagBasicFlow;
	
	
	private RegisterStatus(String key, String image, Boolean flagInsideMegaEvent, Boolean flagBasicFlow) {
		this.key = key;
		this.image = image;
		this.flagInsideMegaEvent = flagInsideMegaEvent;
		this.flagBasicFlow = flagBasicFlow;
	}

	
	//acessores...
	public String getKey() {
		return key;
	}

	public String getImage() {
		return image;
	}

	public Boolean getFlagInsideMegaEvent() {
		return flagInsideMegaEvent;
	}

	public Boolean getFlagBasicFlow() {
		return flagBasicFlow;
	}
}


