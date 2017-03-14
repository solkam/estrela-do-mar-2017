package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import com.cb.mundo.model.entity.enumeration.IncomingCategory;
import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;


/**
 * Representa uma Receita de seminario,
 * podendo ser de staff ou participante
 * 
 * @author Solkam
 * @since 25 set 2011
 */
@Entity
@Table(name="incoming")
@Audited
public class Incoming implements Serializable {
	private static final long serialVersionUID = -7210180706930972046L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional=false)
	private Production production = new Production();

	@ManyToOne(optional=true)
	private Participant participant;
	
	@ManyToOne(optional=true)
	private Staff staff;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private SeminarPaymentMethod paymentMethod = SeminarPaymentMethod.BANK_TRANSFER; 

	@Column
	private int paymentQuota = 1;
	
	@Basic(optional=false)
	private Double value;
	
	@Temporal(TemporalType.DATE)
	@Basic(optional=false)
	private Date date = new Date();
	
	@Enumerated(EnumType.STRING)
	@Basic(optional=false)
	private IncomingCategory category = IncomingCategory.P;
	
	@Enumerated(EnumType.STRING)
	@Basic(optional=false)
	private FinancialStatus status = FinancialStatus.PE;
	
	@Column(length=500)
	private String note;
	
	
	

//acessors..
	
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public IncomingCategory getCategory() {
		return category;
	}
	public void setCategory(IncomingCategory category) {
		this.category = category;
	}
	public FinancialStatus getStatus() {
		return status;
	}
	public void setStatus(FinancialStatus status) {
		this.status = status;
	}
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public SeminarPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(SeminarPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public int getPaymentQuota() {
		return paymentQuota;
	}
	public void setPaymentQuota(int paymentQuota) {
		this.paymentQuota = paymentQuota;
	}

	public boolean isParticipantCategorySelected() {
		return this.category.equals(IncomingCategory.P);
	}
	public boolean isStaffCategorySelected() {
		return this.category.equals(IncomingCategory.S);
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
		Incoming other = (Incoming) obj;
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
		return "Incoming [id=" + id + ", production=" + production
				+ ", value=" + value
				+ ", date=" + date + ", note=" + note + ", category="
				+ category + ", status=" + status + "]";
	}
	
	
/*
 * script de atualizacao:
 
alter table incoming modify column payment_method varchar(20) not null;        
        
update incoming set payment_method='CHECK' where payment_method='CH' ;
update incoming set payment_method='CASH' where payment_method='CA' ;
update incoming set payment_method='BANK_TRANFER' where payment_method='BA' ;
 
 */

	
}
