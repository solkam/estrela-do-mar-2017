/*
 * *@autor Yamarty
 * @fecha 20-03-2015
 */
package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.SchoolEntity;

@Stateless
public class SchoolService {

	@PersistenceContext
	private EntityManager managerSchool;
	
	public SchoolEntity salvarSchool(SchoolEntity s){
		return managerSchool.merge(s);
	}
	public List<SchoolEntity> searchSchoolByFilters(){

		CriteriaBuilder builder = managerSchool.getCriteriaBuilder();
		CriteriaQuery<SchoolEntity> criteria = builder.createQuery(SchoolEntity.class);
		Root<SchoolEntity> root = criteria.from(SchoolEntity.class);
		Predicate conjunction = builder.conjunction();
		/*if (fNombre!= null && !fNombre.isEmpty()){
			conjunction = builder.and(conjunction
					,builder.like(root.<String>get("nombre"), "%" + fNombre+ "%"));
		
		}
		if(fFecha != null ){
			conjunction = builder.and(conjunction,
			
					builder.equal(root.<Date>get("fechaFabricacion"),fFecha));
	
		}
		*/
		criteria.where(conjunction);
		criteria.orderBy(builder.asc(root.<String>get("name")));
		return managerSchool.createQuery(criteria).getResultList(); 
	}
	
	public List<SchoolEntity> searchSchool(){

		CriteriaBuilder builder = managerSchool.getCriteriaBuilder();
		CriteriaQuery<SchoolEntity> criteria = builder.createQuery(SchoolEntity.class);
		Root<SchoolEntity> root = criteria.from(SchoolEntity.class);
		Predicate conjunction = builder.conjunction();
		
		criteria.where(conjunction);
		criteria.orderBy(builder.asc(root.<String>get("name")));
		return managerSchool.createQuery(criteria).getResultList(); 
	}
}
