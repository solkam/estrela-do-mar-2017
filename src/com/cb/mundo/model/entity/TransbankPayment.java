package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Entidade que representa um pagamento da transbank
 * 
 * @author Solkam
 * @since 31 AGO 2013
 */
@Entity
@Audited
public class TransbankPayment implements Serializable {
	
	private static final String PARAM_REGISTER_ID   = "TBK_ORDEN_COMPRA";
	private static final String PARAM_PAYMENT_VALUE = "TBK_MONTO";

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@OneToMany(mappedBy="transbankPayment")
	private List<TransbankPaymentParam> params;
	
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private TransbankPaymentStatus status;
	
	@Column(length=500)
	private String note;


	public enum TransbankPaymentStatus {
		F //fail
		, 
		S //sucess
	}
	
	
	
	private static final long serialVersionUID = -1763862291846734555L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public TransbankPaymentStatus getStatus() {
		return status;
	}
	public void setStatus(TransbankPaymentStatus status) {
		this.status = status;
	}
	public List<TransbankPaymentParam> getParams() {
		return params;
	}
	public void setParams(List<TransbankPaymentParam> params) {
		this.params = params;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
		TransbankPayment other = (TransbankPayment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
//metodos de busca entre os parametros:
	public Long retrieveRegisterId() {
		for (TransbankPaymentParam param : getParams()) {
			if (param.getParamName().equals(PARAM_REGISTER_ID) ) {
				return Long.parseLong( param.getParamValue() );
			}
		}
		return -1L;
	}
	
	public BigDecimal retrievePaymentValue() {
		for (TransbankPaymentParam param : getParams() ) {
			if (param.getParamName().equals(PARAM_PAYMENT_VALUE)) {
				return new BigDecimal( param.getParamValue() );
			}
		}
		return new BigDecimal("0.00");
	}
	
	public String getParamsAsString() {
		StringBuilder sb = new StringBuilder();
		for (TransbankPaymentParam param : getParams() ) {
			sb.append("[");
			sb.append( param.getParamName() );
			sb.append( " : " );
			sb.append( param.getParamValue() );
			sb.append("]");
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
