package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.util.QueryUtil;

/**
 * EJB para seminarios complementarios
 * 
 * @author Solkam
 * @since 28 dez 2013
 */
@Stateless
public class SeminarComplementarService {
	
	@PersistenceContext
	private EntityManager manager;

	
	/**
	 * Pesquisa seminario complement‡rios pela semana forcando a carga das
	 * inscricoes
	 */
	public List<Event> searchSeminarComplementarEventByEventWeek(EventWeek eventWeek) {
		//1.pesquisa
		List<Event> events = manager.createNamedQuery("searchSeminarComplementarEventByEventWeek", Event.class)
				.setParameter("pEventWeek", eventWeek)
				.setParameter("pType", EventType.COMPLEMENTAR)
				.getResultList();
		
		//2.forca a carga das inscricoes
		for (Event event : events) {
			event.getDetails().size();
		}
		
		return events;
	}


	
	public Event reloadSeminarComplementarEvent(Event event) {
		//traz para gerenciada
		event = manager.find(Event.class, event.getId());
		//forca carga dos details
		event.getDetails().size();
		
		return event;
	}
	
	
	public List<Register> searchRegisterByMegaEventAndContactName(MegaEvent megaEvent, String contactName) {
		return manager.createNamedQuery("searchRegisterByMegaEventAndContactName", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pContactName", QueryUtil.toLikeMatchModeANY(contactName))
				.getResultList();
	}
	
	
	public RegisterDetail saveRegisterDetail(RegisterDetail detail) {
		return manager.merge( detail );
	}
	
	
	public void removeRegisterDetail(RegisterDetail detail) {
		manager.remove( manager.merge(detail) );
	}
	

	
	
}
