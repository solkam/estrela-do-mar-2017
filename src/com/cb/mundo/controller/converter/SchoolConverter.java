package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.enumeration.School;

/**
 * Converter para combo de escolas.
 * @author Solkam
 * @since 10 FEV 2014
 */
@ManagedBean(name="schoolConverter")
public class SchoolConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String text) {
		if (text==null || text.trim().isEmpty()) {
			return null;
		}
		
		School scholl = School.valueOf(text);
		return scholl;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		School scholl = (School)object;
		if (scholl==null) {
			return null;
		}
		return scholl.name();
	}
	
	

}
