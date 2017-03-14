package com.cb.mundo.model.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cb.mundo.model.entity.EventWeek;

/**
 * Helper para Informe 7.6: Movimento de Pessoas
 * 
 * @author Solkam
 * @since 05 dez 2012
 */
public class MegaEventMovimentSummary implements Serializable {
	private static final long serialVersionUID = 5575984324518066622L;
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy EEEE");
	

	private Date date;
	
	private EventWeek eventWeek;

	
	private Long insideQuantity;
	
	private Long arrivingQuantity;
	
	private Long leavingQuantity;

	
//construtores...	
	public MegaEventMovimentSummary() {
	}
	

	public MegaEventMovimentSummary(Date date, EventWeek eventWeek, Long insideQuantity, Long arrivingQuantity, Long leavingQuantity) {
		super();
		this.date = date;
		this.eventWeek = eventWeek;
		this.insideQuantity = insideQuantity;
		this.arrivingQuantity = arrivingQuantity;
		this.leavingQuantity = leavingQuantity;
	}


//acessores...	
	public Long getTotalQuantity() {
		return insideQuantity + arrivingQuantity - leavingQuantity;
	}
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EventWeek getEventWeek() {
		return eventWeek;
	}

	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}

	public Long getInsideQuantity() {
		return insideQuantity;
	}

	public void setInsideQuantity(Long insideQuantity) {
		this.insideQuantity = insideQuantity;
	}

	public Long getArrivingQuantity() {
		return arrivingQuantity;
	}

	public void setArrivingQuantity(Long arrivingQuantity) {
		this.arrivingQuantity = arrivingQuantity;
	}

	public Long getLeavingQuantity() {
		return leavingQuantity;
	}

	public void setLeavingQuantity(Long leavingQuantity) {
		this.leavingQuantity = leavingQuantity;
	}
	
	/**
	 * Formata a data no estilo '25/09/12 Terca-feira'
	 * @return
	 */
	public String getDateDesc() {
		return DATE_FORMAT.format( this.date );
	}

	

}
