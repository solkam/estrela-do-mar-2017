package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.RoomType;
import com.cb.mundo.model.service.RoomTypeService;

/**
 * Converter para Tipo de Quarto
 * @author Solkam
 * @since 25 MAI 2015
 */
@ManagedBean(name="roomTypeConverter")
public class RoomTypeConverter implements Converter {

	@EJB RoomTypeService service;
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String code) {
		if (code==null || code.trim().isEmpty()) {
			return null;
		}
		RoomType roomType = service.findRoomTypeByCode(code);
		return roomType;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj==null) {
			return null;
		}
		RoomType roomType = (RoomType) obj;
		return roomType.getCode();
	}

}
