package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.reportexporter.PaymentByEventReportExcelExporter;
import com.cb.estreladomar.controller.reportexporter.PaymentByEventReportPdfExporter;
import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;
import com.cb.mundo.model.exporter.pdf.PdfExporter;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Managed Bean para visualizar relatorio de pagamentos por evento
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportPaymentByEventBean")
@ViewScoped
public class ReportPaymentByEventBean implements Serializable {
	private static final long serialVersionUID = -6977223521008828119L;
	
	@EJB EventService eventService;
	@EJB ReportMegaEventService reportService;

	@ManagedProperty(value="#{reportBean}")
	private ReportBean reportBean;

	//combos...
	private List<MegaEvent> megaEvents;
	private List<EventWeek> eventWeeks;
	private List<Event> events;
	
	//filters...
	private MegaEvent selectedMegaEvent = new MegaEvent();
	private EventWeek selectedEventWeek = new EventWeek();
	private List<Event> selectedEvents;
	private EventPresence[] selectedPresences;
	private List<RegisterStatus> selectedStatusList;
	
	//result...
	private List<RegisterDetail> registerDetails;
	

	@PostConstruct void init() {
		populateMegaEvents();
		populateSelectedMegaEvent();
		populateEventWeeks();
		populateSelectedEventWeek();
		populateEvents();
		populateSelectedEvent();
		populateSelectedPresences();
		populateSelectedStatusList();
	}
	
	private void populateSelectedMegaEvent() {
		selectedMegaEvent = reportBean.getCurrentMegaEvent();
	}
	
	private void populateMegaEvents() {
		megaEvents = eventService.searchActiveMegaEvent();
	}
	
	private void populateEventWeeks() {
		if (selectedMegaEvent!=null && selectedMegaEvent.getId()!=null) {
			eventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);
		}
	}
	
	private void populateSelectedEventWeek() {
		if (eventWeeks!=null && !eventWeeks.isEmpty()) {
			selectedEventWeek = eventWeeks.get(0);
		}
	}

	private void populateEvents() {
		if (selectedEventWeek!=null && selectedEventWeek.getId()!=null) {
			events = eventService.searchEventByEventWeek(selectedEventWeek);
		} else {
			events = new ArrayList<Event>();
		}
	}
	
	private void populateSelectedEvent() {
		if (events!=null && !events.isEmpty()) {
			selectedEvents = new ArrayList<>();
			selectedEvents.add( events.get(0) );
		}
	}
	
	private void populateSelectedPresences() {
		selectedPresences = new EventPresence[2];
		selectedPresences[0] = EventPresence.PARTICIPANT;
	}
	
	private void populateSelectedStatusList() {
		selectedStatusList = new ArrayList<>();
		selectedStatusList.add( RegisterStatus.INCOMPLETE );
		selectedStatusList.add( RegisterStatus.REGISTERED );
		selectedStatusList.add( RegisterStatus.CHECKEDIN );
	}
	
	/**
	 * Ao seleciona outro mega evento, somente seu id eh seleciona
	 * e o nome continua o antigo. Na hora de exportar, precisa
	 * o nome atual.
	 * @return
	 */
	private MegaEvent reloadSelectedMegaEvent() {
		return eventService.findMegaEventById( selectedMegaEvent.getId() );
	}
	
	//listeners...
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
	
	
	//actions...
	public void search() {
		if (isValidSelectedPresences()) {
			registerDetails = reportService.searchRegisterDetailByEventAndPresencesWithPayments(selectedEvents, selectedPresences, selectedStatusList);
			totalize();
			
			//msgs de aviso
			JSFUtil.addMessageAboutResult(registerDetails);
		}
	}
	
	private BigDecimal totalValue;
	private BigDecimal totalPaidValue;
	private BigDecimal totalPendentValue;
	
	
	private void totalize() {
		totalValue = NumberUtil.VALUE_ZERO;
		totalPaidValue = NumberUtil.VALUE_ZERO;
		totalPendentValue = NumberUtil.VALUE_ZERO;
		for (RegisterDetail detailVar : registerDetails) {
			totalValue = NumberUtil.add(totalValue, detailVar.getValue() );
			totalPaidValue = NumberUtil.add(totalPaidValue, detailVar.getCalculatedPaidValue() );
			totalPendentValue = NumberUtil.add(totalPendentValue, detailVar.getCalculatedPendentValue() );
		}
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
	
	
	
	//actions para export...
	
	public void exportToPDF() {
		String[] reportTitles = getReportTitles();
		String filename = getFilename("pdf");
		
		PdfExporter exporter = new PaymentByEventReportPdfExporter( registerDetails, totalValue, totalPaidValue, totalPendentValue );
		exporter.export(reportTitles, filename);
	}
	
	public void exportToExcel() {
		String[] reportTitles = getReportTitles();
		String filename = getFilename("xls");
		
		SimpleExcelExporter exporter = new PaymentByEventReportExcelExporter( registerDetails, totalValue, totalPaidValue, totalPendentValue );
		exporter.export(reportTitles, filename);
	}
	
	
	private String[] getReportTitles() {
		String title1 = I18nUtil.getSimpleMessage("label_informe_payment_by_event");
		String title2 = reloadSelectedMegaEvent().getName();
		String title3 = CalendarUtil.getTodayAsFilename();
		return new String[] {title1, title2, title3};
	}

	private String getFilename(String extension) {
		String filenamePattern = I18nUtil.getSimpleMessage("label_informe_payment_by_event_filename_pattern");
		return String.format(filenamePattern, CalendarUtil.getTodayAsFilename(), extension);
	}
	
	
	
//acessoress...
	
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public BigDecimal getTotalPaidValue() {
		return totalPaidValue;
	}

	public BigDecimal getTotalPendentValue() {
		return totalPendentValue;
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

	public EventPresence[] getSelectedPresences() {
		return selectedPresences;
	}

	public void setSelectedPresences(EventPresence[] selectedPresences) {
		this.selectedPresences = selectedPresences;
	}

	public List<RegisterStatus> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedStatusList(List<RegisterStatus> selectedStatusList) {
		this.selectedStatusList = selectedStatusList;
	}
	
	
	
}
