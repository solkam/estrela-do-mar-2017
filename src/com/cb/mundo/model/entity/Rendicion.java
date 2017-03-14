package com.cb.mundo.model.entity;

import static com.cb.mundo.model.util.NumberUtil.VALUE_ZERO;
import static com.cb.mundo.model.util.NumberUtil.add;
import static com.cb.mundo.model.util.NumberUtil.divide;
import static com.cb.mundo.model.util.NumberUtil.isEqualToZero;
import static com.cb.mundo.model.util.NumberUtil.subtract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.cb.mundo.model.calculum.RendicionCalculum;
import com.cb.mundo.model.dto.OutcomingSummary;
import com.cb.mundo.model.entity.enumeration.IncomingCategory;
import com.cb.mundo.model.entity.enumeration.WayOfPaymentToAdmin;


/**
 * Representa o registro de uma rendicion.
 * Varias campos sao calculados em tempo de execucao
 * 
 * @author Solkam
 * @since 23 NOV 2014
 */
@Entity
public class Rendicion implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@NotNull
	@JoinColumn(unique=true)
	private Production production;
	
	

	@Column(nullable=false)
	private Integer participantNumber;
	
	@Column(nullable=false)
	private BigDecimal participantTotalValue;
	

	@Column(nullable=false)
	private Integer staffNumber;
	
	@Column(nullable=false)
	private BigDecimal staffTotalValue;
	
	
	@Column(nullable=false)
	private BigDecimal incomingsTotal;
	
	@Column(nullable=false)
	private BigDecimal outcomingsTotal;
	
	@ElementCollection(targetClass=RendicionOutcomingDetail.class)
	@CollectionTable(name="rendicion_x_outcomingdetail",
		joinColumns=@JoinColumn(name="rendicion_id")
	)
	private List<RendicionOutcomingDetail> outcomingDetails;
	
	@Column(nullable=false)
	private BigDecimal balance;
	
	
	@Column(nullable=false)
	private BigDecimal valueToMountain;
	
	@Column(nullable=false)
	private BigDecimal valueToFoundation;
	
	@Column(nullable=false)
	private BigDecimal valueToFacilitators;
	
	@Column(nullable=false)
	private BigDecimal valueToProductors;
	
	@Column(nullable=false)
	private BigDecimal valueToMarketing;

	
	@Enumerated(EnumType.STRING)
	@Column(length=12, nullable=false)
	private WayOfPaymentToAdmin wayOfPaymentToAdmin = WayOfPaymentToAdmin.CASH_DOLLAR;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date datePaymentToAdmin;
	
	@Column(length=500)
	private String notePaymentToAdmin;
	
	
	@Column(nullable=false)
	private BigDecimal dolarExchange;
	
	
	@OneToMany(mappedBy="rendicion")
	private List<RendicionPayment> payments;

	
	//logs
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	
	
	@PrePersist void onPersist() {
		this.createDate = new Date();
	}
	
	@PreUpdate void onUpdate() {
		this.updateDate = new Date();
	}
	

	//construtores...
	public Rendicion(Production production, 
			BigDecimal dolarExchange, 
			WayOfPaymentToAdmin way, 
			Date datePaymentoToAdmin,
			String notePaymentToAdmin,
			List<OutcomingSummary> outcomingSummaries) {
		
		this.production = production;
		this.dolarExchange = dolarExchange;
		this.wayOfPaymentToAdmin = way;
		this.datePaymentToAdmin = datePaymentoToAdmin;
		this.notePaymentToAdmin = notePaymentToAdmin;
		handleValues();
		handleOutcomingDetails(outcomingSummaries);
	}
	

	public Rendicion() {}
	
	
	//handlers...
	private void handleValues() {
		handleParticipantNumber();
		handleParticipantTotalValue();

		handleStaffNumber();
		handleStaffTotalValue();
	
		handleBalance();
		handleIncomingsTotal();
		handleOutcomingsTotal();
		
		handlePercentualValues();
	}

	private void handleParticipantNumber() {
		this.participantNumber = production.getParticipants().size();
	}

	private void handleStaffNumber() {
		this.staffNumber = production.getStaffs().size();
	}

	private void handleBalance() {
		this.balance = convertToDolar( production.getCalculatedBalance() );
	}

	private void handleOutcomingsTotal() {
		this.outcomingsTotal = convertToDolar( production.getCalculatedOutcomingTotal() );
	}

	private void handleIncomingsTotal() {
		this.incomingsTotal = convertToDolar( production.getCalculatedIncomingTotal() );
	}
	

	private void handleOutcomingDetails(List<OutcomingSummary> outcomingSummaries) {
		outcomingDetails = new ArrayList<>();
		for (OutcomingSummary os : outcomingSummaries) {
			RendicionOutcomingDetail detail = new RendicionOutcomingDetail();
			detail.setOutcomingCategory( os.getCategory() );
			detail.setTotalValue( convertToDolar(os.getTotal() ));
			outcomingDetails.add( detail );
		}
	}
	
	
	
	private void handlePercentualValues() {
		RendicionCalculum rendicionCalculum = production.getSchool().getRendicionCalculum();
		double balanceAsDouble = production.getCalculatedBalance();
		
		BigDecimal localCurrencyvalueToMountain     = rendicionCalculum.getValueToMountain( balanceAsDouble );
		BigDecimal localCurrencyvalueToFoundation   = rendicionCalculum.getValueToFoundation( balanceAsDouble );
		BigDecimal localCurrencyvalueToFacilitators = rendicionCalculum.getValueToFacilitators( balanceAsDouble );
		BigDecimal localCurrencyvalueToProductors   = rendicionCalculum.getValueToProductors( balanceAsDouble );
		BigDecimal localCurrencyvalueToMarketing    = rendicionCalculum.getValueToMarketing( balanceAsDouble );
		
		
		this.valueToMountain     = convertToDolar(localCurrencyvalueToMountain);
		this.valueToFoundation   = convertToDolar(localCurrencyvalueToFoundation);
		this.valueToFacilitators = convertToDolar(localCurrencyvalueToFacilitators);
		this.valueToProductors   = convertToDolar(localCurrencyvalueToProductors);
		this.valueToMarketing    = convertToDolar(localCurrencyvalueToMarketing);
		
	}

	private void handleParticipantTotalValue() {
		double total = 0.0;
		for (Incoming incoming : production.getIncomings()) {
			if (IncomingCategory.P.equals(incoming.getCategory())) {//eh participant
				total += incoming.getValue();
			}
		}
		this.participantTotalValue = convertToDolar(total);
	}

	private void handleStaffTotalValue() {
		double total = 0.0;
		for (Incoming incoming : production.getIncomings()) {
			if (IncomingCategory.S.equals(incoming.getCategory())) {//eh staff
				total += incoming.getValue();
			}
		}
		this.staffTotalValue = convertToDolar(total);
	}


	/**
	 * Util para converter moeda local para dolar
	 * @param localCurrencyValue
	 * @return
	 */
	public BigDecimal convertToDolar(double localCurrencyValue) {
		return divide(localCurrencyValue, dolarExchange);
	}



	/**
	 * Versao overloaded para BigDecimal
	 * @param localCurrencyValue
	 * @return
	 */
	public BigDecimal convertToDolar(BigDecimal localCurrencyValue) {
		return divide(localCurrencyValue, dolarExchange);
	}


	//runtime
	public String getDescribedDates() {
		return production.getDescribedDates();
	}
	
	public String getEvent() {
		return production.getDescEvent();
	}
	
	public String getCity() {
		return production.getCity().getFullDesc();
	}
	
	public int getNumberOfParticipants() {
		return production.getParticipants().size();
	}
	
	public int getNumberOfStaffs() {
		return production.getStaffs().size();
	}
	
	public String getDescFacilitators() {
		return production.getDescFacilitators();
	}
	
	public String getDescProductors() {
		return production.getDescProductors();
	}
	
	public BigDecimal getParticipantAvgValue() {
		return divide(getParticipantTotalValue(), getParticipantNumber() );
	}

	public BigDecimal getStaffAvgValue() {
		return divide(getStaffTotalValue(), getStaffNumber() );
	}

	
	//payment
	
	/**
	 * Valor total eh a soma do valor da montanha e da fundacao
	 * @return
	 */
	public BigDecimal getCalculatedValueCB() {
		return add( valueToMountain, valueToFoundation);
	}
	
	/**
	 * Valor pago eh a soma dos pagamentos parciais
	 * @return
	 */
	public BigDecimal getCalculatedValuePaid() {
		BigDecimal total = VALUE_ZERO;
		for (RendicionPayment paymentVar : getPayments()) {
			total = add( total, paymentVar.getPaymentValue() );
		}
		return total;
	}
	
	/**
	 * Valor pendente eh o valor total menos o valor ja pago
	 * @return
	 */
	public BigDecimal getCalculatedValuePendent() {
		return subtract( getCalculatedValueCB(), getCalculatedValuePaid()); 
	}
	
	/**
	 * Indicacao se nao ha mais valor pendente
	 * @return
	 */
	public Boolean getFlagPaymentOK() {
		return isEqualToZero( getCalculatedValuePendent() );
	}
	
	
	
	//acessores
	private static final long serialVersionUID = 5325360157640238756L;
	
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
	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}
	public Integer getParticipantNumber() {
		return participantNumber;
	}
	public void setParticipantNumber(Integer participantNumber) {
		this.participantNumber = participantNumber;
	}
	public BigDecimal getParticipantTotalValue() {
		return participantTotalValue;
	}
	public void setParticipantTotalValue(BigDecimal participantTotalValue) {
		this.participantTotalValue = participantTotalValue;
	}
	public Integer getStaffNumber() {
		return staffNumber;
	}
	public void setStaffNumber(Integer staffNumber) {
		this.staffNumber = staffNumber;
	}
	public BigDecimal getStaffTotalValue() {
		return staffTotalValue;
	}
	public void setStaffTotalValue(BigDecimal staffTotalValue) {
		this.staffTotalValue = staffTotalValue;
	}
	public BigDecimal getIncomingsTotal() {
		return incomingsTotal;
	}
	public void setIncomingsTotal(BigDecimal incomingsTotal) {
		this.incomingsTotal = incomingsTotal;
	}
	public BigDecimal getOutcomingsTotal() {
		return outcomingsTotal;
	}
	public void setOutcomingsTotal(BigDecimal outcomingsTotal) {
		this.outcomingsTotal = outcomingsTotal;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getValueToMountain() {
		return valueToMountain;
	}
	public void setValueToMountain(BigDecimal valueToMountain) {
		this.valueToMountain = valueToMountain;
	}
	public BigDecimal getValueToFoundation() {
		return valueToFoundation;
	}
	public void setValueToFoundation(BigDecimal valueToFoundation) {
		this.valueToFoundation = valueToFoundation;
	}
	public BigDecimal getValueToFacilitators() {
		return valueToFacilitators;
	}
	public void setValueToFacilitators(BigDecimal valueToFacilitators) {
		this.valueToFacilitators = valueToFacilitators;
	}
	public BigDecimal getValueToProductors() {
		return valueToProductors;
	}
	public void setValueToProductors(BigDecimal valueToProductors) {
		this.valueToProductors = valueToProductors;
	}
	public BigDecimal getValueToMarketing() {
		return valueToMarketing;
	}
	public void setValueToMarketing(BigDecimal valueToMarketing) {
		this.valueToMarketing = valueToMarketing;
	}
	public WayOfPaymentToAdmin getWayOfPaymentToAdmin() {
		return wayOfPaymentToAdmin;
	}
	public void setWayOfPaymentToAdmin(WayOfPaymentToAdmin wayOfPaymentToAdmin) {
		this.wayOfPaymentToAdmin = wayOfPaymentToAdmin;
	}
	public BigDecimal getDolarExchange() {
		return dolarExchange;
	}
	public void setDolarExchange(BigDecimal dolarExchange) {
		this.dolarExchange = dolarExchange;
	}
	public Date getDatePaymentToAdmin() {
		return datePaymentToAdmin;
	}
	public void setDatePaymentToAdmin(Date datePaymentToAdmin) {
		this.datePaymentToAdmin = datePaymentToAdmin;
	}
	public String getNotePaymentToAdmin() {
		return notePaymentToAdmin;
	}
	public void setNotePaymentToAdmin(String notePaymentToAdmin) {
		this.notePaymentToAdmin = notePaymentToAdmin;
	}
	public List<RendicionOutcomingDetail> getOutcomingDetails() {
		return outcomingDetails;
	}
	public void setOutcomingDetails(List<RendicionOutcomingDetail> outcomingDetails) {
		this.outcomingDetails = outcomingDetails;
	}
//	public Boolean getFlagPaidToAdmin() {
//		return flagPaidToAdmin;
//	}
//	public void setFlagPaidToAdmin(Boolean flagPaidToAdmin) {
//		this.flagPaidToAdmin = flagPaidToAdmin;
//	}
//	public Date getDateRealPayment() {
//		return dateRealPayment;
//	}
//	public void setDateRealPayment(Date dateRealPayment) {
//		this.dateRealPayment = dateRealPayment;
//	}
//	public String getWhoReceivedPayment() {
//		return whoReceivedPayment;
//	}
//	public void setWhoReceivedPayment(String whoReceivedPayment) {
//		this.whoReceivedPayment = whoReceivedPayment;
//	}
	public List<RendicionPayment> getPayments() {
		return payments;
	}
	public void setPayments(List<RendicionPayment> payments) {
		this.payments = payments;
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
		Rendicion other = (Rendicion) obj;
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
	
}
