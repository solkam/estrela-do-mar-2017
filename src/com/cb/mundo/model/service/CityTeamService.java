package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.CityTeam;
import com.cb.mundo.model.entity.CityTeamMember;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.BusinessException;

/**
 * Servicos para Equipe de City
 * @author Solkam
 * @since 02 DEZ 2014
 */
@Stateless
public class CityTeamService {
	
	@PersistenceContext
	private EntityManager manager;
	
	
	/* *********
	 * city team	
	 ***********/
	
	/**
	 * Salva city team aplicando RN
	 * @param team
	 * @return
	 */
	public CityTeam saveCityTeam(CityTeam team) {
		verifyIfTeamAlreadyExistsInCity(team);
		return manager.merge( team );
	}
	
	/**
	 * Itera sobre as equipes da cidade e verifica se equipe ja foi cadastrada
	 * @param team
	 */
	private void verifyIfTeamAlreadyExistsInCity(CityTeam team) {
		City city = manager.find(City.class, team.getCity().getId() );
		
		for (CityTeam existingTeam : city.getTeams()) {
			if ( existingTeam.getSchool().equals( team.getSchool() )) {
				if (team.isTransient() || !existingTeam.equals(team)) {
					throw new BusinessException("msg_city_team_already_exists_in_city", null);
				}
			}
		}
	}

	/**
	 * Remove uma equipe
	 * @param team
	 */
	public void removeCityTeam(CityTeam team) {
		team = manager.find(CityTeam.class, team.getId() );
		manager.remove( team );
	}
	
	
	/**
	 * Busca uma equipe pela cidade e escola.
	 * @param city
	 * @param school
	 * @return
	 */
	public CityTeam findCityTeamByCityAndSchool(City city, School school) {
		try {
			return manager.createNamedQuery("findCityTeamByCityAndSchool", CityTeam.class)
					.setParameter("pCity", city)
					.setParameter("pSchool", school)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Recarrega equipe com os membros
	 * @param cityTeam
	 * @return
	 */
	public CityTeam refreshCityTeam(CityTeam cityTeam) {
		cityTeam = manager.find(CityTeam.class, cityTeam.getId());
		cityTeam.getMembers().size();
		return cityTeam;
	}
	
	
	
	/* ****************
	 * city team member
	 ******************/
	
	/**
	 * Salva um integrante da equipe
	 * @param member
	 * @return
	 */
	public CityTeamMember saveCityTeamMember(CityTeamMember member) {
		return manager.merge( member );
	}
	
	
	/**
	 * Remove um integrante da equipe
	 * @param member
	 */
	public void removeCityTeamMember(CityTeamMember member) {
		manager.remove( manager.merge( member ));
	}
	
	
	/**
	 * Pesquisa membro da equipe pela cidade e escola
	 * @param city
	 * @param school
	 * @return
	 */
	public List<CityTeamMember> searchCityTeamMemberByCityAndSchool(City city, School school) {
		return manager.createNamedQuery("searchCityTeamMemberByCityAndSchool", CityTeamMember.class)
				.setParameter("pCity", city)
				.setParameter("pSchool", school)
				.getResultList();
	}
	
	
	

}
