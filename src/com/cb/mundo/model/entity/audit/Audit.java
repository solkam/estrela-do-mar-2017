package com.cb.mundo.model.entity.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * Entidade de Revisao do Envers responsavel por guardar
 * o usuario autenticado que fez operacoes de persistencia
 * @author vitor
 * @since 20 JAN 2014
 */
@Entity
@RevisionEntity(AuditListener.class)
@Table(name="_audit")
public class Audit implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@RevisionNumber
	private Long rev;
	
	@RevisionTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String userEmail;
	
	private String remoteIP;


	
//acessores...	
	private static final long serialVersionUID = -1534610448284903032L;
	

	public Long getRev() {
		return rev;
	}
	public void setRev(Long rev) {
		this.rev = rev;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rev == null) ? 0 : rev.hashCode());
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
		Audit other = (Audit) obj;
		if (rev == null) {
			if (other.rev != null)
				return false;
		} else if (!rev.equals(other.rev))
			return false;
		return true;
	}
	
}
