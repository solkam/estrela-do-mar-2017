package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.Transport;
import com.cb.mundo.model.entity.enumeration.TransportDirection;
import com.cb.mundo.model.service.TransportService;

@ManagedBean(name="transportBean")
@ViewScoped
public class TransportBean implements Serializable {
	private static final long serialVersionUID = -3613270138299713840L;

	@EJB TransportService transportService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	private TransportDirection filterTransportDirection = TransportDirection.INPUT;
	
	private List<Transport> transports;
	
	private Transport transport;
	
	private List<Register> registersWithoutTransport;

	@PostConstruct void init() {
		search();
	}
	
	public void search() {
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		transports = transportService.searchTransportByMegaEventAndDirection(currentMegaEvent, filterTransportDirection);
	}
	
	private void populateRegistersWithoutTransport() {
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		registersWithoutTransport = transportService.searchRegisterWithoutTransportByMegaEventAndDirection(
				currentMegaEvent, 
				filterTransportDirection);
	}
	
	public void reset() {
		transport = new Transport();
		transport.setDate( new Date() );
		transport.setDirection(filterTransportDirection);
	}
	
	public void save() {
		try {
			transport.setMegaEvent( sessionHolder.getCurrentMegaEvent() );
			transport = transportService.saveTransport(transport);
			
			refreshTransport();
			populateRegistersWithoutTransport();
			search();

			JSFUtil.addInfoMessage("msg_transport_save_sucess");
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage( e );
		}
	}
	
	public void remove() {
		try {
			transportService.removeTransport(transport);
			
			search();
		
			JSFUtil.addInfoMessage("msg_transport_remove_sucess");
		
		} catch (Exception e) {
			JSFUtil.addErrorMessage( e );
		}
	}
	
	public void manageTransport(Transport selectedTransport) {
		this.transport = selectedTransport;
		refreshTransport();
		populateRegistersWithoutTransport();
	}
	
	public void addRegisterToTransport(Register selectedRegister) {
		transport.getRegisters().add( selectedRegister );
		transport = transportService.saveTransport(transport);
		
		refreshTransport();
		populateRegistersWithoutTransport();
	}
	
	public void removeRegisterFromTransport(Register selectedRegister) {
		transport.getRegisters().remove( selectedRegister );
		transport = transportService.saveTransport(transport);
		
		refreshTransport();
		populateRegistersWithoutTransport();
	}
	
//utilitario	
	private void refreshTransport() {
		transport = transportService.refreshTransport(transport);
	}

	
	
	
	public TransportDirection getFilterTransportDirection() {
		return filterTransportDirection;
	}
	public void setFilterTransportDirection(TransportDirection filterTransportDirection) {
		this.filterTransportDirection = filterTransportDirection;
	}
	public List<Transport> getTransports() {
		return transports;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public Transport getTransport() {
		return transport;
	}
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	public List<Register> getRegistersWithoutTransport() {
		return registersWithoutTransport;
	}
	
	
	
}
