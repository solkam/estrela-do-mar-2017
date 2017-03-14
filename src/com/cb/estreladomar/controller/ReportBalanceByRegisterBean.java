package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.RegisterSummary;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.ReportMegaEventService;

/**
 * Managed Bean para visualizar o saldo por inscricao
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportBalanceByRegisterBean")
@ViewScoped
public class ReportBalanceByRegisterBean implements Serializable {
	private static final long serialVersionUID = -7216725864789183295L;

	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;

	@EJB ReportMegaEventService service;
	
	private Contact selectedContact;
	
	private List<RegisterSummary> summaries;
	
	@PostConstruct void init () {
	}
	
	public void search() {
		try {
			if (isValidSelectedContact()) {
				MegaEvent selectedMegaEvent = reportBean.getCurrentMegaEvent();
				summaries = service.searchRegisterSummaryByMegaEventAndContact(selectedMegaEvent, selectedContact);
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}



	private boolean isValidSelectedContact() {
		if (selectedContact==null || selectedContact.getId()==null) {
			JSFUtil.addErrorMessage("msg_no_contact_selected", null);
			return false;
		}
		return true;
	}

//acessores...
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public List<RegisterSummary> getSummaries() {
		return summaries;
	}

	public Contact getSelectedContact() {
		return selectedContact;
	}

	public void setSelectedContact(Contact selectedContact) {
		this.selectedContact = selectedContact;
	}

	

}
