package com.cb.mundo.controller.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.cb.mundo.model.entity.Staff;

@FacesConverter(forClass=Staff.class)
public class StaffConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value==null) {
			return null;
			
		} else {
			Long staffId = Long.parseLong( value );
			
			Staff staff = new Staff();
			staff.setId( staffId );
			return staff;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj==null) {
			return null;
			
		} else {
			Staff staff = (Staff)obj;
			return staff.getId().toString();
		}
	}
}
