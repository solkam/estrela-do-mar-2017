package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.enumeration.Profile;

/**
 * Converter para profile
 * @author Solkam
 * @since 15 MAR 2015
 */
@ManagedBean(name="profileConverter")
@ViewScoped
public class ProfileConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String text) {
		if (text==null || text.trim().isEmpty()) {
			return null;
		}
		return Profile.valueOf(text);
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) {
			return null;
		}
		Profile profile = (Profile) obj;
		return profile.name();
	}

}
