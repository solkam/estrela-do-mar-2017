package com.cb.mundo.model.entity;

import com.cb.mundo.model.entity.enumeration.EventPresence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author Solkam
 *
 */
@Entity
@Table(name="register_detail")
@Audited
public class RegisterDetail implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Register register;

	@ManyToOne//nao pode ser obrigatorio por causa de contratados
	@OrderBy("beginDate")
	private EventWeek eventWeek;
	
	@ManyToOne//nao pode ser obrigatorio por causa de contratados
	private Event event;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private EventPresence presence;
	
	@OneToMany(mappedBy="registerDetail")
	private List<RegisterDetailPayment> payments = new ArrayList<RegisterDetailPayment>();
        
	@Column(nullable=false)
	private BigDecimal value;
	
	@Column(length=2000)
	private String note;
	
    @ManyToOne(optional=true)
	private Contact productorCommission;
        
	@OneToOne(mappedBy = "registerDetail", orphanRemoval = true, cascade = CascadeType.REMOVE)
	private RegisterDetailAccountReceivable accountReceivable;
	
	
	private static final long serialVersionUID = -1931010149705727793L;
	
//construtors..
	public RegisterDetail() {
		super();
	}

	public RegisterDetail(Long id, Register register, EventWeek eventWeek, Event event, EventPresence presence) {
		super();
		this.id = id;
		this.register = register;
		this.event = event;
		this.eventWeek = eventWeek;
		this.presence = presence;
	}

	
//metodos de calculos em runtime...	
	/**
	 * Calcula o total pago.
	 * Cuidado para evitar LIA
	 * @return
	 */
	public BigDecimal getCalculatedPaidValue() {
		BigDecimal paidValue = new BigDecimal("0.00");
		for (RegisterDetailPayment payment : this.getPayments() ) {
			paidValue = paidValue.add( payment.getValue() );
		}
		return paidValue;
	}
	
	/**
	 * Calcula o valor pendente a ser pago na inscricao de um evento.
	 * @return valor pendente
	 */
	public BigDecimal getCalculatedPendentValue() {
		BigDecimal value     = this.getValue();
		BigDecimal paidValue = this.getCalculatedPaidValue();
		
		BigDecimal pendentValue = value.subtract( paidValue );
		return pendentValue;
	}
	
	/**
	 * Retorna se o pagamento esta ok (sem pendencias)
	 * @return
	 */
	public Boolean getPaymentOK() {
		return getCalculatedPendentValue().equals( new BigDecimal("0.00") );
	}
	
	public String getDependentContactName() {
		if (getPresence().equals(EventPresence.DEPENDENT)) {
			return getRegister().getContact().getFullDesc();
		}
		return "";
	}
	
	
	
//acessors...	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<RegisterDetailPayment> getPayments() {
		return payments;
	}
	public void setPayments(List<RegisterDetailPayment> payments) {
		this.payments = payments;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public EventWeek getEventWeek() {
		return eventWeek;
	}
	public void setEventWeek(EventWeek eventWeek) {
		this.eventWeek = eventWeek;
	}
	public EventPresence getPresence() {
		return presence;
	}
	public void setPresence(EventPresence presence) {
		this.presence = presence;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
        public Contact getProductorCommission() {
            return productorCommission;
        }
        public void setProductorCommission(Contact productorCommission) {
            this.productorCommission = productorCommission;
        }
        public RegisterDetailAccountReceivable getAccountReceivable() {
            return accountReceivable;
        }
        public void setAccountReceivable(RegisterDetailAccountReceivable accountReceivable) {
            this.accountReceivable = accountReceivable;
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
		RegisterDetail other = (RegisterDetail) obj;
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

        public RegisterDetail cloneAsTransient() {
            RegisterDetail clon = new RegisterDetail();
            clon.setEvent(event);
            clon.setEventWeek(eventWeek);
            clon.setNote(note);
            clon.setPayments(payments);
            clon.setPresence(presence);
            clon.setRegister(register);
            clon.setValue(value);
            return clon;
        }
	
	

   public String getFullDesc() {
	   return String.format("%s (%s)", getEvent().getDisplayNameOrSchool(), getPresence().getInitial() );
   }
   
   
   public String getEventShortDesc() {
	   return String.format("%s", getEvent().getDisplayNameOrSchool() );
   }
        
        
}
