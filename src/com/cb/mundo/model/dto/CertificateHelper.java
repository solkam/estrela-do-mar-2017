package com.cb.mundo.model.dto;

import com.cb.mundo.model.entity.Certificate;
import com.cb.mundo.model.entity.Production;


/**
 * Helper para guardar as meta-informacoes de certificados
 * 
 * @author Solkam
 * @since 01 JAN 2014
 */
public class CertificateHelper {
	
	private String facilitator1Name;
	
	private String facilitator2Name;
	
	private String facilitator3Name;
	
	private String facilitator4Name;

	private String facilitator1Label;
	
	private String facilitator2Label;
	
	private String facilitator3Label;
	
	private String facilitator4Label;

	private String describedDate;
	
	private String localName;
	
	
	public CertificateHelper(Production production) {
		populateAttributes(production);
	}
	
	private void populateAttributes(Production production) {
		if (production.getFlagFacilitator1()) {
			facilitator1Name = production.getFacilitator1().getContact().getShortDesc();
		}
		if (production.getFlagFacilitator2() ) {
			facilitator2Name = production.getFacilitator2().getContact().getShortDesc();
		}
		if (production.getFlagFacilitator3() ) {
			facilitator3Name = production.getFacilitator3().getContact().getShortDesc();
		}
		if (production.getFlagFacilitator4() ) {
			facilitator4Name = production.getFacilitator4().getContact().getShortDesc();
		}
		
		this.localName = production.getCity().getName();
		this.describedDate = production.getDescribedDates();
	}
	
	public void assignAtributesToCertificate(Certificate certificate) {
		certificate.setFacilitator1Name( getFacilitator1Name() );
		certificate.setFacilitator2Name( getFacilitator2Name() );
		certificate.setFacilitator3Name( getFacilitator3Name() );
		certificate.setFacilitator4Name( getFacilitator4Name() );

		certificate.setFacilitator1Label( getFacilitator1Label() );
		certificate.setFacilitator2Label( getFacilitator2Label() );
		certificate.setFacilitator3Label( getFacilitator3Label() );
		certificate.setFacilitator4Label( getFacilitator4Label() );
		
		certificate.setDescribedDate( getDescribedDate() );
		certificate.setLocalName( getLocalName() );
		
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
	

	
}
