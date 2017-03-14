package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.service.MgmtReportService;
import com.cb.mundo.model.util.NumberUtil;

@ViewScoped
public class BalanceMegaEventByEventDTO {
	
	private EventWeek eventWeek;
	private Event event;	
	private EventPresence presence;
	private BigDecimal total;
	private BigDecimal totalRealPaid;
	private BigDecimal totalVirtualPaid;
	private Long countContact;
	
	
	private List<BalanceMegaEventByEventDTO> list;
	private List<BalanceMegaEventMethodByEventDTO> listTotalMethod;
	private List<BalanceMegaEventPresenceByEventDTO> listTotalPresence;
	
	
	@EJB MgmtReportService service;
	
	
	// extra fields
    public BigDecimal getTotalPendent(){
		
		return NumberUtil.subtract(NumberUtil.subtract(total, totalRealPaid),totalVirtualPaid);
	}

  //filters...
	private MegaEvent selectedMegaEvent = new MegaEvent();

    
 
   public BalanceMegaEventByEventDTO(EventWeek varEventWeek, Event varEvent, EventPresence varPresence,
			BigDecimal varTotal, BigDecimal varTotalRealPaid, BigDecimal varTotalVirtualPaid, Long varCountContact) {
		super();
		this.eventWeek = varEventWeek;
		this.event = varEvent;
		this.presence = varPresence;
		this.total = varTotal;
		this.totalRealPaid = varTotalRealPaid;
		this.totalVirtualPaid = varTotalVirtualPaid;
		this.countContact = varCountContact;
	}


	//access
	public List<BalanceMegaEventMethodByEventDTO> getListTotalMethod() {
	return listTotalMethod;
	}
	
	public void setListTotalMethod(
		List<BalanceMegaEventMethodByEventDTO> listTotalMethod) {
	this.listTotalMethod = listTotalMethod;
	}
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
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getTotalRealPaid() {
		return totalRealPaid;
	}
	
	public void setTotalRealPaid(BigDecimal totalRealPaid) {
		this.totalRealPaid = totalRealPaid;
	}
	
	public BigDecimal getTotalVirtualPaid() {
		return totalVirtualPaid;
	}
	
	public void setTotalVirtualPaid(BigDecimal totalVirtualPaid) {
		this.totalVirtualPaid = totalVirtualPaid;
	}
	
	public Long getCountContact() {
		return countContact;
	}
	
	public void setCountContact(Long countContact) {
		this.countContact = countContact;
	}

	public List<BalanceMegaEventByEventDTO> getList() {
		return list;
	}

	public void setList(List<BalanceMegaEventByEventDTO> list) {
		this.list = list;
	}
	public List<BalanceMegaEventPresenceByEventDTO> getListTotalPresence() {
		return listTotalPresence;
	}


	public void setListTotalPresence(
			List<BalanceMegaEventPresenceByEventDTO> listTotalPresence) {
		this.listTotalPresence = listTotalPresence;
	}

	public MegaEvent getSelectedMegaEvent() {
		return selectedMegaEvent;
	}

	public void setSelectedMegaEvent(MegaEvent selectedMegaEvent) {
		this.selectedMegaEvent = selectedMegaEvent;
	}
	public EventPresence getPresence() {
		return presence;
	}
	public void setPresence(EventPresence presence) {
		this.presence = presence;
	}
	
	
	
	
}
