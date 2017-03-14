package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.service.MergeService;

@ManagedBean
@ViewScoped
public class SlideContactBean implements Serializable {
	
	@EJB MergeService mergeService;
	
	private List<Contact> seletedContacts;
	
	private Long idInitial;
	private Long idFinal;
	
	public void preview() {
		seletedContacts = mergeService.pesquisarContactByRangeIds(idInitial, idFinal);
	}
	
	public void slideNow() {
		mergeService.slideContacts(idInitial, idFinal);
		
		FacesMessage fm = new FacesMessage("Slide de contatos realizado com sucesso");
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}

	public Long getIdInitial() {
		return idInitial;
	}

	public void setIdInitial(Long idInitial) {
		this.idInitial = idInitial;
	}

	public Long getIdFinal() {
		return idFinal;
	}

	public void setIdFinal(Long idFinal) {
		this.idFinal = idFinal;
	}

	public List<Contact> getSeletedContacts() {
		return seletedContacts;
	}
	
	

	private static final long serialVersionUID = 3018842547617912190L;
}
