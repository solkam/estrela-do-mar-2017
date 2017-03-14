package com.cb.mundo.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.config.CertificateConfigConstant;


/**
 * Representa a certificado generico (todas as escolas)
 * 
 * @author solkam
 * @since 02 out 2011
 */
@Entity
@Audited
public class Certificate implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=20)
	private String number;
	
	@Column(length=200)
	private String nameOnCertificate;
	
	@Column(length=200)
	private String facilitator1Name;
	
	@Column(length=200)
	private String facilitator2Name;
	
	@Column(length=200)
	private String facilitator3Name;
	
	@Column(length=200)
	private String facilitator4Name;
	
	@Column(length=90)
	private String facilitator1Label;
	
	@Column(length=90)
	private String facilitator2Label;
	
	@Column(length=90)
	private String facilitator3Label;
	
	@Column(length=90)
	private String facilitator4Label;
	


	@Column(length=90)
	private String describedDate;
	
	@Column(length=200)
	private String localName;
	
	@ManyToOne
	private Production production;

	
	public Certificate() {}
	
	public Certificate(String number, String nameOnCertificate,
			String facilitator1Name, String facilitator2Name,
			String facilitator3Name, String describedDate, String localName) {
		super();
		this.number = number;
		this.nameOnCertificate = nameOnCertificate;
		this.facilitator1Name = facilitator1Name;
		this.facilitator2Name = facilitator2Name;
		this.facilitator3Name = facilitator3Name;
		this.describedDate = describedDate;
		this.localName = localName;
	}


	//acessores...
	private static final long serialVersionUID = -3026456856808474731L;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getNameOnCertificate() {
		return nameOnCertificate;
	}
	public void setNameOnCertificate(String nameOnCertificate) {
		this.nameOnCertificate = nameOnCertificate;
	}
	public String getFacilitator1Name() {
		return facilitator1Name;
	}
	public void setFacilitator1Name(String facilitator1Name) {
		this.facilitator1Name = facilitator1Name;
	}
	public String getFacilitator2Name() {
		return facilitator2Name;
	}
	public void setFacilitator2Name(String facilitator2Name) {
		this.facilitator2Name = facilitator2Name;
	}
	public String getFacilitator3Name() {
		return facilitator3Name;
	}
	public void setFacilitator3Name(String facilitator3Name) {
		this.facilitator3Name = facilitator3Name;
	}
	public String getFacilitator4Name() {
		return facilitator4Name;
	}
	public void setFacilitator4Name(String facilitator4Name) {
		this.facilitator4Name = facilitator4Name;
	}
	public String getDescribedDate() {
		return describedDate;
	}
	public void setDescribedDate(String describedDate) {
		this.describedDate = describedDate;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFacilitator1Label() {
		return facilitator1Label;
	}

	public void setFacilitator1Label(String facilitator1Label) {
		this.facilitator1Label = facilitator1Label;
	}

	public String getFacilitator2Label() {
		return facilitator2Label;
	}

	public void setFacilitator2Label(String facilitator2Label) {
		this.facilitator2Label = facilitator2Label;
	}

	public String getFacilitator3Label() {
		return facilitator3Label;
	}

	public void setFacilitator3Label(String facilitator3Label) {
		this.facilitator3Label = facilitator3Label;
	}

	public String getFacilitator4Label() {
		return facilitator4Label;
	}

	public void setFacilitator4Label(String facilitator4Label) {
		this.facilitator4Label = facilitator4Label;
	}

	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}


	// realtime getters:
	private static final String FILE_NAME_PATTERN = "%s_%s_certNum%s.png";
	
	public String getFileName() {
		return String.format(FILE_NAME_PATTERN
				,getProduction().getSchool()
				,getProduction().getModule().getSigla()
				,getNumber() );
	}

	/**
	 * Path relativo da imagem do certificate (a partir do folder onde foram gerados os certificados)
	 * @return
	 */
	public String getWebPath() {
		return "/"+CertificateConfigConstant.CERTIFICATE_GENERATED_FOLDER + "/" + getFileName();
	}
	
	public boolean isNumberOK() {
		return getNumber()!=null && !getNumber().trim().isEmpty();
	}

}
