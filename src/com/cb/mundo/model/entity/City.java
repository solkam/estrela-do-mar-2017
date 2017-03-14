package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Representa uma cidade onde existe CB
 * 
 * @author solkam
 * @since 26 out 2011
 */
@Entity 
@Table(name="city")
@Audited
public class City implements Serializable, Comparable<City> {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=60)
	private String name;
	
	@ManyToOne(optional=false)
	private Country country;
	
	private Boolean flagActive = true; //For logical remove
	
	@OneToMany(mappedBy="city")
	private List<CityTeam> teams;

	
	
//construtors...
	public City() {}
	
	public City(Long id) {
		this.id = id;
	}
	
	
	
//acessores...	
	private static final long serialVersionUID = 3244783346682190560L;
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
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}
	public List<CityTeam> getTeams() {
		return teams;
	}
	public void setTeams(List<CityTeam> teams) {
		this.teams = teams;
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
		City other = (City) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(City that) {
		return this.id.compareTo(that.id);
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", country=" + country	+ "]";
	}

	public boolean isTransient() {
		return getId()==null;
	}
	

	
//metodos especiais...	
	public String getFullDesc() {
		return String.format("%s (%s)", this.name, this.country.getName());
	}
	
	public String getSafetyName() {
		return getName().replace("/", "").replace(" ", "_");
	}


}
