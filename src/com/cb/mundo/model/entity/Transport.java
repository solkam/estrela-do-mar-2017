package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.TransportDirection;

/**
 * Entidade para controle de transporte (de entrada e saida)
 * 
 * @author Solkam
 * @since 11 OUT 2013
 */
@Entity
@Audited
public class Transport implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Size(max=90)
	@NotNull
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	
	private Integer capacity;
	
	@Enumerated(EnumType.STRING)
	private TransportDirection direction = TransportDirection.INPUT;
	
	private Boolean flagOficial = true;
	
	@ManyToOne
	@NotNull
	private MegaEvent megaEvent;
	
	@ManyToMany
	@JoinTable(name="Transport_x_Register",
		joinColumns=@JoinColumn(name="transport_id"),
		inverseJoinColumns=@JoinColumn(name="register_id")
		)
	private List<Register> registers;

	
	
	
	
	private static final long serialVersionUID = 8263101676306349557L;

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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public TransportDirection getDirection() {
		return direction;
	}
	public void setDirection(TransportDirection direction) {
		this.direction = direction;
	}
	public Boolean getFlagOficial() {
		return flagOficial;
	}
	public void setFlagOficial(Boolean flagOficial) {
		this.flagOficial = flagOficial;
	}
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public void setMegaEvent(MegaEvent megaEvent) {
		this.megaEvent = megaEvent;
	}
	public List<Register> getRegisters() {
		return registers;
	}
	public void setRegisters(List<Register> registers) {
		this.registers = registers;
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
		Transport other = (Transport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isTransient() {
		return getId()==null;
	}
   
	
//metodos especiais
	public Integer getCalculatedReminds() {
		return getCapacity() - getRegisters().size();
	}
	
	public Boolean getFlagAvailableCapacity() {
		return getCalculatedReminds() > 0;
	}
	
}
