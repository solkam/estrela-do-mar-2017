package com.cb.mundo.model.service;

import java.math.BigDecimal;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.util.NumberUtil;




public class BalanceMegaEventData {

	private BigDecimal total;
	private BigDecimal realPayment;
	private BigDecimal virtualPayment;
	private Long amount;
	
	
	public BalanceMegaEventData(BigDecimal total, BigDecimal realPayment,
			BigDecimal virtualPayment, Long amount) {
		super();
		this.total = total;
		this.realPayment = realPayment;
		this.virtualPayment = virtualPayment;
		this.amount = amount;
	}
	
	public BigDecimal getPendentValue(){
		return NumberUtil.subtract(total, NumberUtil.add(realPayment, virtualPayment));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((realPayment == null) ? 0 : realPayment.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime * result
				+ ((virtualPayment == null) ? 0 : virtualPayment.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BalanceMegaEventData other = (BalanceMegaEventData) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (realPayment == null) {
			if (other.realPayment != null)
				return false;
		} else if (!realPayment.equals(other.realPayment))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		if (virtualPayment == null) {
			if (other.virtualPayment != null)
				return false;
		} else if (!virtualPayment.equals(other.virtualPayment))
			return false;
		return true;
	}

	
	//access
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getRealPayment() {
		return realPayment;
	}

	public void setRealPayment(BigDecimal realPayment) {
		this.realPayment = realPayment;
	}

	public BigDecimal getVirtualPayment() {
		return virtualPayment;
	}

	public void setVirtualPayment(BigDecimal virtualPayment) {
		this.virtualPayment = virtualPayment;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}


	
	
	
	
	
	
	
	
}
