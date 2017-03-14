package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.FacilitatorCategory;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa um instrutor de seminarios.
 * Considera-se que um instrutor e capacitada a ministrar todos os modulos
 * 
 * @author Solkam
 * @since 14 mar 2012
 */
@Entity
@Table(name="facilitator")
@Audited
public class Facilitator implements Serializable {
	private static final long serialVersionUID = 4208966754714427860L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Contact contact = new Contact();
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private School school;
	
	@Column(length=90)
	private String signatureFilename;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private FacilitatorCategory category;
	
	private Boolean flagActive = true;

	
	//construtors...
	public Facilitator() {}
	
	public Facilitator(Contact contact, School school) {
		this.contact = contact;
		this.school = school;
	}
	
	//acessors...
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
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}
	public String getSignatureFilename() {
		return signatureFilename;
	}
	public void setSignatureFilename(String signatureFilename) {
		this.signatureFilename = signatureFilename;
	}
	public FacilitatorCategory getCategory() {
		return category;
	}
	public void setCategory(FacilitatorCategory category) {
		this.category = category;
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
		Facilitator other = (Facilitator) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Facilitator [id=" + id + ", contact=" + contact + ", school=" + school + "]";
	}
	
	
	

}
