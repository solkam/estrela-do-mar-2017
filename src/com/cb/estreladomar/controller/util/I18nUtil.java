package com.cb.estreladomar.controller.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Utilit�rio para messagens de i18n.
 * Copia simplificada do que j� tem em seminario.
 * 
 * @author Solkam
 * @since 26 JUL 2013
 */
public class I18nUtil {
	
	public static String getSimpleMessage(String key) {
		return getBundle().getString(key);
	}
	
	
	private static ResourceBundle getBundle() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();

		String bundleName = facesCtx.getApplication().getMessageBundle();
		Locale locale     = facesCtx.getViewRoot().getLocale();
		
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale );
		return bundle;
	}

}
