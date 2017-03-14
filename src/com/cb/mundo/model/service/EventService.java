package com.cb.mundo.model.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.EventDAO;
import com.cb.mundo.model.dao.RegisterDAO;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.entity.enumeration.MegaEventType;
import com.cb.mundo.model.entity.enumeration.util.EventTypeUtil;
import com.cb.mundo.model.exception.EventHasRegisterDetailException;
import com.cb.mundo.model.exception.EventWeekHasEventException;
import com.cb.mundo.model.exception.EventWeekHasRegisterDetailException;
import com.cb.mundo.model.exception.MegaEventHasEventWeeksException;
import com.cb.mundo.model.exception.MegaEventHasRegisterException;
import com.cb.mundo.model.exception.MegaEventNameAlreadyExistsException;

/**
 * Servicos de Negocio para MegaEvento, EventWeek e Event.
 * [Refactorado em 31MAR2015 para remover os metodos de Register]
 * 
 * @author Solkam
 * @since 17 ABR 2012
 */
@Stateless
public class EventService {
	
	@PersistenceContext EntityManager manager;
	
	
	private EventDAO eventDAO;
	
	private RegisterDAO registerDAO;

	@PostConstruct void init() {
		eventDAO = new EventDAO(manager);
		registerDAO = new RegisterDAO(manager);
	}
	

	/* **********
	 * Mega Event
	 ************/
	
	public MegaEvent saveMegaEvent(MegaEvent megaEvent) {
		verifyIfMegaEventNameAlreadyExists(megaEvent);
		
		return eventDAO.saveMegaEvent(megaEvent);
	}
	
	private void verifyIfMegaEventNameAlreadyExists(MegaEvent megaEvent) {
		MegaEvent existingMegaEvent = eventDAO.findMegaEventByName( megaEvent.getName() );
		if (existingMegaEvent!=null && !megaEvent.equals(existingMegaEvent)) {
			throw new MegaEventNameAlreadyExistsException();
		}
	}

	
	
	public List<MegaEvent> searchActiveMegaEvent() {
		return eventDAO.searchActiveMegaEvent();
	}
	
	
	
	public List<Register> searchRegistersByMegaEventAndContact(MegaEvent me, Contact c) {
		return eventDAO.searchRegisterByMegaEventAndContact(me, c);
	}
	
	
	
	public MegaEvent findMegaEventById(Long id) {
		return eventDAO.findMegaEventById(id);
	}
	
	
	
	public MegaEvent findCurrentMegaEvent() {
		return eventDAO.findCurrentMegaEvent();
	}

	
	public List<MegaEvent> searchActivePendentMegaEventByContact(Contact contact) {
		return eventDAO.searchActivePendentMegaEventByContact(contact);
	}

	
	public List<MegaEvent> searchMegaEventByTypeAndActive(MegaEventType megaEventType, Boolean megaEventActive) {
		return eventDAO.searchMegaEventByTypeAndActive(megaEventType, megaEventActive);
	}

	
	public void removeMegaEvent(MegaEvent megaEvent) {
		verifyIfMegaEventHasWeeks(megaEvent);
		verifyIfMegaEventHasRegisters(megaEvent);
		
		eventDAO.removeMegaEvent(megaEvent);
	}
	
	private void verifyIfMegaEventHasRegisters(MegaEvent megaEvent) {
		if (!registerDAO.searchRegistersByMegaEvent(megaEvent).isEmpty()) {
			throw new MegaEventHasRegisterException();
		}
	}
	
	private void verifyIfMegaEventHasWeeks(MegaEvent megaEvent) {
		if (!eventDAO.searchEventWeekByMegaEvent(megaEvent).isEmpty()) {
			throw new MegaEventHasEventWeeksException();
		}
	}
	
	/**
	 * Define o Mega Event como corrente. Para tal, atualiza todos os registros como nao-correntes,
	 * e entao atualiza o mega evento como corrente
	 * @param megaEvent
	 * @return mega eventa atualizado como corrente
	 */
	public MegaEvent defineMegaEventAsCurrent(MegaEvent megaEvent) {
		eventDAO.udpateMegaEventSetCurrentIsFalse();
		
		megaEvent.setFlagCurrent(true);
		return eventDAO.saveMegaEvent(megaEvent);
	}
	

	/**
	 * Pesquisa pelos mega eventos que estã acontecendo na data
	 * @param date
	 * @return
	 */
	public List<MegaEvent> searchActiveMegaEventByDate(Date date) {
		return manager.createNamedQuery("searchActiveMegaEventByDate", MegaEvent.class)
				.setParameter("pDate", date)
				.getResultList();
	}
	
	
	
	/* **********
	 * Event Week
	 ************/
	
