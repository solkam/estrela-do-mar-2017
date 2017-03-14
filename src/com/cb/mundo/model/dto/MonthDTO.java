package com.cb.mundo.model.dto;

/**
 * DTO que representa um mes com indice e nome
 * @author Solkam
 * @since 14 MAI 2015
 */
public class MonthDTO {
	
	private int number;
	
	private String name;
	

	//construtor
	public MonthDTO(int number, String name) {
		super();
		this.number = number;
		this.name = name;
	}

	//acessores...
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthDTO other = (MonthDTO) obj;
		if (number != other.number)
			return false;
		return true;
	}
	
	

}
