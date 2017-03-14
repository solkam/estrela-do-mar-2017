package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.QueryUtil.isNotBlank;
import static com.cb.mundo.model.util.QueryUtil.isNotNull;
import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeANY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;

import com.cb.mundo.model.dao.UserDAO;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.PasswordInvalidException;
import com.cb.mundo.model.exception.PasswordNewEqualsOldException;
import com.cb.mundo.model.exception.UserEmailNotFoundException;
import com.cb.mundo.model.exception.UserNotAuthenticatedException;
import com.cb.mundo.model.exception.UserWithoutAllowedSchoolsException;
import com.cb.mundo.model.exception.UsernameAlreadyExistException;
import com.cb.mundo.model.util.Constants;
import com.cb.mundo.model.util.Randomizer;
import com.cb.mundo.model.util.StringUtil;

/**
 * EJB Service for Users
 * 
 * @author Solkam
 * @since 26 out 2011
 */
@Stateless
public class UserService {
	
	@PersistenceContext 
	EntityManager manager;

	private UserDAO userDAO;
	
//	private FacilitatorDAO facilitatorDAO;
	
//	private ContactDAO contactDAO;
	
	
	@PostConstruct 
	void init() {
		userDAO = new UserDAO(manager);
	}
	
	
	public UserCB changePassword(UserCB user, UserCB autenticatedUser) {
		applyUserLogs(user, autenticatedUser);
		return manager.merge( user );
	}
	
	
	public UserCB saveUser(UserCB user, UserCB autenticatedUser) {
		verifyContactAlreadyHasUser(user);
		
		handleDefaultSchool(user);
		handleDefaultCity(user);
		handleInitialPassword(user);
		
		applyUserLogs(user, autenticatedUser);
		
		return manager.merge(user);
	}

	
	private void applyUserLogs(UserCB user, UserCB autenticatedUser) {
		if (autenticatedUser!=null) {
			if (user.isTransient()) {//eh novo
				user.setCreatedBy( autenticatedUser.getContact().getShortDesc() );
				user.setCreateDate( new Date() );
			
			} else {
				user.setUpdatedBy( autenticatedUser.getContact().getShortDesc() );
				user.setUpdateDate( new Date() );
			}
		}
	}


	/**
	 * Se senha do usuario estiver vazia, seta uma randomica
	 * @param user
	 */
	private void handleInitialPassword(UserCB user) {
		if ( StringUtil.isBlank(user.getPassword())) {
			String randomPass = new Randomizer().randomString();
			user.setPassword(randomPass);
		}
	}
	
	
	/**
	 * Se usuario nao tiver cidade default, seta uma.
	 * [Nota] eh possivel que um usuario nao tenha cidades.
	 * @param user
	 */
	private void handleDefaultCity(UserCB user) {
		if (user.getDefaultCity()==null) {
			
			if (user.getAllowedCities()!=null && !user.getAllowedCities().isEmpty()) {
				user.setDefaultCity( user.getAllowedCities().get(0) );
			}
		}
	}
	
	
	/**
	 * 1) Valida as escolas do usuario
	 * 2) Se nao tiver escola default, seta uma
	 * @param user
	 */
	private void handleDefaultSchool(UserCB user) {
		if (user.getDefaultSchool()==null) {
			
			if (user.getAllowedSchools()==null || user.getAllowedSchools().isEmpty()) {
				throw new UserWithoutAllowedSchoolsException();//nao tem como definir escola default
			} else {
				user.setDefaultSchool( user.getAllowedSchools().get(0) );
			}
		}
	}

	/**
	 * Verifica se ja existe um usario para o contato
	 * @param user
	 */
	private void verifyContactAlreadyHasUser(UserCB user) {
		UserCB foundUser = userDAO.findUserByContact( user.getContact() );
		
		if (foundUser!=null && !foundUser.equals(user)) {
			throw new UsernameAlreadyExistException( user.getContact().getEmail() );
		}
	}
	
	
	
	/**
	 * Salva escola e cidade defaults do usuario
	 * (no momento do logou)
	 */
	
	public void saveUserDefaultSchoolAndCity(UserCB user, School defaultSchool, City defaultCity) {
		user = manager.find(UserCB.class, user.getId() );
		user.setDefaultSchool(defaultSchool);
		user.setDefaultCity( defaultCity );
	}
	
	

	
	
	public void removeUser(UserCB user) {
		manager.remove( manager.merge(user) );
	}
	
	
	
