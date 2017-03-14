package com.cb.mundo.model.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.MegaEventType;
import com.cb.mundo.model.exception.EventDuplicatedOnRegisterException;

/**
 * DAO para Eventos, MegaEventos e EventWeek
 * [31Mar2015] Refatorado e removido os metodos de Register e associados
 * 
 * @author solkam
 * @since 15 ABR 2012
 */
public class EventDAO	 {
	
	private EntityManager manager;
	
	public EventDAO(EntityManager manager) {
		this.manager = manager;
	}
	
	
	/* **********
	 * mega event	
	 ************/
	public MegaEvent saveMegaEvent(MegaEvent megaEvent) {
		return manager.merge(megaEvent);
	}
	
	
	public void removeMegaEvent(MegaEvent megaEvent) {
		manager.remove( manager.merge(megaEvent) );
	}
	
	
	public void udpateMegaEventSetCurrentIsFalse() {
		manager.createNamedQuery("MegaEvent.updateCurrentIsFalse")
			.executeUpdate();
	}
	
	
	public List<MegaEvent> searchActiveMegaEvent() {
		return manager.createNamedQuery("MegaEvent.searchActive", MegaEvent.class)
				.getResultList();
	}
	
	
	public MegaEvent findMegaEventById(Serializable id) {
		return manager.find(MegaEvent.class, id);
	}

	
	public MegaEvent findMegaEventByName(String name) {
		try {
			return manager.createNamedQuery("MegaEvent.findByName", MegaEvent.class)
					.setParameter("pName", name)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	

	
	
	/**
	 * Seleciona os mega eventos ativos que nao ainda nao foram inscritos pelo contato
	 * @param contact
	 * @return
	 */
	public List<MegaEvent> searchActivePendentMegaEventByContact(Contact contact) {
		return manager.createNamedQuery("searchActivePendentMegaEventByContact", MegaEvent.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	public MegaEvent findCurrentMegaEvent() {
			List<MegaEvent> megaEvents = manager.createNamedQuery("MegaEvent.findCurrent", MegaEvent.class).getResultList();
			
			if (megaEvents.size()>=1) {
				return megaEvents.get(0);
			}

			megaEvents = searchActiveMegaEvent();
			return megaEvents.get(0);
	}
	
	
	/**
	 * Pesquisar register pelo mega event e contact.
	 * (Usado para evitar duplicacao em Meu Inscritos)
	 * @param me
	 * @param c
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventAndContact(MegaEvent me, Contact c) {
		return manager.createNamedQuery("searchRegisterByMegaEventAndContact", Register.class)
				.setParameter("pMegaEvent", me)
				.setParameter("pContact", c)
				.getResultList();
	}
	
	
	public List<MegaEvent> searchMegaEventByTypeAndActive(MegaEventType megaEventType, Boolean megaEventActive) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<MegaEvent> criteria = builder.createQuery(MegaEvent.class);
		Root<MegaEvent> root = criteria.from(MegaEvent.class);
		
		Predicate conjunction = builder.conjunction();
		if (megaEventType!=null) {
			conjunction = builder.and(conjunction,
				builder.equal(root.<MegaEventType>get("type"), megaEventType) 
			);
		}
		
		if (megaEventActive!=null) {
			conjunction = builder.and(conjunction,
				builder.equal(root.<Boolean>get("flagActive"), megaEventActive)
			);
		}
		
		criteria.where(conjunction);
		return manager.createQuery(criteria).getResultList();
	}
	
	
	
	
	/* **********
	 * Event week
	 ************/
	
	public EventWeek saveEventWeek(EventWeek eventWeek) {
		return manager.merge( eventWeek );
	}
	
	public void removeEventWeek(EventWeek eventWeek) {
		manager.remove( manager.merge(eventWeek) );
	}

	public List<EventWeek> searchEventWeekOficialByMegaEvent(MegaEvent megaEvent) {
		List<EventWeek> weeks = manager.createNamedQuery("searchEventWeekOficialByMegaEvent", EventWeek.class)
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();

		return weeks;
	}
	
	public List<EventWeek> searchEventWeekByMegaEvent(MegaEvent megaEvent) {
		List<EventWeek> weeks = manager.createNamedQuery("EventWeek.searchByMegaEvent", EventWeek.class)
		.setParameter("pMegaEvent", megaEvent)
		.getResultList();
		
		return weeks;
	}

	
	
	public EventWeek findEventWeekById(Serializable id) {
		return manager.find(EventWeek.class, id);
	}
	
	
	
	public EventWeek findCurrentEventWeekByMegaEvent(MegaEvent megaEvent) {
		 List<EventWeek> weeks = manager.createNamedQuery("findCurrentEventWeekByMegaEvent", EventWeek.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pNow", new Date())
				.getResultList();
		 
		 if (weeks.size()>=1) {
			 return weeks.get(0);
		 }

		 return null;
	}

	
	
	/* *****
	 * Event
	 *******/
	
	public Event saveEvent(Event event) {
		return manager.merge(event);
	}
	
	public Event findEventById(Long id) {
		return manager.find(Event.class, id);
	}
	
	public void removeEvent(Event event) {
		manager.remove( manager.merge(event) );
	}
	
	
	public List<Event> searchEventByEventWeek(EventWeek ew) {
		return manager.createNamedQuery("Event.searchByEventWeek", Event.class)
				.setParameter("pEventWeek", ew)
				.getResultList();
	}

	/**
	 * Pesquisa os eventos ainda nao selecionados por uma inscricao num dado mega evento
	 * @param megaEvent
	 * @param register
	 * @return
	 */
	public List<Event> searchEventNotSelectedByEventWeekAndRegister(EventWeek week, Register register) {
		return manager.createNamedQuery("searchEventNotSelectedByEventWeekAndRegister", Event.class)
				.setParameter("pEventWeek", week)
				.setParameter("pRegister", register)
				.getResultList();
	}
	
	/**
	 * Pesquisa todos os eventos de um mega evento (para inscricoes iniciais)
	 * @param megaEvent
	 * @return
	 */
	public List<Event> searchEventByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("Event.searchByMegaEvent", Event.class)
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	/**
	 * Pesquisa todos os eventos ja selecionado em uma inscricao.
	 * @param register
	 * @return
	 */
	public List<Event> searchEventsByRegister(Register register) {
		return manager.createNamedQuery("Event.searchByRegister", Event.class)
				.setParameter("pRegister", register)
				.getResultList();
	}
	
	/**
	 * Pesquisa os futuros eventos da mesma formacao (school e module)
	 * @param actualEvent
	 * @return
	 */
	public List<Event> searchFutureEventsWithSameFormation(Event actualEvent) {
		return manager.createNamedQuery("searchFutureEventsWithSameFormation", Event.class)
				.setParameter("pSchool"   , actualEvent.getSchool() )
				.setParameter("pModule"   , actualEvent.getModule() )
				.setParameter("pBeginDate", actualEvent.getBeginDate() )
				.getResultList();
	}

	/**
	 * Busca o detalhe pelo evento e register
	 * @param event
	 * @return
	 */
	public RegisterDetail findRegisterDetailByEventAndRegister(Event event, Register register) {
		try {
			return manager.createNamedQuery("findRegisterDetailByEventAndRegister", RegisterDetail.class)
					.setParameter("pEvent", event)
					.setParameter("pRegister", register)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
			
		} catch (NonUniqueResultException e) {
			throw new EventDuplicatedOnRegisterException();
		}
	}
	
	
	
}
