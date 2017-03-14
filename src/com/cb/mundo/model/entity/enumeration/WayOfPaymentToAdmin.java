package com.cb.mundo.model.entity.enumeration;

/**
 * Maneira dos produtores acertarem com a admin CB
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public enum WayOfPaymentToAdmin {
	
	WEB("WAY_OF_PAYMENT_TO_ADMIN_WEB")
	,
	TRANSFER("WAY_OF_PAYMENT_TO_ADMIN_TRANSFER")
	,
	CASH_DOLLAR("WAY_OF_PAYMENT_TO_ADMIN_CASH_DOLLAR")
//	,
//	CHECK("WAY_OF_PAYMENT_TO_ADMIN_CHECK")[26MAR15]a pedido de Yamarty
//	,
//	OTHER("WAY_OF_PAYMENT_TO_ADMIN_OTHER")[26MAR15]a pedido de Yamarty
	;
	
	private final String key;

	private WayOfPaymentToAdmin(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
}
