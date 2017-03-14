package com.cb.estreladomar.controller.holder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.util.CalendarUtil;


/**
 * ManagedBean especial que guarda as informacoes de sessao.
 * - Mega Evento currente (ou de sessao)
 * 
 * @author Solkam
 * @since 02 JUN 2013
 */
@ManagedBean(name="sessionHolder")
@SessionScoped
public class SessionHolder implements Serializable {
	private static final long serialVersionUID = -573636959261514487L;

	@EJB EventService eventService;
	
	private MegaEvent currentMegaEvent;

	@PostConstruct void init() {
		populateCurrentMegaEvent();
	}
	
//populates...	
	/**
	 * Inicialmente o mega evento da sessao sera o que esta 
	 * marcado na base de dados
	 */
	private void populateCurrentMegaEvent() {
		this.currentMegaEvent = eventService.findCurrentMegaEvent();
	}
	
//acessores...	
	public void setSessionMegaEventById(Long megaEventId) {
		this.currentMegaEvent = eventService.findMegaEventById(megaEventId);
	}

	public MegaEvent getCurrentMegaEvent() {
		return currentMegaEvent;
	}

	public void setCurrentMegaEvent(MegaEvent currentMegaEvent) {
		this.currentMegaEvent = currentMegaEvent;
	}
	
	public String getTimeZone() {
		return Calendar.getInstance().getTimeZone().getID();
	}
	
}
