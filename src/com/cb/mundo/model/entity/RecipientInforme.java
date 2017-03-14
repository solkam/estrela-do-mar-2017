package com.cb.mundo.model.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.InformeType;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Destinatario de email de um informe
 * 
 * @author Solkam
 * @since 24 out 2011
 */
@Entity
@Table(name="recipient_informe")
@Audited
public class RecipientInforme {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Contact contact;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=3)
	private School school;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=30)
	private InformeType informeType;
	
	
	
	
	public RecipientInforme(Contact contact, School school,
			InformeType informeType) {
		super();
		this.contact = contact;
		this.school = school;
		this.informeType = informeType;
	}


	public RecipientInforme() {
		super();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public InformeType getInformeType() {
		return informeType;
	}
	public void setInformeType(InformeType informeType) {
		this.informeType = informeType;
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
		RecipientInforme other = (RecipientInforme) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.contact.getName()+" (" + this.contact.getEmail() + ")";
	}
	

}
