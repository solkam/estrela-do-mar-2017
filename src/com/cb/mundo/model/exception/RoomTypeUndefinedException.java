package com.cb.mundo.model.exception;

/**
 * Exception que representa que o tipo de quarto nao foi definido
 * 
 * @author Solkam
 * @since 24 ABR 2014
 */
public class RoomTypeUndefinedException extends BusinessException {
	private static final long serialVersionUID = 5490476257213743427L;

	public RoomTypeUndefinedException() {
		super("msg_room_type_undefined", null);
	}

}
