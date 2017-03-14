package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.util.FormatUtil;

/**
 * Representa uma semana ou uma fase de um mega evento
 *  
 * RNs:
 * 1) Datas de checkin e checkout sao obrigatarias
 * 
 * @author Solkam
 * @since 14 abr 2012 
 */
@Entity
@Table(name="event_week")
@Audited
public class EventWeek implements Serializable, Comparable<EventWeek> {
	private static final long serialVersionUID = -6611114337185586674L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private MegaEvent megaEvent;

	@OneToMany(mappedBy="eventWeek")
	@OrderBy("beginDate")
	private List<Event> events = new ArrayList<Event>();
	
	@Column(length=60, nullable=false)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date beginDate;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date endDate;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date dateForCheckin;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date dateForCheckout;
	
	private Boolean flagOficial = true;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public void setMegaEvent(MegaEvent megaEvent) {
		this.megaEvent = megaEvent;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public Date getDateForCheckin() {
		return dateForCheckin;
	}
	public void setDateForCheckin(Date dateForCheckin) {
		this.dateForCheckin = dateForCheckin;
	}
	public Date getDateForCheckout() {
		return dateForCheckout;
	}
	public void setDateForCheckout(Date dateForCheckout) {
		this.dateForCheckout = dateForCheckout;
	}
	public Boolean getFlagOficial() {
		return flagOficial;
	}
	public void setFlagOficial(Boolean oficial) {
		this.flagOficial = oficial;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventWeek other = (EventWeek) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EventWeek [id=" + id + ", name=" + name + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", megaEvent="
				+ megaEvent + "]";
	}
	@Override
	public int compareTo(EventWeek that) {
		return this.id.compareTo( that.id );
	}

	
//metodos especiais	
	public String getDescDates() {
		String beginDateDesc = FormatUtil.toMediumFormat( this.beginDate );
		String endDateDesc   = FormatUtil.toMediumFormat( this.endDate );
		final String PATTERN = "%s - %s";
		return String.format(PATTERN, beginDateDesc, endDateDesc);
	}
	

	
	

}
