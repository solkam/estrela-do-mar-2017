package com.cb.mundo.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

/**
 * Utilitario para formatacao de string e numeros
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public class FormatUtil {
	private final static String EMPTY_STRING = "";
	private final static String WHITESPACE = " ";
	private final static String COMA_WHITESPACE = ", ";
	private final static String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	
	
	private static Locale getDefaultLocale() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale==null) {
			locale = new Locale("pt");
		}
		return locale;
	}
	
	public static String toExtendedString(Date d) {
		if (d==null) return EMPTY_STRING;

		DateFormat format = DateFormat.getDateInstance( );
		return format.format( d );
	}
	
	public static String toMediumFormat(Date d) {
		if (d==null) return EMPTY_STRING;
		
		DateFormat format = DateFormat.getDateInstance( DateFormat.MEDIUM, getDefaultLocale() );
		return format.format( d );
	}
	
	public static String toLongFormat(Date d) {
		if (d==null) return EMPTY_STRING;
		
		DateFormat format = DateFormat.getDateInstance( DateFormat.LONG, getDefaultLocale() );
		return format.format( d );
	}
	
	
	public static String toString(Date d) {
		if (d==null) return EMPTY_STRING;
		
		DateFormat format = new SimpleDateFormat( DEFAULT_DATE_PATTERN );
		return format.format( d );
	}

//	public static String toDescribedDates(Date firstDate, Date lastDate) {
//		if (firstDate==null || lastDate==null) return EMPTY_STRING;
//		
//		return toDescribedDates(firstDate, lastDate, getDefaultLocale() );
//	}

	
	/**
	 * Descreve por extenso o periodo entre duas datas, usando
	 * tambem o locale (para nome do mes).
	 * @param firstDate
	 * @param lastDate
	 * @param locale
	 * @return
	 */
	public static String toDescribedDates(Date firstDate, Date lastDate, Locale locale) {
		if (firstDate==null || lastDate==null) return EMPTY_STRING;
		
		//convert Date to Calendar
		Calendar firstCal = Calendar.getInstance(locale);
		firstCal.setTime( firstDate );
		Calendar lastCal = Calendar.getInstance(locale);
		lastCal.setTime( lastDate );
		
		//hours adjustments for loop condition:
		firstCal.set(Calendar.HOUR_OF_DAY, 0);
		lastCal.set(Calendar.HOUR_OF_DAY, 12);
		
		final int lastDay   = lastCal.get(Calendar.DAY_OF_MONTH);
		final int lastMonth = lastCal.get(Calendar.MONTH);
		final int lastYear  = lastCal.get(Calendar.YEAR);
		int currentDay   = 0;
		int currentMonth = 0;
		
		Calendar currentCalendar = firstCal;
		StringBuilder describedDate = new StringBuilder();
		boolean firstElement = true;

		do {
			currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
			currentMonth = currentCalendar.get(Calendar.MONTH);
			
			//a) simplest case: starting date is the same month than ending date
			if (currentMonth == lastMonth) {
				//handle days
				if (currentDay < lastDay) {
					appendDay(describedDate, currentDay, firstElement);
				}
				//handle last day (append 'and' internacionalizable)
				//handle month
				if (currentDay == lastDay) {
					appendLastDay(describedDate, currentDay, locale);
					appendMonth(describedDate, currentCalendar, locale);
				}
			
			//b) when months are diferents
			} else {
				appendDay(describedDate, currentDay, firstElement);

				//append month if day is last of month
				if (isLastDayOfMonth(currentCalendar)) {
					appendMonth(describedDate, currentCalendar, locale);
				}
			}

			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
			firstElement = false;
			
		}while (currentCalendar.before( lastCal ) );

		appendYear(describedDate, lastYear);
		
		return describedDate.toString();
	}
	
	
	private static boolean isLastDayOfMonth(Calendar currentCalendar) {
		Calendar cloned = (Calendar)currentCalendar.clone();
		
		int currentMonth = cloned.get(Calendar.MONTH );
		cloned.add(Calendar.DAY_OF_MONTH, 1);
		int nextMonth = cloned.get(Calendar.MONTH);
		return currentMonth!=nextMonth;
	}

	private static void appendDay(StringBuilder describedDate, int currentDay, boolean firstElement) {
		if (!firstElement) {
			describedDate.append( COMA_WHITESPACE );
		}
		describedDate.append( currentDay );
	}
	
	private static void appendLastDay(StringBuilder describedDate, int currentDay, Locale locale) {
		describedDate.append( SimpleTranslator.getLabelAnd(locale) );
		describedDate.append( currentDay );
	}
	
	private static void appendMonth(StringBuilder describedDate, Calendar currentCalendar, Locale locale) {
		String descCurrentMonth = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
		describedDate.append( WHITESPACE );
		describedDate.append(descCurrentMonth);
	}
	
	private static void appendYear(StringBuilder describedDate, int year) {
		describedDate.append( WHITESPACE );
		describedDate.append( year );
	}
	

}
