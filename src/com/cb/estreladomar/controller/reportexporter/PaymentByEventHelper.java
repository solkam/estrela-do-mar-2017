package com.cb.estreladomar.controller.reportexporter;

import java.math.BigDecimal;
import java.util.Date;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Helper para Pagamentos por Evento
 * 
 * @author Solkam
 * @since 20 nov 2012
 */
public class PaymentByEventHelper {
	
	private final RegisterDetail detail;
	
	protected PaymentByEventHelper(RegisterDetail detail) {
		this.detail = detail;
	}
	

	protected String extractContactFullDesc() {
		return detail.getRegister().getContact().getFullDesc();
	}
	
	protected String extractContactGender() {
		if (detail.getRegister().getContact().getGender()==null) {
			return "";
		}
		return detail.getRegister().getContact().getGender().name();
	}
	
	protected String extractContactEmail() {
		return detail.getRegister().getContact().getEmail();
	}
	
	protected String extractContactCountry() {
		return detail.getRegister().getContact().getCountry();
	}
	
	protected String extractContactProductorName() {
		return detail.getRegister().getContact().getSafetyProductorName();
	}
	
	protected String extractSchoolDescription() {
		if (detail.getRegister().getContact().getRootSchool()==null) {
			return "";
		}
		return I18nUtil.getSimpleMessage( detail.getRegister().getContact().getRootSchool().getKey() );
	}
	
	protected Date extractRegisterDate() {
		return detail.getRegister().getCreateDate();
	}
	
	protected Date extractCheckinDate() {
		return detail.getRegister().getCheckin().getCheckinDate();
	}
	
	protected Date extractCheckoutDate() {
		return detail.getRegister().getCheckout().getCheckoutDate();
	}
	
	protected String extractEventPresenceName() {
		return I18nUtil.getSimpleMessage( detail.getPresence().getKey() );
	}
	
	protected BigDecimal extractRegisterDetailValue() {
		return detail.getValue();
	}
	
	protected BigDecimal extractCalculatedPaidValue() {
		return detail.getCalculatedPaidValue();
	}
	
	protected BigDecimal extractCalculatedPendentValue() {
		return detail.getCalculatedPendentValue();
	}


	public String extractContactIdentityDoc() {
		return detail.getRegister().getContact().getFullIdentityDocument();
	}


	public String extractContactBirthDay() {
		return CalendarUtil.formatDateToStandard( detail.getRegister().getContact().getBirthDate() );
	}


	public String extractEventName() {
		return detail.getEvent().getDisplayNameOrSchool();
	}


	public String extractCity() {
		return detail.getRegister().getContact().getCity();
	}


	public String extractNeighborhood() {
		return detail.getRegister().getContact().getNeighborhood();
	}


	public String extractAddress() {
		return detail.getRegister().getContact().getAddress();
	}


	public String extractZip() {
		return detail.getRegister().getContact().getZipPostal();
	}


	public String extractTelefone() {
		return detail.getRegister().getContact().getTelephone1();
	}
	
	
	
	
	
}
