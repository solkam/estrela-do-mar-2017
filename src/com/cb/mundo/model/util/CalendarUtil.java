package com.cb.mundo.model.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Utilitarios para manipulacao de datas
 * 
 * @author Solkam
 * @since 23 abr 2011
 */
public class CalendarUtil {
	
	/**
	 * Calcula a idade pela data de nascimento
	 * @param birthDate
	 * @return idade calculada
	 */
	public static int calculateAge(Date birthDate) {
		if (birthDate==null) return -1;
		
		Calendar birthCalendar = Calendar.getInstance();
		birthCalendar.setTime(birthDate);
		
		Calendar today = Calendar.getInstance();
		
		int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
			age--;
		} 
		return age;
	}

	
	/**
	 * A partir de uma data inicial e outra final, criar uma lista de datas intermediarias
	 * @param initDate
	 * @param finalDate
	 * @return lista de datas intermediarias inclusivas
	 */
	public static List<Date> buildDateList(Date initDate, Date finalDate) {
		verifyDates(initDate, finalDate);
		
		List<Date> dateList = new ArrayList<Date>();

		Calendar initCal = Calendar.getInstance();
		initCal.setTime( initDate );
		
		Calendar finalCal = Calendar.getInstance();
		finalCal.setTime( finalDate );
		
		Calendar indexCal = initCal; 
		while (indexCal.before(finalCal) || indexCal.equals(finalCal)) {
			dateList.add( indexCal.getTime() );
			
			indexCal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return dateList;
	}
	
	
	/**
	 * Verifica se datas:
	 * - Sao validas (nao nulas)
	 * - Data inicial eh realmente anterior a Data final
	 * 
	 * @param initDate
	 * @param finalDate
	 * @throws IllegalArgumentException caso um teste falhe
	 */
	private static void verifyDates(Date initDate, Date finalDate) {
		if (initDate==null) throw new IllegalArgumentException("Initial Date is null");
		if (finalDate==null) throw new IllegalArgumentException("Final Date is null");
		if (initDate.after(finalDate)) throw new IllegalArgumentException("Initial Date is after Final Date");
	}
	
	
	/**
	 * Retorna a data de ontem
	 * @return Date com um dia a menos
	 */
	public static Date getYesterdayDate() {
		Calendar today = new GregorianCalendar();
		today.add(Calendar.DAY_OF_YEAR, -1);
		return today.getTime();
	}
	
	
	/**
	 * Retorna a data de amanha
	 * @return Date com um dia a mais
	 */
	public static Date getTomorowDate() {
		Calendar today = new GregorianCalendar();
		today.add(Calendar.DAY_OF_YEAR, +1);
		return today.getTime();
	}
	
	
	/**
	 * Data de hoje formata para string
	 * @return string no formato yyyy-MM-dd
	 */
	public static String getTodayAsFilename() {
		String todayAsString = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
		return todayAsString;
	}
	
	
	/**
	 * Data de hoje em long
	 * @return long de hoje
	 */
	public static Long getTimestampAsLong() {
		String timeAsString = new SimpleDateFormat("yyyyMMddHHmmss").format( new Date() );
		return Long.parseLong(timeAsString);
	}

	
	/**
	 * Data atual formatada para hora, minuto e segundo 
	 * @return string no formato HH:mm:ss
	 */
	public static String getTimeAsString() {
		return new SimpleDateFormat("HH:mm:ss").format( new Date() );
	}

	
	/**
	 * Retorna o ano atual
	 * @return ano em inteiro
	 */
	public static Integer getThisYear() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}
	
	
	/**
	 * Formata data para string para ser usada em nome de arquivo
	 * @param dt
	 * @return string no formato dd_MM_yyy
	 */
	public static String formatDateToFilename(Date dt) {
		if (dt==null) return "";

		return new SimpleDateFormat("dd_MM_yyyy").format(dt);
	}
	
	/**
	 * Formata data para o padrao (usando '/')
	 * @param dt
	 * @return
	 */
	public static String formatDateToStandard(Date dt) {
		if (dt==null) return "";

		return new SimpleDateFormat("dd/MM/yyyy").format(dt);
	}
	
	/**
	 * Formata data segundo um padrao
	 * @param dt
	 * @param PATTERN
	 * @return
	 */
	public static String formatDate(Date dt, final String PATTERN) {
		if (dt==null) return "";
		if (PATTERN==null || PATTERN.trim().isEmpty()) return "";

		return new SimpleDateFormat( PATTERN ).format(dt);
	}
	
	/**
	 * Adiciona dias na data
	 * @param d
	 * @param daysToAdd
	 * @return
	 */
	public static Date addDays(Date d, int daysToAdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( d );
		cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
		return cal.getTime();
	}


	/**
	 * Calcula a representaca de uma data em dias
	 * @param d1
	 * @return
	 */
	public static long getTimeInDays(Date d1) {
		long timeInMilisec = d1.getTime();
		long timeInDays = (long) timeInMilisec / (1000 * 60 * 60 * 25);
		return timeInDays;
	}


	/**
	 * Verifica se uma data está entre outras duas
	 * @param theDate
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static boolean isBetween(Date theDate, Date beginDate, Date endDate) {
		//validacao short-cut
		if (theDate==null) return false;
		if (beginDate==null) return false;
		if (endDate==null) return false;
		
		long beginTime = getTimeInDays( beginDate );
		long theTime = getTimeInDays( theDate );
		long endTime = getTimeInDays( endDate );

		return beginTime <= theTime && theTime <= endTime;
	}


	/**
	 * Extrai o ano de uma data
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * Calcula a diferenca de 2 datas em dias
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long differenceInDays(Date d1, Date d2) {
		return getTimeInDays(d2) - getTimeInDays(d1);
	}
	
	
	/**
	 * Retorna os meses que ja se passaram dentro do ano
	 * @return
	 */
	public static List<Integer> getMonthsAlreadyPast() {
		Integer currentMonthNumber = Calendar.getInstance().get(Calendar.MONTH)+1;
		
		List<Integer> monthsAlreadPast = new ArrayList<Integer>();
		for (int i=1; i<=12; i++) {
			if (i <= currentMonthNumber) {
				monthsAlreadPast.add( i );
			} else {
				break;
			}
		}
		return monthsAlreadPast;
	}
	

}
