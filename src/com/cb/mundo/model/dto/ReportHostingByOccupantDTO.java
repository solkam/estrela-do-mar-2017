package com.cb.mundo.model.dto;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Hosting;

/**
 * Relatorio de Quarto com visao do ocupante
 * 
 * @author Solkam
 * @since 02 JUL 2014
 */
public class ReportHostingByOccupantDTO {
	
	private final Contact roomMateContact;
	
	private final Hosting hosting;

	
	public ReportHostingByOccupantDTO(Contact roomMateContact, Hosting hosting) {
		super();
		this.roomMateContact = roomMateContact;
		this.hosting = hosting;
	}


	public Contact getRoomMateContact() {
		return roomMateContact;
	}


	public Hosting getHosting() {
		return hosting;
	}
	
	

}
