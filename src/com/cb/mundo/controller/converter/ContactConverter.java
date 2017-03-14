package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.service.ContactService;

@ManagedBean(name="contactConverter")
public class ContactConverter implements Converter {

	@EJB ContactService contactService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valueSubmited) {
		if (valueSubmited==null || valueSubmited.trim().isEmpty()) {
			return null;
		}
			
		Long id = Long.parseLong( valueSubmited );
		Contact contact = contactService.findContactById(id);
		return contact;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Contact contact = (Contact)obj;
		if (obj==null || contact.getId()==null) {
			return null;
		}
		
		return contact.getId().toString();
	}

}
