package com.cb.mundo.model.util;

import java.util.Locale;

/**
 * Tradutor simples
 * @author Solkam
 *
 */
public class SimpleTranslator {
	
	/**
	 * Get word 'and' internacionalizable
	 */
	public static String getLabelAnd(Locale locale) {
		if (locale.getLanguage().equals("es")) {
			return " y ";
		}
		if (locale.getLanguage().equals("en")) {
			return " and ";
		}
		//default portugues:
		return " e ";
	}
	

}
