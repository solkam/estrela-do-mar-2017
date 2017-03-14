package com.cb.mundo.model.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.FacilitatorDAO;
import com.cb.mundo.model.dao.ProductionDAO;
import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.FacilitatorAlreadyExistException;
import com.cb.mundo.model.exception.FacilitatorReferencedByProduction;
import com.cb.mundo.model.searchfilter.FacilitatorSearchFilter;


/**
 * EJB responsavel pelas operacoes em Facilitator
 * 
 * @author Solkam
 * @since 14 mar 2012
 */
@Stateless
public class FacilitatorService {
	
	@PersistenceContext
	private EntityManager manager;
	
	private FacilitatorDAO facilitatorDAO;
	
	private ProductionDAO productionDAO;
	
	@PostConstruct void init() {
		facilitatorDAO = new FacilitatorDAO(manager);
		productionDAO = new ProductionDAO(manager);
	}
	
	
	
	
	public Facilitator saveFacilitator(Facilitator f) {
		verifyIfAlreadyExistsFacilitatorWithSameSchoolAndContact(f);
		return manager.merge(f);
	}
	
	private void verifyIfAlreadyExistsFacilitatorWithSameSchoolAndContact(Facilitator f) {
		Facilitator existingFacilitator = facilitatorDAO.findFacilitatorBySchoolAndContact(f.getSchool(), f.getContact());
		
		if (existingFacilitator!=null && !existingFacilitator.equals(f)) {
			throw new FacilitatorAlreadyExistException(f.getSchool(), f.getContact());
		}
	}
	
	
	public void removeLogicallyFacilitator(Facilitator f) {
		verifyIfFacilitatorIsReferencedByProduction(f);
		manager.remove( manager.merge(f) );
	}
	
	private void verifyIfFacilitatorIsReferencedByProduction(Facilitator f) {
		List<Production> productionsReferencedByFacilitator = productionDAO.searchProductionByFacilitator(f);
		if (!productionsReferencedByFacilitator.isEmpty()) {
			throw new FacilitatorReferencedByProduction(f);
		}
	}



	
	public Facilitator findFacilitatorById(Serializable id) {
		return manager.find(Facilitator.class, id);
	}

	
	
	public List<Facilitator> searchFacilitatorsBySchool(School school) {
		return facilitatorDAO.searchFacilitadorBySchool(school);
	}
	
	
	public List<Facilitator> searchAllFacilitators() {
		return facilitatorDAO.searchAllFacilitators();
	}


	
	public List<Facilitator> searchFacilitatorBySearchFilter(FacilitatorSearchFilter filter) {
		return facilitatorDAO.searchFacilitatorBySearchFilter(filter);
	}
	
	
	
	
	

}
