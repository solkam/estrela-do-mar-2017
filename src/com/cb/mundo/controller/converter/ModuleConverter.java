package com.cb.mundo.controller.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.service.ModuleService;

/**
 * Converter para o enum Module
 * @author Solkam
 * @since 05 MAR 2015
 */
@ManagedBean(name="moduleConverter")
@ViewScoped
public class ModuleConverter implements Converter, Serializable {

	@EJB
	private ModuleService service;
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String txt) {
		if (txt==null || txt.trim().isEmpty()) return null;

		Long moduleId = Long.parseLong(txt);
		Module module = service.findModuleByID(moduleId);
		return module;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) return null;
		
		Module module = (Module) obj;
		return String.valueOf( module.getId() );
	}

	
	
	private static final long serialVersionUID = -7825851724097606129L;
}
