package com.cb.mundo.model.exception;

/**
 * Exception que representa o numero maximo de pessoas num quarto foi 
 * extrapolado
 * 
 * @author Solkam
 * @since 24 ABR 2014
 */
public class RoomMaxSuportedExtrapoledException extends BusinessException {
	private static final long serialVersionUID = -1739375322989183606L;

	public RoomMaxSuportedExtrapoledException() {
		super("msg_room_max_supported_extrapoled", null);
	}

}
