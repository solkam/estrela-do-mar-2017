package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.searchfilter.FacilitatorSearchFilter;

/**
 * Data Access Object para Facilitator
 * 
 * @author Solkam
 * @since 14 mar 2012
 */
public class FacilitatorDAO {

	private EntityManager manager;

	public FacilitatorDAO(EntityManager manager) {
		this.manager = manager;
	}

	
	public void inactivateFacilitatorByContact(Contact contact) {
		manager.createNamedQuery("Facilitator.inactivateByContact")
			.setParameter("pContact", contact)
			.executeUpdate();
	}
	
	public Facilitator findFacilitatorById(Long id) {
		return manager.find(Facilitator.class, id);
	}
	
	public Facilitator findFacilitatorBySchoolAndContact(School school, Contact contact) {
		try {
			return manager.createNamedQuery("Facilitator.findBySchoolAndContact", Facilitator.class)
					.setParameter("pSchool", school)
					.setParameter("pContact", contact)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return new Facilitator();
		}
	}
	
	
	public List<Facilitator> searchAllFacilitators() {
		return manager.createNamedQuery("Facilitator.searchAll", Facilitator.class)
				.getResultList();
	}

	
	public List<Facilitator> searchFacilitadorBySchool(School school) {
		return manager.createNamedQuery("Facilitator.searchBySchool", Facilitator.class)
				.setParameter("pSchool", school)
				.getResultList();
	}

	public List<Facilitator> searchFacilitatorByContact(Contact c) {
		return manager.createNamedQuery("Facilitator.searchByContact", Facilitator.class)
				.setParameter("pContact", c)
				.getResultList();
	}

	public List<Facilitator> searchFacilitatorBySearchFilter(FacilitatorSearchFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Facilitator> criteria = builder.createQuery(Facilitator.class);
		Root<Facilitator> root = criteria.from(Facilitator.class);
		
		Predicate conjunction = builder.conjunction();
		if (filter.isValidSchool()) {
			conjunction = builder.and(conjunction, 
				builder.equal(root.<School>get("school"), filter.getSchool() )
			);
		}
		
		if (filter.isValidActive()) {
			conjunction = builder.and(conjunction,
				builder.equal(root.<Boolean>get("flagActive"), filter.getFlagActive())
			);
		}
		
		criteria.where(conjunction);
		return manager.createQuery(criteria).getResultList();
	}
	


}
