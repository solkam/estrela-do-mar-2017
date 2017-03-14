package com.cb.mundo.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.enumeration.IncomingCategory;

/**
 * Sumario para receitas
 * 
 * @author Solkam
 * @since 24 nov 2011
 */
public class IncomingSummary {
	
	private Production production;
	
	private IncomingCategory category;
	
	private List<Incoming> incomings = new ArrayList<Incoming>();

	
	
	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}

	public IncomingCategory getCategory() {
		return category;
	}

	public void setCategory(IncomingCategory category) {
		this.category = category;
	}

	public List<Incoming> getIncomings() {
		return incomings;
	}

	public void setIncomings(List<Incoming> incomings) {
		this.incomings = incomings;
	}

	public Double getTotal() {
		Double total = 0.0;
		for (Incoming i : getIncomings()) {
			total += i.getValue();
		}
		return total;
	}
	
	

}
