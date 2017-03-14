package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.enumeration.EventPresence;

/**
 * Converter para o enum EventPresence
 * 
 * @author Solkam
 * @since 21 ABR 2014
 */
@ManagedBean(name="eventPresenceConverter")
public class EventPresenceConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String text) {
		if (text==null || text.trim().isEmpty()) {
			return null;
		}
		EventPresence presence = EventPresence.valueOf(text);
		return presence;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (object==null) {
			return null;
		}
		EventPresence presence = (EventPresence) object;
		return presence.name();
	}

}
