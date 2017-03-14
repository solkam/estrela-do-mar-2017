package com.cb.mundo.model.entity.config;


/**
 * Atributo para certificados
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public enum CertificateItemAttribute {
	
	PARTICIPANT_NAME(       "Nome Participante"        , 1)
	,
	DATE(                   "Data"                     , 2)
	,
	LOCAL(                  "Local"                    , 3)
	,
	NUMBER(                 "Numero"                   , 4)
	,
	
	FACILITATOR_1_SIGNATURE("Facilitador 1: Assinatura", 5)
	,
	FACILITATOR_1_LINE(     "Facilitador 1: Linha"     , 6)
	,
	FACILITATOR_1_NAME(     "Facilitador 1: Nome"      , 7)
	,
	FACILITATOR_1_LABEL(    "Facilitador 1: Rotulo"    , 8)
	,

	FACILITATOR_2_SIGNATURE("Facilitador 2: Assinatura", 9)
	,
	FACILITATOR_2_LINE(     "Facilitador 2: Linha"     , 10)
	,
	FACILITATOR_2_NAME(     "Facilitador 2: Nome"      , 11)
	,
	FACILITATOR_2_LABEL(    "Facilitador 2: Rotulo"    , 12)
	,
	
	FACILITATOR_3_NAME(     "Facilitador 3: Nome"      , 13)
	,
	FACILITATOR_3_LINE(     "Facilitador 3: Linha"     , 14)
	,
	FACILITATOR_3_LABEL(    "Facilitador 3: Rotulo"    , 15)
	,
	FACILITATOR_3_SIGNATURE("Facilitador 3: Assinatura", 16)
	,
	
	FACILITATOR_4_NAME(     "Facilitador 4: Nome"      , 17)
	,
	FACILITATOR_4_LINE(     "Facilitador 4: Linha"     , 18)
	,
	FACILITATOR_4_SIGNATURE("Facilitador 4: Assinatura", 19)
	,
	FACILITATOR_4_LABEL(    "Facilitador 4: Rotulo"    , 20)
	;
	
	private final String description;
	private final Integer displayOrder;
	
	private CertificateItemAttribute(String desc, Integer displayOrder) {
		this.description = desc;
		this.displayOrder = displayOrder;
	}

	public String getDescription() {
		return description;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	
	


}
