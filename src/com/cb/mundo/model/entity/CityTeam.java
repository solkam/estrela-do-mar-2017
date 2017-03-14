package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa a equipe de producao de uma cidade.
 * 
 * @author Solkam
 * @since 10 FEV 2014
 */
@Entity
@Audited
public class CityTeam implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private City city;
	
	@Enumerated(EnumType.STRING)
	private School school;
	
	@NotNull
	private String email;
	
	
	@OneToMany(mappedBy="cityTeam")
	private List<CityTeamMember> members;
	
	
	
	//construtores...
	
	public CityTeam() {
	}
	
	public CityTeam(City c, School s) {
		this.city = c;
		this.school = s;
	}
	
	
	//acessores...
	
	private static final long serialVersionUID = 1518068885420418536L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<CityTeamMember> getMembers() {
		return members;
	}
	public void setMembers(List<CityTeamMember> members) {
		this.members = members;
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
		CityTeam other = (CityTeam) obj;
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
