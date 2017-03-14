package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.MegaEventType;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.util.Constants;

/**
 * Managed Bean para configuracao de Mega Eventos
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="configMegaEventBean")
@ViewScoped
public class ConfigMegaEventBean implements Serializable {
	private static final long serialVersionUID = 1545938106505914737L;

	@EJB EventService eventService;
	
	private MegaEvent megaEvent = new MegaEvent();
	private List<MegaEvent> megaEvents;
	
	//filters
	private MegaEventType megaEventType = MegaEventType.ESTRELA;
	private Boolean megaEventActive = true;

	
	@PostConstruct void init() {
		searchMegaEvent();
	}
	
	//actions for MegaEvent
	
	public void searchMegaEvent() {
		megaEvents = eventService.searchMegaEventByTypeAndActive(megaEventType, megaEventActive);
		JSFUtil.addMessageAboutResult(megaEvents);
	}
	
	
	public void resetMegaEvent() {
		megaEvent = new MegaEvent();
		megaEvent.setType( MegaEventType.CAMPAMENTO );
		megaEvent.setAdminEmail( Constants.INSCRIPTION_ESTRELAMAR_CONTACT.getEmail() );
	}
	

	
	public void manageMegaEvent(MegaEvent selectedMegaEvent) {
		this.megaEvent = selectedMegaEvent;
	}
	
	
	public void saveMegaEvent() {
		megaEvent = eventService.saveMegaEvent(megaEvent);
		
		searchMegaEvent();
		
		JSFUtil.addInfoMessage("msg_megaevent_save_sucess", null);
	}
	
	public void defineAsCurrentMegaEvent() {
		megaEvent = eventService.defineMegaEventAsCurrent(megaEvent);
		
		searchMegaEvent();
		
		Object[] params = {megaEvent.getName()};
		JSFUtil.addInfoMessage("msg_megaevent_defined_as_current", params);
	}
	
	public void removeMegaEvent() {
		eventService.removeMegaEvent(megaEvent);
		searchMegaEvent();
		JSFUtil.addInfoMessage("msg_megaevent_remove_sucess", null);
	}

	
	
//acessores...	

	public List<MegaEvent> getMegaEvents() {
		return megaEvents;
	}
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public void setMegaEvent(MegaEvent megaEvent) {
		this.megaEvent = megaEvent;
	}
	public MegaEventType getMegaEventType() {
		return megaEventType;
	}
	public void setMegaEventType(MegaEventType megaEventType) {
		this.megaEventType = megaEventType;
	}
	public Boolean getMegaEventActive() {
		return megaEventActive;
	}
	public void setMegaEventActive(Boolean megaEventActive) {
		this.megaEventActive = megaEventActive;
	}


}
