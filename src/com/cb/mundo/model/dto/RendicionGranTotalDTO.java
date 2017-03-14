package com.cb.mundo.model.dto;

import java.math.BigDecimal;

import com.cb.mundo.model.util.NumberUtil;


/**
 * Representa os gran totais de rendiciones
 * @author Solkam
 * @since 14 MAI 2015
 */
public class RendicionGranTotalDTO {
	
	private Long granQtdEvents       = 0L;
	private Long granQtdParticipants = 0L;
	private Long granQtdStaffs       = 0L;
	
	private BigDecimal granTotalIncomings = NumberUtil.VALUE_ZERO;
	private BigDecimal granTotalOutcoming = NumberUtil.VALUE_ZERO;
	private BigDecimal granBalance        = NumberUtil.VALUE_ZERO;
	
	private BigDecimal granValueToMontain     = NumberUtil.VALUE_ZERO;
	private BigDecimal granValueToFoundation  = NumberUtil.VALUE_ZERO;
	private BigDecimal granValueToFacilitator = NumberUtil.VALUE_ZERO;
	private BigDecimal granValueToProductor   = NumberUtil.VALUE_ZERO;
	private BigDecimal granValueToMarketing   = NumberUtil.VALUE_ZERO;
	
	private BigDecimal granTotalValueCB = NumberUtil.VALUE_ZERO;
	private BigDecimal granTotalInternalMontainValue    = NumberUtil.VALUE_ZERO;
	private BigDecimal granTotalInternalFoundationValue = NumberUtil.VALUE_ZERO;
	private BigDecimal granTotalInternalDirectorValue   = NumberUtil.VALUE_ZERO;
	
	
	private BigDecimal granTotalPaid    = NumberUtil.VALUE_ZERO;
	private BigDecimal granTotalPendent = NumberUtil.VALUE_ZERO;
	
	
	public void increase(IRendicionDTO dtoVar) {
		granQtdEvents       += dtoVar.getQtdEvents();
		granQtdParticipants += dtoVar.getQtdParticipants();
		granQtdStaffs       += dtoVar.getQtdStaffs();
		
		granTotalIncomings = NumberUtil.add(granTotalIncomings, dtoVar.getTotalIncomings());
		granTotalOutcoming = NumberUtil.add(granTotalOutcoming, dtoVar.getTotalOutcomings());
		granBalance        = NumberUtil.add(granBalance       , dtoVar.getCalculatedBalance());
		
		granValueToMontain     = NumberUtil.add(granValueToMontain    , dtoVar.getTotalValueToMountain());
		granValueToFoundation  = NumberUtil.add(granValueToFoundation , dtoVar.getTotalValueToFoundation());
		granValueToFacilitator = NumberUtil.add(granValueToFacilitator, dtoVar.getTotalValueToFacilitator());
		granValueToProductor   = NumberUtil.add(granValueToProductor  , dtoVar.getTotalValueToProductor());
		granValueToMarketing   = NumberUtil.add(granValueToMarketing  , dtoVar.getTotalValueToMarketing() );
		
		granTotalValueCB       = NumberUtil.add(granTotalValueCB      , dtoVar.getCalculatedTotalValueCB());
		granTotalInternalMontainValue    = NumberUtil.add(granTotalInternalMontainValue   , dtoVar.getCalculatedInternalMountainValue() );
		granTotalInternalFoundationValue = NumberUtil.add(granTotalInternalFoundationValue, dtoVar.getCalculatedInternalFoundationValue() );
		granTotalInternalDirectorValue   = NumberUtil.add(granTotalInternalDirectorValue  , dtoVar.getCalculatedInternalDirectorValue() );

		granTotalPaid          = NumberUtil.add(granTotalPaid         , dtoVar.getTotalPaid());
		granTotalPendent       = NumberUtil.add(granTotalPendent      , dtoVar.getCalculatedTotalPendent());
	}


	
	//acessores...
	public Long getGranQtdEvents() {
		return granQtdEvents;
	}
	public Long getGranQtdParticipants() {
		return granQtdParticipants;
	}
	public Long getGranQtdStaffs() {
		return granQtdStaffs;
	}
	public BigDecimal getGranTotalIncomings() {
		return granTotalIncomings;
	}
	public BigDecimal getGranTotalOutcoming() {
		return granTotalOutcoming;
	}
	public BigDecimal getGranBalance() {
		return granBalance;
	}
	public BigDecimal getGranValueToMontain() {
		return granValueToMontain;
	}
	public BigDecimal getGranValueToFoundation() {
		return granValueToFoundation;
	}
	public BigDecimal getGranValueToFacilitator() {
		return granValueToFacilitator;
	}
	public BigDecimal getGranValueToProductor() {
		return granValueToProductor;
	}
	public BigDecimal getGranValueToMarketing() {
		return granValueToMarketing;
	}
	public BigDecimal getGranTotalValueCB() {
		return granTotalValueCB;
	}
	public BigDecimal getGranTotalInternalMontainValue() {
		return granTotalInternalMontainValue;
	}
	public BigDecimal getGranTotalInternalFoundationValue() {
		return granTotalInternalFoundationValue;
	}
	public BigDecimal getGranTotalInternalDirectorValue() {
		return granTotalInternalDirectorValue;
	}

	public BigDecimal getGranTotalPaid() {
		return granTotalPaid;
	}
	public BigDecimal getGranTotalPendent() {
		return granTotalPendent;
	}

	//runtime
	public BigDecimal getGranAvgQtdParticipant() {
		return NumberUtil.divide(granQtdParticipants, granQtdEvents);
	}

}
