package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.service.EventService;

/**
 * Managed Bean para configura��o de semanas.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="configWeekBean")
@ViewScoped
public class ConfigEventWeekBean implements Serializable {

	@EJB
	EventService eventService;

	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
//combo...	
	private List<MegaEvent> activeMegaEvents;
	private MegaEvent selectedMegaEvent = new MegaEvent();

	private List<EventWeek> eventWeeks;
	private EventWeek eventWeek = new EventWeek();
	
//cria��o automatica de eventos para uma nova semana
	private Boolean flagBabysitterEvent = false;
	private Boolean flagChildEvent      = false;
	private Boolean flagYoungEvent      = false;
	private Boolean flagTeenagerEvent   = false;
	
	private Event babysitterEvent;
	private Event childEvent;
	private Event youngEvent;
	private Event teenagerEvent;


	/**
	 * Preenche a combo com os Mega Eventos ativos
	 * Havendo Mega Eventos ativos, considera o primeiro da lista como o selecionado
	 * e faz uma pre-pesquisa
	 */
	@PostConstruct void init() {
		populateActiveMegaEvents();
		populateSelectedMegaEvent();
		searchEventWeek();
	}

//populate...
	private void populateActiveMegaEvents() {
		activeMegaEvents = eventService.searchActiveMegaEvent();
	}

	private void populateSelectedMegaEvent() {
		//o mega evento corrente j� est� selecionado...
		selectedMegaEvent = sessionHolder.getCurrentMegaEvent();
	}


