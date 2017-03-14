package com.cb.mundo.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

/**
 * Entidade que representa os confirmados nos quartos
 * 
 * @author Solkam
 * @since 23 ABR 2014
 */
@Entity
@Audited
public class HostingConfirmed implements Serializable {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@NotNull
	private Hosting hosting;
	
	@ManyToOne
	@NotNull
	private Register register;

	
//construores...	
	public HostingConfirmed() {}
	
	public HostingConfirmed(Hosting hosting, Register register) {
		super();
		this.hosting = hosting;
		this.register = register;
	}



//acessores...	
	private static final long serialVersionUID = 5087161390291502122L;
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

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
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
		HostingConfirmed other = (HostingConfirmed) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
