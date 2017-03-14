package com.cb.mundo.model.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.AddressDAO;
import com.cb.mundo.model.dao.ProductionDAO;
import com.cb.mundo.model.dao.UserDAO;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.exception.CityNameAlreadyExistException;
import com.cb.mundo.model.exception.CityReferencedByProductionException;
import com.cb.mundo.model.exception.CityReferencedByUserException;
import com.cb.mundo.model.exception.CountryCodeAlreadyExistException;
import com.cb.mundo.model.exception.CountryNameAlreadyExistException;

/**
 * EJB para Cidades e Paises
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
@Stateless
public class AddressService {
	
	@PersistenceContext
	private EntityManager manager;
	
	private AddressDAO addressDao;
	private UserDAO userDao;
	private ProductionDAO productionDAO;
	
	@EJB UserService userService;
	
	
	
	@PostConstruct void init() {
		addressDao = new AddressDAO(manager);
		userDao = new UserDAO(manager);
		productionDAO = new ProductionDAO(manager);
	}

	
	
	/* ****
	 * city
	 ******/
	
	public City saveCity(City city) {
		verifyCityNameIsUnique(city);

		boolean isNewCity = city.isTransient();//hold if city is new. It will be used later.
		
		city = manager.merge(city);

		handleNewCity(isNewCity, city);
		
		return city;
	}
	
	
	public City findCityByName(String nameCity) {
		return addressDao.findCityByName(nameCity);
	}

	/**
	 * if is a new city, every high user must have accesso to it;
	 * 
	 * @param isNewCity
	 * @param city
	 */
	private void handleNewCity(boolean isNewCity, City city) {
		if (isNewCity) {
			userService.updateSuperUsersWithNewCity(city);
		}
	}

	/**
	 * Business Rule:
	 * Look for a city with same name. Throw exception if found it. 
	 * 
	 * @param city
	 * @throws CityNameAlreadyExistException if already exist a city with same name
	 */
	private void verifyCityNameIsUnique(City city) {
		City alreadyExistCity = addressDao.findCityByName( city.getName() );
		if (alreadyExistCity != null && !alreadyExistCity.equals(city)) {
			throw new CityNameAlreadyExistException( city.getName() );
		}
	}
	
	
	/**
	 * Remove um cidade aplicando RNs
	 * @param city
	 */
	public void removeCity(City city) {
		city = manager.find(City.class, city.getId() );
		verifyProductionsOfCity(city);
		dereferenceUsersOfCity(city);
		manager.remove( city );
	}

	/** 
	 * Antes de remover, verifica se existem producoes referenciando
	 * a cidade a ser removida
	 * @throws CityReferencedByProductionException
	 */
	private void verifyProductionsOfCity(City city) {
		List<Production> productions = productionDAO.searchProductionByCity(city);
		if (!productions.isEmpty()) {
			throw new CityReferencedByProductionException(city);
		}
	}
	
	
	/**
	 * Remove a referencia da cidade nos usuarios
	 * @param city
	 * @throws CityReferencedByUserException
	 */
	private void dereferenceUsersOfCity(City city) {
		List<UserCB> users = userDao.searchUserByCity(city);
		for (UserCB user : users) {
			user.removeAllowedCity(city);
		}
	}
	


	
	public City findCityById(Long id) {
		return manager.find(City.class, id);
	}

	
	public List<City> searchCityByCountry(Country country) {
		return addressDao.searchCityByCountry(country);
	}

	
	public List<City> searchAllCity() {
		return addressDao.searchAllCity();
	}
	
	
	public List<City> searchAllCityLoadingTeams() {
		List<City> cities = addressDao.searchAllCity();
		return cities;
	}
	
	
	public List<City> searchCityByFlagActice(boolean flagActive) {
		return addressDao.searchCityByFlagActive(flagActive);
	}
	
	
	public List<City> searchActiveCity() {
		return addressDao.searchCityByFlagActive(true);
	}
	
	
	
	public City refreshCity(City city) {
		city = manager.find(City.class, city.getId() );
		city.getTeams().size();
		return city;
	}
	

	
	

	/* *******
	 * country
	 *********/
	
	/**
	 * Salva pais aplicando RNs.
	 * @param country
	 * @return
	 */
	public Country saveCountry(Country country) {
		verifyCountryCodeIsUnique(country);
		verifyCountryNameIsUnique(country);

		return manager.merge( country );
	}
	
	
	
	/**
	 * Verify if country code already exist
	 * @param country
	 * @throws CountryCodeAlreadyExistException
	 */
	private void verifyCountryCodeIsUnique(Country country) {
		Country alreadExistCountry = findCountryByCode(country.getCode());
		
		if (alreadExistCountry != null && alreadExistCountry.equals(country)) {
			if (!alreadExistCountry.getCreatedDate().equals(country.getCreatedDate()) ) {
				throw new CountryCodeAlreadyExistException( country.getCode() );
			}
		}
	}
	
	/**
	 * Business rule: find a country with the same name.
	 * Throw exception if find it.
	 * @param country
	 * @throw CountryNameAlreadyExistException if already exist a country with same name 
	 */
	private void verifyCountryNameIsUnique(Country country) {
		Country alreadExistCountry = findCountryByName(country.getName());
		if (alreadExistCountry != null && !alreadExistCountry.equals(country)) {
			throw new CountryNameAlreadyExistException(country.getName());
		}
	}
	
	
	/**
	 * Remove pais verificando RN
	 * @param country
	 */
	public void removeCountry(Country country) {
		//traz para Managed
		country = manager.find(Country.class, country.getCode() );
		//RN
		verifyCitiesOfCountry(country);
		//remove
		manager.remove( country );
	}
	

	
	/**
	 * RN: Verifica se pais tem cidades antes de remover
	 * @param country
	 */
	private void verifyCitiesOfCountry(Country country) {
		if (!country.getCities().isEmpty()) {
			throw new BusinessException("msg_country_has_cities", null);
		}
	}

	
	public Country refreshCountry(Country country) {
		country = manager.find(Country.class, country.getCode() );
		country.getCities().size();
		return country;
	}
	
	
	/**
	 * Busca pais pelo nome
	 * @param nameCountry
	 * @return pais encontrado ou null (para RN funcionar)
	 */
	public Country findCountryByName(String nameCountry) {
		try {
			return manager.createNamedQuery("findCountryByName", Country.class)
					.setParameter("pName", nameCountry)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 * Busca pais pela PK
	 * @param code
	 * @return pais encontrado ou null
	 */
	public Country findCountryByCode(String code) {
		try {
			return manager.find(Country.class, code);
			
		}catch(NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 * Busca por todos os paises
	 * @return
	 */
	public List<Country> searchCountryByFlagActive(boolean flagActive) {
		return manager.createNamedQuery("searchCountryByFlagActive", Country.class)
				.setParameter("pFlagActive", flagActive)
				.getResultList();
	}
	
	
	/**
	 * Busca somente os paises ativos
	 * @return
	 */
	public List<Country> searchActiveCountry() {
		return searchCountryByFlagActive( true );
	}



}
