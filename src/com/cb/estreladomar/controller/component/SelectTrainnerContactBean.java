package com.cb.estreladomar.controller.component;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.service.ContactService;

@ManagedBean(name="selectTrainnerContactBean")
@ViewScoped
public class SelectTrainnerContactBean implements Serializable {
	private static final long serialVersionUID = 1176283876259082863L;

	@EJB ContactService contactService;
	
	private String contactName;
	private List<Contact> contacts;
	
	public void search() {
		if (contactName!=null && ! contactName.isEmpty()) {
			try {
				contacts = contactService.searchTrainnerContactByCivilNameOrNewName(contactName);
				
			}catch(Exception e) {
				JSFUtil.addErrorMessage(e);
			}
		}
	}


//acessores...	
	public List<Contact> getContacts() {
		return contacts;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	

}
