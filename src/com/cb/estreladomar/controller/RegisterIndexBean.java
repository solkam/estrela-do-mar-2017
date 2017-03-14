package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;

/**
 * Managed Bean para mostrar o indice das inscricoes
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="registerIndexBean")
@ViewScoped
public class RegisterIndexBean implements Serializable {
	private static final long serialVersionUID = -8513341254257006820L;
	

	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	private List<Register> doneRegisters;
	
	private List<MegaEvent> pendentMegaEvents;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;

//seleted objects:
	private Register selectedRegister;
	
	
	@PostConstruct void init() {
		Contact loggedUserContact = loginBean.getAuthenticatedUser().getContact();
		
		doneRegisters = registerService.searchActiveRegistersByContact( loggedUserContact );
		pendentMegaEvents = eventService.searchActivePendentMegaEventByContact( loggedUserContact ); 
	}

	
//acessores...	
	
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public List<Register> getDoneRegisters() {
		return doneRegisters;
	}
	public List<MegaEvent> getPendentMegaEvents() {
		return pendentMegaEvents;
	}
	public Register getSelectedRegister() {
		return selectedRegister;
	}
}