//actions...

	public void searchEventWeek() {
		eventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);
	}

	public void resetEventWeek() {
		eventWeek = new EventWeek();
		selectedMegaEvent = eventService.findMegaEventById( selectedMegaEvent.getId() );
		eventWeek.setMegaEvent(selectedMegaEvent);
		
		resetBabysitter();
		resetChildEvent();
		resetTeenagerEvent();
		resetYoungEvent();
	}
	
	private void resetBabysitter() {
		flagBabysitterEvent = true;
		babysitterEvent = new Event();
		babysitterEvent.setName("Bab�s");
		babysitterEvent.setValueParticipant( selectedMegaEvent.getBabysitterWeekValue() );
	}
	
	private void resetChildEvent() {
		flagChildEvent = true;
		childEvent = new Event();
		childEvent.setName("Ninos (crian�as) de 0 a 12 anos");
		childEvent.setValueParticipant( selectedMegaEvent.getChildWeekValue() );
	}
	
	private void resetTeenagerEvent() {
		flagTeenagerEvent = true;
		teenagerEvent = new Event();
		teenagerEvent.setName("Adolescentes de 12 a 18 anos");
		teenagerEvent.setValueParticipant( selectedMegaEvent.getTeenagerWeekValue() );
	}
	
	private void resetYoungEvent() {
		flagYoungEvent = true;
		youngEvent = new Event();
		youngEvent.setName("Jovens de 19 a 24 anos");
		youngEvent.setValueParticipant( selectedMegaEvent.getYoungWeekValue() );
	}
	
	

	
	
	public void saveEventWeek() {
		try {
			boolean isNewWeek = eventWeek.getId()==null;
			
			eventWeek = eventService.saveEventWeek(eventWeek);
			searchEventWeek();
			JSFUtil.addInfoMessage("msg_eventweek_save_sucess", null);
			
			handleAutomaticDependentEvents(isNewWeek);

		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	/**
	 * Para semanas novas, cria automaticamente eventos para
	 * dependentes.
	 * [02-10-2013] Tanto na cria��o quanto na edi��o podem ser 
	 * criados eventos autom�ticos
	 * @param isNewWeek
	 */
	private void handleAutomaticDependentEvents(boolean isNewWeek) {
		if (flagBabysitterEvent) {
			createBabyEvent();
		}
		if (flagChildEvent) {
			createChildEvent();
		}
		if (flagTeenagerEvent) {
			createTeenagerEvent();
		}
		if (flagYoungEvent) {
			createPartnerEvent();
		}
	}

	private void createBabyEvent() {
		babysitterEvent.setEventWeek(eventWeek);
		babysitterEvent.setBeginDate( eventWeek.getBeginDate() );
		babysitterEvent.setEndDate( eventWeek.getEndDate() );
		babysitterEvent.setType( EventType.DEPENDENT_BABY );
		babysitterEvent = eventService.saveEvent( babysitterEvent );
	}

	private void createChildEvent() {
		childEvent.setEventWeek(eventWeek);
		childEvent.setBeginDate( eventWeek.getBeginDate() );
		childEvent.setEndDate( eventWeek.getEndDate() );
		childEvent.setType( EventType.DEPENDENT_CHILD );
		childEvent = eventService.saveEvent( childEvent );
	}
	
	private void createTeenagerEvent() {
		teenagerEvent.setEventWeek(eventWeek);
		teenagerEvent.setBeginDate( eventWeek.getBeginDate() );
		teenagerEvent.setEndDate( eventWeek.getEndDate() );
		teenagerEvent.setType( EventType.DEPENDENT_TEENAGER );
		teenagerEvent = eventService.saveEvent( teenagerEvent );
	}

	private void createPartnerEvent() {
		youngEvent.setEventWeek(eventWeek);
		youngEvent.setBeginDate( eventWeek.getBeginDate() );
		youngEvent.setEndDate( eventWeek.getEndDate() );
		youngEvent.setType( EventType.DEPENDENT_PARTNER );
		youngEvent = eventService.saveEvent( youngEvent );
	}
	
	

	public void removeEventWeek() {
		try {
			eventService.removeEventWeek(eventWeek);
			searchEventWeek();
			JSFUtil.addInfoMessage("msg_eventweek_remove_sucess", null);

		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}

	
	private static final long serialVersionUID = 1062018600378242090L;
	
	public EventWeek getEventWeek() {
		return eventWeek;
	}

	public List<EventWeek> getEventWeeks() {
		return eventWeeks;
	}

	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}

	public MegaEvent getSelectedMegaEvent() {
		return selectedMegaEvent;
	}

	public void setSelectedMegaEvent(MegaEvent selectedMegaEvent) {
		this.selectedMegaEvent = selectedMegaEvent;
	}

	public List<MegaEvent> getActiveMegaEvents() {
		return activeMegaEvents;
	}

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public Boolean getFlagChildEvent() {
		return flagChildEvent;
	}

	public void setFlagChildEvent(Boolean flagChildEvent) {
		this.flagChildEvent = flagChildEvent;
	}

	public Boolean getFlagYoungEvent() {
		return flagYoungEvent;
	}

	public void setFlagYoungEvent(Boolean flagYoungEvent) {
		this.flagYoungEvent = flagYoungEvent;
	}

	public Boolean getFlagTeenagerEvent() {
		return flagTeenagerEvent;
	}

	public void setFlagTeenagerEvent(Boolean flagTeenagerEvent) {
		this.flagTeenagerEvent = flagTeenagerEvent;
	}

	public Event getChildEvent() {
		return childEvent;
	}

	public void setChildEvent(Event childEvent) {
		this.childEvent = childEvent;
	}

	public Event getYoungEvent() {
		return youngEvent;
	}

	public void setYoungEvent(Event youngEvent) {
		this.youngEvent = youngEvent;
	}

	public Event getTeenagerEvent() {
		return teenagerEvent;
	}

	public void setTeenagerEvent(Event teenagerEvent) {
		this.teenagerEvent = teenagerEvent;
	}

	public Event getBabysitterEvent() {
		return babysitterEvent;
	}

	public void setBabysitterEvent(Event babysitterEvent) {
		this.babysitterEvent = babysitterEvent;
	}

	public Boolean getFlagBabysitterEvent() {
		return flagBabysitterEvent;
	}

	public void setFlagBabysitterEvent(Boolean flagBabysitterEvent) {
		this.flagBabysitterEvent = flagBabysitterEvent;
	}

	
}
