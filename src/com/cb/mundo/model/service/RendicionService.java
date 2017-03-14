package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.QueryUtil.isNotEmpty;
import static com.cb.mundo.model.util.QueryUtil.isNotNegative;
import static com.cb.mundo.model.util.QueryUtil.isNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.Rendicion;
import com.cb.mundo.model.entity.RendicionPayment;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Servicos para rendiciones
 * 
 * @author Solkam
 * @since 23 NOV 2014
 */
@Stateless
public class RendicionService {
	
	@PersistenceContext EntityManager manager;
	

	/**
	 * Salva um rendicion
	 * @param r
	 * @return
	 */
	public Rendicion saveRendicion(Rendicion r) {
		return manager.merge( r );
	}
	
	

	/**
	 * Refresca a rendicion com todas as referencias
	 * @param rendicion
	 * @return
	 */
	public Rendicion refreshRendicion(Rendicion rendicion) {
		rendicion = manager.find(Rendicion.class, rendicion.getId() );
		rendicion.getOutcomingDetails().size();
		rendicion.getPayments().size();

		Production production = rendicion.getProduction();
		production.getIntegrants().size();
		production.getParticipants().size();
		production.getStaffs().size();
		production.getOutcomings().size();
		production.getIncomings().size();
		return rendicion;
	}
		
	
	/**
	 * Pesquisar rendiciones por filtros
	 * (usando no relatorio)
	 * @param schools
	 * @param cities
	 * @param year
	 * @param months
	 * @param flagTotallyPaid
	 * @return
	 */
	public List<Rendicion> searchRendicionByFilters(
			List<School> schools, 
			List<City> cities,	
			int year, 
			List<Integer> months,
			Boolean flagTotallyPaid) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Rendicion> criteria = builder.createQuery(Rendicion.class);
		
		Root<Rendicion> rootRendicion = criteria.from(Rendicion.class);
		Path<Production> rootProduction = rootRendicion.<Production>get("production");
		
		Predicate conjunction = builder.conjunction();
		//schools
		if (isNotEmpty(schools)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<School>get("school").in(schools) 
			);
		}
		//cities
		if (isNotEmpty(cities)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<City>get("city").in(cities) 
			);
		}
		//year
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction, 
				builder.equal( 
					builder.function("YEAR", Integer.class, rootProduction.<Date>get("firstDate")),
					year
				));
		}
		//cities
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction, 
				builder.function("MONTH", Integer.class, rootProduction.<Date>get("firstDate")).in(months)
			);
		}
		
		criteria.where(conjunction);
		
		//order by
		criteria.orderBy( 
				builder.asc(rootProduction.<School>get("school")), 
				builder.asc(rootProduction.<City>get("city")) 
		);
		
		List<Rendicion> rendicoesAll = manager.createQuery(criteria).getResultList();
		
		
		//flag totally paid
		List<Rendicion> rendiconesFiltered = new ArrayList<Rendicion>();
		if (isNotNull(flagTotallyPaid)) {
			for (Rendicion rendicionVar : rendicoesAll) {
				if (flagTotallyPaid.equals(rendicionVar.getFlagPaymentOK() )) {
					rendiconesFiltered.add( rendicionVar );
				}
			}
		}
		
		return rendiconesFiltered;
	}
	
	
	/**
	 * Busca rendicao pela producao
	 * @param production
	 * @return rendicion ou null
	 */
	public Rendicion findRendicionByProduction(Production production) {
		try {
			return manager.createNamedQuery("findRendicionByProduction", Rendicion.class)
					.setParameter("pProduction", production)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}


	//Payments
	
	/**
	 * Salva um pagamento parcial de rendicao
	 * @param payment
	 * @return
	 */
	public RendicionPayment saveRendcionPayment(RendicionPayment payment) {
		return manager.merge( payment );
	}
	
	
	/**
	 * Remove um pagamento parcial de rendicao
	 * @param payment
	 */
	public void removeRendicionPayment(RendicionPayment payment) {
		manager.remove( manager.merge(payment) );
	}
	
	
}
