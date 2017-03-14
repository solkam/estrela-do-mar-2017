package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Managed Bean para visualizar pessoas com limitacoes fisicas
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportPhyLimBean")
@ViewScoped
public class ReportPeopleWithPhysicalLimitationBean implements Serializable {
	private static final long serialVersionUID = 6376959823932950625L;

	@EJB EventService eventServic;
	@EJB ReportMegaEventService reportService;
	
	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	//filters
	private Date initDate;
	private Date endDate;
	
	//result
	private List<MedicalAnswer> answers;
	
	@PostConstruct void init() {
		populateDates();
	}

	private void populateDates() {
		initDate = CalendarUtil.getYesterdayDate();
		endDate = CalendarUtil.getTomorowDate();
	}


//actions...	
	public void search() {
		MegaEvent currentMegaEvent = reportBean.getCurrentMegaEvent();
		answers = reportService.searchMedicalAnswerWithLimitationByMegaEventAndDates(currentMegaEvent, initDate, endDate);
	}

//acessores...
	public List<MedicalAnswer> getAnswers() {
		return answers;
	}
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getFilenameExportedExcel() {
		final String FILENAME = "Informe Medico %s - %s";
		String initDateStr = CalendarUtil.formatDateToFilename(initDate);
		String endDateStr  = CalendarUtil.formatDateToFilename(endDate);
		return String.format(FILENAME, initDateStr, endDateStr);
	}

}
