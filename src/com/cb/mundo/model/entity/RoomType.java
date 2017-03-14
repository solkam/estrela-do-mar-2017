package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

/**
 * Indica os tipos de quartos
 * com o numero de pessoas e disposicao
 * das camas.
 *  
 * @author Solkam
 * @since 25 MAI 2015
 */
@Entity
@Audited
public class RoomType implements Serializable {

	@Id
	private String code;
	
	@NotNull
	private String description;
	
	@NotNull
	private Integer maxSupported;
	
	
	private Boolean flagActive = true;
	
	

	
	//acessores...
	private static final long serialVersionUID = 3162669792656626314L;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getMaxSupported() {
		return maxSupported;
	}
	public void setMaxSupported(Integer maxSupported) {
		this.maxSupported = maxSupported;
	}
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		RoomType other = (RoomType) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	

}
