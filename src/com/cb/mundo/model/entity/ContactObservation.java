package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

/**
 * Observacoes de Contact
 * @author Solkam
 * @since 25 MAR 2015
 */
@Entity
@Audited
public class ContactObservation implements Serializable {
    

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Size(max=2000)
    @NotNull
    private String text;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date entryDate;
    
    @ManyToOne
    @NotNull
    private Contact contact;
    
    @NotNull
    private String createdBy;
    
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    
    //listener
    @PrePersist void onPersit() {
    	createDate = new Date();
    	updateDate = new Date();
    }
    
    @PreUpdate void onUpdate() {
    	updateDate = new Date();
    }


    
    //acessores...
    private static final long serialVersionUID = 2124130236464273492L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
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
		ContactObservation other = (ContactObservation) obj;
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
