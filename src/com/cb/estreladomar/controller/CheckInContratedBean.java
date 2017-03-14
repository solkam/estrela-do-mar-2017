package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;

/**
 * Managed Bean para realizacao de check-in de contratados
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="checkInContratedBean")
@ViewScoped
public class CheckInContratedBean implements Serializable {
	private static final long serialVersionUID = 4060688141099826105L;

	@EJB ContactService contactService;
	
	@EJB RegisterService registerService;
	
	@EJB EventService eventService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	private MegaEvent currentMegaEvent;
	
	//email usado para buscar
	private String emailForSearch;
	
	private Contact contact = new Contact();
	
	private Register register = new Register();
	private RegisterDetail detail = new RegisterDetail();

	private String note;
	
	private List<EventWeek> weeks;
	private EventWeek selectedEventWeek = new EventWeek();


	
	@PostConstruct void init() {
		currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		weeks = eventService.searchEventWeekByMegaEvent(currentMegaEvent);
	}
	
	public void onBlurEmail() {
		if (emailForSearch==null) {
			contact = new Contact();
			contact.setEmail(emailForSearch);

		} else {
			//pesquisar por email...
			contact = contactService.findContactByEmail( emailForSearch );
			if (contact==null) {
				contact = new Contact();
				contact.setEmail(emailForSearch);
			}
		}
	}
	
	
	public void reset() {
		contact = new Contact();
		selectedEventWeek = new EventWeek();
		register = new Register();
		detail = new RegisterDetail();
		note = null;
		emailForSearch = null;
	}
	
	public void confirmCheckin() {
		try {
			contact = contactService.saveContact(contact, null);
			
			register.setContact(contact);
			register.setMegaEvent(currentMegaEvent);
			register.setStatus(RegisterStatus.CHECKEDIN);
			register.setNote(note);
			
			register = registerService.saveRegister(register);
			
			detail = new RegisterDetail();
			detail.setRegister(register);
			detail.setPresence(EventPresence.CONTRATED);
			detail.setEventWeek(selectedEventWeek);
			detail.setValue( BigDecimal.ZERO );
			
			detail = registerService.saveRegisterDetail(detail);
			
			JSFUtil.addInfoMessage("msg_checkin_confirmed_sucess", null);
			
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
		
	}

	
	
	
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public MegaEvent getCurrentMegaEvent() {
		return currentMegaEvent;
	}

	public List<EventWeek> getWeeks() {
		return weeks;
	}

	public EventWeek getSelectedEventWeek() {
		return selectedEventWeek;
	}

	public void setSelectedEventWeek(EventWeek selectedEventWeek) {
		this.selectedEventWeek = selectedEventWeek;
	}

	public String getEmailForSearch() {
		return emailForSearch;
	}

	public void setEmailForSearch(String emailForSearch) {
		this.emailForSearch = emailForSearch;
	}

	public Register getRegister() {
		return register;
	}

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	

}
