package com.cb.mundo.model.entity.enumeration.util;

import java.util.ArrayList;
import java.util.List;

import com.cb.mundo.model.entity.enumeration.EventType;

/**
 * Utilitaario para Tipos de Eventos
 * @author Solkam
 * @since 05 ABR 2015
 */
public class EventTypeUtil {
	
	/**
	 * Retorna lista de eventos de dependentes
	 * @return
	 */
	public static List<EventType> getEventTypeDependents() {
		List<EventType> types = new ArrayList<EventType>();
		types.add( EventType.DEPENDENT_BABY );
		types.add( EventType.DEPENDENT_CHILD );
		types.add( EventType.DEPENDENT_TEENAGER );
		types.add( EventType.DEPENDENT_PARTNER );
		return types;
	}

}
