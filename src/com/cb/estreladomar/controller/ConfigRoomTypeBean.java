package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.RoomType;
import com.cb.mundo.model.service.RoomTypeService;

/**
 * Controller para tipo de quartos
 * @author Solkam
 * @since 25 MAI 2015
 */
@ManagedBean(name="configRoomTypeBean")
public class ConfigRoomTypeBean implements Serializable {
	
	@EJB RoomTypeService service;


	private RoomType newRoomType;
	
	private List<RoomType> roomTypes;
	
	
	@PostConstruct void init() {
		reset();
		populateRoomTypes();
	}
	
	private void populateRoomTypes() {
		roomTypes = service.searchRoomType();
	}
	
	//actions...
	
	private void reset() {
		newRoomType = new RoomType();
	}
	
	public void save() {
		service.saveRoomType(newRoomType);
		reset();
		populateRoomTypes();
		JSFUtil.addInfoMessage("msg_room_type_save_sucess");
	}
	
	public void saveAll() {
		for (RoomType typeVar : roomTypes) {
			service.saveRoomType(typeVar);
		}
		populateRoomTypes();
		JSFUtil.addInfoMessage("msg_room_types_save_sucess");
	}
	
	public void remove(RoomType selectedRoomType) {
		service.removeRoomType(selectedRoomType);
	}


	
	//acessores...
	
	private static final long serialVersionUID = 2095929097060680824L;
	public RoomType getNewRoomType() {
		return newRoomType;
	}
	public void setNewRoomType(RoomType newRoomType) {
		this.newRoomType = newRoomType;
	}
	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}
	
}
