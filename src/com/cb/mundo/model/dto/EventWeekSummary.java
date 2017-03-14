package com.cb.mundo.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.RegisterDetail;

/**
 * Classe sumario para relatorio de incritos por semana
 * @author Solkam
 * @since 10 set 2012
 */
public class EventWeekSummary implements Serializable {
	private static final long serialVersionUID = 9151030332793135590L;

	private EventWeek eventWeek;//ok no construtor
	
	private BigDecimal totalValue;//ok construtor
	
	
	private BigDecimal paidValue;
	
	private BigDecimal discountValue;
	
	private BigDecimal pendentValue;
	
	private List<RegisterDetail> registerDetails;
	
	
	
	
	public EventWeekSummary(EventWeek eventWeek, List<RegisterDetail> registerDetails) {
		this.eventWeek = eventWeek;
		this.registerDetails = registerDetails;
		totalizeValuesFromRegisterDetails();
		totalizeTotalValue();
	}


	public EventWeekSummary(EventWeek eventWeek, BigDecimal totalValue) {
		this.eventWeek = eventWeek;
		this.totalValue = totalValue;
	}
	
	/**
	 * Totaliza valor pago, valor de desconto e valor pendente 
	 */
	private void totalizeValuesFromRegisterDetails() {
		this.paidValue     = new BigDecimal("0.00");
		this.pendentValue  = new BigDecimal("0.00");
		for (RegisterDetail rd : this.registerDetails) {
			paidValue     = paidValue.add( rd.getCalculatedPaidValue() );
			pendentValue  = pendentValue.add( rd.getCalculatedPendentValue() );
		}
	}

	private void totalizeTotalValue() {
		this.totalValue = new BigDecimal("0.00");
		for (RegisterDetail detail : this.registerDetails) {
			this.totalValue = this.totalValue.add( detail.getValue() );
		}
	}
	
	public void setRegisterDetails(List<RegisterDetail> registerDetails) {
		this.registerDetails = registerDetails;
		totalizeValuesFromRegisterDetails();
	}
	
	public List<RegisterDetail> getRegisterDetails() {
		return registerDetails;
	}
	public BigDecimal getPendentValue() {
		return pendentValue;
	}
	public void setPendentValue(BigDecimal pendentValue) {
		this.pendentValue = pendentValue;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	public EventWeek getEventWeek() {
		return eventWeek;
	}
	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	public BigDecimal getPaidValue() {
		return paidValue;
	}
	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}
}
