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

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.IntegrantRole;

/**
 * Representa um membro da equipe de producao 
 * como Produtor, Lider-produtor ou co-produtor.  
 * 
 * @author solkam
 * @since 13 out 2011
 */
@Entity
@Audited
public class Integrant implements Serializable {
	private static final long serialVersionUID = -9118053431031774494L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Production production = new Production();
	
	@ManyToOne(optional=false)
	private Contact contact = new Contact();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=30)
	private IntegrantRole role = IntegrantRole.PROD;
	
	
	/**
	 * Flag para receber informe feedback?
	 */
	private Boolean flagInformeFeedback = false;
	
	
	/**
	 * Flag para receber informe de rendicion
	 */
	private Boolean flagInformeRendicion = false;
	
	
	

	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public IntegrantRole getRole() {
		return role;
	}

	public void setRole(IntegrantRole role) {
		this.role = role;
	}

	public Boolean getFlagInformeFeedback() {
		return flagInformeFeedback;
	}

	public void setFlagInformeFeedback(Boolean flagInformeFeedback) {
		this.flagInformeFeedback = flagInformeFeedback;
	}

	public Boolean getFlagInformeRendicion() {
		return flagInformeRendicion;
	}

	public void setFlagInformeRendicion(Boolean flagInformeRendicion) {
		this.flagInformeRendicion = flagInformeRendicion;
	}

	
	

	@Override
	public String toString() {
		return "Integrant [id=" + id + ", contact=" + contact + ", role="+ role + ", production=" + production+ ", flagInformeFeedback=" + flagInformeFeedback+ ", flagInformeRendicion=" + flagInformeRendicion + "]";
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
		Integrant other = (Integrant) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
