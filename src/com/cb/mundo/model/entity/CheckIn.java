package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cb.mundo.model.util.CalendarUtil;

/**
 * Representa o processo de check-in de alguem no mega-evento
 * 
 * @author Solkam
 * @since 09 set 2012
 */
@Embeddable
public class CheckIn implements Serializable {
	private static final long serialVersionUID = -2979480600255428605L;

	@ManyToOne
	private UserCB checkinUser;
	
	@Temporal(TemporalType.TIMESTAMP)	
	private Date checkinDate;
	
	@Column(length=2000)
	private String checkinNote;

	//acessores...
	public UserCB getCheckinUser() {
		return checkinUser;
	}

	public void setCheckinUser(UserCB checkinUser) {
		this.checkinUser = checkinUser;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckinNote() {
		return checkinNote;
	}

	public void setCheckinNote(String checkinNote) {
		this.checkinNote = checkinNote;
	}
	
	//formatador
	public String getCheckinDateAsString() {
		return CalendarUtil.formatDateToStandard(checkinDate);
	}
	
	
	

}
