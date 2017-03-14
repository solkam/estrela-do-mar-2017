package com.cb.mundo.model.entity.enumeration;

/**
 * Status sobre acerto financeiro
 * @author Solkam
 * @since 25 set 2011
 */
public enum FinancialStatus {
	
	PE("FINANCIAL_STATUS_PENDENT", "icon_status_pendent_32.png" )
	,
	OK("FINANCIAL_STATUS_OK"     , "icon_status_ok_32.png" )
	;
	
	private final String key;
	private final String image;
	
	private FinancialStatus(String key, String image) {
		this.key = key;
		this.image = image;
	}

	public String getKey() {
		return key;
	}

	public String getImage() {
		return image;
	}

	

}
