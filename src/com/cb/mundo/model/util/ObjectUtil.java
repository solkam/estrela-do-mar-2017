package com.cb.mundo.model.util;

import java.lang.reflect.Field;

import java.util.Date;

import javax.persistence.Id;

/**
 * Utilitario para Objetos
 * 
 * @author Solkam
 * @since 20 nov 2011
 */
public class ObjectUtil {

	/**
	 * Check a object parameter looking for its @Id annotated fields and then
	 * test if value is not null. If is not null, parameter is detached
	 * @param obj
	 * @return
	 */
	public static boolean isValid(Object obj) {
		if (obj==null) return false;
		try {
			Class<? extends Object> clazz = obj.getClass();
			for (Field field : clazz.getDeclaredFields() ) {
				if (field.isAnnotationPresent( Id.class )) {
					field.setAccessible(true);
					Object valueId = field.get(obj);
					field.setAccessible(false);
					return isNotNull(valueId);
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * A date is valid where is not null and its internal time is greater than 0.
	 * @param d
	 * @return true if valid
	 */
	public static boolean isValidDate(Date d) {
		return isNotNull(d) && d.getTime()>0L;
	}
	
	public static boolean isNotNull(Object obj) {
		return obj!=null;
	}
	
	public static boolean isNull(Object obj) {
		return obj==null;
	}
	
	
}
