package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.enumeration.RegisterStatus;

/**
 * Converter para Status de Register
 * @author Solkam
 * @since 16 ABR 2015
 */
@ManagedBean(name="registerStatusConverter")
public class RegisterStatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String txt) {
		if (txt==null || txt.trim().isEmpty()) {
			return null;
		}
		RegisterStatus status = RegisterStatus.valueOf(txt);
		return status;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj==null) {
			return null;
		}
		RegisterStatus status = (RegisterStatus) obj;
		return status.name();
	}

}
