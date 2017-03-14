package com.cb.mundo.model.exception;


/**
 * Exception que disse o numero de um quarto ja foi usado por outro
 * 
 * @author Solkam
 * @since 24 ABR 2014
 */
public class RoomNumberAlreadyUsedException extends BusinessException {
	private static final long serialVersionUID = 7782305022508837592L;

	public RoomNumberAlreadyUsedException() {
		super("msg_room_number_already_used", null);
	}
	
	

}