	public EventWeek saveEventWeek(EventWeek eventWeek) {
		return eventDAO.saveEventWeek(eventWeek);
	}
	
	/**
	 * Pesquisa semanas pelo Register de maneira EAGER, ou seja,
	 * buscando os eventos da semana mas filtrando os ainda nao selecionados
	 * 
	 * @param Register a ser considerado nos eventos disponiveis
	 * @return lista de EventWeek com os eventos disponiveis ja carregados
	 */
	
	public List<EventWeek> searchEventWeekAndAvailableEventsByRegister(Register register) {
		MegaEvent megaEvent = register.getMegaEvent();
		
		List<EventWeek> eventWeeks = eventDAO.searchEventWeekOficialByMegaEvent(megaEvent);
		
		for (EventWeek eventWeek : eventWeeks) {
			List<Event> events = eventDAO.searchEventNotSelectedByEventWeekAndRegister(eventWeek, register);
			eventWeek.setEvents(events);
		}
		return eventWeeks;
	}
	
	/**
	 * Pesquisa as semanas de um MegaEvento e, para cada semana, carrega todos os eventos.
	 * Este metodo eh bem parecido com o 'searchEventWeekAndAvailableEventsByRegister' com 
	 * a diferenca que carregado todos os eventos e nao somente os disponiveis.
	 * eh um carregamento EAGER 
	 *  
	 * @param megaEvent
	 * @return lista com semanas e todos os eventos de cada uma
	 */
	
	public List<EventWeek> searchEventWeekAndAllEventsByMegaEvent(MegaEvent megaEvent) {
		List<EventWeek> eventWeeks = eventDAO.searchEventWeekByMegaEvent(megaEvent);
		for (EventWeek eventWeek : eventWeeks) {
			eventWeek.getEvents().size();//forca o eager loading
		}
		return eventWeeks;
	}
	
	
	public List<EventWeek> searchEventWeekByMegaEvent(MegaEvent megaEvent) {
		return eventDAO.searchEventWeekByMegaEvent(megaEvent);
	}
	
	
	
	public EventWeek findEventWeekById(Long id) {
		return eventDAO.findEventWeekById(id);
	}

	
	
	public EventWeek findCurrentEventWeekByMegaEvent(MegaEvent megaEvent) {
		return eventDAO.findCurrentEventWeekByMegaEvent(megaEvent);
	}

	
	public void removeEventWeek(EventWeek eventWeek) {
		verifyIfEventWeekHasEvents(eventWeek);
		verifyIfEventWeeksHasRegisterDetails(eventWeek);
		
		eventDAO.removeEventWeek(eventWeek);
	}
	
	private void verifyIfEventWeekHasEvents(EventWeek eventWeek) {
		if (!eventDAO.searchEventByEventWeek(eventWeek).isEmpty()) {
			throw new EventWeekHasEventException();
		}
	}
	
	private void verifyIfEventWeeksHasRegisterDetails(EventWeek eventWeek) {
		if (!registerDAO.searchRegisterDetailByEventWeek(eventWeek).isEmpty()) {
			throw new EventWeekHasRegisterDetailException();
		}
	}
	
	

	/* *****
	 * Event
	 *******/
	
	public Event saveEvent(Event event) {
		return eventDAO.saveEvent(event);
	}
        
	public List<Event> searchEventByEventWeek(EventWeek ew) {
		return eventDAO.searchEventByEventWeek(ew);
	}
        
	public Event findEventById(Long id) {
		return eventDAO.findEventById(id);
	}
	
	
	public List<Event> searchFutureEventsWithSameFormation(Event actualEvent) {
		return eventDAO.searchFutureEventsWithSameFormation(actualEvent);
	}

	
	public void removeEvent(Event event) {
		verifyIfEventHasRegisterDetails(event);
		
		eventDAO.removeEvent(event);
	}
	
	private void verifyIfEventHasRegisterDetails(Event event) {
		if (!registerDAO.searchRegisterDetailByEvent(event).isEmpty()) {
			throw new EventHasRegisterDetailException();
		}
	}
	
	
	public List<Event> searchEventByMegaEvent(MegaEvent megaEvent) {
		return eventDAO.searchEventByMegaEvent(megaEvent);
	}
	
	
	/**
	 * Pesquisa todos os eventos de um mega evento e cujos tipos são de dependentes
	 * @param megaEvent
	 * @return
	 */
	public List<Event> searchDependentEventByMegaEvent(MegaEvent megaEvent) {
		List<EventType> typeDependents = EventTypeUtil.getEventTypeDependents();
		
		return manager.createNamedQuery("searchEventByMegaEventAndEventTypes", Event.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pEventTypes", typeDependents)
				.getResultList();
	}

	
	

	
	

       
        
}

	
