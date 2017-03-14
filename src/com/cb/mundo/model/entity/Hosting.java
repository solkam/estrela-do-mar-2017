package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.RoomOccupancy;


/**
 * Informacoes sobre a hospedagem durante o mega evento
 * 
 * @author Solkam
 * @since 06 dez 2012 
 */
@Entity
@Audited
public class Hosting implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=500)
	private String hostingArea;
	
	@Column(length=90)
	private String hostingNumber;
	
	@ManyToOne
	private RoomType roomType;
	
	@ManyToOne
	private MegaEvent megaEvent;
	
	@OneToMany(mappedBy="hosting", cascade=CascadeType.ALL)
	private List<HostingSugested> sugesteds;
	
	@OneToMany(mappedBy="hosting", cascade=CascadeType.ALL)
	private List<HostingConfirmed> confirmeds;
	
	@Size(max=1000)
	private String note;
	
	
	// log
	@Size(max = 100)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Size(max = 100)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	
	
	//construtores
	public Hosting() {}
	
	public Hosting(MegaEvent me) {
		this.megaEvent = me;
	}
	
	


	
	//acessores...	
	private static final long serialVersionUID = -3100617893694253316L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getHostingArea() {
		return hostingArea;
	}
	public void setHostingArea(String hostingArea) {
		this.hostingArea = hostingArea;
	}
	public List<HostingConfirmed> getConfirmeds() {
		return confirmeds;
	}
	public void setConfirmeds(List<HostingConfirmed> confirmeds) {
		this.confirmeds = confirmeds;
	}
	public String getHostingNumber() {
		return hostingNumber;
	}
	public void setHostingNumber(String hostingNumber) {
		this.hostingNumber = hostingNumber;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public void setMegaEvent(MegaEvent megaEvent) {
		this.megaEvent = megaEvent;
	}
	public List<HostingSugested> getSugesteds() {
		return sugesteds;
	}
	public void setSugesteds(List<HostingSugested> sugesteds) {
		this.sugesteds = sugesteds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Hosting other = (Hosting) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isTransient() {
		return getId()==null;
	}
	

	
	public int getRoomMaxSupported() {
		if (getRoomType()==null) {
			return 0;
		}
		return getRoomType().getMaxSupported();
	}
	
	
	
	// builders
	/**
	 * Instancia um sugerido e ja adiciona internamente.
	 * @param contact
	 * @return
	 */
	public HostingSugested buildHostingSugessted(Contact contact) {
		HostingSugested sugested = new HostingSugested(this, contact);
		addHostingSugested(sugested);
		return sugested;
	}
	
	public HostingConfirmed buildHostingConfirmed(Register register) {
		HostingConfirmed confirmed = new HostingConfirmed(this, register);
		addHostingConfirmed(confirmed);
		return confirmed;
	}
	
	

	// helpers para colecoes	
	public void addHostingSugested(HostingSugested sugested) {
		getSugesteds().add( sugested );
	}
	
	public void removeHostingSugested(HostingSugested sugested) {
		getSugesteds().remove( sugested );
	}
	
	
	public void addHostingConfirmed(HostingConfirmed confirmed) {
		if (getConfirmeds()==null) {
			setConfirmeds( new ArrayList<HostingConfirmed>());
		}
		getConfirmeds().add(confirmed);
	}
	
	public void removeHostingConfirmed(HostingConfirmed confirmed) {
		getConfirmeds().remove( confirmed );
	}
	
	

	
	
	
	/* *******************
	 * Gestao de Sugeridos 	
	 *********************/
	public int getSuggestedsActual() {
		if (getSugesteds()==null) {//curto-circuito
			return 0;
		}
		return getSugesteds().size();
	}
	
	public int getSugestedsAvailable() {
		return getRoomMaxSupported() - getSuggestedsActual();
	}
	
	public Boolean getFlagSugestedsAvailable() {
		return getSugestedsAvailable() > 0;
	}
	

	
	
	/* *********************
	 * Gestao de Confirmados
	 ***********************/
	public int getConfirmedsActual() {
		if (getConfirmeds()==null) {//curto-circuito
			return 0;
		}
		return getConfirmeds().size();
	}
	
	public int getConfirmedsAvailable() {
		return getRoomMaxSupported() - getConfirmedsActual();
	}

	public Boolean getFlagConfirmedsAvailable() {
		return getConfirmedsAvailable() > 0;
	}


	
	/* *****************
	 * Nivel de ocupacao
	 *******************/
	public RoomOccupancy getRoomOccupancy() {
		if (getConfirmedsActual()==0) {
			return RoomOccupancy.EMPTY;
		}
		if (getConfirmedsAvailable()==0) {
			return RoomOccupancy.FULL;
		}
		if (getConfirmedsAvailable()<0) {
			return RoomOccupancy.OVER;
		}
		return RoomOccupancy.HALF;
	}
	
}
