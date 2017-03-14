package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Managed Bean para visualizar relatorio de pessoas 
 * para check-out por data.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportCheckOutByDateBean")
@ViewScoped
public class ReportPeopleForCheckOutByDateBean implements Serializable {

	@EJB ReportMegaEventService service;

	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	//filter
	private Date initialDate;
	private Date endDate;
	private RegisterStatus filterStatus = RegisterStatus.CHECKEDIN;
	
	//results
	private List<Register> registers = new ArrayList<Register>();

	@PostConstruct void init() {
		populateDates();
		search();
	}
	
	/**
	 * A initialDate ser� ontem e a endDate ser� amanha
	 */
	private void populateDates() {
		initialDate = CalendarUtil.getYesterdayDate();
		endDate = CalendarUtil.getTomorowDate();
	}
	
	public void search() {
		MegaEvent currentMegaEvent = reportBean.getCurrentMegaEvent();
		registers = service.searchRegisterForCheckOutByMegaEventAndDates(currentMegaEvent, initialDate, endDate, filterStatus);
		
		JSFUtil.addMessageAboutResult(registers);
	}

	public String getFilename() {
		String todayStr = CalendarUtil.getTodayAsFilename();
		return "Informe Checkout "+todayStr;
	}
	
	
	//acessores..	
	private static final long serialVersionUID = -6889903746912374606L;
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public List<Register> getRegisters() {
		return registers;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

	public RegisterStatus getFilterStatus() {
		return filterStatus;
	}

	
	
	
	
}
