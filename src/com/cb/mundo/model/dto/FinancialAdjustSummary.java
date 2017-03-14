package com.cb.mundo.model.dto;

import java.util.List;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Outcoming;

/**
 * Classe helper que representa o ajuste financeiro interno
 * com relacao aos staff e a producao.
 * Notar que o que eh receita para producao eh debito aqui
 * e o que eh despesa para a producao eh credito com o staff
 * 
 * @author Solkam
 * @since 08 JUN 2013
 */
public class FinancialAdjustSummary {
	
	private final Contact contact;
	
	private final List<Incoming> debits;
	
	private final List<Outcoming> credits;

	public FinancialAdjustSummary(Contact contact, List<Incoming> debits, List<Outcoming> credits) {
		super();
		this.contact = contact;
		this.debits = debits;
		this.credits = credits;
	}

	public Contact getContact() {
		return contact;
	}

	public List<Incoming> getDebits() {
		return debits;
	}

	
	
	public List<Outcoming> getCredits() {
		return credits;
	}

	public Double getTotalDebits() {
		Double total = 0.0;
		for (Incoming incoming: getDebits()) {
			total += incoming.getValue();
		}
		return total;
	}
	
	public Double getTotalCredits() {
		Double total = 0.0;
		for (Outcoming outcoming : getCredits() ){
			total += outcoming.getValue();
		}
		return total;
	}
	
	
	public Double getBalance() {
		return getTotalCredits() - getTotalDebits();
	}
	
	

	
	

}
