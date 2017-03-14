package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;

/**
 * Representa um participante de seminario
 * 
 * @author solkam
 * @since 07 out 2011
 */
@Entity
@Table(name="participant")
@Audited
public class Participant implements Serializable  {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Production production = new Production();
	
	@ManyToOne(optional=false)
	private Contact contact = new Contact();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=30)
	private SeminarPaymentMethod paymentMethod = SeminarPaymentMethod.CASH;
	
	private double alreadyPaidValue;
	
	private double pendentValue;
	
	private int paymentQuota = 1;
	
	@ManyToOne
	private Contact registerContact;
	
	@Temporal(TemporalType.DATE)
	private Date registerDate;
	
	
	@PrePersist
	protected void onPersist() {
		if (registerDate==null) {
			registerDate = new Date();
		}
	}
	
	
	//acessores...
	private static final long serialVersionUID = 3345772465589436438L;
	
	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public SeminarPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(SeminarPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public double getAlreadyPaidValue() {
		return alreadyPaidValue;
	}
	public void setAlreadyPaidValue(double alreadyPaidValue) {
		this.alreadyPaidValue = alreadyPaidValue;
	}
	public double getPendentValue() {
		return pendentValue;
	}
	public void setPendentValue(double pendentValue) {
		this.pendentValue = pendentValue;
	}
	public int getPaymentQuota() {
		return paymentQuota;
	}
	public void setPaymentQuota(int paymentQuota) {
		this.paymentQuota = paymentQuota;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Contact getRegisterContact() {
		return registerContact;
	}
	public void setRegisterContact(Contact registerContact) {
		this.registerContact = registerContact;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
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
		Participant other = (Participant) obj;
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
