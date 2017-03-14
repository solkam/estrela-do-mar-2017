package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.EventHasNoValueForParticipantNeitherForStaffException;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ModuleService;

/**
 * Controller para configuracao de evento do Estrela do Mar
 * 
 * @author Solkam
 * @since 05 JUN 2015
 */
@ManagedBean(name="configEventBean")
@ViewScoped
public class ConfigEventBean implements Serializable {


	@EJB EventService eventService;
	
	@EJB ModuleService moduleService;

	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	private Event event = new Event();
	private List<Event> events = new ArrayList<Event>();

	//combo
	private List<Module> comboModules;
	
	//filters...	
	private List<MegaEvent> activeMegaEvents;
	private MegaEvent selectedMegaEvent = new MegaEvent();
	
	private List<EventWeek> selectableEventWeeks;
	private EventWeek selectedEventWeek = new EventWeek();


	/**
	 * Ja faz uma pre-pesquisa com os primeiros elementos das combos
	 */
	@PostConstruct void init() {
		populateActiveMegaEvents();
		populateSelectedMegaEvent();
		populateEventWeeksAndSearch();
	}


	private void populateEventWeeksAndSearch() {
		selectableEventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);

		if (!selectableEventWeeks.isEmpty()) {
			selectedEventWeek = selectableEventWeeks.get(0);
			searchEvent();
		}
	}
	
	private void populateActiveMegaEvents() {
		activeMegaEvents = eventService.searchActiveMegaEvent();
	}
	
	private void populateSelectedMegaEvent() {
		selectedMegaEvent = sessionHolder.getCurrentMegaEvent();
	}


	
	// listener...

	/**
	 * Listener para mudanca na combo de mega evento.
	 * A primeira opcaoo submita null precisando fazer o if
	 */
	public void onChangeMegaEvent() {
		if (selectedMegaEvent.getId()!=null) {
			selectableEventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);
		} else {
			selectableEventWeeks.clear();
		}
		events.clear();
	}

	/**
	 * Se for evento de formacao, seleciona os 
	 * modulos da escola
	 */
	public void onChangeTypeOrSchool() {
		if (EventType.FORMATION.equals( event.getType() )) {
			comboModules = moduleService.searchModuleBySchool(event.getSchool(), Idiom.pt );
		}
	}


	//actions...
	
	public void searchEvent() {
		events = eventService.searchEventByEventWeek(selectedEventWeek);
		JSFUtil.addMessageAboutResult(events);
	}

	public void resetEvent() {
		event = new Event();
		//novo evento sera da semana selecionada...
		selectedEventWeek = eventService.findEventWeekById( selectedEventWeek.getId() );
		event.setEventWeek( selectedEventWeek );
		
		//...e tera as datas de inicio e fim iguais a da semana
		event.setBeginDate( selectedEventWeek.getBeginDate() );
		event.setEndDate(   selectedEventWeek.getEndDate() );
		
		//...ja seleciona um tipo de evento e escola
		event.setType( EventType.FORMATION );
		event.setSchool( School.LCB );
		onChangeTypeOrSchool();
	}
	

	public void manage(Event selectedEvent) {
		this.event = selectedEvent;
	}
	
	
	public void saveEvent() {
		validateEventEnableForParticipantOrStaff();
		event = eventService.saveEvent(event);
		searchEvent();
		JSFUtil.addInfoMessage("msg_event_save_sucess", null);
	}

	/**
	 * Evento deve estar habilitado ou para Staff ou Participante 
	 * ou para os dois.
	 * Nunca para nenhum
	 */
	private void validateEventEnableForParticipantOrStaff() {
		if (event.getValueParticipant()==null && event.getValueStaff()==null) {
			throw new EventHasNoValueForParticipantNeitherForStaffException(event);
		}
	}

	public void removeEvent() {
		eventService.removeEvent(event);
		searchEvent();
		JSFUtil.addInfoMessage("msg_event_remove_sucess", null);
	}
	
	
	public void saveAllEvents() {
		for (Event eventVar : events) {
			eventService.saveEvent(eventVar);
		}
		searchEvent();
		JSFUtil.addInfoMessage("msg_events_save_sucess");
	}
	

	
	//acessores..
	private static final long serialVersionUID = -8829016623734999531L;
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public List<Event> getEvents() {
		return events;
	}
	public MegaEvent getSelectedMegaEvent() {
		return selectedMegaEvent;
	}
	public void setSelectedMegaEvent(MegaEvent selectedMegaEvent) {
		this.selectedMegaEvent = selectedMegaEvent;
	}
	public EventWeek getSelectedEventWeek() {
		return selectedEventWeek;
	}
	public void setSelectedEventWeek(EventWeek selectedEventWeek) {
		this.selectedEventWeek = selectedEventWeek;
	}
	public List<MegaEvent> getSelectableMegaEvents() {
		return activeMegaEvents;
	}
	public List<EventWeek> getSelectableEventWeeks() {
		return selectableEventWeeks;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public List<Module> getComboModules() {
		return comboModules;
	}
}
