package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.RegisterDetail;

/**
 * Classe helper para relatorio de inscritos por eventos
 * 
 * @author Solkam
 * @since 14 Set 2012
 */
public class EventSummary {
	
	private Event event;
	
	private BigDecimal total;
	
	private List<RegisterDetail> registerDetails;

	public EventSummary(Event event, BigDecimal total) {
		super();
		this.event = event;
		this.total = total;
	}

	
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<RegisterDetail> getRegisterDetails() {
		return registerDetails;
	}

	public void setRegisterDetails(List<RegisterDetail> registerDetails) {
		this.registerDetails = registerDetails;
	}
	
	

}
