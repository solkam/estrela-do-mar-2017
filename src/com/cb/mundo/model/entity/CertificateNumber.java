package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;




import com.cb.mundo.model.entity.enumeration.School;

/**
 * Faz o gerenciamento de numeros de certificado
 * @author Solkam
 * @since 02 JUL 2012
 */
@Entity
@Audited
public class CertificateNumber implements Serializable {
	private static final long serialVersionUID = 6606553483690142529L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=3, nullable=false)
	private School school;
	
	@ManyToOne
	private Module module;
	
	@Column(nullable=false)
	private Long number;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "CertificateNumber [id=" + id + ", school=" + school
				+ ", module=" + module + ", number=" + number + "]";
	}
	
	
	
	

	

   
}
