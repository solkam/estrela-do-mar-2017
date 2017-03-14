package com.cb.mundo.model.searchfilter;

import java.io.Serializable;

import com.cb.mundo.model.entity.enumeration.School;

/**
 * Filtro para pesquisa de facilitatores
 * @author Solkam
 * @since 24 nov 2011
 */
public class FacilitatorSearchFilter implements Serializable {
	private static final long serialVersionUID = 4090031413603515279L;

	/**
	 * Value inicial eh LCB
	 */
	private School school = School.LCB;
	
	/**
	 * 3 possiveis valores: null, true ou false
	 */
	private Boolean flagActive = true;

	public boolean isValidSchool() {
		return school!=null;
	}
	
	public boolean isValidActive() {
		return flagActive!=null;
	}

	
	
	
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Boolean getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}

	
	
	
	

}
