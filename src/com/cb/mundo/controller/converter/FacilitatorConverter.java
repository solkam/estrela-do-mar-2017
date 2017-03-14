package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.service.FacilitatorService;

/**
 * Converter para Facilitator
 * 
 * @author Solkam
 * @since 28 NOV 2014
 */
@ManagedBean(name="facilitatorConverter")
public class FacilitatorConverter implements Converter {

	@EJB FacilitatorService service;
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String txt) {
		if (txt==null || txt.trim().isEmpty()) {
			return null;
		}
		Long facId = Long.parseLong( txt );
		Facilitator foundFacilitator = service.findFacilitatorById( facId );
		return foundFacilitator;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) {
			return null;
		}
		Facilitator facilitator = (Facilitator) obj;
		String facId = Long.toString( facilitator.getId() );
		return facId;
	}

}
