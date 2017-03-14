package com.cb.mundo.model.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration for a Profile of access
 * 
 * @author solkam
 * @since 17 ago 2011
 */
public enum Profile {
												  //SEM  //INS  //SUPER
	TEC("PROFILE_TECNOLOGY"                  , 1, false, false, true)
	,
	FUN("PROFILE_FUNDATION"                  , 1, false, false, true)
	,
	ADM("PROFILE_ADMINISTRATION"             , 2, false, true, true)
	,
	DIR("PROFILE_DIRECTOR"                   , 3, false, false, true)
	,
	MAR("PROFILE_MARKETING"                  , 5, false, false, false)
	,
	LMU("PROFILE_LEADER_MUNDIAL"             , 5, true, true, true)
	,
	PRO("PROFILE_PRODUCTOR_ELITE"            , 6, true, true, false)
	,
	PLI("PROFILE_PRODUCTOR_LIGHT"            , 7, true, true, false)
	,
	STT("PROFILE_STAFF_TRANSPORT"            , 8, false, false, false)
	,
	SSC("PROFILE_STAFF_SEMINAR_COMPLEMENTARY", 8, false, false, false)
	,
	STG("PROFILE_STAFF_GENERAL"              , 8, false, false, false)
	,
	CLI("PROFILE_CLIENT"                     , 9, false, true, false)
	;
	
	private final String key;
	private final int hierarchy;
	private final Boolean flagSeminar;
	private final Boolean flagInscripcion;
	private final Boolean flagSuperProfile;
	
	private Profile(String key, int hierarchy, Boolean flagSeminar, Boolean flagInscription, Boolean flagSuperProfile) {
		this.key = key;
		this.hierarchy = hierarchy;
		this.flagSeminar = flagSeminar;
		this.flagInscripcion = flagInscription;
		this.flagSuperProfile = flagSuperProfile;
	}
	
	public String getKey() {
		return key;
	}

	public int getHierarchy() {
		return hierarchy;
	}
	
	public Boolean getFlagSeminar() {
		return flagSeminar;
	}
	
	public Boolean getFlagInscripcion() {
		return flagInscripcion;
	}

	public Boolean getFlagSuperProfile() {
		return flagSuperProfile;
	}

	
	/**
	 * Seleciona os perfis acessiveis
	 * @return
	 */
	public List<Profile> getAcessibleProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		for (Profile p : values()) {
			if (hierarchy <= p.getHierarchy()) {
				profiles.add(p);
			}
		}
		return profiles;
	}
	
	/**
	 * Seleciona somente os perfils acessiveis para Seminario
	 * @return
	 */
	public List<Profile> getAcessibleSeminarProfiles() {
		List<Profile> seminarProfiles = new ArrayList<Profile>();
		for (Profile p : getAcessibleProfiles() ) {
			if (p.getFlagSeminar()) {
				seminarProfiles.add( p );
			}
		}
		return seminarProfiles;
	}
	
	/**
	 * Seleciona somente os perfils acessiveis para Inscricao
	 * @return
	 */
	public List<Profile> getAcessibleInscriptionProfiles() {
		List<Profile> inscriptionProfiles = new ArrayList<Profile>();
		for (Profile p : getAcessibleProfiles()) {
			if (p.getFlagInscripcion()) {
				inscriptionProfiles.add( p );
			}
		}
		return inscriptionProfiles;
	}

	
	
		
}
