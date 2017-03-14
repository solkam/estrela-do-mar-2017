package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.CertificateNumber;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.config.CertificateConfigItem;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * EJB para configuracao de certificados
 * [refatoring(2015_03_16): removido o DAO]
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
@Stateless
public class CertificateConfigService {
	
	@PersistenceContext
	private EntityManager manager;

	
	
	/*
	 * Certificate Config Item
	 *************************/
	
	/**
	 * Salva item de configuracao de certificado
	 * @param item
	 * @return item
	 */
	public CertificateConfigItem saveCertificateConfigItem(CertificateConfigItem item) {
		item.assignDisplayOrder();
		return manager.merge(item);
	}

	/**
	 * Pesquisa pelas configuracoes de certificado pela escola, modulo e numero de facilitadores
	 * @param school
	 * @param module
	 * @param numberOfFacilitator
	 * @return
	 */
	public List<CertificateConfigItem> searchCertificateConfigItemBySchoolAndModuleAndNumberOfFacilitor(
			School school, 
			Module module, 
			int numberOfFacilitator) {
		
		return manager.createNamedQuery("searchCertificateConfigItemBySchoolAndModuleAndNumberOfFacilitor", CertificateConfigItem.class)
				.setParameter("pSchool", school)
				.setParameter("pModule", module)
				.setParameter("pNumber", numberOfFacilitator)
				.getResultList();
	}	

	
	/**
	 * Remove todos o item de configuracao de certificado
	 * @param item
	 */
	public void removeCertificateConfigItem(CertificateConfigItem item) {
		manager.remove( manager.merge(item) );
	}
	
	
	/*
	 * Certificate Number
	 ********************/
	
	/**
	 * Salva o numero de certificado
	 * @param certificateNumber
	 * @return
	 */
	public CertificateNumber saveCertificateNumber(CertificateNumber certificateNumber) {
		return manager.merge( certificateNumber );
	}
	
	
//	public void removeAllCertificateConfigItems() {
//		certificateConfigDAO.removeAllCertificateConfigItems();
//	}
	
	
//	public void removeAllCertificateNumbers() {
//		certificateConfigDAO.removerAllCertificateNumber();
//	}

}
