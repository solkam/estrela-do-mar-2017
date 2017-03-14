package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

/**
 * Representa uma questao do formulario medico no ato da inscrição
 * 
 * @Solkam
 * @since 20 ABR 2012
 */
@Entity
@Table(name="medical_question")
@Audited
public class MedicalQuestion implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)    
	private Long id;
	
	@NotNull
	@Size(max=30)
	private String number;//for question order

	@NotNull
	@Size(max=512)
	private String textPT;

	@NotNull
	@Size(max=512)
	private String textES;

	private Boolean flagActive = true;


	

	private static final long serialVersionUID = -3136890302220599272L;
//acessores...
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}
	public String getTextPT() {
		return textPT;
	}
	public void setTextPT(String textPT) {
		this.textPT = textPT;
	}
	public String getTextES() {
		return textES;
	}
	public void setTextES(String textES) {
		this.textES = textES;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
		MedicalQuestion other = (MedicalQuestion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

//metodos especiais
	public String displayTextByLocale(Locale locale) {
		if ("pt".equals(locale.getLanguage() )) {
			return getTextPT();
		} else {
			return getTextES();
		}
	}


	
}
