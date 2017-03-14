package com.cb.estreladomar.controller.mgmtreport;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MgmtReportService;

/**
 * Controller para o relatorio de novos participantes no mega evento
 * @author Solkam
 * @since 22 ABR 2015
 */
@ManagedBean(name="mrNewParticipantMEBean")
@ViewScoped
public class MgmtReportNewParticipantInMegaEventBean implements Serializable {

	@EJB EventService eventService;
	
	@EJB MgmtReportService service;
	
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	//combo
	private List<MegaEvent> comboMegaEvents;

	//filter
	private MegaEvent filterMegaEvent;
	
	//resultado
	private List<Register> registers;
	
	//charts
	private PieChartModel chartModelByCountry;
	private PieChartModel chartModelBySchool;
	private PieChartModel chartModelByGender;
	private PieChartModel chartModelByMaturity;
	
	
	//init...
	
	@PostConstruct void init() {
		populateComboMegaEvents();
		populateFilterMegaEvent();
	}
	
	private void populateComboMegaEvents() {
		comboMegaEvents = eventService.searchActiveMegaEvent();
	}
	
	private void populateFilterMegaEvent() {
		filterMegaEvent = sessionHolder.getCurrentMegaEvent();
	}

	
	//action...
	
	public void search() {
		registers = service.searchNewRegisterByMegaEvent(filterMegaEvent);
		JSFUtil.addMessageAboutResult(registers);
		
		buildAllChartModels();
	}
	
	
	//utils...
	
	private void buildAllChartModels() {
		Map<String, Number> mapByCountry = new TreeMap<>();
		Map<String, Number> mapBySchool = new TreeMap<>();
		Map<String, Number> mapByGender = new TreeMap<>();
		Map<String, Number> mapByMaturity = new TreeMap<>();
		
		Number total = 0;
		for (Register registerVar : registers) {
			//by country
			String countryKey = extractCountryKey(registerVar);
			total = mapByCountry.get( countryKey );
			mapByCountry.put( countryKey, increase(total) );
			
			//by school
			String schoolKey = extractSchoolKey(registerVar);
			total = mapBySchool.get( schoolKey );
			mapBySchool.put( schoolKey, increase(total) );
			
			//by gender
			String genderKey = extractGenderKey(registerVar);
			total = mapByGender.get( genderKey );
			mapByGender.put( genderKey, increase(total) );
			
			//by maturity
			String maturityKey = extractMaturityKey(registerVar);
			total = mapByMaturity.get( maturityKey );
			mapByMaturity.put(maturityKey, increase(total) );
		}
		
		chartModelByCountry = new PieChartModel(mapByCountry);
		chartModelBySchool = new PieChartModel(mapBySchool);
		chartModelByGender = new PieChartModel(mapByGender);
		chartModelByMaturity = new PieChartModel(mapByMaturity);
	}

	
	
	private String extractGenderKey(Register registerVar) {
		if (null==registerVar.getContact().getGender()) {
			return "???";
		} else {
			return I18nUtil.getSimpleMessage( registerVar.getContact().getGender().getKey() );
		}
	}

	private String extractCountryKey(Register registerVar) {
		if (null==registerVar.getContact().getCountry()) {
			return "???";
		} else {
			return registerVar.getContact().getCountry().toUpperCase() ;
		}
	}
	
	private String extractSchoolKey(Register registerVar) {
		if (null==registerVar.getContact().getRootSchool()) {
			return "???";
		} else {
			return I18nUtil.getSimpleMessage( registerVar.getContact().getRootSchool().getKey() );
		}
	}
	
	private String extractMaturityKey(Register registerVar) {
		return I18nUtil.getSimpleMessage( registerVar.getContact().getMaturity().getKey() );
	}
	
	
	private Number increase(Number total) {
		if (null==total) {
			return 1;
		} else {
			return total.intValue() + 1;
		}
	}


	//acessores...
	private static final long serialVersionUID = 4541266327270991077L;
	public List<MegaEvent> getComboMegaEvents() {
		return comboMegaEvents;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public MegaEvent getFilterMegaEvent() {
		return filterMegaEvent;
	}
	public void setFilterMegaEvent(MegaEvent filterMegaEvent) {
		this.filterMegaEvent = filterMegaEvent;
	}
	public List<Register> getRegisters() {
		return registers;
	}

	public PieChartModel getChartModelByCountry() {
		return chartModelByCountry;
	}

	public PieChartModel getChartModelBySchool() {
		return chartModelBySchool;
	}

	public PieChartModel getChartModelByGender() {
		return chartModelByGender;
	}

	public PieChartModel getChartModelByMaturity() {
		return chartModelByMaturity;
	}
	
	
}