package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Event;

/**
 * Exception que representa que um evento deve ter valor para participant e/ou stafff
 * Nunca sem valor nenhum
 * 
 * @author Solkam
 * @since 14 OUT 2013
 */
public class EventHasNoValueForParticipantNeitherForStaffException extends BusinessException {
	private static final long serialVersionUID = 7941150714533534465L;

	public EventHasNoValueForParticipantNeitherForStaffException(Event event) {
		super("msg_event_has_no_value_for_participant_neither_for_staff", null);
	}

}
