package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Informacao da viagem do contato que entra num mega-evento
 * 
 * @author Solkam
 * @since 25 nov 2012
 */
@Embeddable
public class TripInfo implements Serializable {
	private static final long serialVersionUID = 376902708990033069L;

	@Temporal(TemporalType.DATE)
	private Date tripArrivalDate;
	
	@Temporal(TemporalType.TIME)
	private Date tripArrivalTime;
	
	@Column(length=60)
	private String tripArrivalFlightCompany;
	
	@Column(length=30)
	private String tripArrivalFlightNumber;
	
	
	@Temporal(TemporalType.DATE)
	private Date tripDepartureDate;

	@Temporal(TemporalType.TIME)
	private Date tripDepartureTime;
	
	@Column(length=60)
	private String tripDepartureFlightCompany;
	
	@Column(length=30)
	private String tripDepartureFlightNumber;
	
	
	@Column(length=2000)
	private String tripNote;

//acessors...	
	public Date getTripArrivalDate() {
		return tripArrivalDate;
	}

	public void setTripArrivalDate(Date tripArrivalDate) {
		this.tripArrivalDate = tripArrivalDate;
	}

	public Date getTripArrivalTime() {
		return tripArrivalTime;
	}

	public void setTripArrivalTime(Date tripArrivalTime) {
		this.tripArrivalTime = tripArrivalTime;
	}

	public String getTripArrivalFlightCompany() {
		return tripArrivalFlightCompany;
	}

	public void setTripArrivalFlightCompany(String tripArrivalFlightCompany) {
		this.tripArrivalFlightCompany = tripArrivalFlightCompany;
	}

	public String getTripArrivalFlightNumber() {
		return tripArrivalFlightNumber;
	}

	public void setTripArrivalFlightNumber(String tripArrivalFlightNumber) {
		this.tripArrivalFlightNumber = tripArrivalFlightNumber;
	}

	public Date getTripDepartureDate() {
		return tripDepartureDate;
	}

	public void setTripDepartureDate(Date tripDepartureDate) {
		this.tripDepartureDate = tripDepartureDate;
	}

	public Date getTripDepartureTime() {
		return tripDepartureTime;
	}

	public void setTripDepartureTime(Date tripDepartureTime) {
		this.tripDepartureTime = tripDepartureTime;
	}

	public String getTripDepartureFlightCompany() {
		return tripDepartureFlightCompany;
	}

	public void setTripDepartureFlightCompany(String tripDepartureFlightCompany) {
		this.tripDepartureFlightCompany = tripDepartureFlightCompany;
	}

	public String getTripDepartureFlightNumber() {
		return tripDepartureFlightNumber;
	}

	public void setTripDepartureFlightNumber(String tripDepartureFlightNumber) {
		this.tripDepartureFlightNumber = tripDepartureFlightNumber;
	}

	public String getTripNote() {
		return tripNote;
	}

	public void setTripNote(String tripNote) {
		this.tripNote = tripNote;
	}
	

	
	

}
