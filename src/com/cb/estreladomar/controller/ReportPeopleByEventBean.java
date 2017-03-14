package com.cb.estreladomar.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.ReportPeopleResume;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

/**
 * ManagedBean para o informe de pessoas 
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportPeopleByEventBean")
@ViewScoped
public class ReportPeopleByEventBean implements Serializable {
	private static final long serialVersionUID = -5445450358990724174L;

	@EJB EventService eventService;
	@EJB ReportMegaEventService reportService;
	@EJB MedicalFormService medicalFormService;

	@ManagedProperty(value="#{reportBean}")
	private ReportBean reportBean;

	// combos...
	private List<MegaEvent> megaEvents;
	private List<EventWeek> eventWeeks;
	private List<Event> events;

	// filters...
	private MegaEvent selectedMegaEvent = new MegaEvent();
	private EventWeek selectedEventWeek = new EventWeek();
	private List<Event> selectedEvents = new ArrayList<>();
	private EventPresence[] selectedPresences;
	private List<RegisterStatus> selectedStatusList;

	// result...
	private List<RegisterDetail> registerDetails;
	
	//resume...
	private ReportPeopleResume resume;

	@PostConstruct
	void init() {
		populateMegaEvents();
		populateSelectedMegaEvent();
		populateEventWeeks();
		populateSelectedEventWeek();
		populateEvents();
		populateSelectedEvent();
		populateSelectedPresences();
		populateSelectecStatusList();
	}

	private void populateSelectedMegaEvent() {
		selectedMegaEvent = reportBean.getCurrentMegaEvent();
	}

	private void populateMegaEvents() {
		megaEvents = eventService.searchActiveMegaEvent();
	}

	private void populateEventWeeks() {
		if (selectedMegaEvent != null && selectedMegaEvent.getId() != null) {
			eventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);
		}
	}

	private void populateSelectedEventWeek() {
		if (eventWeeks != null && !eventWeeks.isEmpty()) {
			selectedEventWeek = eventWeeks.get(0);
		}
	}

	private void populateEvents() {
		if (selectedEventWeek != null && selectedEventWeek.getId() != null) {
			events = eventService.searchEventByEventWeek(selectedEventWeek);
		} else {
			events = new ArrayList<Event>();
		}
	}

	private void populateSelectedEvent() {
		if (events != null && !events.isEmpty()) {
			selectedEvents.add( events.get(0) );
		}
	}

	private void populateSelectedPresences() {
		selectedPresences = new EventPresence[2];
		selectedPresences[0] = EventPresence.PARTICIPANT;
	}
	
	private void populateSelectecStatusList() {
		selectedStatusList = new ArrayList<>();
		selectedStatusList.add( RegisterStatus.INCOMPLETE );
		selectedStatusList.add( RegisterStatus.REGISTERED );
		selectedStatusList.add( RegisterStatus.CHECKEDIN );
	}

// listeners...
	public void onMegaEventChange() {
		populateEventWeeks();
		populateSelectedEventWeek();
		populateEvents();
		selectedEvents = new ArrayList<>();
	}

	public void onEventWeekChange() {
		populateEvents();
		selectedEvents = new ArrayList<>();
	}
	
	/**
	 * Metodo de pr�-processamento de exporta��o para PDF.
	 * N�o est� totalmente ajustado pois a tabela de conte�do N�O est� sendo ajustada conforme a margem
	 * @param document
	 * @throws IOException
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		Document pdf = (Document)document;
		
		pdf.setPageSize(PageSize.A4.rotate());
		pdf.setMargins(20, 20, 20, 20);
		pdf.setMarginMirroring( true );
		
		pdf.open();

		ServletContext servletCtx = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logoCB = servletCtx.getRealPath("")+File.separator+"resources"+File.separator+"img"+File.separator+"logo_cb_principal.png";

		PdfPTable headTable = new PdfPTable(2);
		headTable.setWidths(new int[]{10, 90}); 

		headTable.getDefaultCell().setPadding( 5 ); 
		headTable.getDefaultCell().setBorderWidth( 0.75F );
		headTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		headTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		//celula 1
		headTable.addCell( Image.getInstance(logoCB) ); 
		
		//celula 2 (alinhada a esquerda)
		headTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		
		String textInform = "Informe de Personas por Evento";
		String textMegaEvent = "Mega Evento: " + selectedMegaEvent.getName();
		String textEventWeek = "Semana: "      + selectedEventWeek.getName();
		String textEvent     = "Eventos: "     + selectedEvents;
		
		PdfPTable centralText = new PdfPTable(1);
		centralText.getDefaultCell().setPadding( 2 );
		centralText.getDefaultCell().setBorder( 0 );
		centralText.addCell( new Phrase(textInform,    FontFactory.getFont("Verdana", 10, Font.BOLD  ) ));
		centralText.addCell( new Phrase(textMegaEvent, FontFactory.getFont("Verdana", 10, Font.NORMAL) ));
		centralText.addCell( new Phrase(textEventWeek, FontFactory.getFont("Verdana", 10, Font.NORMAL) ));
		centralText.addCell( new Phrase(textEvent,     FontFactory.getFont("Verdana", 9, Font.NORMAL) ));

		headTable.addCell( centralText );
		
		headTable.setSpacingAfter(15);
		
		
		pdf.add( headTable );
	}

	

// actions...
	public void search() {
		if (isValidSelectedPresences()) {
			registerDetails = reportService.searchRegisterDetailByEventAndInPresencesAndInStatus(selectedEvents, selectedPresences, selectedStatusList); 
			JSFUtil.addMessageAboutResult(registerDetails);
			buildResume();
		}
	}
	
	
	private void buildResume() {
		resume = new ReportPeopleResume(registerDetails);
	}

	/**
	 * Ao menos um opcao deve ser selecionada
	 * @return
	 */
	private boolean isValidSelectedPresences() {
		if (selectedPresences.length <= 0) {
			JSFUtil.addErrorMessage("msg_no_presence_selected", null);
			return false;
		}
		return true;
	}
	
	
	public String getFilename() {
		String PATTERN = "InformePersonasPorEvento_%s";
		return String.format(PATTERN, CalendarUtil.getTodayAsFilename() );
	}
	
	
	//imprimir ficha medica
	private Register selectedRegister;
	
	private List<MedicalAnswer> medicalAnswers;
	
	public void printMedicalForm(RegisterDetail detail) {
		this.selectedRegister = detail.getRegister();
		
		medicalAnswers = medicalFormService.searchMedicalAnswerByRegister(selectedRegister);
	}
	
	

	// acessores...
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}
	

	public List<MedicalAnswer> getMedicalAnswers() {
		return medicalAnswers;
	}

	public Register getSelectedRegister() {
		return selectedRegister;
	}

	public MegaEvent getSelectedMegaEvent() {
		return selectedMegaEvent;
	}

	public void setSelectedMegaEvent(MegaEvent selectedMegaEvent) {
		this.selectedMegaEvent = selectedMegaEvent;
	}

	public EventWeek getSelectedEventWeek() {
		return selectedEventWeek;
	}

	public void setSelectedEventWeek(EventWeek selectedEventWeek) {
		this.selectedEventWeek = selectedEventWeek;
	}

	public List<Event> getSelectedEvents() {
		return selectedEvents;
	}

	public void setSelectedEvents(List<Event> selectedEvents) {
		this.selectedEvents = selectedEvents;
	}

	public EventPresence[] getSelectedPresences() {
		return selectedPresences;
	}

	public void setSelectedPresences(EventPresence[] selectedPresences) {
		this.selectedPresences = selectedPresences;
	}

	public List<MegaEvent> getMegaEvents() {
		return megaEvents;
	}

	public List<EventWeek> getEventWeeks() {
		return eventWeeks;
	}

	public List<Event> getEvents() {
		return events;
	}

	public List<RegisterDetail> getRegisterDetails() {
		return registerDetails;
	}


	public List<RegisterStatus> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedStatusList(List<RegisterStatus> selectedStatusList) {
		this.selectedStatusList = selectedStatusList;
	}

	public ReportPeopleResume getResume() {
		return resume;
	}
	
}
