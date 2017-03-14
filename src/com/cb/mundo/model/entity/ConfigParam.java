package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.cb.mundo.model.entity.enumeration.ConfigParamName;

/**
 * Entidade para guardar parametros de configuração
 * @author Solkam
 * @since 20 ABR 2015
 */
@Entity
public class ConfigParam implements Serializable {

	@Id
	@Enumerated(EnumType.STRING)
	private ConfigParamName paramName;

	
	@Size(max=1000)
	private String paramValue;


	
	//acessores...
	private static final long serialVersionUID = -466203455759165757L;
	public ConfigParamName getParamName() {
		return paramName;
	}
	public void setParamName(ConfigParamName paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((paramName == null) ? 0 : paramName.hashCode());
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
		ConfigParam other = (ConfigParam) obj;
		if (paramName != other.paramName)
			return false;
		return true;
	}
	
	
	public int getValueAsInteger() {
		try {
			return Integer.parseInt( getParamValue() );
			
		}catch(NumberFormatException e) {
			return -1;
		}
	}
	
	
}
