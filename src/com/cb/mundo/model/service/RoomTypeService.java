package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

import com.cb.mundo.model.entity.RoomType;

/**
 * Servicos para Tipos de Quarto
 * @author Solkam
 * @since 25 MAI 2015
 */
@Stateless
public class RoomTypeService {

	@PersistenceContext
	private EntityManager manager;
	
	
	
	public RoomType saveRoomType(RoomType roomType) {
		return manager.merge( roomType );
	}
	
	public RoomType findRoomTypeByCode(String code) {
		return manager.find(RoomType.class, code);
	}
	
	public void removeRoomType(RoomType roomType) {
		manager.remove( manager.merge(roomType) );
	}
	
	
	public List<RoomType> searchRoomType() {
		return manager.createNamedQuery("searchRoomType", RoomType.class)
				.getResultList();
	}
	
	public List<RoomType> searchActiveRoomType() {
		return manager.createNamedQuery("searchActiveRoomType", RoomType.class)
				.getResultList();
	}
	
	
	
	
}