	/**
	 * Realiza a autenticacao de usuario pela email e senha.
	 * Se nao encontrar nenhum usuario, lanca exception.
	 * Se encontrar, colocar o email no escopo da session (para envers).
	 * E carrega suas escolas e suas cidades
	 * @param username
	 * @param password
	 * @param httpSession
	 * @return
	 */
	public UserCB autenticateUser(String username, String password, HttpSession httpSession) {
		//1.find user
		UserCB foundUser = userDAO.findUserByUsernameAndPassword(username, password);
		if (foundUser == null) {
			throw new UserNotAuthenticatedException();
		} 
		//2.carregar escolas e cidades
		foundUser.getAllowedCities().size();
		foundUser.getAllowedSchools().size();
		
		//3.guarda na session para auditoria
		httpSession.setAttribute(Constants.SESSION_ATTRIBUTE_AUTENTICATED_USERNAME, foundUser.getContact().getEmail() );
		
		//4.retorna
		return foundUser;
	}
	
	
	public UserCB findUserByContact(Contact contact) {
		return userDAO.findUserByContact(contact);
	}


	/**
	 * Forca a carga completa do usuario
	 * @param user
	 * @return
	 */
	public UserCB refreshUser(UserCB user) {
		//traz para gerenciado
		user = manager.find(UserCB.class, user.getId() );
		//forca carga
		user.getAllowedCities().size();
		user.getAllowedSchools().size();
		
		return user;
	}

	
	
	/**
	 * Sempre uma cidade é criado, todos os super 
	 * usuarios passam a ter acessa a ele
	 * @param city
	 */
	public void updateSuperUsersWithNewCity(City newCity) {
		//monta a lista de super profiles...
		List<Profile> superProfiles = new ArrayList<Profile>();
		for (Profile profileVar : Profile.values()) {
			if (profileVar.getFlagSuperProfile()) {
				superProfiles.add( profileVar );
			}
		}
		//atualiza os super usuarios com a nova cidade...
		List<UserCB> superUsers = userDAO.searchUserByAccessibleProfiles(superProfiles);
		for (UserCB user : superUsers) {
			user.getAllowedCities().add(newCity);
			manager.merge( user );
		}
	}

	
	public UserCB resetUserPassword(String email) {
		//1.testa se email existe
		UserCB user = userDAO.findUserByEmail(email);
		if (user==null) {
			throw new UserEmailNotFoundException(email);
		}
		
		//3.se tudo OK, reseta a senha e salva
		String newPass = new Randomizer().randomString();
		user.setPassword( newPass );
		user = manager.merge( user );
		
		//retorna
		return user;
	}

	
	/**
	 * Pesquisa usuario pelos filtros de pesquisa
	 * @param name
	 * @param civilName
	 * @param email
	 * @param profile
	 * @return usuarios ordenados pelo nome civil
	 */
	public List<UserCB> searchUserByFilters(String name
										   ,String civilName 
										   ,String email
										   ,Profile profile
										   ) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<UserCB> criteria = builder.createQuery(UserCB.class);
		Root<UserCB> root = criteria.from(UserCB.class);
		
		Join<UserCB, Contact> joinContact = root.<UserCB, Contact>join("contact");
		
		Predicate conjunction = builder.conjunction();
		//name
		if (isNotBlank(name)) {
			conjunction = builder.and(conjunction, 
					builder.like(joinContact.<String>get("name"), toLikeMatchModeANY(name) )
				);
		}
		//civil name
		if (isNotBlank(civilName)) {
			conjunction = builder.and(conjunction, 
					builder.like(joinContact.<String>get("civilName"), toLikeMatchModeANY(civilName) )
				);
		}
		//email
		if (isNotBlank(email)) {
			conjunction = builder.and(conjunction, 
					builder.like(joinContact.<String>get("email"), toLikeMatchModeANY(email) )
				);
		}
		//profile
		if (isNotNull(profile)) {
			conjunction = builder.and(conjunction, 
					builder.equal(root.<Profile>get("profile"), profile)
				);
		}
		//where e order by
		criteria.where(conjunction);
		criteria.orderBy( builder.asc( joinContact.<String>get("civilName") ) );
		
		List<UserCB> users = manager.createQuery(criteria).getResultList();
		return users;
	}
	
	
	
	public void validatePasswordEqualTheCurrentOne(UserCB user, String password) {
		password = cripto(password);
		if (!user.getPassword().equals(password)) {
			throw new PasswordInvalidException();
		}
	}
	
	
	public void validateNewPasswordEqualTheCurrentOne(UserCB user, String newPassword) {
		newPassword = cripto(newPassword);
		if (user.getPassword().equals(newPassword)) {
			throw new PasswordNewEqualsOldException();
		}
	}
	
	private String cripto(String plain) {
		return plain;
	}


}
