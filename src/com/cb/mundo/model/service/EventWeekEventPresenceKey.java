package com.cb.mundo.model.service;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.enumeration.EventPresence;

public class EventWeekEventPresenceKey {

	
	private final EventWeek eventWeek;
	private final Event event;
	private final EventPresence eventPresence;
	
	
	
	public EventWeekEventPresenceKey(EventWeek eventWeek, Event event,
			EventPresence eventPresence) {
		super();
		this.eventWeek = eventWeek;
		this.event = event;
		this.eventPresence = eventPresence;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result
				+ ((eventPresence == null) ? 0 : eventPresence.hashCode());
		result = prime * result
				+ ((eventWeek == null) ? 0 : eventWeek.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventWeekEventPresenceKey other = (EventWeekEventPresenceKey) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (eventPresence != other.eventPresence)
			return false;
		if (eventWeek == null) {
			if (other.eventWeek != null)
				return false;
		} else if (!eventWeek.equals(other.eventWeek))
			return false;
		return true;
	}


	
	public EventWeek getEventWeek() {
		return eventWeek;
	}


	public Event getEvent() {
		return event;
	}


	public EventPresence getEventPresence() {
		return eventPresence;
	}
	
	
	
	
}
