package com.cb.mundo.controller.converter;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.EventService;

/**
 * Converter para mega event com acesso a BD
 * 
 * @author Solkam
 * @since 20 ABR 2014
 */
@ManagedBean(name="megaEventConverter")
public class MegaEventConverter implements Converter {

	@EJB EventService eventService;
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String text) {
		if (text==null ||text.trim().isEmpty()) {
			return null;
		}
		Long megaEventId = Long.parseLong( text );
		MegaEvent megaEvent = eventService.findMegaEventById(megaEventId);
		return megaEvent;
	}

	
	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) {
			return null;
		}
		MegaEvent megaEvent = (MegaEvent) obj;
		return megaEvent.getId().toString();
	}

}
