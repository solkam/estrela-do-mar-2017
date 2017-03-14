package com.cb.mundo.model.entity.enumeration;

/**
 * Parametros de configuracao que estarão na base de dados
 * @author Solkam
 * @since 20 ABR 2015
 */
public enum ConfigParamName {
	
	PREVIOUS_DAYS_FOR_TOTAL_REFUND_WHEN_CANCEL_REGISTER("Dias prévios para cancelar uma inscricao e ter reembolso de 100%")
	,
	PREVIOUS_DAYS_FOR_HALF_REFUND_WHEN_CANCEL_REGISTER("Dias prévios para cancelar uma inscricao e ter o reembolso de 50%")
	;
	
	private final String description;
	
	private ConfigParamName(String d) {
		this.description = d;
	}


	public String getDescription() {
		return description;
	}

}
