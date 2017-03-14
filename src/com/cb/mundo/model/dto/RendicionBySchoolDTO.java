package com.cb.mundo.model.dto;

import static com.cb.mundo.model.util.NumberUtil.isEqualToZero;

import static com.cb.mundo.model.util.NumberUtil.divide;

import static com.cb.mundo.model.util.NumberUtil.multiply;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO para o relatorio de Rendiciona para Administracion
 * @author Solkam
 * @since 13 MAI 2015
 */
public class RendicionBySchoolDTO implements IRendicionDTO {
	
	private final School school;
	
	private final Long qtdEvents;
	
	private final Long qtdParticipants;
	
	private final Long qtdStaffs;
	
	private final BigDecimal totalIncomings;
	
	private final BigDecimal totalOutcomings;
	
	private final BigDecimal totalValueToMountain;
	
	private final BigDecimal totalValueToFoundation;
	
	private final BigDecimal totalValueToFacilitator;
	
	private final BigDecimal totalValueToProductor;
	
	private final BigDecimal totalValueToMarketing;
	
	
	//valor calculado a postori
	private BigDecimal totalPaid;
	
	
	//lista de agrega por outras entidades
	private List<RendicionByModuleDTO> rendicionByModuleDtos;
	private List<RendicionByCityDTO> rendicionByCityDtos;
	

	
	//construtores...
	public RendicionBySchoolDTO(School school, 
			Long qtdEvents,	Long qtdParticipants, Long qtdStaffs, 
			BigDecimal totalIncomings, BigDecimal totalOutcomings, 
			BigDecimal totalValueToMountain,
			BigDecimal totalValueToFoundation,
			BigDecimal totalValueToFacilitator,
			BigDecimal totalValueToProductor, 
			BigDecimal totalValueToMarketing) {
		super();
		this.school = school;
		this.qtdEvents = qtdEvents;
		this.qtdParticipants = qtdParticipants;
		this.qtdStaffs = qtdStaffs;
		this.totalIncomings = totalIncomings;
		this.totalOutcomings = totalOutcomings;
		this.totalValueToMountain = totalValueToMountain;
		this.totalValueToFoundation = totalValueToFoundation;
		this.totalValueToFacilitator = totalValueToFacilitator;
		this.totalValueToProductor = totalValueToProductor;
		this.totalValueToMarketing = totalValueToMarketing;
	}

	
	public School getSchool() {
		return school;
	}

	@Override
	public Long getQtdEvents() {
		return qtdEvents;
	}

	@Override
	public Long getQtdParticipants() {
		return qtdParticipants;
	}

	@Override
	public Long getQtdStaffs() {
		return qtdStaffs;
	}

	@Override
	public BigDecimal getTotalIncomings() {
		return totalIncomings;
	}

	@Override
	public BigDecimal getTotalOutcomings() {
		return totalOutcomings;
	}

	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid = totalPaid;
	}

	@Override
	public BigDecimal getTotalPaid() {
		return totalPaid;
	}

	@Override
	public BigDecimal getTotalValueToMountain() {
		return totalValueToMountain;
	}

	@Override
	public BigDecimal getTotalValueToFoundation() {
		return totalValueToFoundation;
	}

	@Override
	public BigDecimal getTotalValueToFacilitator() {
		return totalValueToFacilitator;
	}

	@Override
	public BigDecimal getTotalValueToProductor() {
		return totalValueToProductor;
	}

	@Override
	public BigDecimal getTotalValueToMarketing() {
		return totalValueToMarketing;
	}
	

	//runtime
	
	@Override
	public BigDecimal getCalculatedAvgParticipants() {
		return NumberUtil.divide(qtdParticipants, qtdEvents);
	}
	
	@Override
	public BigDecimal getCalculatedBalance() {
		return NumberUtil.subtract(totalIncomings, totalOutcomings);
	}
	
	@Override
	public BigDecimal getCalculatedTotalValueCB() {
		return NumberUtil.add(totalValueToMountain, totalValueToFoundation);
	}
	
	@Override
	public BigDecimal getCalculatedTotalPendent() {
		return NumberUtil.subtract(getCalculatedTotalValueCB(), getTotalPaid() );
	}
	
	//agregados
	
	public List<RendicionByModuleDTO> getRendicionByModuleDtos() {
		return rendicionByModuleDtos;
	}
	public void setRendicionByModuleDtos(
			List<RendicionByModuleDTO> rendicionByModuleDtos) {
		this.rendicionByModuleDtos = rendicionByModuleDtos;
	}

	public void setRendicionByCityDtos(List<RendicionByCityDTO> rendicionByCityDtos) {
		this.rendicionByCityDtos = rendicionByCityDtos;
	}
	public List<RendicionByCityDTO> getRendicionByCityDtos() {
		return rendicionByCityDtos;
	}


	@Override
	public String getReference() {
		return getSchool().getDescription();
	}


	@Override
	public Boolean getFlagTopLevel() {
		return true;
	}
	
	
	@Override
	public Boolean getFlagPaymentOK() {
		return isEqualToZero( getCalculatedTotalPendent() );
	}


	@Override
	public BigDecimal getCalculatedInternalMountainValue() {
		return multiply(getCalculatedTotalValueCB(), INTERNAL_MOUNTAIN_PERCENT);
	}

	@Override
	public BigDecimal getCalculatedInternalFoundationValue() {
		return multiply(getCalculatedTotalValueCB(), INTERNAL_FOUNDATION_PERCENT);
	}

	@Override
	public BigDecimal getCalculatedInternalDirectorValue() {
		return multiply(getCalculatedTotalValueCB(), INTERNAL_DIRECTOR_PERCENT);
	}
	

}
