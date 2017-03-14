package com.cb.mundo.model.report;

import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Helper para mostra combo de mes-ano em relatorios
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public class MonthYearDisplayer {
	
	private Calendar internalCalendar;
	private Locale locale;
	private boolean isFirstDayOfMonth;

	public MonthYearDisplayer(Calendar calendar, Locale locale, boolean isFirstDayOfMonth) {
		this.locale = locale;
		this.isFirstDayOfMonth = isFirstDayOfMonth;
		
		internalCalendar = new GregorianCalendar(locale);
		internalCalendar.setTime( calendar.getTime() );
	}
	
	
	private void adjustInternalCalendar() {
		if (isFirstDayOfMonth) {
			internalCalendar.set(Calendar.DAY_OF_MONTH, 1);
		} else {
			int lastDayOfMouth = internalCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			internalCalendar.set(Calendar.DAY_OF_MONTH, lastDayOfMouth);
		}
	}

	
	public String getDisplay() {
		String monthDisplay = internalCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);
		String yearDisplay  = Integer.toString( internalCalendar.get(Calendar.YEAR) );
		return String.format("%s/%s", monthDisplay, yearDisplay); 
	}

	public Calendar getCalendar() {
		adjustInternalCalendar();
		return internalCalendar;
	}
	
	public void setTime(long time) {
		this.internalCalendar.setTimeInMillis( time );
	}
	
	

}
