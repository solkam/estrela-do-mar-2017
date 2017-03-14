package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.estreladomar.controller.util.Navigation;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.ProductionStatus;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.exception.PasswordNotConfirmedException;
import com.cb.mundo.model.exception.UserOfContactAlreadyExistsException;
import com.cb.mundo.model.service.AddressService;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.UserService;

@ManagedBean(name="signupBean")
@SessionScoped
public class SignupBean implements Serializable {
	private static final long serialVersionUID = -937281669290668872L;
	
	@EJB ContactService contactService;
	@EJB UserService userService;
	@EJB AddressService addressService;
	
	private UserCB user;

	private Contact contact;
	
	//combos
	private Idiom idiom = Idiom.pt;
	
	
	//confirmacao de senha
	private String pass1 = "";
	private String pass2 = "";

	
	@PostConstruct void init() {
		resetContact();
	}
	
	private void resetContact() {
		contact = new Contact();
	}
	
	private void instanciateUser() {
		user = new UserCB();
		user.setProfile( Profile.CLI );
	}
	
	/**
	 * Query para o autocomplete
	 * @param fragment
	 * @return
	 */
	public List<Contact> queryContact(String fragment) {
		return contactService.searchContactByNameOrCivilNameOrEmail(fragment, fragment, fragment);
	}
	
	/**
	 * Busca se existe usuario associado ao contato selecionado.
	 * Se existir, lanca exception (somente contato sem usuario podem criar usuario)
	 * Se nao, instancia objeto user e prossegue.
	 */
	public void onSelectContact() {
		user = userService.findUserByContact(contact);
		
		//RN: reseta o contact (para nao mostrar mais detalhes) e lanca ex
		if (user!=null) {
			Contact contactAux = contact;
			resetContact();
			throw new UserOfContactAlreadyExistsException(contactAux);
		}
		
		//ok go
		instanciateUser();
	}
	
	public String defineContactForUser(Contact contact) {
		this.contact = contact;
		return Navigation.SIGNUP_FORM;
	}
	
	public void saveClientUser() {
		//1.valida senhas
		validatePasswords();
		
		//2.salva contact
		contact = contactService.saveContact(contact, null);
		
		//3.salva user (novo ou reseta a senha)
		if (user==null) {
			user = new UserCB();
		}
		user.setContact( contact );
		user.setProfile( Profile.CLI );
		user.addAllowedSchool( contact.getRootSchool() );
		user.setDefaultSchool( contact.getRootSchool() );
		user.setDefaultIdiom(idiom);
		user.setPassword( pass1 );
		user = userService.saveUser( user, null);

		//4.msgs de sucesso
		JSFUtil.addInfoMessage("msg_user_saved_sucess", null);
	}
	
	
	private void validatePasswords() {
		if (pass1!=null && !pass1.equals(pass2)) {
			throw new PasswordNotConfirmedException();
		}
	}
	

	//[05 JAN 2014] substituido por autocomplete de contact	
//	/**
//	 * Busca por email um contact para criar seu usuario:
//	 * Situacoes possiveis:
//	 * 1) Email nao existe na base
//	 * 2) Email existe mas nao tem usuario
//	 * 3) Email existe e ja tem um usuario
//	 * @return
//	 */
//	public String searchByEmail() {
//		if (emailForSearching==null || emailForSearching.trim().isEmpty()) {
//			JSFUtil.addErrorMessage("msg_email_not_provided", null);
//			return Navigation.LOGIN;
//		}
//		
//		contact = contactService.findContactByEmail(emailForSearching);
//		
//		//1) email nao existe na base
//		if (contact==null) {
//			contact = new Contact();
//			contact.setEmail(emailForSearching);
//			return Navigation.SIGNUP_FORM;
//		}
//		
//		//2) Email existe mas nao tem usuario
//		user = userService.findUserByEmailUnsafely(emailForSearching);
//		if (user==null) {
//			contact.setEmail(emailForSearching);
//			return Navigation.SIGNUP_FORM;
//		}
//		
//		//3) Email existe e ja tem um usaurio
//		JSFUtil.addErrorMessage("msg_user_already_exists_with_this_email", null);
//		return Navigation.LOGIN;
//	}
	
	
//acessores...	
	
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public UserCB getUser() {
		return user;
	}
	
	public void setUser(UserCB user) {
		this.user = user;
	}
	
	public String getPass1() {
		return pass1;
	}
	
	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}
	
	public String getPass2() {
		return pass2;
	}
	
	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}
	
	public Idiom getIdiom() {
		return idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

}
