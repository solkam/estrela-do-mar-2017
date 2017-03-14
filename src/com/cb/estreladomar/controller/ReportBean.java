package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * Managed Bean para gestao de relatorios.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportBean")
@ViewScoped
public class ReportBean implements Serializable {
	private static final long serialVersionUID = -7052551208947407120L;

	@EJB EventService eventService;
	
	@EJB ReportMegaEventService reportService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
//filtros de pesquisa
	private MegaEvent currentMegaEvent;
	private List<MegaEvent> megaEvents;

	private List<EventWeek> currentEventWeeks;
	

	@PostConstruct
	protected void init() {
		populateCurrentMegaEventFromHolder();
		populateMegaEvents();
		populateEventWeeks();
	}
	

	private void populateCurrentMegaEventFromHolder() {
		this.currentMegaEvent = sessionHolder.getCurrentMegaEvent();
	}
	
	private void populateMegaEvents() {
		megaEvents = eventService.searchActiveMegaEvent(); 
	}
	
	private void populateEventWeeks() {
		currentEventWeeks = eventService.searchEventWeekByMegaEvent( currentMegaEvent );
	}
	
	
	
	
//listener	
	public void onChangeMegaEvent() {
		currentMegaEvent = eventService.findMegaEventById(currentMegaEvent.getId());
	}
	
//acessores...	
	public List<MegaEvent> getMegaEvents() {
		return megaEvents;
	}
	

	public MegaEvent getCurrentMegaEvent() {
		return currentMegaEvent;
	}


	public List<EventWeek> getCurrentEventWeeks() {
		return currentEventWeeks;
	}


	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}



	
}
