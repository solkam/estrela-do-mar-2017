package com.cb.mundo.model.dto;

import java.math.BigDecimal;

import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.util.NumberUtil;

public interface IRendicionDTO {
	
	BigDecimal INTERNAL_MOUNTAIN_PERCENT   = NumberUtil.newBigDecimal( 0.75 ); 
	BigDecimal INTERNAL_FOUNDATION_PERCENT = NumberUtil.newBigDecimal( 0.10 ); 
	BigDecimal INTERNAL_DIRECTOR_PERCENT   = NumberUtil.newBigDecimal( 0.15 ); 
	

	/**
	 * Entity que o DTO está agregado: escola, modulo ou cidade
	 * @return
	 */
	public String getReference();
	
	public School getSchool();
	
	public Long getQtdEvents();

	public Long getQtdParticipants();

	public Long getQtdStaffs();

	public BigDecimal getTotalIncomings();

	public BigDecimal getTotalOutcomings();

	public BigDecimal getTotalPaid();

	public BigDecimal getTotalValueToMountain();

	public BigDecimal getTotalValueToFoundation();

	public BigDecimal getTotalValueToFacilitator();

	public BigDecimal getTotalValueToProductor();

	public BigDecimal getTotalValueToMarketing();

	//runtime
	/**
	 * Calcula a media de partipantes por evento
	 * @return
	 */
	public BigDecimal getCalculatedAvgParticipants();

	/**
	 * Saldo é o total de receitas menos o total de despesas
	 */
	public BigDecimal getCalculatedBalance();

	/**
	 * Valor para admin é o total da montanha mais o total da fundacao
	 */
	public BigDecimal getCalculatedTotalValueCB();

	/**
	 * Valor pendente é o total ja pago menos o total da administracao
	 */
	public BigDecimal getCalculatedTotalPendent();

	
	/**
	 * Define se o DTO é nivel topo.
	 * School o será. Os demais, não
	 * @return
	 */
	public Boolean getFlagTopLevel();

	/**
	 * Verifica o valor pendente é zero
	 * @return
	 */
	public Boolean getFlagPaymentOK();
	
	/**
	 * Calcula o valor interno para montanha
	 * @return
	 */
	public BigDecimal getCalculatedInternalMountainValue();
	
	/**
	 * Calcular o valor interno para fundacao
	 * @return
	 */
	public BigDecimal getCalculatedInternalFoundationValue();
	
	
	/**
	 * Calcula o valor interno para diretores
	 * @return
	 */
	public BigDecimal getCalculatedInternalDirectorValue();

}