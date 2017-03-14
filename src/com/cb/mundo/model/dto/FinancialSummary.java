package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.calculum.RendicionCalculum;
import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Outcoming;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Sumario financeiro de uma producao.
 * 
 * [02 DEZ 2014] O calculo dos valores para montanha, fundacao e outros
 * agora eh feito por um strategy definido por escola.
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public class FinancialSummary {
	
	private final Production production;
	
	private Double totalOutcoming;
	private Double totalIncoming;
	
	private Double balance;
	
	private BigDecimal valueMontain;
	private BigDecimal valueFoundation;
	private BigDecimal valueFacilitator;
	private BigDecimal valueProductor;
	private BigDecimal valueMarketing;
	
	public FinancialSummary(Production production, List<Outcoming> outcomings, List<Incoming> incomings) {
		this.production = production;
		Double totalOutcoming = 0.00;
		for (Outcoming o : outcomings) {
			totalOutcoming += o.getValue();
		}
		
		Double totalIncoming = 0.00;
		for (Incoming i : incomings) {
			totalIncoming += i.getValue();
		}
		
		calculateValues(totalOutcoming, totalIncoming);
	}
	
	public FinancialSummary(Production production, Double totalOutcoming, Double totalIncoming) {
		this.production = production;
		calculateValues(totalOutcoming, totalIncoming);
	}
	
	
	private void calculateValues(Double totalOutcoming, Double totalIncoming) {
		this.totalOutcoming = NumberUtil.roundTwoDecimals(totalOutcoming);
		this.totalIncoming = NumberUtil.roundTwoDecimals(totalIncoming);
		
		this.balance = this.totalIncoming - this.totalOutcoming;
		
		RendicionCalculum calculum = production.getSchool().getRendicionCalculum();
		
		this.valueMontain     = calculum.getValueToMountain(balance);
		this.valueFoundation  = calculum.getValueToFoundation(balance);
		this.valueFacilitator = calculum.getValueToFacilitators(balance);
		this.valueProductor   = calculum.getValueToProductors(balance);
		this.valueMarketing   = calculum.getValueToMarketing(balance);

//		this.valueMontain     = NumberUtil.roundTwoDecimals(balance * PERCENT_MOUNTAIN);
//		this.valueFoundation  = NumberUtil.roundTwoDecimals(balance * PERCENT_FOUNDATION);
//		this.valueFacilitator = NumberUtil.roundTwoDecimals(balance * PERCENT_FACILITATOR);
//		this.valueProductor   = NumberUtil.roundTwoDecimals(balance * PERCENT_PRODUCTOR);
//		this.valueMarketing   = NumberUtil.roundTwoDecimals(balance * PERCENT_MARKETING);
	}
	


	public Production getProduction() {
		return production;
	}


	public Double getTotalOutcoming() {
		return totalOutcoming;
	}


	public Double getTotalIncoming() {
		return totalIncoming;
	}


	public Double getBalance() {
		return balance;
	}


	public BigDecimal getValueMontain() {
		return valueMontain;
	}


	public BigDecimal getValueFoundation() {
		return valueFoundation;
	}


	public BigDecimal getValueProductor() {
		return valueProductor;
	}


	public BigDecimal getValueFacilitator() {
		return valueFacilitator;
	}

	public BigDecimal getValueMarketing() {
		return valueMarketing;
	}
	
	
	
	
	
	
	
}
