package com.cb.estreladomar.controller.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.util.StringUtil;

/**
 * Utilitario para templates de email
 * 
 * @author Solkam
 * @since 04 NOV 2013
 */
public class EmailTemplateUtil {

	private static final String EMAIL_TEMPLATE_BUNDLE = "/resources/email_template";
	
	private static final String MEGA_EVENT_REFERENCE   = "@megaEventName";
	private static final String REGISTER_ID_REFERENCE  = "@registerId";
	private static final String CONTACT_NAME_REFERENCE = "@contactName";
	private static final String EVENT_ROWS_REFERENCE   = "@eventRows";
	private static final String FOOTER_ROW_REFERENCE   = "@footerRow";
	
	private static Locale getCurrentLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}
	
	private static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(EMAIL_TEMPLATE_BUNDLE, getCurrentLocale() );
	}
	
	public static String getEmailTemplate(String key) {
		return getBundle().getString(key); 
	}
	
	

//formata��o...	
	public static String formatMegaEventName(String text, Register register) {
		return StringUtil.replace(text, MEGA_EVENT_REFERENCE, register.getMegaEvent().getName() );
	}
	
	public static String formatRegisterId(String text, Register register) {
		return StringUtil.replace(text, REGISTER_ID_REFERENCE, register.getId() );
	}
	
	public static String formatContactName(String text, Register register) {
		return StringUtil.replace(text, CONTACT_NAME_REFERENCE, register.getContact().getFullDesc() );
	}
	
	public static String formatEventsRows(String text, String eventRowsReady) {
		return StringUtil.replace(text, EVENT_ROWS_REFERENCE, eventRowsReady);
	}
	
	public static String formatFooterRow(String text, String footerRow) {
		return StringUtil.replace(text, FOOTER_ROW_REFERENCE, footerRow);
	}
	
}
