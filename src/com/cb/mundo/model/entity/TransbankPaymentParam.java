package com.cb.mundo.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Representa um parametro enviado pela transbank num pagamento
 *
 * @author Solkam
 * @since 31 AGO 2013
 */
@Entity
@Audited
public class TransbankPaymentParam implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private TransbankPayment transbankPayment;
	
	private String paramName;
	
	private String paramValue;

	
	
	
	private static final long serialVersionUID = 8267837102408943763L;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransbankPayment getTransbankPayment() {
		return transbankPayment;
	}

	public void setTransbankPayment(TransbankPayment transbankPayment) {
		this.transbankPayment = transbankPayment;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
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
		TransbankPaymentParam other = (TransbankPaymentParam) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
   
}
