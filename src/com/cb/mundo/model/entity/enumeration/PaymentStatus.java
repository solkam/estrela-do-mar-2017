package com.cb.mundo.model.entity.enumeration;

/**
 * Status de pagamento de mega-eventos
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public enum PaymentStatus {
	
	PENDENT("PAYMENT_STATUS_PENDENT", "icon_payment_pendent.png")
	,
	PARCIAL("PAYMENT_STATUS_PARCIAL", "icon_payment_parcial.png")
	,
	TOTAL("PAYMENT_STATUS_TOTAL", "icon_payment_total.png")
	;
	
	private final String key;
	private final String image;

	private PaymentStatus(String key, String image) {
		this.key = key;
		this.image = image;
	}

	
	public String getImage() {
		return image;
	}

	public String getKey() {
		return key;
	}
	

}
