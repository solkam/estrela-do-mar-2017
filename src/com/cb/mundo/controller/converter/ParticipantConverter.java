package com.cb.mundo.controller.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.cb.mundo.model.entity.Participant;

@FacesConverter(forClass=Participant.class)
public class ParticipantConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value==null) {
			return null;
			
		} else {
			Long participantId = Long.parseLong(value);

			Participant participant = new Participant();
			participant.setId(participantId);
			return participant;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,	Object obj) {
		if (obj==null) {
			return null;
			
		} else {
			Participant participant = (Participant)obj;
			return participant.getId().toString();
		}
	}
}
