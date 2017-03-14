package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.StaffRole;

/**
 * Representa um staff de producao relaciona o
 * contato e a funcao
 * 
 * @author solkam
 * @since 08 out 2011
 */
@Entity
@Audited
public class Staff implements Serializable {
	
	public static final double INCOMING_VALUE = 290.00;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@NotNull
	private Production production = new Production();
	
	@ManyToOne
	@NotNull
	private Contact contact = new Contact();
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private StaffRole role;
	
	
	@NotNull
	private Double valuePaid = INCOMING_VALUE;//pode ser zero mas nunca null
		
		
	
//construtors
	public Staff() {}
	
	
	public Staff(Production production, Contact contact, StaffRole role) {
		super();
		this.production = production;
		this.contact = contact;
		this.role = role;
	}




//acessors
	private static final long serialVersionUID = -7842178710885248949L;
	
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

	public StaffRole getRole() {
		return role;
	}

	public void setRole(StaffRole role) {
		this.role = role;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Double getValuePaid() {
		return valuePaid;
	}

	public void setValuePaid(Double valuePaid) {
		this.valuePaid = valuePaid;
	}

	@Override
	public String toString() {
		return "Staff [production=" + production.getSchool() 
				+ ", person=" + contact.getName() 
				+ ", staffRole=" + role + "]";
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
		Staff other = (Staff) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public boolean isNewOne() {
		return this.id==null;
	}

}
