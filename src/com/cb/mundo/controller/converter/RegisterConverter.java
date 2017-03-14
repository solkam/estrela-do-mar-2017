package com.cb.mundo.controller.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.service.RegisterService;

/**
 * Converter para register
 * (usado para transferir evento para outra inscricao e agregar inscricao de terceiro)
 * @author Solkam
 * @since 06 OUT 2013
 */
@ManagedBean(name="registerConverter")
@ViewScoped
public class RegisterConverter implements Converter, Serializable {
	
	@EJB RegisterService registerService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if (submittedValue==null || submittedValue.trim().isEmpty()) {
			return null;
		}
		Long id = Long.parseLong(submittedValue);
		Register register = registerService.findRegisterById( id );
		return register;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		Register register = (Register) object;
		if (register==null || register.getId()==null) {
			return null;
		}
		return String.valueOf( register.getId() );
	}

	private static final long serialVersionUID = -9194826352184803457L;
}
