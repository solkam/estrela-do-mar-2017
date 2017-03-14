package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.RecipientInforme;
import com.cb.mundo.model.entity.enumeration.InformeType;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Servico para informes
 * 
 * @author Solkam
 * @since 23 NOV 2014
 */
@Stateless
public class InformeService {
	
	@PersistenceContext
	private EntityManager manager;
	
		
	public RecipientInforme saveRecipientInforme(RecipientInforme ri) {
		return manager.merge( ri );
	}
	
	
	public void removeRecipientInforme(RecipientInforme ri) {
		manager.remove( manager.merge(ri) );
	}
	
	
	public List<RecipientInforme> searchRecipientInformeBySchoolAndType(School school, InformeType informeType) {
		return manager.createNamedQuery("searchRecipientInformeBySchoolAndInformeType", RecipientInforme.class)
				.setParameter("pSchool", school)
				.setParameter("pInformeType", informeType)
				.getResultList();
	}
	
	
	public List<RecipientInforme> searchRecipientInformeByType(InformeType informeType) {
		return manager.createNamedQuery("searchRecipientInformeByInformeType", RecipientInforme.class)
				.setParameter("pInformeType", informeType)
				.getResultList();
	}
	 

}
