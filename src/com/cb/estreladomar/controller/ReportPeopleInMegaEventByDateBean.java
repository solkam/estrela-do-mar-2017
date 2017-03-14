package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * Managed Bean para visualizar pessoas no mega evento por data.
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportPeopleInMegaEventByDateBean")
@ViewScoped
public class ReportPeopleInMegaEventByDateBean implements Serializable {
	private static final long serialVersionUID = -380324507171200373L;

	@EJB ReportMegaEventService service;
	
	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
//filters
	private Date date = new Date();
	
//results	
	private List<RegisterDetail> registerDetails;
	
//actions
	public void search() {
		MegaEvent currentMegaEvent = reportBean.getCurrentMegaEvent();
		
		registerDetails = service.searchRegisterDetailByMegaEventAndDate(currentMegaEvent, date);
	}
	
//acessores	
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}


	public List<RegisterDetail> getRegisterDetails() {
		return registerDetails;
	}
	

}
