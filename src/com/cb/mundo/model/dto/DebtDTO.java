package com.cb.mundo.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO para o relatorio de dividas
 * @author Solkam
 * @since 19 MAI 2015
 */
public class DebtDTO implements Serializable {
	

	private final MegaEvent megaEvent;
	
	private final Register register;
	
	private final Event event;
	
	private final BigDecimal valueTotal;
	
	
	
	private BigDecimal valuePaid;



	public DebtDTO(MegaEvent megaEvent, Register register, Event event, BigDecimal valueTotal) {
		super();
		this.megaEvent = megaEvent;
		this.register = register;
		this.event = event;
		this.valueTotal = valueTotal!=null ? valueTotal : NumberUtil.VALUE_ZERO;
	}



	//acessores...
	private static final long serialVersionUID = -1274981274136413720L;
	public BigDecimal getValuePaid() {
		return valuePaid;
	}
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public Register getRegister() {
		return register;
	}
	public Event getEvent() {
		return event;
	}
	
	public BigDecimal getValueTotal() {
		return valueTotal;
	}
	
	public void setValuePaid(BigDecimal valuePaid) {
		this.valuePaid  = valuePaid!=null  ? valuePaid  : NumberUtil.VALUE_ZERO;
	}
	

	
	//runtime 
	
	public BigDecimal getCalculatedPendentValue() {
		return NumberUtil.subtract(valueTotal, valuePaid);
	}

}
