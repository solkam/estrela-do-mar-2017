package com.cb.estreladomar.controller.mgmtreport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.DebtDTO;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.entity.enumeration.util.RegisterStatusUtil;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MgmtReportService;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Controller para Relatorio Gerencial de Dividas
 * @author Solkam
 * @since 19 MAI 2015
 */
@ManagedBean(name="mrDebtBean")
@ViewScoped
public class MgmtReportDebtBean implements Serializable {
	
	@EJB MgmtReportService reportService;
	
	@EJB EventService eventService;

	//combo
	private List<MegaEvent> comboMegaEvents;
	
	//filter
	private List<MegaEvent> filterMegaEvents;
	private List<RegisterStatus> filterRegisterStatusList;
	
	//result
	private List<DebtDTO> debtDtos;
	
	
	@PostConstruct void init() {
		populateComboMegaEvent();
		populateFilterMegaEvents();
		populateFilterRegisterStatusList();
	}

	
	private void populateComboMegaEvent() {
		comboMegaEvents = eventService.searchActiveMegaEvent();
	}

	private void populateFilterMegaEvents() {
		MegaEvent currentMegaEvent = eventService.findCurrentMegaEvent();
		filterMegaEvents = new ArrayList<MegaEvent>();
		filterMegaEvents.add( currentMegaEvent );
	}

	private void populateFilterRegisterStatusList() {
		filterRegisterStatusList = RegisterStatusUtil.getRegisterStatusListBasicFlow();
	}
	
	

	
	
	//actions
	
	public void search() {
		debtDtos = reportService.searchDebtDTOByMegaEvents(filterMegaEvents, filterRegisterStatusList);
		JSFUtil.addMessageAboutResult(debtDtos);
	}
	
	
	public String getFilenameToExport() {
		return String.format("InformeGerencialDeDudas_%s", CalendarUtil.getTodayAsFilename() );
	}
	

	//acessores...
	private static final long serialVersionUID = -6654063636874875862L;
	public List<MegaEvent> getFilterMegaEvents() {
		return filterMegaEvents;
	}
	
	public List<RegisterStatus> getFilterRegisterStatusList() {
		return filterRegisterStatusList;
	}

	public void setFilterRegisterStatusList(
			List<RegisterStatus> filterRegisterStatusList) {
		this.filterRegisterStatusList = filterRegisterStatusList;
	}


	public void setFilterMegaEvents(List<MegaEvent> filterMegaEvents) {
		this.filterMegaEvents = filterMegaEvents;
	}
	
	public List<MegaEvent> getComboMegaEvents() {
		return comboMegaEvents;
	}

	public List<DebtDTO> getDebtDtos() {
		return debtDtos;
	}
}
