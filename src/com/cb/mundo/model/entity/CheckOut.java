package com.cb.mundo.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cb.mundo.model.util.CalendarUtil;

/**
 * Guarda informacoes referente ao checkout de pessoas
 * em mega-eventos
 * 
 * @author Solkam
 * @since 25 set 2012
 */
@Embeddable
public class CheckOut {
	
	@ManyToOne
	private UserCB checkoutUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkoutDate;
	
	@Column(length=2000)
	private String checkoutNote;

	
//acessores...
	public UserCB getCheckoutUser() {
		return checkoutUser;
	}

	public void setCheckoutUser(UserCB checkoutUser) {
		this.checkoutUser = checkoutUser;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getCheckoutNote() {
		return checkoutNote;
	}

	public void setCheckoutNote(String checkoutNote) {
		this.checkoutNote = checkoutNote;
	}
	
	//formatador
	public String getCheckoutDateAsString() {
		return CalendarUtil.formatDateToStandard(checkoutDate);
	}
	
	

}
