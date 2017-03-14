package com.cb.mundo.model.entity.enumeration;

/**
 * Enum com as opcoes de reembolso ao cancelar uma inscricao
 * @author Solkam
 * @since 21 ABR 2015
 */
public enum RefundStrategy {
	
	/**
	 * Reembolso de 100%
	 */
	REFUND_TOTAL("Reembolso de 100% ao cancelar inscricao"),
	
	/**
	 * Reembolso de 50%
	 */
	REFUND_HALF("Reembolso de 50% ao cancelar inscricao"),
	
	/**
	 * Nenhum reembolso
	 */
	REFUND_NONE("Nenhum reembolso ao cancelar inscricao");
	
	
	private final String noteAboutCancelRegister;
	
	
	private RefundStrategy(String n) {
		this.noteAboutCancelRegister = n;
	}


	public String getNoteAboutCancelRegister() {
		return noteAboutCancelRegister;
	}

}
