package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.reportexporter.ReportMedicalFormExcelExporter;
import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.MedicalAnswerDTO;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.entity.enumeration.util.RegisterStatusUtil;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Controller para o relatorio de fichas medicas
 * @author Solkam
 * @since 27 ABR 2015
 */
@ManagedBean(name="rMedicalFormBean")
@ViewScoped
public class ReportMedicalFormBean implements Serializable {
	
	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	@EJB MedicalFormService medicalFormService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	//combo
	private List<MegaEvent> comboMegaEvents;
	private List<EventWeek> comboWeeks;
	private List<Event> comboEvents;
	private List<RegisterStatus> comboStatus;
	
	private MegaEvent filterMegaEvent;
	private EventWeek filterWeek;
	private List<Event> filterEvents;
	private List<RegisterStatus> filterStatusList;
	private Boolean filterPhysicalLimitationOnly = true;
	private Register filterRegister;
	
	//result
	private List<MedicalAnswerDTO> dtos;
	
	
	
	@PostConstruct void init() {
		populateComboMegaEvents();
		initFilterMegaEvent();

		populateComboWeeks();
		initFilterWeek();
		
		populatateComboEvents();
		populateFilterEvents();
		
		populateComboStatus();
		populateFilterStatusList();
	}

	private void populateComboMegaEvents() {
		comboMegaEvents = eventService.searchActiveMegaEvent();
	}
	
	private void initFilterMegaEvent() {
		filterMegaEvent = sessionHolder.getCurrentMegaEvent();
	}
	
	
	private void populateComboWeeks() {
		comboWeeks = eventService.searchEventWeekByMegaEvent(filterMegaEvent);
	}
	
	private void initFilterWeek() {
		if (!comboWeeks.isEmpty()) {
			filterWeek = comboWeeks.get(0);
		}
	}

	
	private void populatateComboEvents() {
		if (filterWeek!=null) {
			comboEvents = eventService.searchEventByEventWeek(filterWeek);
		}
	}
	
	private void populateFilterEvents() {
		filterEvents = new ArrayList<Event>();
	}


	private void populateComboStatus() {
		comboStatus = RegisterStatusUtil.getRegisterStatusListBasicFlow();
	}

	private void populateFilterStatusList() {
		filterStatusList = new ArrayList<RegisterStatus>();
		filterStatusList.add( RegisterStatus.REGISTERED );
		filterStatusList.add( RegisterStatus.CHECKEDIN );
	}
	
	
	
	//listeners
	public void onChangeMegaEvent() {
		populateComboWeeks();
		initFilterWeek();
		
		populatateComboEvents();
		populateFilterEvents();
	}
	
	public void onChangeWeek() {
		populatateComboEvents();
		populateFilterEvents();
	}
	
	
	//auto complete	
	/**
	 * Pesquisa Register para o auto complete (outras inscricoes)
	 * @param fragment
	 * @return
	 */
	public List<Register> completeRegister(String fragment) {
		return medicalFormService.searchRegisterByMegaEventContactNameOrCivilNameOrCity(filterMegaEvent, fragment, fragment, fragment);
	}
	
	
	//actions..
	public void search() {
		dtos = new ArrayList<MedicalAnswerDTO>();
		
		handleFilterEventsBeforeSearch();
		handleFilterStatusListBeforeSearch();

		//1.pesquisa todos os register da lista de evento e da lista de status...
		List<Register> registers = null;
		if (filterRegister==null) {
			registers = registerService.searchRegisterByListOfEventAndListOfStatus(filterEvents, filterStatusList);
		} else {
			registers = new ArrayList<>();
			registers.add( filterRegister );
		}
		
		
		//2.cada register...
		for (Register registerVar : registers) {

			//..2.1.seleciona as respostas..
			List<MedicalAnswer> answers = medicalFormService.searchMedicalAnswerByRegister(registerVar);
			
			//...2.2.monta o DTO...
			MedicalAnswerDTO dto = new MedicalAnswerDTO(registerVar, answers);
			
			//...2.3.verifica se deve considera o DTO conforme a limitacao fisica...
			if (mustConsiderDTOByPhysicalLimitation(dto)) {
				//...2.4.adiciona o dto ao resultado!
				dtos.add( dto );
			}
		}
		
		//3.message sobre o resultado
		JSFUtil.addMessageAboutResult( dtos );
	}

	
	
