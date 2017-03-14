package com.cb.mundo.model.entity.enumeration;

/**
 * Enum com as capacidades de quarto
 * 
 * @author Solkam
 * @since 24 ABR 2014
 */
public enum RoomOccupancy {
	
	EMPTY("ROOM_OCCUPANCY_EMPTY", "icon_room_occupancy_empty_64.png")
	,
	HALF("ROOM_OCCUPANCY_HALF"  , "icon_room_occupancy_half_64.png")
	,
	FULL("ROOM_OCCUPANCY_FULL"  , "icon_room_occupancy_full_64.png")
	,
	OVER("ROOM_OCCUPANCY_OVER"  , "icon_room_occupancy_over_64.png")
	;
	
	private final String key;
	private final String imagem;
	

	private RoomOccupancy(String key, String imagem) {
		this.key = key;
		this.imagem = imagem;
	}

	
	public String getKey() {
		return key;
	}

	public String getImagem() {
		return imagem;
	}

}
