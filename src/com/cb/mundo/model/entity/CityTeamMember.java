package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.IntegrantRole;

/**
 * Representa um membro da equipe de producao da cidade
 * @author Solkam
 * @since 08 DEZ 2014
 */
@Entity
@Audited
public class CityTeamMember implements Serializable {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne
	@NotNull
	private CityTeam cityTeam;
	
	
	@ManyToOne
	@NotNull
	private Contact contact;
	
	
	@Enumerated(EnumType.STRING)
	private IntegrantRole role = IntegrantRole.PROD;
	
	
	@Temporal(TemporalType.DATE)
	private Date dateIntegrantSince;
	
	/**
	 * Deve receber informes de feedback
	 */
	private Boolean flagInformeFeedback = false;
	
	/**
	 * Deve receber informes de rendiciones
	 */
	private Boolean flagInformeRendicion = false;

	
	
	//acessores...
	private static final long serialVersionUID = -3557839961494636030L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CityTeam getCityTeam() {
		return cityTeam;
	}
	public void setCityTeam(CityTeam cityTeam) {
		this.cityTeam = cityTeam;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public IntegrantRole getRole() {
		return role;
	}
	public void setRole(IntegrantRole role) {
		this.role = role;
	}
	public Date getDateIntegrantSince() {
		return dateIntegrantSince;
	}
	public void setDateIntegrantSince(Date dateIntegrantSince) {
		this.dateIntegrantSince = dateIntegrantSince;
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
		CityTeamMember other = (CityTeamMember) obj;
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
