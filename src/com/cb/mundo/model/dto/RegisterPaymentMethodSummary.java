package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;

/**
 * Sumarizador para meios de pagamento listando os inscritos
 * @author Solkam
 * @since 08 nov 2012
 */
public class RegisterPaymentMethodSummary {
	
	private MegaEventPaymentMethod paymentMethod;
	
	private BigDecimal totalValue;
	
	private List<RegisterDetailPayment> payments;

	
	public RegisterPaymentMethodSummary(MegaEventPaymentMethod paymentMethod, BigDecimal totalValue) {
		super();
		this.paymentMethod = paymentMethod;
		this.totalValue = totalValue;
	}

	
	public MegaEventPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(MegaEventPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	public List<RegisterDetailPayment> getPayments() {
		return payments;
	}
	public void setPayments(List<RegisterDetailPayment> payments) {
		this.payments = payments;
	}
	

}
