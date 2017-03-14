package com.cb.mundo.model.entity;

import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.SaleReceipt;

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

/**
 * Representa um dos pagamentos de uma inscricao interrelacionando
 * <code>RegisterDetail</code> e <code>PaymentMethod</code> bem como a data
 * do pagamento e o valor
 * 
 * @author Solkam
 * @since 07 out 2012
 */
@Entity
@Table(name="register_detail_payment")
@Audited
public class RegisterDetailPayment implements Serializable {
	private static final long serialVersionUID = 3875972365095180326L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private RegisterDetail registerDetail;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private MegaEventPaymentMethod method = MegaEventPaymentMethod.CC;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private SaleReceipt saleReceipt = SaleReceipt.TICKET;
	
	private String saleOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creditDate;
	
	@Column(nullable=false)
	private BigDecimal value;
	
	@Column(length=2000)
	private String note;
	
	//especificos para cheques
	@Column(length=90)
	private String checkTitular;
	
	@Column(length=90)
	private String checkNumber;
	
	@Column(length=30)
	private String checkAccount;
	
	@Column(length=90)
	private String checkBank;
	
	@Column(length=90)
	private String checkDate;
        
    @ManyToOne
    private PaymentCurrency paymentCurrency;
    
    @Column
    private String webCodeNumber;
    
    @ManyToOne(optional = true)
    private Company company;
        
//construtors
	public RegisterDetailPayment() {
	}
	
	/**
	 * A partir de RegisterCredit, retira as info relevantes
	 * @param credit
	 */
	public RegisterDetailPayment(RegisterCredit credit) {
		this.method = credit.getMethod();
		this.saleOrder = credit.getSaleOrder();
		this.value = credit.getValue();
		//data do pagamento
		if (credit.getPaymentDate()!=null) {
			this.date = credit.getPaymentDate();
		} else {
			this.date = new Date();
		}
		this.creditDate = credit.getDate();
        this.note = credit.getNote();
        this.paymentCurrency = credit.getPaymentCurrency();
        this.webCodeNumber = credit.getWebCodeNumber();
        this.company = credit.getCompany();
        this.saleReceipt = credit.getSaleReceipt();
	}

//acessores
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}
	public RegisterDetail getRegisterDetail() {
		return registerDetail;
	}
	public void setRegisterDetail(RegisterDetail registerDetail) {
		this.registerDetail = registerDetail;
	}
	public MegaEventPaymentMethod getMethod() {
		return method;
	}
	public void setMethod(MegaEventPaymentMethod method) {
		this.method = method;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getCheckTitular() {
		return checkTitular;
	}
	public void setCheckTitular(String checkTitular) {
		this.checkTitular = checkTitular;
	}
	public String getCheckNumber() {
		return checkNumber;
	}
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	public String getCheckAccount() {
		return checkAccount;
	}
	public void setCheckAccount(String checkAccount) {
		this.checkAccount = checkAccount;
	}
	public String getCheckBank() {
		return checkBank;
	}
	public void setCheckBank(String checkBank) {
		this.checkBank = checkBank;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
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
        public Company getCompany() {
            return company;
        }
        public void setCompany(Company company) {
            this.company = company;
        }
        public SaleReceipt getSaleReceipt() {
            return saleReceipt;
        }
        public void setSaleReceipt(SaleReceipt saleReceipt) {
            this.saleReceipt = saleReceipt;
        }
        
	/**
	 * Retorna se metodo de pagamento e cheque ou cheque pre-datado
	 * @return
	 */
	public Boolean getMethodIsCheck() {
		if (getMethod()==null) return false;
		return MegaEventPaymentMethod.CH.equals( getMethod() ) || MegaEventPaymentMethod.PC.equals( getMethod() );
	}
        
        public Boolean getSaleReceiptIsForCompanies(){
            if(getSaleReceipt() == null) return false;
            return saleReceipt.isForCompanies();
        }
	
	/**
	 * Retorna a descricao das informacoes sobre cheque.
	 * @return
	 */
	public String getDescCheckInfos() {
		StringBuilder checkInfos = new StringBuilder();
		checkInfos.append("Titular:").append( getCheckTitular() ).append("\n");
		checkInfos.append("Banco:"  ).append( getCheckBank()    ).append("\n");
		checkInfos.append("Cuenta:" ).append( getCheckAccount() ).append("\n");
		checkInfos.append("Numero:" ).append( getCheckNumber()  ).append("\n");
		return checkInfos.toString();
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
		RegisterDetailPayment other = (RegisterDetailPayment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegisterDetailPayment [id=" + id + ", method=" + method
				+ ", saleOrder=" + saleOrder + ", date=" + date + ", value="
				+ value + ", note=" + note + "]";
	}
}
