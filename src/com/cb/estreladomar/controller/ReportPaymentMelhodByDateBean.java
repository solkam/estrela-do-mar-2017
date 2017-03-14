package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.dto.RegisterPaymentMethodSummary;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * Managed Bean para visualizar o meio de pagamento por data.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="rPMbyDateBean")
@ViewScoped
public class ReportPaymentMelhodByDateBean implements Serializable {
	private static final long serialVersionUID = 1771665372275416831L;

	@EJB ReportMegaEventService service;
	
	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	
	private List<RegisterPaymentMethodSummary> summaries;
	
	//filtros...
	private Date firstDate;
	private Date lastDate;
	
	@PostConstruct void init() {
		initDate();
	}
	
	/**
	 * Inicializa as datas conforme o MegaEvento
	 */
	private void initDate() {
		MegaEvent megaEvent = reportBean.getCurrentMegaEvent();
		firstDate = megaEvent.getBeginDate();
		lastDate  = megaEvent.getEndDate();
	}
	

	public void search() {
		MegaEvent actualMegaEvent = reportBean.getCurrentMegaEvent();
		
		handleDates();
		summaries = service.searchRegisterPaymentMethodSummaryByMegaEvent(actualMegaEvent, firstDate, lastDate);
	}


	private void handleDates() {
		
	}

//acessores....
	public Date getFirstDate() {
		return firstDate;
	}


	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}


	public Date getLastDate() {
		return lastDate;
	}


	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}


	public List<RegisterPaymentMethodSummary> getSummaries() {
		return summaries;
	}


	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}
	
	

}
