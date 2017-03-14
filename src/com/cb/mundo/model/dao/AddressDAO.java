package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Country;

/**
 * DAO para cidades e paises
 * [13 MAR 2015] os metodos de paise foram movidos para o service.
 * (podemos fazer os mesmo para cidades e matar este DAO).
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public class AddressDAO {
	
	private EntityManager manager;
	
	public AddressDAO(EntityManager manager) {
		this.manager = manager;
	}
	
	
	//city..	
	
	public List<City> searchCityByFlagActive(Boolean flagActive) {
		return manager.createNamedQuery("searchCityByFlagActive", City.class)
				.setParameter("pFlagActive", flagActive)
				.getResultList();
	}
	
	public List<City> searchAllCity() {
		return manager.createNamedQuery("searchCity", City.class)
				.getResultList();
	}

	
	public List<City> searchCityByCountry(Country country) {
		return manager.createNamedQuery("searchCityByCountry", City.class)
				.setParameter("pCountry", country)
				.getResultList();
	}
	
	public City findCityByName(String nameCity) {
		try {
			return manager.createNamedQuery("findCityByName", City.class)
					.setParameter("pName", nameCity.toLowerCase().trim() )
					.getSingleResult();
			
		}catch(NoResultException e) {
			//must be return null in order by unique name business rule works
			return null;
		}
	}

	
	//country...
	
	//(tudo transferido para o service)
	


	
	

}
