package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.FinancialStatus;
import com.cb.mundo.model.entity.enumeration.OutcomingCategory;

/**
 * Representa as despesas de um seminario.
 * 
 * @author Solkam
 * @since 25 set 2011
 */
@Entity
@Table(name="outcoming")
@Audited
public class Outcoming implements Serializable {
	private static final long serialVersionUID = -1222316636798633608L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Production production;
	
	@ManyToOne
	private Contact responsable = new Contact();
	
	@Column(nullable=false)
	private String description;
	
	@Column(nullable=false)
	private Double value;
	
	private Boolean paidByProduction = false;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date date = new Date();
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private OutcomingCategory category = OutcomingCategory.COFBR;//just sample
	
	@Enumerated(EnumType.STRING)
	private FinancialStatus status = FinancialStatus.PE;
	
	@Column(length=500)
	private String note;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Boolean getPaidByProduction() {
		return paidByProduction;
	}
	public void setPaidByProduction(Boolean paidByProduction) {
		this.paidByProduction = paidByProduction;
	}
	public Contact getResponsable() {
		return responsable;
	}
	public void setResponsable(Contact responsable) {
		this.responsable = responsable;
	}
	public OutcomingCategory getCategory() {
		return category;
	}
	public void setCategory(OutcomingCategory category) {
		this.category = category;
	}
	public FinancialStatus getStatus() {
		return status;
	}
	public void setStatus(FinancialStatus status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
		Outcoming other = (Outcoming) obj;
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


	
	@Override
	public String toString() {
		return "Outcoming [id=" + id + ", description=" + description
				+ ", value=" + value + ", date=" + date + ", paidByProduction="
				+ paidByProduction + ", production=" + production
				+ ", responsable=" + responsable + ", category=" + category
				+ ", status=" + status + "]";
	}
	
	
	
	
	
	
	

}
