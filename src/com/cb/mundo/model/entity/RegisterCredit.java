package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.SaleReceipt;

@Entity
@Table(name="register_credit")
@Audited
public class RegisterCredit implements Serializable {

	//taxa de comissao de um produtor
	private static final BigDecimal PRODUCTOR_COMISSION_RATE = new BigDecimal("0.10");
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private SaleReceipt saleReceipt = SaleReceipt.TICKET;
        
    @ManyToOne(optional = true)
    private Company company;
        
	@ManyToOne(optional=false)
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private MegaEventPaymentMethod method;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;
	
	
    @ManyToOne
    private PaymentCurrency paymentCurrency;
    
    @Column
    private String webCodeNumber;
    
	@Column(nullable=false)
	private BigDecimal value;
	
	@Column(length=300)
	private String saleOrder;

	@Column(length=2000)
	private String note;
	
	
	/**
	 * Indica se o credito ja foi distribuido (ou pro-rateado) 
	 * entre varios pagamentos.
	 */
	private Boolean alreadyUsed = false;
	
	
	
	/**
	 * Se credito veio de um pagamento, 
	 * guarda o rastreabilidade
	 */
	@Column(length=500)
	private String registerDetailPaymentOrigin;

	
	
//construtores
	public RegisterCredit() {
	}
	
	/**
	 * A partir de um payment, extrai todas as infos
	 * @param payment
	 */
	public RegisterCredit(RegisterDetailPayment payment) {
		this.method = payment.getMethod();
		this.saleOrder = payment.getSaleOrder();
		this.value = payment.getValue();
		//data do credito
		if (payment.getCreditDate()!=null) {
			this.date = payment.getCreditDate();
		} else {
			this.date = new Date();
		}
		
		this.paymentDate = payment.getDate();
		this.account = payment.getRegisterDetail().getRegister().getAccount();
		this.registerDetailPaymentOrigin = payment.toString();
        this.paymentCurrency = payment.getPaymentCurrency();
        this.webCodeNumber = payment.getWebCodeNumber();
        this.note = payment.getNote();
        this.saleReceipt = payment.getSaleReceipt();
        this.company = payment.getCompany();
	}

	
	
	//acessores...	
	private static final long serialVersionUID = -910263041821917587L;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MegaEventPaymentMethod getMethod() {
		return method;
	}
	public void setMethod(MegaEventPaymentMethod method) {
		this.method = method;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getRegisterDetailPaymentOrigin() {
		return registerDetailPaymentOrigin;
	}
	public void setRegisterDetailPaymentOrigin(String registerDetailPaymentOrigin) {
		this.registerDetailPaymentOrigin = registerDetailPaymentOrigin;
	}
	public Boolean getAlreadyUsed() {
		return alreadyUsed;
	}
	public void setAlreadyUsed(Boolean alreadyUsed) {
		this.alreadyUsed = alreadyUsed;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
    public PaymentCurrency getPaymentCurrency() {
        return paymentCurrency;
    }
    public void setPaymentCurrency(PaymentCurrency paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }
    public String getWebCodeNumber() {
        return webCodeNumber;
    }
    public void setWebCodeNumber(String webCodeNumber) {
        this.webCodeNumber = webCodeNumber;
    }
    public SaleReceipt getSaleReceipt() {
        return saleReceipt;
    }
    public void setSaleReceipt(SaleReceipt saleReceipt) {
        this.saleReceipt = saleReceipt;
    }
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
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
		RegisterCredit other = (RegisterCredit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegisterCredit [id=" + id + ", method=" + method
				+ ", saleOrder=" + saleOrder + ", date=" + date + ", value="
				+ value + ", note=" + note + ", registerDetailPaymentOrigin="
				+ registerDetailPaymentOrigin + "]";
	}
	
	
	
	//metodos especiais
	
	/**
	 * A partir do valor total de inscricao de um produzido, 
	 * calcula o valor da comissao de seu produtor.
	 * 
	 * @param produtedRegisterTotalValue
	 */
	public void defineProductorComissionValue(BigDecimal produtedRegisterTotalValue) {
		BigDecimal productorComissionValue = produtedRegisterTotalValue.multiply( PRODUCTOR_COMISSION_RATE );
		setValue( productorComissionValue );
	}
	
	
    public Boolean getSaleReceiptIsForCompanies(){
        if(getSaleReceipt() == null) return false;
        return saleReceipt.isForCompanies();
    }
    
    /**
     * Adiciona uma nota adicional a nota existente.
     * Se nao houver nota, toma como nota a adicional.
     * @param adicionalNote
     */
    public void addNote(String adicionalNote) {
    	if (this.note==null) {
    		this.note = adicionalNote;
    	} else {
    		this.note = this.note + System.lineSeparator() + adicionalNote;
    	}
    }

}
