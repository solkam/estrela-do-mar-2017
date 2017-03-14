package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.reportexporter.RegisterByEventWeekReportExcelExporter;
import com.cb.estreladomar.controller.reportexporter.RegisterByEventWeekReportPdfExporter;
import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.dto.EventWeekRegisterSummary;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;
import com.cb.mundo.model.exporter.pdf.PdfExporter;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;


/**
 * Informe 7.5: Pessoas por semana
 * 
 * @author Solkam
 * @since 14 nov 2012
 */
@ManagedBean(name="reportRegisterByWeekBean")
@ViewScoped
public class ReportRegisterByWeekBean implements Serializable {
	private static final long serialVersionUID = -8171239507898367462L;

	@EJB ReportMegaEventService service;
	
	@EJB EventService eventService;
	
	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	//filter
	private List<EventWeek> selectedEventWeeks = new ArrayList<EventWeek>();
	private List<RegisterStatus> selectedStatusList;
	
	private List<EventWeekRegisterSummary> summaries;

	
	@PostConstruct void init() {
		populateSelectedStatusList();
	}
	
	private void populateSelectedStatusList() {
		selectedStatusList = new ArrayList<>();
		selectedStatusList.add( RegisterStatus.CHECKEDIN );
	}
	
	
	
//actions...
	
	public void search() {
		summaries = service.searchEventWeekRegisterSummaryByEventWeeksAndStatusList(selectedEventWeeks, selectedStatusList);
	}
	
	
//actions para export...
	
	public void exportToPdf() {
		String filename = getFilenameExported("pdf");
		String[] reportTitles = getReportTitles();

		PdfExporter exporter = new RegisterByEventWeekReportPdfExporter( summaries );
		exporter.export(reportTitles, filename);
	}
	
	public void exportToExcel() {
		String filename = getFilenameExported("xls");
		String[] reportTitles = getReportTitles();
		
		SimpleExcelExporter exporter = new RegisterByEventWeekReportExcelExporter( summaries );
		exporter.export(reportTitles, filename);
	}
	
	private String getFilenameExported(String extension) {//util
		String filenamePattern = I18nUtil.getSimpleMessage("label_informe_register_by_week_filename_pattern");
		return String.format(filenamePattern, CalendarUtil.getTodayAsFilename(), extension );
	}
	
	private String[] getReportTitles() {//util
		String reportTitle1 = I18nUtil.getSimpleMessage("label_informe_register_by_week");
		String reportTitle2 = reportBean.getCurrentMegaEvent().getName();
		String reportTitle3  = CalendarUtil.getTodayAsFilename();
		return new String[] {reportTitle1, reportTitle2, reportTitle3};
	}
	
	public String getExcelFilename() {//util
		return getFilenameExported("xls");
	}
	

	
//acessores...
	public List<EventWeekRegisterSummary> getSummaries() {
		return summaries;
	}
	public List<EventWeek> getSelectedEventWeeks() {
		return selectedEventWeeks;
	}
	public void setSelectedEventWeeks(List<EventWeek> selectedEventWeeks) {
		this.selectedEventWeeks = selectedEventWeeks;
	}
	public List<RegisterStatus> getSelectedStatusList() {
		return selectedStatusList;
	}
	public void setSelectedStatusList(List<RegisterStatus> selectedStatusList) {
		this.selectedStatusList = selectedStatusList;
	}


	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	
}
