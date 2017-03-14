package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.Register;

/**
 * Sumario para Informe 7.2
 * 
 * @author Solkam
 * @since 07 nov 2012
 */
public class RegisterSummary {
	
	private Register register;
	
	private BigDecimal totalValue;
	
	
	private BigDecimal paidValue;//totalizavel
	
	private BigDecimal pendentValue;//totalizavel
	
	private List<EventWeekSummary> eventWeekSummaries;

	//construtor
	public RegisterSummary(Register register, BigDecimal totalValue) {
		super();
		this.register = register;
		this.totalValue = totalValue;
	}

	/**
	 * Totalize os valores pagos e valore pendentes.
	 */
	private void totalizeValuesFromEventWeekSummaries() {
		this.paidValue = new BigDecimal("0.00");
		this.pendentValue = new BigDecimal("0.00");
		for (EventWeekSummary summary : this.eventWeekSummaries) {
			paidValue = paidValue.add( summary.getPaidValue() );
			pendentValue = pendentValue.add( summary.getPendentValue() );
		}
	}
	
	
	public void setEventWeekSummaries(List<EventWeekSummary> eventWeekSummaries) {
		this.eventWeekSummaries = eventWeekSummaries;
		totalizeValuesFromEventWeekSummaries();
	}
	public List<EventWeekSummary> getEventWeekSummaries() {
		return eventWeekSummaries;
	}

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
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

	public BigDecimal getPendentValue() {
		return pendentValue;
	}

	public void setPendentValue(BigDecimal pendentValue) {
		this.pendentValue = pendentValue;
	}
	
}
