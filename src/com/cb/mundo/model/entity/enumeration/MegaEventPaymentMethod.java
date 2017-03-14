package com.cb.mundo.model.entity.enumeration;




/**
 * All payment methods allowed em productions
 * 
 * @author solkam
 * @since 07 out 2011
 */
public enum MegaEventPaymentMethod  {
	
//	CB("PAYMENT_METHOD_CB_BONO", false),//bono cb
	CC("PAYMENT_METHOD_CREDITCARD", false, true, true, true),//cartao de credito
	CH("PAYMENT_METHOD_CHECK", true, true, false),//cheque
	PC("PAYMENT_METHOD_POSTDATECHECK", true, true, false),//cheque predatado
	PR("PAYMENT_METHOD_PRODUCTOR", false),//comissao produtor
	PD("PAYMENT_METHOD_PERSONAL_DISCOUNT", false),//descont pessoal
//	CA("PAYMENT_METHOD_CASH", false, true, false),//dinheiro (Yanisu para tirar pediu em 10/06/16)
	MO("PAYMENT_METHOD_MOUNTAIN", false),//solicitado por yanisu em 09/05
	NC("PAYMENT_METHOD_CREDIT_NOTE", false),//nota de credito
	ND("PAYMENT_METHOD_DEBIT_NOTE", false),//nota de debito
	PP("PAYMENT_METHOD_PAYPAL", false),//pay pall
	PS("PAYMENT_METHOD_PAG_SEGURO", false),//pague seguro
	ES("PAYMENT_METHOD_EXCHANGE_FOR_SERVICE", false),//permuta por servico
	BT("PAYMENT_METHOD_BANKTRANSFER", false, true, false, false),//transferencia bancaria
	TBB("PAYMENT_METHOD_TBB", false),//transferencia bb
	TBI("PAYMENT_METHOD_TBI", false),//transferencia itau
	WB("PAYMENT_METHOD_WEB", false),//web
	DP("PAYMENT_METHOD_MONEY_PRE_EVENT", false),//dinheiro pre-event  (Yanisu para incluir pediu em 10/06/16)
	DC("PAYMENT_METHOD_MONEY_CHECKIN", false),//dinheiro no checkin (Yanisu para incluir pediu em 10/06/16)
	BB("PAYMENT_METHOD_BB_DEPOSIT", false),//deposito bco brasil montanha (Yanisu para incluir pediu em 31/06/16)
	;

	private final String key;
	private final Boolean flagCheck;
    private final Boolean supportFee;
    private final Boolean supportPaymentType;
    private final Boolean supportCreditCard;

        private MegaEventPaymentMethod(String key, Boolean flagCheck) {
            this.key = key;
            this.flagCheck = flagCheck;
            this.supportFee = false;
            this.supportPaymentType = false;
            this.supportCreditCard = false;
        }

        private MegaEventPaymentMethod(String key, Boolean flagCheck, Boolean supportFee) {
            this.key = key;
            this.flagCheck = flagCheck;
            this.supportFee = supportFee;
            this.supportPaymentType = false;
            this.supportCreditCard = false;
        }

        private MegaEventPaymentMethod(String key, Boolean flagCheck, Boolean supportFee, Boolean supportPaymentType) {
            this.key = key;
            this.flagCheck = flagCheck;
            this.supportFee = supportFee;
            this.supportPaymentType = supportPaymentType;
            this.supportCreditCard = false;
        }

        private MegaEventPaymentMethod(String key, Boolean flagCheck, 
                Boolean supportFee, Boolean supportPaymentType, Boolean supportCreditCard) {
            this.key = key;
            this.flagCheck = flagCheck;
            this.supportFee = supportFee;
            this.supportPaymentType = supportPaymentType;
            this.supportCreditCard = supportCreditCard;
        }

        
        
	public String getKey() {
		return key;
	}

	public Boolean getFlagCheck() {
		return flagCheck;
	}

        public Boolean getSupportCreditCard() {
            return supportCreditCard;
        }

        public Boolean getSupportPaymentType() {
            return supportPaymentType;
        }

        public Boolean getSupportFee() {
            return supportFee;
        }
	
}
