package com.cb.mundo.model.entity.enumeration;

/**
 * Papel de alguem em Mega Evento
 * @author Solkam
 * @since 30 MAR 2015
 */
public enum MegaEventRole {
	
	DIRECTORIO(        "Directorio"        , true,  1),
	LAMA(              "Lama"              , true,  2),
	YAIKIN(            "Yaikin"            , true,  3),
	MENTOR(            "Mentor"            , true,  4),
	DIRECTOR(          "Director"          , true,  5),
	INSTRUCTOR(        "Instructor"        , true,  6),
	INVITED(           "Invitado"          , false, 6),
	LIDER_AREA(        "Lider de Area"     , false, 7),
	COLABORADOR_CB(    "Colaborador CB"    , false, 8),
	COLABORADOR(       "Coloborador"       , true,  9),
	ECO_ALDEA(         "Eco Aldea"         , false, 10),
	PARTICIPANT(       "Participante"      , false, 11),
	STAFF_SEMINAR(     "Staff de Seminario", false, 13),
	STAFF_SERVICE(     "Staff de Servico"  , false, 14),
	DEPENDENT_BABY(    "Bebes"             , false, 15),
	DEPENDENT_CHILD(   "Niños"             , false, 16),
	DEPENDENT_TEENAGER("Adolescentes"      , false, 17),
	DEPENDENT_PARTNER( "Acompanhante"      , false, 18);
	
	
	private final String description;
	private final Boolean flagCBRole;
	private final Integer priority;
	
	private MegaEventRole(String d, Boolean flagCBRole, Integer p) {
		this.description = d;
		this.flagCBRole = flagCBRole;
		this.priority = p;
	}
	
	public String getDescription() {
		return description;
	}

	public Boolean getFlagCBRole() {
		return flagCBRole;
	}

	public boolean isMorePriorityThan(MegaEventRole thatRole) {
		return this.priority <= thatRole.priority;
	}
	

}
