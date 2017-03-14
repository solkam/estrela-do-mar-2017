package com.cb.mundo.model.report;

import java.util.Date;

import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Representa todos os possiveis filtro para relatorios
 * 
 * @author solkam
 * @since 15 dez 2011
 */
public class SearchFilter {
	
	private Country country = new Country();
	private City city = new City();
	private School school = School.LCB;
	
	private Date beginDate = new Date();
	private Date endDate = new Date();
	
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}

	@Override
	public String toString() {
		return "SearchFilter [country=" + country + ", city=" + city
				+ ", beginTime=" + beginDate + ", endTime=" + endDate + "]";
	}
}
