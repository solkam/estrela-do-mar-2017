package com.cb.mundo.model.dto;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Hosting;

/**
 * Helper ou DTO para relatorio de quartos
 * 
 * @author Solkam
 * @since 12 MAI 2014
 */
public class ReportHostingByRoomDTO {
	
	private final Hosting hosting;
	
	private final Contact roomMateContact1;
	private final Contact roomMateContact2;
	private final Contact roomMateContact3;
	private final Contact roomMateContact4;
	private final Contact roomMateContact5;
	private final Contact roomMateContact6;
	
	
	public ReportHostingByRoomDTO(Hosting hosting
								 ,Contact roomMateContact1
								 ,Contact roomMateContact2 
								 ,Contact roomMateContact3	
								 ,Contact roomMateContact4
								 ,Contact roomMateContact5
								 ,Contact roomMateContact6
								 ) {
		super();
		this.hosting = hosting;
		this.roomMateContact1 = roomMateContact1;
		this.roomMateContact2 = roomMateContact2;
		this.roomMateContact3 = roomMateContact3;
		this.roomMateContact4 = roomMateContact4;
		this.roomMateContact5 = roomMateContact5;
		this.roomMateContact6 = roomMateContact6;
	}
	
	
	public Hosting getHosting() {
		return hosting;
	}
	public Contact getRoomMateContact1() {
		return roomMateContact1;
	}
	public Contact getRoomMateContact2() {
		return roomMateContact2;
	}
	public Contact getRoomMateContact3() {
		return roomMateContact3;
	}
	public Contact getRoomMateContact4() {
		return roomMateContact4;
	}
	public Contact getRoomMateContact5() {
		return roomMateContact5;
	}
	public Contact getRoomMateContact6() {
		return roomMateContact6;
	}
}
