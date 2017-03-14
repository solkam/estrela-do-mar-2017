package com.cb.mundo.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

/**
 * Entidade que representa o detalhe da hospedagem
 * indicando o register e a hospedagem
 * 
 * @author Solkam
 * @since 21 ABR 2014
 */
@Entity
@Audited
public class HostingSugested implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@NotNull
	private Hosting hosting;
	
	@ManyToOne
	@NotNull
	private Contact contact;

	
//construtores...	
	public HostingSugested() {}
	
	public HostingSugested(Hosting hosting, Contact contact) {
		this.hosting = hosting;
		this.contact = contact;
	}




//acessores...	
	private static final long serialVersionUID = 749366586788912447L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Hosting getHosting() {
		return hosting;
	}
	public void setHosting(Hosting hosting) {
		this.hosting = hosting;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
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
		HostingSugested other = (HostingSugested) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
