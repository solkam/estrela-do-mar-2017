package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.cb.mundo.model.entity.CertificateNumber;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.config.CertificateConfigItem;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * DAO para configuracoes de certificados
 * 
 * @author Solkam
 * @since 23 jun 2012
 */
public class CertificateConfigDAO {
	
	private EntityManager manager;
	
	private DAO<CertificateConfigItem> certificateConfigItemDAO;
	private DAO<CertificateNumber> certificateNumberDAO;
	
	
	public CertificateConfigDAO(EntityManager manager) {
		this.manager = manager;
		this.certificateConfigItemDAO = new JavaxPersistenceDAO<CertificateConfigItem>(manager, CertificateConfigItem.class);
		this.certificateNumberDAO = new JavaxPersistenceDAO<CertificateNumber>(manager, CertificateNumber.class);
	}


	/**
	 * Salva item de configuracao de certificado
	 * @param item
	 * @return item desanexado
	 */
	public CertificateConfigItem saveCertificateConfigItem(CertificateConfigItem item) {
		return certificateConfigItemDAO.save(item);
	}
	
	public CertificateNumber saveCertificateNumber(CertificateNumber certNumber) {
		return certificateNumberDAO.save(certNumber);
	}

	/**
	 * Remove uma configuracao de item de certificacao
	 * @param item
	 */
	public void remove(CertificateConfigItem item) {
		certificateConfigItemDAO.remove(item);
	}
	
	public void removeAllCertificateConfigItems() {
		manager.createNamedQuery("CertificateConfigItem.removeAll").executeUpdate();
	}
	
	public void removerAllCertificateNumber() {
		manager.createNamedQuery("CertificateNumber.removeAll").executeUpdate();
	}
	
	/**
	 * Busca pelas configuracoes de certificado pela escola, modulo e numero de facilitadores
	 * @param school
	 * @param module
	 * @param numberOfFacilitator
	 * @return
	 */
	public List<CertificateConfigItem> searchCertificateConfigItemBySchoolAndModuleAndNumberOfFacilitor(
			School school, 
			Module module, 
			int numberOfFacilitator) {
		
		return manager.createNamedQuery("CertificateConfigItem.searchSchoolAndModuleAndNumberOfFacilitor", CertificateConfigItem.class)
				.setParameter("pSchool", school)
				.setParameter("pModule", module)
				.setParameter("pNumber", numberOfFacilitator)
				.getResultList();
	}	
	
	

}
