package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.Idiom;

/**
 * Representa um pais onde existe CB
 * 
 * @author solkam
 * @since 26 out 2011
 */
@Entity 
@Audited
public class Country implements Serializable, Comparable<Country> {

	@Id
	@Column(length=2)
	private String code;
	
	@Column(length=60, nullable=false)
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	
	@Enumerated(EnumType.STRING)
	private Idiom idiom = Idiom.es;
	
	private Boolean flagActive = true;
	
	
	
	@OneToMany(mappedBy="country")
	private List<City> cities;
	
	
	//listener
	@PrePersist @PreUpdate
	protected void onCreateOrUpdate() {
		code = code.toUpperCase();
	}
	
	
	//acessors
	private static final long serialVersionUID = 7277196332415409710L;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}
	public Idiom getIdiom() {
		return idiom;
	}
	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Country other = (Country) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [code=" + code + ", name=" + name + "]";
	}
	
	
	@Override
	public int compareTo(Country that) {
		return this.code.compareTo(that.code);
	}

	
	public boolean isTransient() {
		return getCode()==null;
	}
	
	
	public String getFullDesc() {
		return String.format("%s", name);
	}
	
	

}
