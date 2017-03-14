package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Representa um evento.
 * Se for de formacao, tera a escola e modulo
 * Se for outros, tera um nome
 * 
 * @author Solkam
 * @since 14 abr 2012
 */
@Entity
@Table(name="event")
@Audited
public class Event implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private EventWeek eventWeek;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private EventType type;

	@Column(length=60)
	private String name;
	
	@Column(length=200)
	private String description;
	
	@Enumerated(EnumType.STRING)
	private School school = School.LCB;
	
	@ManyToOne
	private Module module;
	
	

	@Temporal(TemporalType.DATE)
	private Date beginDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	
	private BigDecimal valueParticipant;
	
	private BigDecimal valueStaff;
	
	private Integer capacity;
	
	/**
	 * visivel para clientes na inscricao
	 */
	private Boolean flagVisible = false;
	
	/**
	 * deve ser considerado no calculo de comissoes
	 */
	private Boolean flagCommissionable = false;

	
	
	@OneToMany(mappedBy="event")
	private List<RegisterDetail> details;
	

	
	//acessores...
	private static final long serialVersionUID = 2140408511111936508L;
	
	public Long getId() {
		return id;
	}
	public EventType getType() {
		return type;
	}
	
	public Boolean getFlagVisible() {
		return flagVisible;
	}
	public void setFlagVisible(Boolean flagVisible) {
		this.flagVisible = flagVisible;
	}
	public Boolean getFlagCommissionable() {
		return flagCommissionable;
	}
	public void setFlagCommissionable(Boolean flagCommissionable) {
		this.flagCommissionable = flagCommissionable;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
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
	public EventWeek getEventWeek() {
		return eventWeek;
	}
	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getValueParticipant() {
		return valueParticipant;
	}
	public void setValueParticipant(BigDecimal valueParticipant) {
		this.valueParticipant = valueParticipant;
	}
	public BigDecimal getValueStaff() {
		return valueStaff;
	}
	public void setValueStaff(BigDecimal valueStaff) {
		this.valueStaff = valueStaff;
	}
	public List<RegisterDetail> getDetails() {
		return details;
	}
	public void setDetails(List<RegisterDetail> details) {
		this.details = details;
	}
	public Integer getCapacity() {
		if (capacity==null) {
			capacity = 0;
		}
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
		Event other = (Event) obj;
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

	@Override
	public String toString() {
		return getDisplayNameOrSchool();
	}

	

	//metodos especiais...	
	private static final String PATTERN_CUSTOM_DESC = "(%s) %s";
	
	public String getCustomDesc() {
		return String.format(PATTERN_CUSTOM_DESC, getEventWeek().getName(), getDisplayNameOrSchool() );
	}
	
	public Boolean getFlagFormation() {
		return EventType.FORMATION.equals( type );
	}

	private static final String PATTER_SCHOOL_AND_MODULE = "%s %s";
	
	public String getDisplayNameOrSchool() {
		if (getFlagFormation()) {
			return String.format(PATTER_SCHOOL_AND_MODULE, getSchool().getDescription(), getModule().getFullDesc() );
		} else {
			return name;
		}
	}
	
	/**
	 * Metodo sob-demanda que verifica se o evento
	 * esta habilitado para participante conforme
	 * o valor respectivo esteja null ou nao
	 * @return
	 */
	public Boolean getEnableForParticipant() {
		return NumberUtil.isDifferenceThanZero(valueParticipant);
	}
	
	/**
	 * Mesma ideia do anterior
	 * @return
	 */
	public Boolean getEnableForStaff() {
		return NumberUtil.isDifferenceThanZero(valueStaff);
	}
	
	public Integer getCalculatedCapacityActual() {
		return getDetails()==null ? 0 : getDetails().size();
	}
	
	/**
	 * Calcula a vagas restantes
	 * @return
	 */
	public Integer getCalculatedCapacityRemained() {
		return getCapacity() - getCalculatedCapacityActual();
	}
	
	

}
