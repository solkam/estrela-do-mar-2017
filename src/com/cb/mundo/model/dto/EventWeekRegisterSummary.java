package com.cb.mundo.model.dto;

import java.util.List;

import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.Register;


/**
 * Informe 7.5: Pessoas por semana
 * Ideia: 
 * - pesquisa todas as semana de um megaevento;
 * - para cada semana, pesquisar a lista de inscricoes (com distinct)
 * 
 * @author Solkam
 * @since 14 nov 2012
 */
public class EventWeekRegisterSummary {
	
	private final EventWeek eventWeek;
	
	private final List<Register> registers;
	
	//numbers about maturity
	private  int numberOfChild;
	private  int numberOfYoung;
	private  int numberOfAdult;
	private  int numberOfAncient;
	private  int numberOfUnknown;
	private  int numberOfTotal;

	public EventWeekRegisterSummary(EventWeek eventWeek, List<Register> registers) {
		super();
		this.eventWeek = eventWeek;
		this.registers = registers;
		totalizeMaturityNumbers();
	}
	
	private void totalizeMaturityNumbers() {
		numberOfTotal = registers.size();
		
		for (Register register : registers) {
			switch (register.getContact().getMaturity()) {
			case CHILD:
				numberOfChild++;
				break;

			case YOUNG:
				numberOfYoung++;
				break;
				
			case ADULT:
				numberOfAdult++;
				break;
				
			case ANCIENT:
				numberOfAncient++;
				break;
				
			case UNKNOWN:
				numberOfUnknown++;
				break;
			}
		}
	}
	

	public EventWeek getEventWeek() {
		return eventWeek;
	}

	public List<Register> getRegisters() {
		return registers;
	}

	public int getNumberOfChild() {
		return numberOfChild;
	}

	public int getNumberOfYoung() {
		return numberOfYoung;
	}

	public int getNumberOfAdult() {
		return numberOfAdult;
	}

	public int getNumberOfAncient() {
		return numberOfAncient;
	}

	public int getNumberOfUnknown() {
		return numberOfUnknown;
	}

	public int getNumberOfTotal() {
		return numberOfTotal;
	}
	
	

}
