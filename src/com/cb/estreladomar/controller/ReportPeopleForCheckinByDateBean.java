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
 * Managed Bean para visualizar pessoas para checkin por data
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="reportPeopleForCheckinByDateBean")
@ViewScoped
public class ReportPeopleForCheckinByDateBean implements Serializable {
	private static final long serialVersionUID = -1086889317883603733L;

	@ManagedProperty("#{reportBean}")
	private ReportBean reportBean;
	
	@EJB ReportMegaEventService service;
	
	//filters
	private Date date1;
	private Date date2;
	private boolean flagPreviewCheckin = true;
	private boolean flagAlreadCheckin = false;
	
	//results
	private List<Register> registers;
	
	@PostConstruct void init() {
		populateDates();
		search();
	}
	
	private void populateDates() {
		date1 = CalendarUtil.getYesterdayDate();
		date2 = new Date();
	}
	
	
	public void search() {
		MegaEvent currentMegaEvent = reportBean.getCurrentMegaEvent();
		
		List<RegisterStatus> statusList = new ArrayList<>();
		if (flagPreviewCheckin) {
			statusList.add( RegisterStatus.INCOMPLETE );
			statusList.add( RegisterStatus.REGISTERED );
		}
		if (flagAlreadCheckin) {
			statusList.add( RegisterStatus.CHECKEDIN );
		}
		
		if (validarStatusList(statusList)) {
			registers = service.searchRegisterByMegaEventAndStatusListAndBeetweenCheckinDates(currentMegaEvent, statusList, date1, date2);
			JSFUtil.addMessageAboutResult(registers);
		}
	}
	
	/**
	 * Pelo menos uma opcao quanto ao status deve ser selecionada
	 * @param statusList
	 * @return
	 */
	private boolean validarStatusList(List<RegisterStatus> statusList) {
		if (statusList.isEmpty()) {
			JSFUtil.addErrorMessage("msg_no_status_selected", null);
			return false;
		}
		return true;
	}
	
	
	

	
//acessores...
	public String getFilename() {
		return "InformeCheckinPorFecha";
	}

	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Date getDate2() {
		return date2;
	}
	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	public boolean isFlagPreviewCheckin() {
		return flagPreviewCheckin;
	}
	public void setFlagPreviewCheckin(boolean flagPreviewCheckin) {
		this.flagPreviewCheckin = flagPreviewCheckin;
	}
	public boolean isFlagAlreadCheckin() {
		return flagAlreadCheckin;
	}
	public void setFlagAlreadCheckin(boolean flagAlreadCheckin) {
		this.flagAlreadCheckin = flagAlreadCheckin;
	}

	public List<Register> getRegisters() {
		return registers;
	}
	

}