	/**
	 * Deve agregar dto considerando:
	 * - o filtro sobre limitacoes fisicas somente
	 * - se tem alguma limitacao fisica
	 * @param answers
	 */
	private Boolean mustConsiderDTOByPhysicalLimitation(MedicalAnswerDTO dto) {
		if (filterPhysicalLimitationOnly) {
			return dto.getFlagPhysicalLimitation();
		} else {
			return true;
		}
	}

	/**
	 * Se nenhum evento foi filtrado, considera todos
	 */
	private void handleFilterEventsBeforeSearch() {
		if (filterEvents==null || filterEvents.isEmpty()) {
			filterEvents = comboEvents;
		}
	}

	/**
	 * Se nenhum status foi filtrado, considera todos
	 */
	private void handleFilterStatusListBeforeSearch() {
		if (filterStatusList==null || filterStatusList.isEmpty()) {
			filterStatusList = comboStatus;
		}
	}

	
	
	
	//exporter

	public void exportToExcel() {
		SimpleExcelExporter exporter = new ReportMedicalFormExcelExporter(dtos, Idiom.pt );
		exporter.export(getReportTitles(), getExcelFilename() );
	}
	

	private String[] getReportTitles() {
		String[] reportTitles = new String[5];
		reportTitles[0] = I18nUtil.getSimpleMessage("report_medical_form");
		reportTitles[1] = filterMegaEvent.getName();
		reportTitles[2] = filterWeek.getName();
		reportTitles[3] = buildFilterEventsAsString();
		reportTitles[4] = CalendarUtil.getTodayAsFilename();
		return reportTitles;
	}
	
	
	private String buildFilterEventsAsString() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Event eventVar : filterEvents) {
			if (first) {//controle das virgulas entre os eventos
				first=false;
			} else {
				builder.append(", ");
			}
			builder.append( eventVar.getDisplayNameOrSchool() );
		}
		return builder.toString();
	}

	
	private String getExcelFilename() {
		return String.format("%s_%s.xls"
				,"InformeFormulariosMedicos"
				,CalendarUtil.getTodayAsFilename()
			);
	}



	//acessores...
	private static final long serialVersionUID = -532470335258057334L;
	public MegaEvent getFilterMegaEvent() {
		return filterMegaEvent;
	}
	public void setFilterMegaEvent(MegaEvent filterMegaEvent) {
		this.filterMegaEvent = filterMegaEvent;
	}
	public EventWeek getFilterWeek() {
		return filterWeek;
	}
	public void setFilterWeek(EventWeek filterWeek) {
		this.filterWeek = filterWeek;
	}
	public List<Event> getFilterEvents() {
		return filterEvents;
	}
	public void setFilterEvents(List<Event> filterEvents) {
		this.filterEvents = filterEvents;
	}
	public List<MegaEvent> getComboMegaEvents() {
		return comboMegaEvents;
	}
	public List<EventWeek> getComboWeeks() {
		return comboWeeks;
	}
	public List<Event> getComboEvents() {
		return comboEvents;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public List<RegisterStatus> getFilterStatusList() {
		return filterStatusList;
	}

	public void setFilterStatusList(List<RegisterStatus> filterStatusList) {
		this.filterStatusList = filterStatusList;
	}

	public List<RegisterStatus> getComboStatus() {
		return comboStatus;
	}


	public List<MedicalAnswerDTO> getDtos() {
		return dtos;
	}

	public Boolean getFilterPhysicalLimitationOnly() {
		return filterPhysicalLimitationOnly;
	}

	public void setFilterPhysicalLimitationOnly(Boolean filterPhysicalLimitationOnly) {
		this.filterPhysicalLimitationOnly = filterPhysicalLimitationOnly;
	}

	public Register getFilterRegister() {
		return filterRegister;
	}

	public void setFilterRegister(Register filterRegister) {
		this.filterRegister = filterRegister;
	}
	

	
}
