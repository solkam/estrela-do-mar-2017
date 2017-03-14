package com.cb.mundo.model.dto;

import java.math.BigDecimal;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.enumeration.EventPresence;

public class BalanceMegaEventPresenceByEventDTO {

	private EventWeek eventWeek;
	private Event event;
	private EventPresence presence;
	private BigDecimal total;
	private Long countContact;
		
	public BalanceMegaEventPresenceByEventDTO(EventWeek varEventWeek, Event varEvent, EventPresence varPresence,	BigDecimal varTotal, Long varCountContact) {
		super();
		this.eventWeek = varEventWeek;
		this.event = varEvent;
		this.presence = varPresence;
		this.total = varTotal;
		this.countContact = varCountContact;
	}

	//access
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

	public EventPresence getPresence() {
		return presence;
	}

	public void setPresence(EventPresence presence) {
		this.presence = presence;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Long getCountContact() {
		return countContact;
	}

	public void setCountContact(Long countContact) {
		this.countContact = countContact;
	}

}
