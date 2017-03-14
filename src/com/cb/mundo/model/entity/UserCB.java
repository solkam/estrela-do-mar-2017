package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa um Usuario no sistema 
 * com credenciais.
 * 
 * @author solkam
 * @since 17 ago 2011
 */
@Entity
@Audited
public class UserCB implements Serializable {
	private static final long serialVersionUID = 8812356431524818273L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Contact contact;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=3)
	private Profile profile;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=3)
	private School defaultSchool;
	
	@ManyToOne
	private City defaultCity;
	
	@Enumerated(EnumType.STRING)
	@Column(length=2)
	private Idiom defaultIdiom;
	

	// allowed fields...
	
	@ElementCollection(targetClass=School.class)
	@CollectionTable(name="user_x_school",
		joinColumns=@JoinColumn(name="user_id")
	)
	@Enumerated(EnumType.STRING)
	@Column(name="school")
	private List<School> allowedSchools;
	
	@ManyToMany
	@JoinTable(name="user_x_city", 
		joinColumns=@JoinColumn(name="user_id"),
		inverseJoinColumns=@JoinColumn(name="city_id")
	)
	@OrderBy("name")
	private List<City> allowedCities;
	

	//credentials
	
	@Column(nullable=false, length=500)
	private String password;

	
	//logs
	@Size(max=100)
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Size(max=100)
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	
	@PrePersist
	@PreUpdate
	protected void onPersist() {
		if (this.defaultIdiom==null) {
			this.defaultIdiom = Idiom.pt;
		}
	}
	
	
//acessores...	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public School getDefaultSchool() {
		return defaultSchool;
	}
	public void setDefaultSchool(School school) {
		this.defaultSchool = school;
	}
	public City getDefaultCity() {
		return defaultCity;
	}
	public void setDefaultCity(City city) {
		this.defaultCity = city;
	}
	public List<School> getAllowedSchools() {
		return allowedSchools;
	}
	public void setAllowedSchools(List<School> allowedSchools) {
		this.allowedSchools = allowedSchools;
	}
	public List<City> getAllowedCities() {
		return allowedCities;
	}
	public void setAllowedCities(List<City> allowedCities) {
		this.allowedCities = allowedCities;
	}
	public Idiom getDefaultIdiom() {
		return defaultIdiom;
	}
	public void setDefaultIdiom(Idiom defaultIdiom) {
		this.defaultIdiom = defaultIdiom;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
		UserCB other = (UserCB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "UserCB [id=" + id + ", profile=" + profile + ", defaultSchool="+ defaultSchool + "]";
	}

	
	
	public boolean isTransient() {
		return getId()==null;
	}
	

	/**
	 * Super usuario eh quem tem super perfil
	 * @return
	 */
	public boolean isSuperUser() {
		return getProfile().getFlagSuperProfile();
	}
	
	
	/**
	 * Verifica se é usuario TEC
	 * (para acoes de extrema gravidade)
	 * @return
	 */
	public boolean isProfileTecnology() {
		return Profile.TEC.equals( getProfile() );
	}
	
	
	/**
	 * Helper para adiciona escola permitida
	 */
	public void addAllowedSchool(School school) {
		if (getAllowedSchools()==null) {
			setAllowedSchools( new ArrayList<School>() );
		}
		getAllowedSchools().add( school );
	}
	
	/**
	 * Helper para remove escola permitida
	 * @param school
	 */
	public void removeAllowedSchool(School school) {
		if (getAllowedSchools()!=null) {
			getAllowedSchools().remove( school );
		}
	}
	
	
	/**
	 * Helper para adicionar cidades permitidas
	 * @param city
	 */
	public void addAllowedCity(City city) {
		if (getAllowedCities()==null) {
			setAllowedCities( new ArrayList<City>() );
		}
		getAllowedCities().add( city );
	}
	
	/**
	 * Helper para remover cidades
	 * @param city
	 */
	public void removeAllowedCity(City city) {
		if (getAllowedCities()!=null) {
			getAllowedCities().remove( city );
		}
	}
	
	
	/**
	 * Retorna o que esta sendo usado como username,
	 * no caso, o email do contact
	 * @return
	 */
	public String getDesc() {
		return getContact().getEmail();
	}

	
	public String getDescAllowedCities() {
		StringBuilder descCities = new StringBuilder();
		for (City city : getAllowedCities()) {
			descCities.append( city.getName() );
			descCities.append(", ");
		}
		return descCities.toString();
	}
	

}
