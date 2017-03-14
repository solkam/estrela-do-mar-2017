package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.ProductionStatus;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.util.CalendarUtil;
import com.cb.mundo.model.util.FormatUtil;
import com.cb.mundo.model.util.ObjectUtil;
import com.cb.mundo.model.util.StringUtil;

/**
 * Representa as informacoes gerais de uma producao
 * 
 * @author Solkam
 * @since 06 out 2011
 */
@Entity
@Table(name="production")
@Audited
public class Production implements Serializable {

	private static final String COMA_AND_SPACE = ", ";
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=3)
	private School school;
	
	@ManyToOne
	private Module module;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date firstDate;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date lastDate;
	
	@ManyToOne(optional=false)
	private City city = new City();
	
	@ManyToOne
	private Facilitator facilitator1;
	
	@ManyToOne
	private Facilitator facilitator2;
	
	@ManyToOne
	private Facilitator facilitator3;
	
	@ManyToOne
	private Facilitator facilitator4;

	private Integer goal = 21;
	
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private ProductionStatus status = ProductionStatus.PLANNED;
	
	
	
	@OneToMany(mappedBy="production")
	private List<Integrant> integrants;
	
	@OneToMany(mappedBy="production")
	private List<Staff> staffs;
	
	@OneToMany(mappedBy="production")
	private List<Participant> participants;
	
	@OneToMany(mappedBy="production")
	private List<Incoming> incomings;
	
	@OneToMany(mappedBy="production")
	private List<Outcoming> outcomings;
	
	
	@OneToOne(mappedBy="production")
	private Feedback feedback;
	
	@OneToOne(mappedBy="production")
	private Rendicion rendicion;
	
	
	//listener
	@PostLoad void onLoad() {
		if (module!=null && getCity()!=null && getCity().getCountry()!=null) {
			module.setCurrentIdiom( getCity().getCountry().getIdiom() );
		}
	}
	
	
	//acessores...
	private static final long serialVersionUID = -4997561246898236817L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Staff> getStaffs() {
		return staffs;
	}
	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public List<Incoming> getIncomings() {
		return incomings;
	}
	public void setIncomings(List<Incoming> incomings) {
		this.incomings = incomings;
	}
	public List<Outcoming> getOutcomings() {
		return outcomings;
	}
	public void setOutcomings(List<Outcoming> outcomings) {
		this.outcomings = outcomings;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public Date getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
		//soma 2 dias na data final
		this.lastDate = CalendarUtil.addDays(firstDate, 2);
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getGoal() {
		return goal;
	}
	public void setGoal(Integer goal) {
		this.goal = goal;
	}
	public Facilitator getFacilitator1() {
		return facilitator1;
	}
	public void setFacilitator1(Facilitator facilitator1) {
		this.facilitator1 = facilitator1;
	}
	public Facilitator getFacilitator2() {
		return facilitator2;
	}
	public void setFacilitator2(Facilitator facilitator2) {
		this.facilitator2 = facilitator2;
	}
	public Facilitator getFacilitator3() {
		return facilitator3;
	}
	public void setFacilitator3(Facilitator facilitator3) {
		this.facilitator3 = facilitator3;
	}
	public Facilitator getFacilitator4() {
		return facilitator4;
	}
	public void setFacilitator4(Facilitator facilitator4) {
		this.facilitator4 = facilitator4;
	}
	public List<Integrant> getIntegrants() {
		return integrants;
	}
	public void setIntegrants(List<Integrant> integrants) {
		this.integrants = integrants;
	}
	public ProductionStatus getStatus() {
		return status;
	}
	public void setStatus(ProductionStatus status) {
		this.status = status;
	}
	public Feedback getFeedback() {
		return feedback;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
	public Rendicion getRendicion() {
		return rendicion;
	}
	public void setRendicion(Rendicion rendicion) {
		this.rendicion = rendicion;
	}


	@Override
	public String toString() {
		return "Production [id=" + id + ", school=" + school + ", module="+ module + ", firstDate=" + firstDate + ", lastDate="	+ lastDate + ", city=" + city + ", status=" + status + "]";
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
		Production other = (Production) obj;
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

	
	
	/* *****************
	 * Metodos Especiais	
	 * *****************/
	
	//1.datas
	
	public String getDescribedDates() {
		Locale countryLocale = getCity().getCountry().getIdiom().getLocale();
		return FormatUtil.toDescribedDates(firstDate, lastDate, countryLocale);
	}
	
	
	//2.produtores
	
	public String getDescProductors() {
		StringBuilder productors = new StringBuilder();
		for (Integrant i : getIntegrants() ) {
			String descProductor = i.getContact().getShortDesc();	
			String descRole = i.getRole().getDescription();
			productors.append( descProductor ).append(" (").append( descRole ).append("), "); 
		}
		return productors.toString();
	}
	
	
	//3.facilitadores
	
	public Boolean getFlagFacilitator1() {
		return ObjectUtil.isValid( facilitator1 );
	}
	
	public Boolean getFlagFacilitator2() {
		return ObjectUtil.isValid( facilitator2 );
	}

	public Boolean getFlagFacilitator3() {
		return ObjectUtil.isValid( facilitator3 );
	}

	public Boolean getFlagFacilitator4() {
		return ObjectUtil.isValid( facilitator4 );
	}
	
	public Integer calculateNumberOfFacilitors() {
		if (getFlagFacilitator4()) return 4;
		if (getFlagFacilitator3()) return 3;
		if (getFlagFacilitator2()) return 2;
		return 1;
	}
	
	public String getDescFacilitators() {
		StringBuilder descFacs = new StringBuilder();
		//facilitator 1:
		if (getFlagFacilitator1() ) {
			descFacs.append( getFacilitator1().getContact().getShortDesc());
		}
		//facilitator 2:
		if (getFlagFacilitator2() ) {
			descFacs.append(COMA_AND_SPACE);
			descFacs.append( getFacilitator2().getContact().getShortDesc());
		}
		//facilitator 3:
		if (getFlagFacilitator3() ) {
			descFacs.append(COMA_AND_SPACE);
			descFacs.append( getFacilitator3().getContact().getShortDesc());
		}
		//facilitator 4:
		if (getFlagFacilitator4() ) {
			descFacs.append(COMA_AND_SPACE);
			descFacs.append( getFacilitator4().getContact().getShortDesc());
		}
		return descFacs.toString();
	}
	

	public List<Facilitator> getFacilitatorsList() {
		List<Facilitator> facilitatorList = new ArrayList<>();
		if (getFlagFacilitator1()) {
			facilitatorList.add( facilitator1 );
		}
		if (getFlagFacilitator2()) {
			facilitatorList.add( facilitator2 );
		}
		if (getFlagFacilitator3()) {
			facilitatorList.add( facilitator3 );
		}
		if (getFlagFacilitator4()) {
			facilitatorList.add( facilitator4 );
		}
		return facilitatorList;
	}
	
	
	public boolean getFlagAllFacilitatorsHaveSignature() {
		//Facilitator 1
		if (!getFlagFacilitator1()) {
			//se producao ainda nao tem facilitator, impossivel preve sua assinatura
			return false;
		
		} else {
			if (StringUtil.isBlank(getFacilitator1().getSignatureFilename())) {
				return false;
			}
		}
		//Facilitator 2
		if (getFlagFacilitator2()) {
			if (StringUtil.isBlank( getFacilitator2().getSignatureFilename() )) {
				return false;
			}
		}
		//Facilitator 3:
		if (getFlagFacilitator3()) {
			if (StringUtil.isBlank( getFacilitator3().getSignatureFilename() )) {
				return false;
			}
		}
		//Facilitator 4:
		if (getFlagFacilitator4()) {
			if (StringUtil.isBlank( getFacilitator4().getSignatureFilename() )) {
				return false;
			}
		}
		return true;
	}
	
	
	
	//4. status

	public Boolean getFlagClosed() {
		return status.getFlagClosed();
	}
	
	public Boolean getFlagActive() {
		return ProductionStatus.ACTIVE.equals(status);
	}
	
	public Boolean getFlagPlanned() {
		return ProductionStatus.PLANNED.equals(status);
	}
	
	public Boolean getFlagCanceled() {
		return ProductionStatus.CANCELED.equals(status);
	}
	
	public Boolean getFlagConcluded() {
		return ProductionStatus.CONCLUDED.equals(status);
	}
	
	
	/**
	 * Faz o controle das transicoes de status
	 * @param newStatus
	 */
	public void changeToStatus(ProductionStatus newStatus) {
		ProductionStatus fromStatus = this.status;
		ProductionStatus toStatus = newStatus;
		
		// from PLANNED to ACTIVE
		if (fromStatus.equals(ProductionStatus.PLANNED) && toStatus.equals(ProductionStatus.ACTIVE)) {
			setStatus( newStatus );
			return;
		}
		
		// from ACTIVE to PLANNED
		if (fromStatus.equals(ProductionStatus.ACTIVE) && toStatus.equals(ProductionStatus.PLANNED)) {
			setStatus( newStatus );
			return;
		}

		// from ACTIVE to CONCLUDED
		if (fromStatus.equals(ProductionStatus.ACTIVE) && toStatus.equals(ProductionStatus.CONCLUDED)) {
			setStatus( newStatus );
			return;
		}
		
		// from ACTIVE to POSTERGATED
		if (fromStatus.equals(ProductionStatus.ACTIVE) && toStatus.equals(ProductionStatus.POSTERGATED)) {
			setStatus( newStatus );
			return;
		}
		
		// from ACTIVE to CANCELED
		if (fromStatus.equals(ProductionStatus.ACTIVE) && toStatus.equals(ProductionStatus.CANCELED)) {
			setStatus( newStatus );
			return;
		}

		//se chegou aqui, esta invalido
		throwInvalidStatusTransition(newStatus);
	}
	
	
	private void throwInvalidStatusTransition(ProductionStatus newStatus) {
		Object[] params = new Object[] {status.name(), newStatus};
		throw new BusinessException("msg_production_status_transition_invalid", params);
	}
	
	
	//5. financeiro
	
	public double getCalculatedIncomingTotal() {
		double total = 0;
		for (Incoming incoming : getIncomings()) {
			total += incoming.getValue();
		}
		return total;
	}
	
	public double getCalculatedOutcomingTotal() {
		double total = 0;
		for (Outcoming outcoming : getOutcomings()) {
			total += outcoming.getValue();
		}
		return total;
	}
	
	public double getCalculatedBalance() {
		return getCalculatedIncomingTotal() - getCalculatedOutcomingTotal();
	}
	
	public Boolean getFlagBalancePositive() {
		if (getCalculatedBalance() >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//6.event
	
	public String getDescEvent() {
		return String.format("%s - %s", getSchool().getDescription(), getModule().getFullDesc() );
	}
	
	
	//7.numero de participantes e staffs
	public int getNumberOfParticipants() {
		return getParticipants().size();
	}
	
	public int getNumberOfStaffs() {
		return getStaffs().size();
	}
	
	
	
	/**
	 * Verifica se 2 producoes tem a mesma data
	 * @param that
	 * @return
	 */
	public boolean hasTheSameDatesAs(Production that) {
		boolean sameFirstDate = this.getFirstDate().equals( that.getFirstDate());
		boolean sameLastDate = this.getLastDate().equals( that.getLastDate() );
		
		return sameFirstDate || sameLastDate;
	}
	
	
	//flag sobre os informes
	
	public Boolean getFlagInformeFeedbackOK() {
		return getFeedback()!=null && getFeedback().getFlagSent();
	}
	
	public Boolean getFlagInformeRendicionOK() {
		return getRendicion()!=null && !getRendicion().isTransient();
	}
	
	public Boolean getFlagAllInformesOK() {
		return getFlagInformeFeedbackOK() && getFlagInformeRendicionOK();
	}
	
	
	
}
