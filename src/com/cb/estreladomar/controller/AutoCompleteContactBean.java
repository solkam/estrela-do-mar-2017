package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.service.ContactService;

/**
 * Managed Bean para o componente de auto-complete de contatos
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="autoCompleteContactBean")
@ViewScoped
public class AutoCompleteContactBean implements Serializable {
	private static final long serialVersionUID = 5057164497982600852L;


	@EJB ContactService contactService;
	
	
	public List<Contact> queryContact(String name) {
		List<Contact> result = contactService.searchContactByNameOrCivilNameOrEmail(name, name, null);
		return result;
	}
	
	public List<Contact> queryProductorContact(String fragment) {
		return contactService.searchProductorContactByCivilNameOrNewName(fragment);
	}
	
	public List<Contact> queryTrainnerContact(String fragment) {
		return contactService.searchTrainnerContactByCivilNameOrNewName(fragment);
	}
	
}
