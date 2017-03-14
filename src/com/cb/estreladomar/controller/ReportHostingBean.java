package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.ReportHostingByOccupantDTO;
import com.cb.mundo.model.dto.ReportHostingByRoomDTO;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.service.HostingService;


/**
 * Controller para o relatorio de quartos
 * 
 * @author Solkam
 * @since 12 MAI 2014
 */
@ManagedBean(name="reportHostingBean")
@ViewScoped
public class ReportHostingBean implements Serializable {
	

	@EJB
	private HostingService hostingService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;

//filtro	
	private MegaEvent currentMegaEvent;
	private Register filterRegister;
	
	//aba 1
	private List<ReportHostingByRoomDTO> hostingByRoomDtos;
	
	//aba 2
	private List<ReportHostingByOccupantDTO> hostingByOccupantDtos;
	
	
	@PostConstruct void init() {
		populateCurrentMegaEvent();
		search();
	}
	
	private void populateCurrentMegaEvent() {
		currentMegaEvent = sessionHolder.getCurrentMegaEvent();
	}



//action...
	public List<Register> queryRegister(String fragment) {
		return hostingService.searchRegisterByMegaEventContactNameOrCivilNameOrCity(currentMegaEvent, fragment, fragment, fragment);
	}

	
	public void search() {
		hostingByRoomDtos = hostingService.searchReportHostingHelperByMegaEventAndRegister(currentMegaEvent, filterRegister);
		JSFUtil.addMessageAboutResult(hostingByRoomDtos);
		
		hostingByOccupantDtos = hostingService.searchReportHostingByOccupantDtosByMegaEvent(currentMegaEvent);
		JSFUtil.addMessageAboutResult( hostingByOccupantDtos );
	}
	
	
	
	
//acessores...	
	private static final long serialVersionUID = 2957605696884676018L;
	
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public MegaEvent getCurrentMegaEvent() {
		return currentMegaEvent;
	}
	public Register getFilterRegister() {
		return filterRegister;
	}
	public void setFilterRegister(Register filterRegister) {
		this.filterRegister = filterRegister;
	}

	public List<ReportHostingByRoomDTO> getHostingByRoomDtos() {
		return hostingByRoomDtos;
	}

	public List<ReportHostingByOccupantDTO> getHostingByOccupantDtos() {
		return hostingByOccupantDtos;
	}

	
	
}
