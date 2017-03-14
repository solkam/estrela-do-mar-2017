package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.service.EventService;

/**
 * Converter para Event com acesso a BD
 * 
 * @author Solkam
 * @since 21 ABR 2014
 */
@ManagedBean(name="eventConverter")
public class EventConverter implements Converter {

	@EJB EventService eventService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value==null || value.trim().isEmpty()) {
			return null;
		}
		Long idEvent = Long.parseLong(value);
		Event event = eventService.findEventById( idEvent );
		return event;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
            try{
		if (object==null) {
			return "";
		}
		Event event = (Event) object;
		return event.getId().toString();
            }catch(Exception ex){
                return "";
            }
	}
	
}
