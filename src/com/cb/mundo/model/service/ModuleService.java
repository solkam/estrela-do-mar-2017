package com.cb.mundo.model.service;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.BusinessException;

/**
 * Servicios para Modulos
 * @author Yamarty
 * @since 23 MAR 2015
 */
@Stateless
public class ModuleService {
	
	@PersistenceContext
	private EntityManager manager ;
	
	/**
	 * Salva um modulo
	 * @param m
	 * @return
	 */
	public Module saveModule(Module m){
		return manager.merge(m);
	}
	
	/**
	 * Remove um modulo
	 * @param module
	 */
	public void removeModule(Module module){
		//traz para estado Gerenciado
		module = manager.find(Module.class, module.getId() );
		//RNs
		verifyEventsOfModule(module);
		verifyProductionOfModule(module);
		verifyCertificateNumberOfModule(module);
		//remove!!!
		manager.remove(module) ;
	}
	
	
	private void verifyEventsOfModule(Module module) {
		if (!module.getEvents().isEmpty()) {
			throw new BusinessException("msg_module_has_events", null);
		}
	}

	private void verifyProductionOfModule(Module module) {
		if (!module.getProductions().isEmpty()) {
			throw new BusinessException("msg_module_has_productions", null);
		}
	}

	private void verifyCertificateNumberOfModule(Module module) {
		if (!module.getCertificateNumbers().isEmpty()) {
			throw new BusinessException("msg_module_has_certificate_numbers", null);
		}
	}
	

	/**
	 * Pesquisa Modulo pelos filtros de pesquisa usando criteria
	 * (usando em Configuracao de Modulo)
	 * @param filterSchool
	 * @return
	 */
	public List<Module> searchModuleByFilter(School filterSchool){
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Module> criteria = builder.createQuery(Module.class);
		Root<Module> root = criteria.from(Module.class);
		
		Predicate conjunction = builder.conjunction();
		//school
		if (filterSchool!= null){
			conjunction = builder.and(conjunction
					,builder.equal(root.<School>get("school"), filterSchool));		
		}
		//where e orderby
		criteria.where(conjunction);
		criteria.orderBy( builder.asc(root.<String>get("sigla") ));
		
		return manager.createQuery(criteria).getResultList(); 
	}
	
	
	/**
	 * Busca um Modulo pela sua PK
	 * @param id
	 * @return
	 */
	public Module findModuleByID(Long id){
		return manager.find(Module.class, id);
	}
	

	/**
	 * Pesquisa os modulos de uma escola
	 * @param school
	 * @return
	 */
	public List<Module> searchModuleBySchool(School school, Idiom currentIdiom) {
		//1.seleciona modulos da escola
		List<Module> modules = manager.createNamedQuery("searchModuleBySchool", Module.class)
				.setParameter("pSchool", school)
				.getResultList();
		
		//2.define o idiom corrent
		for (Module module : modules) {
			module.setCurrentIdiom( currentIdiom );
		}
		return modules;
	}
	
	
}
