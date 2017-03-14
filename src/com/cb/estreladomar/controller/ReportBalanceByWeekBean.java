package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.cb.mundo.model.dto.EventWeekSummary;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * Managed Bean para visualizar o saldo por semana.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportBalanceByWeekBean")
@ViewScoped
public class ReportBalanceByWeekBean implements Serializable {
	private static final long serialVersionUID = 951162052759984737L;

	@EJB ReportMegaEventService reportService;
	
	@EJB EventService eventService;
	
	//combo de semana:
	private List<EventWeek> selectedEventWeeks = new ArrayList<EventWeek>();
	
	
	//Informe de Inscritos por Semana
	private List<EventWeekSummary> eventWeekSummaries;
	private PieChartModel chartModel = new PieChartModel(); 
	
	//view detailed objects
	private Contact contact;
	private RegisterDetail registerDetail;

	
	
	@PostConstruct
	protected void init() {
	}
	
	
	
//actions...
	public void search() {
		System.out.println( selectedEventWeeks.size() );
		eventWeekSummaries = reportService.searchEventWeekSummaryByEventWeeks(selectedEventWeeks);
		buildEventWeekChartModel();
	}
	
	private void buildEventWeekChartModel() {
		chartModel = new PieChartModel();
		for (EventWeekSummary summary : eventWeekSummaries) {
			chartModel.set(summary.getEventWeek().getName(), summary.getTotalValue() );
		}
	}
	
//acessores...	
	public List<EventWeekSummary> getEventWeekSummaries() {
		return eventWeekSummaries;
	}
	public PieChartModel getChartModel() {
		return chartModel;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public RegisterDetail getRegisterDetail() {
		return registerDetail;
	}
	public void setRegisterDetail(RegisterDetail registerDetail) {
		this.registerDetail = registerDetail;
	}
	public List<EventWeek> getSelectedEventWeeks() {
		return selectedEventWeeks;
	}
	public void setSelectedEventWeeks(List<EventWeek> selectedEventWeeks) {
		this.selectedEventWeeks = selectedEventWeeks;
	}

	
	

}
