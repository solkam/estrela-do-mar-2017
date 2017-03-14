package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Pagamentos parciais da rendicao. 
 * Eh gerenciado pela administracao.
 * @author Solkam
 * @since 23 MAR 2015
 */
@Entity
public class RendicionPayment implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Rendicion rendicion;
	
	@NotNull
	private BigDecimal paymentValue;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;
	
	@Size(max=100)
	private String paymentWhoReceived;

	@Size(max=1000)
	private String paymentObservation;
	
	
	//acessores...
	private static final long serialVersionUID = -899023767155514938L;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentWhoReceived() {
		return paymentWhoReceived;
	}

	public void setPaymentWhoReceived(String paymentWhoReceived) {
		this.paymentWhoReceived = paymentWhoReceived;
	}

	public String getPaymentObservation() {
		return paymentObservation;
	}

	public void setPaymentObservation(String paymentObservation) {
		this.paymentObservation = paymentObservation;
	}

	public Rendicion getRendicion() {
		return rendicion;
	}

	public void setRendicion(Rendicion rendicion) {
		this.rendicion = rendicion;
	}

	public BigDecimal getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(BigDecimal paymentValue) {
		this.paymentValue = paymentValue;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
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
		RendicionPayment other = (RendicionPayment) obj;
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
	
}
