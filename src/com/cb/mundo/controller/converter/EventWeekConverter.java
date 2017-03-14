package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.service.EventService;

/**
 * Converter para EventWeek com acesso a BD
 * 
 * @author Solkam
 * @since 21 ABR 2014
 */
@ManagedBean(name="eventWeekConverter")
public class EventWeekConverter implements Converter {

	@EJB EventService eventService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String text) {
		if (text==null || text.trim().isEmpty()) {
			return null;
		}
		
		Long weekId = Long.parseLong(text);
		EventWeek week = eventService.findEventWeekById( weekId );
		return week;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj==null) {
			return null;
		}
			
		EventWeek week = (EventWeek)obj;
		return week.getId().toString();
	}

}
