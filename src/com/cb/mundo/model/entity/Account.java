package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/*
 * ** script carga inicial **

//criar conta para todos os contatos cujo id sera o mesmo do contato
select concat('insert into account (id, contact_id) values (', c.id, ', ',  c.id, ');') 
from contact c, register r where r.contact_id=c.id;


update register set account_id=contact_id where account_id is null;

select concat('update register_credit set account_id=' , r.account_id , ' where register_id=' , r.id , ';' ) 
from register r, register_credit rc 
where r.id=rc.register_id and rc.account_id is null;

alter table register_credit drop column register_id;
 */

/**
 * Representa uma conta de contato, 
 * onde acumula creditos atraves de diferentes
 * mega eventos.
 *
 * @author Solkam
 * @since 24 JUN 2013
 */
@Entity
@Table(name="account")
@Audited
public class Account implements Serializable {

	/**
	 * O valor da PK sera o mesmo da FK para contact
	 */
	@Id
	private Long id;
	
	@OneToOne(optional=false)
	@JoinColumn(unique=true)
	private Contact contact;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@OneToMany(mappedBy="account")
	private List<RegisterCredit> credits;
	
	
	
	
	/**
	 * Antes de persistir: 
	 * - seta o ID como contact_id
	 * - seta o tiemstamp
	 */
	@PrePersist
	protected void handlePK() {
		this.id = contact.getId();
		this.createDate = new Date();
	}

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
	public List<RegisterCredit> getCredits() {
		return credits;
	}
	public void setCredits(List<RegisterCredit> credits) {
		this.credits = credits;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	private static final long serialVersionUID = -5238602725370041999L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
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
		Account other = (Account) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		return true;
	}


//metodos especiais...
	
	/**
	 * Calcula sub-demanda o total de creditos da conta
	 * @return
	 */
	public BigDecimal getCalculatedCreditValue() {
		BigDecimal total = new BigDecimal("0.00");
		if (getCredits()!=null) {
			for (RegisterCredit credit : getCredits()) {
				total = total.add( credit.getValue() );
			}
		}
		return total;
	}
	   
}
