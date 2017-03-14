package com.cb.mundo.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.cb.mundo.model.entity.Outcoming;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.enumeration.OutcomingCategory;

/**
 * Sumario para despesas de seminarios
 * 
 * @author Solkam
 * @since 14 nov 2011
 */
public class OutcomingSummary {
	
	private Production production;
	
	private OutcomingCategory category;
	
	private List<Outcoming> outcomings = new ArrayList<Outcoming>();

	
	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}

	public OutcomingCategory getCategory() {
		return category;
	}

	public void setCategory(OutcomingCategory category) {
		this.category = category;
	}


	public List<Outcoming> getOutcomings() {
		return outcomings;
	}

	public void setOutcomings(List<Outcoming> outcomings) {
		this.outcomings = outcomings;
	}
	
	
	public Double getTotal() {
		Double total = 0.00;
		for (Outcoming outcoming : getOutcomings()) {
			total += outcoming.getValue();
		}
		return total;
	}
	

}
