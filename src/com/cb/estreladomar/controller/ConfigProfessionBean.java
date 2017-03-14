package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Profession;
import com.cb.mundo.model.service.ContactService;

/**
 * ManagedBean para configuracao de profissoes 
 * de contatos
 * 
 * @author Solkam
 * @since 31 JUL 2013
 */
@ManagedBean(name="configProfessionBean")
@ViewScoped
public class ConfigProfessionBean implements Serializable {

	@EJB ContactService contactService;
	
	private List<Profession> professions;
	private Profession profession;
	
	@PostConstruct void init() {
		populateProfessions();
	}

	private void populateProfessions() {
		professions = contactService.searchProfession();
	}
	

	
	private static final long serialVersionUID = 3509745842330993059L;

//action
	public void resetProfession() {
		profession = new Profession();
	}
	
	public void saveProfession() {
		try {
			profession = contactService.saveProfession(profession);
			populateProfessions();

			JSFUtil.addInfoMessage("msg_profession_save_sucess", null);
			
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	
	
//acessores...	
	public Profession getProfession() {
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	public List<Profession> getProfessions() {
		return professions;
	}
	
	
	

}
