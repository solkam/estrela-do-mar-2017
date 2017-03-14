package com.cb.mundo.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO com os detalhes da comissao
 * @author Solkam
 * @since 16 AGO 2015
 */
public class ComissionDetailDTO implements Serializable {
	
	private final Contact participantContact;
	
	private final Event event;
	
	private final RegisterDetail registerDetail;

	
	/**
	 * valor entre 0 e 1
	 */
	private BigDecimal percentualFracionary;
	
	/**
	 * Valor a ser subtraido do valor do participante
	 * Pode representar o valor do hotel (que nao deve ser comissionado)
	 */
	private BigDecimal subractValue;
	
	
	public ComissionDetailDTO(Contact participantContact, Event event, RegisterDetail rd) {
		super();
		this.participantContact = participantContact;
		this.event = event;
		this.registerDetail = rd;
	}


	//calculos
	/**
	 * Se contact tiver 2 produtores, entoa é comissao compartilhada
	 * @return
	 */
	public Boolean getFlagShared() {
		int nroProdutores = 0;
		if (participantContact.getProductorContact()!=null) {
			nroProdutores++;
		}
		if (participantContact.getProductorContact2()!=null) {
			nroProdutores++;
		}
		return nroProdutores==2;
	}

	
	public Boolean getFlagProductor() {
		if (participantContact.getFlagProductor()==null) {
			return false;
		} else {
			return participantContact.getFlagProductor();
		}
	}
	

	/**
	 * Usa o valor que esta em detail, pois pode ter sido 
	 * editado em pagamentos.
	 * @return
	 */
	public BigDecimal getValueParticipant() {
		BigDecimal valueParticipant = registerDetail.getValue();
		if (valueParticipant==null) {
			return NumberUtil.VALUE_ZERO;
		} else {
			return NumberUtil.subtract( valueParticipant, subractValue );
		}
	}
	
	
	
	public BigDecimal getCalculatedComissionValue() {
		//se participant eh produtor, nao gera comissao
		//REGRA REMOVIDO EM 22/08 A PEDIDO DE YANISU
//		if (getFlagProductor()) {
//			return NumberUtil.VALUE_ZERO;
//		}
		
		//se participant tem 2 produtores, cada um recebe a metade
		BigDecimal refValue = null;
		if (getFlagShared()) {
			refValue = NumberUtil.divide(getValueParticipant(), 2);
		} else {
			refValue = getValueParticipant();
		}
		
		return NumberUtil.multiply(refValue, percentualFracionary);
	}


	
	
	//acessores..
	private static final long serialVersionUID = -1614221476861457381L;
	public Contact getParticipantContact() {
		return participantContact;
	}

	public Event getEvent() {
		return event;
	}

	public void setPercentualInteger(Integer percentualInteger) {
		this.percentualFracionary = NumberUtil.divide(percentualInteger, 100L);
	}


	public void setSubractValue(BigDecimal subractValue) {
		if (subractValue==null) {
			this.subractValue = NumberUtil.VALUE_ZERO;
		} else {
			this.subractValue = subractValue;
		}
	}
	
	
	
}
