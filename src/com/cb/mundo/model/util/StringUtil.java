package com.cb.mundo.model.util;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Utilitarios para manipular string
 * 
 * @author Solkam
 * @since 09 FEV 2013
 */
public class StringUtil {
	
	private static final String EMPTY_STRING = "";
	
	/**
	 * Receve uma frase e coloca as primeiras letras de cada palavar em caixa alta
	 * @param phrase
	 * @return frase com primeiras letras em caixa alta
	 */
	public static String initCap(String phrase) {
		if (phrase==null || phrase.trim().isEmpty()) {
			return EMPTY_STRING; 
		}
		
		char[] chars = phrase.toCharArray();
		boolean found = false;
		for (int i=0; i<chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			
			}else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
				found = false;
			}
		}
		return String.valueOf(chars);
	}
	
	
	/**
	 * Faz a troca textual por outro texto
	 * @param text
	 * @param key
	 * @param newValue
	 * @return
	 */
	public static String replace(String text, String key, String newValue) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, nvl(newValue) );
	}

	public static String replace(String text, String key, Integer intValue) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, String.valueOf(intValue) );
	}

	public static String replace(String text, String key, double doubleValue) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, String.valueOf(doubleValue) );
	}

	public static String replace(String text, String key, BigDecimal bigDecimalValue) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, String.valueOf(bigDecimalValue) );
	}

	public static String replace(String text, String key, Date date) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, CalendarUtil.formatDate(date, "dd/MM/yyyy") );
	}
	
	public static String replaceTime(String text, String key, Date date) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, CalendarUtil.formatDate(date, "HH:mm") );
	}

	public static String replace(String text, String key, Boolean flag) {
		avoidNPE(text);
		avoidNPE(key);
		return text.replaceAll(key, nvl(flag) );
	}

	public static String replace(String text, String key, Long longValue) {
		return text.replaceAll(key, String.valueOf(longValue));
	}

	
	//utils...
	
	private static void avoidNPE(String s) {
		if (s==null) {
			throw new IllegalArgumentException("Parametro null nao permitido");
		}
	}

	private static String nvl(String txt) {
		return txt==null ? "" : txt;
	}
	
	private static String nvl(Boolean bool) {
		if (bool==null || bool==false) {
			return "Não";
		} else {
			return "Sim";
		}
	}

	
	/**
	 * Faz a troca textual para outro long
	 * @param text
	 * @param key
	 * @param longValue
	 * @return
	 */
	

	
	
	public static boolean isNotBlank(String txt) {
		return txt!=null && !txt.trim().isEmpty();
	}
	
	public static boolean isBlank(String txt) {
		return txt==null || txt.trim().isEmpty();
	}
	
	
}
