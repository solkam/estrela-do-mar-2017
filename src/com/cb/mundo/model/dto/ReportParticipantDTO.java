package com.cb.mundo.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DTO para o relatorio de participantes
 * @author Solkam
 * @since 20 MAR 2015
 */
public class ReportParticipantDTO {

	private final Production production;
	
	private final Participant participant;
	
	private final List<Incoming> incomings;


	public ReportParticipantDTO(Production production, Participant participant, List<Incoming> incomings) {
		this.production = production;
		this.participant = participant;
		this.incomings = incomings;
	}
	
	
	//acessores...
	public Participant getParticipant() {
		return participant;
	}
	public List<Incoming> getIncomings() {
		return incomings;
	}
	public Production getProduction() {
		return production;
	}

	
	/**
	 * Soma todos as receitas
	 * @return
	 */
	public BigDecimal getCalculatedTotalIncoming() {
		BigDecimal total = NumberUtil.VALUE_ZERO;
		for (Incoming incomingVar : getIncomings()) {
			total = NumberUtil.add(total, incomingVar.getValue());
		}
		return total;
	}
}
