package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.cb.mundo.model.entity.enumeration.OutcomingCategory;

/**
 * Classe embedable que representa o sumario de 
 * despesas e seu respectivo total
 * 
 * @author Solkam
 * @since 24 NOV 2014
 */
@Embeddable 
public class RendicionOutcomingDetail implements Serializable {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private OutcomingCategory outcomingCategory;
	
	@Column(nullable=false)
	private BigDecimal totalValue;
	
	//acessores...
	private static final long serialVersionUID = -3055808820170608707L;
	public OutcomingCategory getOutcomingCategory() {
		return outcomingCategory;
	}
	public void setOutcomingCategory(OutcomingCategory outcomingCategory) {
		this.outcomingCategory = outcomingCategory;
	}
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	
	
}