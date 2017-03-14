package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.exception.PasswordNotConfirmedException;
import com.cb.mundo.model.service.AddressService;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.UserService;

/**
 * Managed Bean para configuracao de Usuario
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="configUserBean")
@ViewScoped
public class ConfigUserBean implements Serializable {
	private static final long serialVersionUID = -8304082787259113920L;

	@EJB UserService userService;
	
	@EJB ContactService contactService;
	
	@EJB AddressService addressService;
	
	@ManagedProperty("#{loginBean}") 
	private LoginBean loginBean;
	
	private List<UserCB> users;
	
	
	private UserCB user = new UserCB();
	private Contact contact = new Contact();
	
	//combos
	private List<City> cities;
	private List<Profile> comboProfiles;
	
	private String password1 = "";
	private String password2 = "";
	
	//filters...	
	private String filterName;
	private String filterCivilName;
	private String filterEmail;
	private Profile filterProfile;
	
	
	@PostConstruct void init() {
		resetUser();
		populateCities();
		populateComboProfiles();
	}


	private void populateCities() {
		cities = addressService.searchActiveCity();
	}
	
	
	private void populateComboProfiles() {
		Profile autenticatedProfile = loginBean.getAuthenticatedUser().getProfile();
		comboProfiles = getLowerProfiles( autenticatedProfile );
	}

	private void populateUsers() {
		users = userService.searchUserByFilters( filterName
										  	   , filterCivilName
											   , filterEmail
											   , filterProfile
											   );
	}
	
	
//actions...
	
	public void searchUser() {
		populateUsers();
		JSFUtil.addMessageAboutResult(users);
	}
	
	/**
	 * Metodo de pesquisa do componente autocomplete
	 * @param name
	 * @return
	 */
	public List<Contact> queryContact(String name) {
		List<Contact> result = contactService.searchContactByNameOrCivilNameOrEmail(name, name, null);
		return result;
	}
	
	public void resetUser() {
		user = new UserCB();
		contact = new Contact();
		user.setProfile( filterProfile );
		
		UserCB authUser = loginBean.getAuthenticatedUser();
		
		user.setDefaultCity(   authUser.getDefaultCity()   );
		user.setDefaultSchool( authUser.getDefaultSchool() );
		user.setDefaultIdiom(  authUser.getDefaultIdiom()  );
	}
	
	public void manageUser(UserCB selectedUser) {
		user = selectedUser;
		contact = selectedUser.getContact();
	}
	
	public void saveUser() {
		//handle pais
		handleCityAndCountry();
		
		//1.salva primeiro o contato
		contact = contactService.saveContact(contact, loginBean.getAuthenticatedUser() );
		
		//2.salva
		user.setContact(contact);
		user = userService.saveUser(user, loginBean.getAuthenticatedUser() );
		
		//3.refresca a lista de usuarios
		populateUsers();
		
		//4.msg de sucesso
		JSFUtil.addInfoMessage("msg_user_saved_sucess", null);
	}
	
	
	
	/**
	 * se nao tiver pais associado, usa o da cidade
	 */
	private void handleCityAndCountry() {
		//se contact eh novo e nao tem pais, use o da cidade default
		if (contact.getCountry()==null || contact.getCountry().trim().isEmpty()){
			contact.setCountry( user.getDefaultCity().getCountry().getName() );
		}
		//se contact nao tiver cidade, use o nome da cidade default
		if (contact.getCity()==null || contact.getCity().trim().isEmpty()) {
			contact.setCity( user.getDefaultCity().getName() );
		}
	}


	public void savePassword() {
		validatePasswords(user);
		assignPassword(user);
		
		userService.changePassword(user, loginBean.getAuthenticatedUser() );

		JSFUtil.addInfoMessage("msg_pass_change_sucess");
	}
	
	/**
	 * Verifica se as senhas conferem.
	 * Para usuarios novos, atribui a senha.
	 * Para usuarios existentes, mantem a senha original
	 */
	private void validatePasswords(UserCB user) {
		if (!password1.equals(password2)) {
			throw new PasswordNotConfirmedException();
		}
	}
	
	private void assignPassword(UserCB user) {
		user.setPassword( password1 );
	}
	
	
	public void removeUser() {
		userService.removeUser(user);
		populateUsers();
		JSFUtil.addInfoMessage("msg_user_remove_sucess");
	}
	
	
	//util
	public List<Profile> getLowerProfiles(Profile autenticatedProfile) {
		List<Profile> profiles = new ArrayList<>();
		for (Profile profileVar : Profile.values()) {
			if (profileVar.getFlagInscripcion() ) {
				if (profileVar.getHierarchy() >= autenticatedProfile.getHierarchy()) {
					profiles.add( profileVar );
				}
			}
		}
		return profiles;
	}
	
	
	//acessores...
	
	public UserCB getUser() {
		return user;
	}

	public void setUser(UserCB user) {
		this.user = user;
		this.contact = user.getContact();
	}

	public List<UserCB> getUsers() {
		return users;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public Contact getContact() {
		if (contact==null) {
			contact = new Contact();
		}
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterCivilName() {
		return filterCivilName;
	}

	public void setFilterCivilName(String filterCivilName) {
		this.filterCivilName = filterCivilName;
	}

	public String getFilterEmail() {
		return filterEmail;
	}

	public void setFilterEmail(String filterEmail) {
		this.filterEmail = filterEmail;
	}

	public Profile getFilterProfile() {
		return filterProfile;
	}

	public void setFilterProfile(Profile filterProfile) {
		this.filterProfile = filterProfile;
	}

	public List<City> getCities() {
		return cities;
	}


	public List<Profile> getComboProfiles() {
		return comboProfiles;
	}
	

}
