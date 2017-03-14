package com.cb.mundo.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO para comissao de produtores
 * @author Solkam
 * @since 16 AGO 2015
 */
public class ComissionDTO implements Serializable, Comparable<ComissionDTO> {

	private Contact productorContact;
	
	private List<ComissionDetailDTO> detailDtos;
	

	//construtor...
	public ComissionDTO(Contact productorContact, List<ComissionDetailDTO> detailDtos) {
		super();
		this.productorContact = productorContact;
		this.detailDtos = detailDtos;
	}

	
	//calculos
	public BigDecimal getCalculatedTotalComission() {
		BigDecimal totalComission = NumberUtil.VALUE_ZERO;
		for (ComissionDetailDTO detailDtoVar : detailDtos) {
			totalComission = NumberUtil.add(totalComission, detailDtoVar.getCalculatedComissionValue() );
		}
		return totalComission;
	}
	
	
	
	//acessores...
	private static final long serialVersionUID = -3195466825042519067L;

	public Contact getProductorContact() {
		return productorContact;
	}

	public List<ComissionDetailDTO> getDetailDtos() {
		return detailDtos;
	}


	/**
	 * Ordena pelo nome civil do produtor
	 */
	@Override
	public int compareTo(ComissionDTO that) {
		return this.productorContact.getCivilName().compareTo( that.getProductorContact().getCivilName() );
	}
	
	
	

}
