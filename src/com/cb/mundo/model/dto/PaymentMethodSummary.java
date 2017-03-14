package com.cb.mundo.model.dto;

import java.util.List;

import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;

/**
 * Sumario para metodos de pagamento
 * 
 * @author Solkam
 * @since 20 nov 2011
 */
public class PaymentMethodSummary {
	
	private SeminarPaymentMethod paymentMethod;
	
	private Double total;
	
	private List<Incoming> incomings;

	
	
	public PaymentMethodSummary() {
	}

	public PaymentMethodSummary(SeminarPaymentMethod paymentMethod, Double total) {
		super();
		this.paymentMethod = paymentMethod;
		this.total = total;
	}


	public SeminarPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(SeminarPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<Incoming> getIncomings() {
		return incomings;
	}

	public void setIncomings(List<Incoming> incomings) {
		this.incomings = incomings;
	}
	
	

}
