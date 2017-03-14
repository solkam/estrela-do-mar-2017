package com.cb.mundo.model.dto;

import static com.cb.mundo.model.util.NumberUtil.divide;
import static com.cb.mundo.model.util.NumberUtil.isEqualToZero;
import static com.cb.mundo.model.util.NumberUtil.multiply;

import java.math.BigDecimal;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO para agregar rendiciones por cidade
 * @author Solkam
 * @since 16 MAI 2015
 */
public class RendicionByCityDTO implements IRendicionDTO {
	
	private final School school;
	private final Module module;
	private final City city;
	
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
	
	private BigDecimal totalPaid;
	
	
	public RendicionByCityDTO(School school, Module module, City city,
			Long qtdEvents, Long qtdParticipants, Long qtdStaffs, 
			BigDecimal totalIncomings, BigDecimal totalOutcomings, 
			BigDecimal totalValueToMountain,
			BigDecimal totalValueToFoundation,
			BigDecimal totalValueToFacilitator,
			BigDecimal totalValueToProductor, 
			BigDecimal totalValueToMarketing) {
		super();
		this.school = school;
		this.module = module;
		this.city = city;
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


	//acessores...

	@Override
	public School getSchool() {
		return school;
	}

	
	public Module getModule() {
		return module;
	}


	@Override
	public BigDecimal getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid = totalPaid;
	}
	public City getCity() {
		return city;
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
	
	@Override
	public BigDecimal getCalculatedTotalValueCB() {
		return NumberUtil.add(totalValueToMountain, totalValueToFoundation);
	}

	
	@Override
	public BigDecimal getCalculatedAvgParticipants() {
		return NumberUtil.divide(qtdParticipants, qtdEvents);
	}
	
	@Override
	public BigDecimal getCalculatedBalance() {
		return NumberUtil.subtract(totalIncomings, totalOutcomings);
	}
	
	@Override
	public BigDecimal getCalculatedTotalPendent() {
		return NumberUtil.subtract(getCalculatedTotalValueCB(), getTotalPaid() );
	}


	@Override
	public Boolean getFlagTopLevel() {
		return false;
	}
	
	@Override
	public String getReference() {
		return String.format("%s - %s", module.getShortDesc(), city.getName() );
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
