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
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Profession;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.util.StringUtil;

/**
 * Managed Bean para configuracao de contatos
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="configContactBean")
@ViewScoped
public class ConfigContactBean implements Serializable {
	private static final long serialVersionUID = 4614485242720241775L;

	@EJB ContactService contactService;
	
	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	private List<Contact> contacts;
	
	private Contact contact = new Contact();
	
	private String keyword;
	
	/**
	 * propriedade auxiliar, pois contact nao tem flagTrainner.
	 */
	private boolean flagTrainner;

	/**
	 * Registros passados
	 */
	private List<Register> historicalRegisters;
	
	
	private List<Profession> selectedProfessions;
	private List<Profession> availableProfessions;
	
	private void buildResultMessage() {
		Object[] params = {contacts.size()};
		JSFUtil.addInfoMessage("msg_results_found", params);
	}
	
//initi
	@PostConstruct void init() {
		populateAvailableProfessions();
	}
	
	
	
// actions de pesquisa...

	public void searchByKeyword() {
		if (keyword==null || keyword.trim().isEmpty()) {
			JSFUtil.addErrorMessage("msg_keyword_empty", null);

		} else {
			populateContacts();
			buildResultMessage();
		}
	}
	
	public void searchByFirstCaracter(String firstCaracter) {
		keyword = firstCaracter;
		searchByKeyword();
	}
	
	private void populateContacts() {
		contacts = contactService.searchContactByCivilNameOrNameOrEmailOrCity(keyword);
	}
	
	
	
//actions para aplicar alteracao de caixa...
	
	public void applyInitCapOnName() {
		String nameOK = StringUtil.initCap( contact.getName().toLowerCase() );
		contact.setName(nameOK);
	}
	
	public void applyInitCapOnCivilName() {
		String civilNameOK = StringUtil.initCap( contact.getCivilName().toLowerCase() );
		contact.setCivilName(civilNameOK);
	}
	
	public void applyLowerCapOnEmail() {
		String emailOK = contact.getEmail().toLowerCase();
		contact.setEmail(emailOK);
	}

	
//actions contact
	
	public void resetContact() {
		contact = new Contact();
		contact.setFlagConsultant( false );
		contact.setFlagProductor( false );
		historicalRegisters = new ArrayList<>();
	}
	
	
//gerenciar contacts

	/**
	 * Action ao clicar em editar contact
	 * @param selectedContact
	 */
	public void manageContact(Contact selectedContact) {
		this.contact = selectedContact;
		
		refreshContact();
		populateSelectedProfessions();
		populateHistoricalRegisters();
	}



	
	
	public void saveContact() {
		contact = contactService.saveContact( contact, loginBean.getAuthenticatedUser() );
		refreshContact();
		populateContacts();
		JSFUtil.addInfoMessage("msg_contact_save_sucess", null);
	}
	
	public void removeContact() {
		contactService.removeContact( contact );
		populateContacts();
		JSFUtil.addInfoMessage("msg_contact_remove_sucess", null);
	}
	
	
//gerenciar produtores
	
	public void saveProductor() {
		contact = contactService.saveContact( contact, loginBean.getAuthenticatedUser() );
		refreshContact(); 
		JSFUtil.addInfoMessage("msg_productor_for_contact_save_sucess", null);
	}
	
	
//gerenciar trainners...
	
	public void saveTrainner() {
		contact = contactService.saveContact( contact, loginBean.getAuthenticatedUser() );
		refreshContact();
		JSFUtil.addInfoMessage("msg_trainner_for_contact_save_sucess", null);
	}
	
	
//gerenciar profiss�ones...

	
	public void saveProfessions() {
		contact.setProfessions(selectedProfessions);
		contact = contactService.saveContact( contact, loginBean.getAuthenticatedUser() );
		refreshContact();
		JSFUtil.addInfoMessage("msg_contact_profissions_save_sucess", null);
	}
	
//utilitarios
	private void refreshContact() {
		contact = contactService.refreshContact( contact );
	}
	
	private void populateHistoricalRegisters() {
		historicalRegisters = registerService.searchRegistersByContactLoadingCreditsAndPayments( contact );
	}

	private void populateSelectedProfessions() {
		selectedProfessions  = contactService.searchProfessionByContact( contact );
	}

	private void populateAvailableProfessions() {
		this.availableProfessions = contactService.searchActiveProfession();
	}


	
//acessores...
	public List<Contact> getContacts() {
		return contacts;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Contact getContact() {
		return contact;
	}

	public boolean isFlagTrainner() {
		return flagTrainner;
	}

	public void setFlagTrainner(boolean flagTrainner) {
		this.flagTrainner = flagTrainner;
	}

	public List<Register> getHistoricalRegisters() {
		return historicalRegisters;
	}

	public List<Profession> getAvailableProfessions() {
		return availableProfessions;
	}

	public void setAvailableProfessions(List<Profession> availableProfessions) {
		this.availableProfessions = availableProfessions;
	}

	public List<Profession> getSelectedProfessions() {
		return selectedProfessions;
	}

	public void setSelectedProfessions(List<Profession> selectedProfessions) {
		this.selectedProfessions = selectedProfessions;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	
	

}
