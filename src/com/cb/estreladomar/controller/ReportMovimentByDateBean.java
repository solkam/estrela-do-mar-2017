package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.dto.MegaEventMovimentSummary;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * ManagedBean para Informe 7.6: Movimento Pessoas por Data
 * 
 * @author Solkam
 * @since 06 dez 2012
 */
@ManagedBean(name="reportMovimentByDateBean")
@ViewScoped
public class ReportMovimentByDateBean implements Serializable {
	private static final long serialVersionUID = -7417084469739354785L;

	@EJB ReportMegaEventService service;

	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	private MegaEvent megaEvent;
	
//filtros	
	private Date initDate;
	private Date finalDate;
	
//resultado	
	private List<MegaEventMovimentSummary> summaries;
	
	@PostConstruct void initi() {
		megaEvent = reportBean.getCurrentMegaEvent();
		initDate = megaEvent.getBeginDate();
		finalDate = megaEvent.getEndDate();
	}


	public void search() {
		summaries = service.searchMegaEventMovimentSummary(megaEvent, initDate, finalDate);
	}
	
//acessores	
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Date getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}
	public List<MegaEventMovimentSummary> getSummaries() {
		return summaries;
	}
	

}
