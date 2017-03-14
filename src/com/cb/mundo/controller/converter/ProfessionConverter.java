package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Profession;

@ManagedBean(name="professionConverter")
public class ProfessionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String submittedValue) {
		if (submittedValue==null || submittedValue.trim().isEmpty()) {
			return null;
		}
		
		Long id = Long.parseLong(submittedValue);
		Profession profession = new Profession();
		profession.setId( id );
		return profession;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (object == null) {
			return null;
		}
		Profession profession = (Profession) object;
		return profession.getId().toString(); 
	}

}
