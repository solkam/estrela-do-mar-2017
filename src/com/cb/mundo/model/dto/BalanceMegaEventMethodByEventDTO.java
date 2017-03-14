package com.cb.mundo.model.dto;

import java.math.BigDecimal;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;


public class BalanceMegaEventMethodByEventDTO {

	private EventWeek eventWeek;
	private Event event;
	private EventPresence presence;
	private MegaEventPaymentMethod method;	
	private BigDecimal totalPaid;

		
	public BalanceMegaEventMethodByEventDTO(EventWeek varEventWeek, Event varEvent, EventPresence varPresence,
			MegaEventPaymentMethod varMethod, 	BigDecimal varTotalPaid) {
		super();
		this.eventWeek = varEventWeek;
		this.event = varEvent;
		this.presence = varPresence;
		this.method = varMethod;
		this.totalPaid = varTotalPaid;
	}

   
	


/*
 * accesos 
 */
	public EventWeek getEventWeek() {
		return eventWeek;
	}
	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public BigDecimal getTotalPaid() {
		return totalPaid;
	}
	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid = totalPaid;
	}
	
	public MegaEventPaymentMethod getMethod() {
		return method;
	}
	public void setMethod(MegaEventPaymentMethod method) {
		this.method = method;
	}
	public EventPresence getPresence() {
		return presence;
	}
	public void setPresence(EventPresence presence) {
		this.presence = presence;
	}

	
}
