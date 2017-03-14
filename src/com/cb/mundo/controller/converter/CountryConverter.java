package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.service.AddressService;

/**
 * Converter para Paises
 * 
 * @author Solkam
 * @since 30 NOV 2014
 */
@ManagedBean(name="countryConverter")
public class CountryConverter implements Converter {

	@EJB AddressService service;
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String countryCode) {
		if (countryCode==null || countryCode.trim().isEmpty()) {
			return null;
		}
		Country foundCountry = service.findCountryByCode( countryCode );
		return foundCountry;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) {
			return null;
		}
		Country country = (Country) obj;
		return country.getCode();
	}

}
