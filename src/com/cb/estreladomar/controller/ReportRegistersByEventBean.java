package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.cb.mundo.model.dto.EventSummary;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.ReportMegaEventService;


@ManagedBean(name="reportRegistersByEventBean")
@ViewScoped
public class ReportRegistersByEventBean implements Serializable {
	private static final long serialVersionUID = 8174918638356495029L;

	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	@EJB ReportMegaEventService service;
	
	private List<EventSummary> eventSummaries;
	private PieChartModel chartModel;

	
	@PostConstruct void init() {
		search();
	}
	
	public void search() {
		MegaEvent selectedMegaEvent = reportBean.getCurrentMegaEvent();
		
		eventSummaries = service.searchEventSummaryByMegaEvent(selectedMegaEvent);
		populateChartModel();
	}
	
	
	private void populateChartModel() {
		chartModel = new PieChartModel();
		for (EventSummary summary : eventSummaries) {
			chartModel.set(summary.getEvent().getDisplayNameOrSchool(), summary.getTotal());
		}
	}

//acessores	
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public List<EventSummary> getEventSummaries() {
		return eventSummaries;
	}

	public PieChartModel getChartModel() {
		return chartModel;
	}

	
	
}
