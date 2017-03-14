package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Representa a resposta para uma dada pergunta de um contact para um megaevento
 * 
 * @Solkam
 * @since 20 abr 2012
 */
@Entity
@Table(name="medical_answer")
@Audited
public class MedicalAnswer implements Serializable {
	private static final long serialVersionUID = -5021062022228488759L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Boolean booleanAnswer;
	
	@Column(length=512)
	private String textAnswer;
	
	@ManyToOne(optional=false)
	private Register register;
	
	@ManyToOne(optional=false)
	private MedicalQuestion question;
	
	
//construtors...	
	public MedicalAnswer() {
		super();
	}
	
	public MedicalAnswer(Register register, MedicalQuestion question) {
		super();
		this.register = register;
		this.question = question;
	}




	//acessores.	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getBooleanAnswer() {
		return booleanAnswer;
	}
	public void setBooleanAnswer(Boolean booleanAnswer) {
		this.booleanAnswer = booleanAnswer;
	}
	public String getTextAnswer() {
		return textAnswer;
	}
	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	public MedicalQuestion getQuestion() {
		return question;
	}
	public void setQuestion(MedicalQuestion question) {
		this.question = question;
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
		MedicalAnswer other = (MedicalAnswer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MedicalAnswer [id=" + id + ", booleanAnswer=" + booleanAnswer
				+ ", textAnswer=" + textAnswer + ", register=" + register
				+ ", question=" + question + "]";
	}

	
	
   
}
