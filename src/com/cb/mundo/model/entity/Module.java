/*
 * *@autor Yamarty
 * @fecha 20-03-2015
 */

package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa um modulo de Escola ou Academia.
 * Pode ser modulo de formacao ou complementar ou gira de consulta
 * 
 * @author Yamarty
 * @since 24 MAR 2015
 */
@Entity
@Audited
public class Module implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private School school;
	
	
	@Size(max=10)
	private String sigla;
	

	@NotNull
	private Boolean flagFormation = false;
	
	
	@Size(max=200)
	@NotNull
	private String descriptionPT;
	
	@Size(max=200)
	@NotNull
	private String descriptionES;
	
	@Size(max=200)
	@NotNull
	private String descriptionEN;

	@Size(max=200)
	@NotNull
	private String descriptionIT;
	
	@Size(max=200)
	@NotNull
	private String descriptionFR;
	
	
	
	
	@Transient
	private Idiom currentIdiom;
	

	//associacoes...
	
	@OneToMany(mappedBy="module")
	private List<Event> events;
	
	
	@OneToMany(mappedBy="module")
	private List<Production> productions;
	
	
	@OneToMany(mappedBy="module")
	private List<CertificateNumber> certificateNumbers;
	
	
	
	
	//acessores...
	private static final long serialVersionUID = -7637176450340760388L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getFlagFormation() {
		return flagFormation;
	}
	public void setFlagFormation(Boolean flagFormation) {
		this.flagFormation = flagFormation;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getDescriptionPT() {
		return descriptionPT;
	}
	public void setDescriptionPT(String descriptionPT) {
		this.descriptionPT = descriptionPT;
	}
	public String getDescriptionEN() {
		return descriptionEN;
	}
	public void setDescriptionEN(String descriptionEN) {
		this.descriptionEN = descriptionEN;
	}
	public String getDescriptionIT() {
		return descriptionIT;
	}
	public void setDescriptionIT(String descriptionIT) {
		this.descriptionIT = descriptionIT;
	}
	public String getDescriptionFR() {
		return descriptionFR;
	}
	public void setDescriptionFR(String descriptionFR) {
		this.descriptionFR = descriptionFR;
	}
	public String getDescriptionES() {
		return descriptionES;
	}
	public void setDescriptionES(String description) {
		this.descriptionES = description;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public List<Production> getProductions() {
		return productions;
	}
	public void setProductions(List<Production> productions) {
		this.productions = productions;
	}
	public List<CertificateNumber> getCertificateNumbers() {
		return certificateNumbers;
	}
	public void setCertificateNumbers(List<CertificateNumber> certificateNumbers) {
		this.certificateNumbers = certificateNumbers;
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
		Module other = (Module) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Module [id=" + id + ", sigla=" + sigla + ", flagFormation="	+ flagFormation + ", descriptionPT=" + descriptionPT + ", school=" + school + ", currentIdiom=" + currentIdiom + "]";
	}
	
	
	public boolean isTransient() {
		return getId()==null;
	}
	
	
	//runtime
	
	public void setCurrentIdiom(Idiom currentIdiom) {
		this.currentIdiom = currentIdiom;
	}

	public Idiom getCurrentIdiom() {
		if (currentIdiom==null) {
			currentIdiom = Idiom.pt;//pt eh o default
		}
		return currentIdiom;
	}
	
	
	private String getLocalizedDescription() {
		switch(getCurrentIdiom()) {
		case pt:
			return getDescriptionPT();
		case es:
			return getDescriptionES();
		case en:
			return getDescriptionEN();
		case it:
			return getDescriptionIT();
		case fr:
			return getDescriptionFR();
		default:
			return "?";
		}
	}
	
	public String getFullDesc() {
		if (flagFormation) {
			return String.format("%s (%s)", getSigla(), getLocalizedDescription() );
			
		} else {
			return String.format("%s", getLocalizedDescription() );
		}
	}
	

	public String getShortDesc() {
		if (flagFormation) {
			return String.format("%s", getSigla() );
			
		} else {
			return String.format("%s", getLocalizedDescription() );
		}
	}
	
	
}
