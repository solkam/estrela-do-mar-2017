package com.cb.mundo.controller.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.service.AddressService;

/**
 * Converter para cidade
 * @author Solkam
 * @since 04 ABR 2015
 */
@ManagedBean(name="cityConverter")
@ViewScoped
public class CityConverter implements Converter, Serializable {

	@EJB AddressService addressService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String text) {
		if (text==null || text.trim().isEmpty() ){
			return null;
		}
		Long cityId = Long.parseLong( text );
		City city = addressService.findCityById(cityId);
		return city;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj==null) {
			return null;
		}
		City city = (City)obj;
		return String.valueOf( city.getId() );
	}
	
	

	private static final long serialVersionUID = -908634361730341491L;
}
