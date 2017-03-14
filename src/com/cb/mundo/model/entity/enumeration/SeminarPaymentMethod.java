package com.cb.mundo.model.entity.enumeration;

/**
 * Representa os meio de pagamento disponiveis para seminarios nas cidades.
 * Importante dizer que nos MegaEventos sao possiveis outros meios, por isso
 * existem outro enum para meio de pagamento.
 *
 *>> script para producao  
alter table participant modify column paymentMethod varchar(30) not null;

update participant set paymentMethod='CASH' where paymentMethod='CA';
update participant set paymentMethod='CASH' where paymentMethod='CR';
update participant set paymentMethod='CHECK' where paymentMethod='CH';
update participant set paymentMethod='POSTDATED_CHECK' where paymentMethod='PC';
update participant set paymentMethod='POSTDATED_CHECK' where paymentMethod='PO';
update participant set paymentMethod='CREDIT_CARD' where paymentMethod='CC';
update participant set paymentMethod='BANK_TRANSFER' where paymentMethod='BT';
update participant set paymentMethod='BANK_TRANSFER' where paymentMethod='BA';
commit;

update incoming set paymentMethod=payment_method;
update incoming set paymentQuota=payment_quota;
commit;

update incoming set paymentMethod='CASH' where paymentMethod='CA';
update incoming set paymentMethod='CHECK' where paymentMethod='CH';
update incoming set paymentMethod='POSTDATED_CHECK' where paymentMethod='PC';
update incoming set paymentMethod='CREDIT_CARD' where paymentMethod='CC';
update incoming set paymentMethod='BANK_TRANSFER' where paymentMethod='BT';
commit;

alter table incoming drop column payment_method;
alter table incoming drop column payment_quota;

 * 
 * @author Solkam
 * @since 27 FEV 2013
 */

public enum SeminarPaymentMethod  {
	
	CASH("SEMINAR_PAYMENT_METHOD_CASH")
	,
	CHECK("SEMINAR_PAYMENT_METHOD_CHECK")
	,
	POSTDATED_CHECK("SEMINAR_PAYMENT_METHOD_POSTDATED_CHECK")
	,
	CREDIT_CARD("SEMINAR_PAYMENT_METHOD_CREDIT_CARD")
	,
	BANK_TRANSFER("SEMINAR_PAYMENT_METHOD_BANK_TRANSFER")
	,
	BANK_BILLET("SEMINAR_PAYMENT_METHOD_BANK_BILLET")
	;
	
	private final String key;
	
	private SeminarPaymentMethod(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

	
	

}
