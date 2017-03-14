package com.cb.mundo.model.entity;

import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.util.NumberUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Representa uma inscricao a um mega evento
 * RNs:
 * 1) No momento da inscricao, previewCheckinDate deve ser preenchido com a data de checkin da primeira semana inscrita
 * 2) No momento da inscricao, previewCheckoutDate deve ser preenchido com a data de checkout da ultima semana inscrita
 * 3) No momento do checkin, a referencia checkin deve ser criada
 * 4) No momento do checkout, a referencia checkout deve ser criada
 * 
 * @author Solkam
 * @since 22 abr 2012
 */
@Entity
@Table(name="register")
@Audited
public class Register implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private MegaEvent megaEvent;

	@ManyToOne(optional=false)
	private Contact contact;
	
	@OneToOne
	private Account account;

    @ManyToOne(optional = true)
    private Contact eventProductorContact;
    
	@OneToMany(mappedBy="register")
	@OrderBy("eventWeek")
	private List<RegisterDetail> details = new ArrayList<RegisterDetail>();
		
	@Embedded
	private TripInfo tripInfo;
	
	@Temporal(TemporalType.DATE)
	private Date previewCheckinDate;

	@Temporal(TemporalType.DATE)
	private Date previewCheckoutDate;
	
	@Embedded
	private CheckIn checkin;
	
	@Embedded
	private CheckOut checkout;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private RegisterStatus status = RegisterStatus.INCOMPLETE;
	
	@Column(length=2000)
	private String note;
	
	
	//aceites..	
	private Boolean medicalResponsabilityTermAccepted;
	private Boolean saleConditionTermAccepted;
	private Boolean releaseOfLiabilityTermAccepted;
	

	//dependentes...	
	@ManyToOne
	private Register responsableRegister;
	
	@OneToMany(mappedBy="responsableRegister")
	private List<Register> dependentRegisters;
	
	
	@ManyToMany(mappedBy="registers")
	private List<Transport> transports;
	
    @Column(nullable = false)
    private Long transportClass_id;

    
        
    //log	
    @Temporal(TemporalType.DATE)
    private Date createDate;
    
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    
	// listeners
	@PrePersist
	protected void onCreate() {
		this.createDate = new Date();
		this.updateDate = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updateDate = new Date();
	}

	
	//construtores
	public Register() {}
	
	public Register(MegaEvent me, Contact c, Account a) {
		this.megaEvent = me;
		this.contact = c;
		this.account = a;
	}

	
	// acessores
	private static final long serialVersionUID = -8884521678588841164L;

	public List<RegisterDetail> getDetails() {
		return details;
	}
	public void setDetails(List<RegisterDetail> details) {
		this.details = details;
	}
	public void addDetail(RegisterDetail detail) {
		this.details.add(detail);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public MegaEvent getMegaEvent() {
		return megaEvent;
	}
	public void setMegaEvent(MegaEvent megaEvent) {
		this.megaEvent = megaEvent;
	}
	public TripInfo getTripInfo() {
		if (this.tripInfo==null) {
			this.tripInfo = new TripInfo();
		}
		return this.tripInfo;
	}
	public void setTripInfo(TripInfo tripInfo) {
		this.tripInfo = tripInfo;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public CheckIn getCheckin() {
		if (this.checkin==null) {
			this.checkin = new CheckIn();
		}
		return this.checkin;
	}
	public void setCheckin(CheckIn checkin) {
		this.checkin = checkin;
	}
	public CheckOut getCheckout() {
		if (this.checkout==null) {
			this.checkout = new CheckOut();
		}
		return this.checkout;
	}
	public void setCheckout(CheckOut checkout) {
		this.checkout = checkout;
	}
	public Date getPreviewCheckinDate() {
		return previewCheckinDate;
	}
	public void setPreviewCheckinDate(Date previewCheckinDate) {
		this.previewCheckinDate = previewCheckinDate;
	}
	public Date getPreviewCheckoutDate() {
		return previewCheckoutDate;
	}
	public void setPreviewCheckoutDate(Date previewCheckoutDate) {
		this.previewCheckoutDate = previewCheckoutDate;
	}
	public RegisterStatus getStatus() {
		return status;
	}
	public void setStatus(RegisterStatus status) {
		this.status = status;
	}
	public Boolean getMedicalResponsabilityTermAccepted() {
		return medicalResponsabilityTermAccepted;
	}
	public void setMedicalResponsabilityTermAccepted(Boolean medicalResponsabilityTermAccepted) {
		this.medicalResponsabilityTermAccepted = medicalResponsabilityTermAccepted;
	}
	public Boolean getSaleConditionTermAccepted() {
		return saleConditionTermAccepted;
	}
	public void setSaleConditionTermAccepted(Boolean saleConditionTermAccepted) {
		this.saleConditionTermAccepted = saleConditionTermAccepted;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Register getResponsableRegister() {
		return responsableRegister;
	}
	public void setResponsableRegister(Register responsableRegister) {
		this.responsableRegister = responsableRegister;
	}
	public List<Register> getDependentRegisters() {
		return dependentRegisters;
	}
	public void setDependentRegisters(List<Register> dependentRegisters) {
		this.dependentRegisters = dependentRegisters;
	}
	public List<Transport> getTransports() {
		return transports;
	}
	public void setTransports(List<Transport> transports) {
		this.transports = transports;
	}
    public Boolean getReleaseOfLiabilityTermAccepted() {
        return releaseOfLiabilityTermAccepted;
    }
    public void setReleaseOfLiabilityTermAccepted(Boolean releaseOfLiabilityTermAccepted) {
        this.releaseOfLiabilityTermAccepted = releaseOfLiabilityTermAccepted;
    }
    public Long getTransportClass_id() {
        return transportClass_id;
    }
    public void setTransportClass_id(Long transportClass_id) {
        this.transportClass_id = transportClass_id;
    }
    public Contact getEventProductorContact() {
        return eventProductorContact;
    }
    public void setEventProductorContact(Contact eventProductorContact) {
        this.eventProductorContact = eventProductorContact;
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
		Register other = (Register) obj;
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



	//metodos especiais...
	
	/**
	 * Totaliza os valores os eventos inscritos.
	 * Cuidado para evitar LIE.
	 * Soma tambem os dependentes
	 * @return total da inscricao calculado sob demanda
	 */
	public BigDecimal getCalculatedTotalValue() {
		BigDecimal totalValue = NumberUtil.VALUE_ZERO;
		for (RegisterDetail detail : this.getDetails()) {
			totalValue = totalValue.add( detail.getValue() );
		}
		totalValue = totalValue.add( getCalculatedDependentTotalValue() );
		return totalValue;
	}
	
	
	
	/**
	 * Calcula o montante ja pago.
	 * Implicitamente, RegisterDetail.getCalculatedPaidValue faz outro loop.
	 * Cuidado para evitar LIE
	 * Soma tambem os dependentes
	 * @return valor ja pago calculado sob demanda
	 */
	public BigDecimal getCalculatedPaidValue() {
		BigDecimal paidValue = NumberUtil.VALUE_ZERO;
		for (RegisterDetail detail : this.getDetails()) {
			paidValue = paidValue.add( detail.getCalculatedPaidValue() );
		}
		paidValue = paidValue.add( getCalculatedDependentPaidValue() );
		return paidValue;
	}
	
	/**
	 * Calcula o montante pendente, utilizando os dois outros metodos de totalizando
	 * Cuidado para evitar LIE.
	 * Soma tambem os dependentes.
	 * @return valor pendente calculado sob demanda
	 */
	public BigDecimal getCalculatedPendentValue() {
		BigDecimal pendentValue = this.getCalculatedTotalValue().subtract( this.getCalculatedPaidValue() );
		return pendentValue;
	}
	
	
	/** 
	 * Retorna se pagamento esta OK, ou seja, nao ha pendencias.
	 * @return
	 */
	public Boolean getPaymentOK() {
		return NumberUtil.isEqualToZero( this.getCalculatedPendentValue() );
	}
	
	
	/**
	 * Calcula o valor total em credito, 
	 * delegando para tal processando para Account
	 * @return valor em creditos
	 */
	public BigDecimal getCalculatedCreditValue() {
		if (getAccount()==null) {
			return NumberUtil.VALUE_ZERO;
		}
		return getAccount().getCalculatedCreditValue();
	}
	
	/**
	 * Para dependentes, calcula o valor total
	 * @return
	 */
	public BigDecimal getCalculatedDependentTotalValue() {
		BigDecimal totalValue = NumberUtil.VALUE_ZERO;
		if (getDependentRegisters()!=null) {
			for (Register dependentRegister : getDependentRegisters()) {
				totalValue = totalValue.add( dependentRegister.getCalculatedTotalValue() );
			}
		}
		return totalValue;
	}
	
	/**
	 * Para dependentes, calcula o valor  pago
	 * @return
	 */
	public BigDecimal getCalculatedDependentPaidValue() {
		BigDecimal paidValue = NumberUtil.VALUE_ZERO;
		if (getDependentRegisters()!=null) {
			for (Register dependentRegister : getDependentRegisters() ) {
				paidValue = paidValue.add( dependentRegister.getCalculatedPaidValue() );
			}
		}
		return paidValue;
	}

	/**
	 * Para dependentes, calcula o valor pendente de pago
	 * @return
	 */
	public BigDecimal getCalculatedDependentPendentValue() {
		return getCalculatedDependentTotalValue().subtract( getCalculatedDependentPaidValue() );
	}
	
	/**
	 * Totaliza os valores das eventos inscrito como participante de evento de formacao
	 * (util para se calcular comissiones para produtores)
	 * @return
	 */
	public BigDecimal getCalculatedParticipantOfFormationTotalValue() {
		BigDecimal participantTotalValue = NumberUtil.VALUE_ZERO;
		for (RegisterDetail detail : getDetails()) {
			
			if (isParticipantPresence(detail) && isFormationEvent(detail)) {
				participantTotalValue = participantTotalValue.add( detail.getValue() );
			}
		}
		return participantTotalValue;
	}

	/**
	 * Seleciona os eventos de formacao como participante
	 * (util para comissao de produtores no momento do checkin)
	 * @return
	 */
	public List<Event> getFormationEventsAsParticipant() {
		List<Event> events = new ArrayList<>();
		for (RegisterDetail detail : getDetails()) {
			if (isParticipantPresence(detail) && isFormationEvent(detail)) {
				events.add( detail.getEvent() );
			}
		}
		return events;
	}

	
	private boolean isFormationEvent(RegisterDetail detail) {
		return EventType.FORMATION.equals( detail.getEvent().getType() );
	}

	private boolean isParticipantPresence(RegisterDetail detail) {
		return EventPresence.PARTICIPANT.equals( detail.getPresence() );
	}
	

	
	/**
	 * Retorna os creditos de Account
	 * @return
	 */
	public List<RegisterCredit> getCredits() {
		if (getAccount()==null) {
			return new ArrayList<RegisterCredit>();
		} else {
			return getAccount().getCredits();
		}
	}
	
	/**
	 * Dentre todos os eventos registrados, seleciona
	 * as semanas unicas (sem repeticao)
	 * @return
	 */
	public Set<EventWeek> getDistinctWeeks() {
		Set<EventWeek> distinctWeeks = new TreeSet<EventWeek>();
		for (RegisterDetail detail : getDetails()) {
			distinctWeeks.add( detail.getEventWeek() );
		}
		return distinctWeeks;
	}
	
	
	/**
	 * Junta todos os details dos dependentes
	 * @return
	 */
	public List<RegisterDetail> getDependentDetails() {
		List<RegisterDetail> dependentDetails = new ArrayList<>();
		for (Register register : getDependentRegisters()) {
			dependentDetails.addAll( register.getDetails() );
		}
		return dependentDetails;
	}
	
	
	/**
	 * Junta os detalhes proprios e os dos dependentes
	 * @return
	 */
	public List<RegisterDetail> getAllDetails() {
		List<RegisterDetail> allDetails = new ArrayList<>();
		allDetails.addAll( getDependentDetails() );
		allDetails.addAll( getDetails() );
		return allDetails;
	}
	
	
	public boolean getStatusIncomplete() {
		return RegisterStatus.INCOMPLETE.equals( this.status );
	}
	
	public boolean getStatusRegistered() {
		return RegisterStatus.REGISTERED.equals( this.status );
	}
	
	public boolean getStatusCheckedIn() {
		return RegisterStatus.CHECKEDIN.equals( this.status );
	}
	
	public boolean getStatusCheckinVirtual() {
//		return RegisterStatus.CHECKEDIN_VIRTUAL.equals( this.status );
		return false;
	}

	public boolean getStatusCheckedOut() {
		return RegisterStatus.CHECKEDOUT.equals( this.status );
	}
	
	public boolean getStatusMegaEventOut() {
		return RegisterStatus.MEGAEVENTOUT.equals( this.status );
	}

	
	
	/**
	 * Uma instancia de register so pode ser removida se, e somente se:
	 * - NAO for transiente (id!=null)
	 * - status=INCOMPLETE ou status=REGISTERED
	 * @return
	 */
	public Boolean getFlagAllowToBeRemoved() {
		return !isTransient() && ( getStatusIncomplete() || getStatusRegistered() );
	}
	
	
	/**
	 * Percorre todos as semanas de uma inscricao buscando a data de checkin
	 * , ou seja, a mais recente possivel
	 * @return
	 */
	public Date getCalculatedCheckinDate() {
		Date calculatedCheckinDate = null;
		if (getDetails()!=null) {
			for (RegisterDetail detail : getDetails()) {
				
				if (!EventPresence.CONTRATED.equals( detail.getPresence() )) {//contradados NAO tem semana
					//[14ABR2015]sao os eventos que definem a data de checkin
					Date eventBeginDate = detail.getEvent().getBeginDate();
					
					if (calculatedCheckinDate==null || eventBeginDate.before(calculatedCheckinDate)) {
						calculatedCheckinDate = eventBeginDate;
					}
				}
			}
		}
		return calculatedCheckinDate;
	}
	
	/**
	 * Percorre todas as semanas de uma inscricao busca a data de checkout,
	 * ou seja, a mais posterior possivel.
	 * @param register
	 */
	public Date getCalculatedCheckoutDate() {
		Date oficialCheckoutDate = null;
		if (getDetails()!=null) {
			for (RegisterDetail detail : getDetails()) {
				
				if (!EventPresence.CONTRATED.equals( detail.getPresence() )) {//contratados NAO tem semana
					//[14ABR2015]sao os eventos que definem a data de checkout
					Date eventCheckoutDate = detail.getEvent().getEndDate();
					
					if (oficialCheckoutDate==null || eventCheckoutDate.after(oficialCheckoutDate)) {
						oficialCheckoutDate = eventCheckoutDate;
					}
				}
			}
		}
		return oficialCheckoutDate;
	}
	
	/**
	 * Pelo status, determina se participou do mega evento ou nao.
	 * @return
	 */
	public Boolean getFlagHasBeenParticipated() {
		return getStatusCheckedOut() || getStatusMegaEventOut();
	}
	
	
	public String getFullDesc() {
		StringBuilder fullDesc = new StringBuilder();
		for (RegisterDetail detail : getDetails()) {
			fullDesc.append( detail.getFullDesc() );
			fullDesc.append(", ");
		}
		return fullDesc.toString();
	}
	

}
