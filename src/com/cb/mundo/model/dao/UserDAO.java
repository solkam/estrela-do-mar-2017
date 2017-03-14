package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Profile;

/**
 * DAO para usuario.
 * 
 * @author Solkam
 * @since 25 ago 2011
 */
public class UserDAO {
	
	private EntityManager manager;
	

	public UserDAO(EntityManager manager) {
		this.manager = manager;
	}

	
	public UserCB findUserByUsernameAndPassword(String username, String password) {
		try {
			return manager.createNamedQuery("findUserByUsernameAndPassword", UserCB.class)
					.setParameter("pUsername", username)
					.setParameter("pPassword", password)
					.getSingleResult();
			
		}catch (NoResultException e) {
			return null;
		}
	}
	

	public List<UserCB> searchUserByContact(Contact c) {
		return manager.createNamedQuery("searchUserByContact", UserCB.class)
				.setParameter("pContact", c)
				.getResultList();
	}

	
	public List<UserCB> searchUserByAccessibleProfiles(List<Profile> profiles) {
		return manager.createNamedQuery("searchUserByAccessibleProfiles", UserCB.class)
				.setParameter("pProfiles", profiles)
				.getResultList();
	}

	
	public UserCB findUserByEmail(String email) {
		try {
			return manager.createNamedQuery("findUserByEmail", UserCB.class)
					.setParameter("pEmail", email)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	public UserCB findUserByContact(Contact contact) {
		try {
			return manager.createNamedQuery("findUserByContact", UserCB.class)
					.setParameter("pContact", contact)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	

	/**
	 * Busca usuarios de um cidade (considera somente allowedCities)
	 * Usado na RN para remover cidade.
	 * @param city
	 * @return
	 */
	public List<UserCB> searchUserByCity(City city) {
		return manager.createNamedQuery("searchUserByCity", UserCB.class)
				.setParameter("pCity", city)
				.getResultList();
	}

	

}
